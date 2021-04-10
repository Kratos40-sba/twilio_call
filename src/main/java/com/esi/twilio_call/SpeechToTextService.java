package com.esi.twilio_call;

import com.google.api.gax.rpc.ClientStream;
import com.google.api.gax.rpc.ResponseObserver;
import com.google.api.gax.rpc.StreamController;
import com.google.cloud.speech.v1.*;
import com.google.protobuf.ByteString;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Base64;
import java.util.function.Consumer;


public class SpeechToTextService {
    final static Logger logger = LoggerFactory.getLogger(SpeechToTextService.class);
    ClientStream<StreamingRecognizeRequest> req ;
    ResponseObserver<StreamingRecognizeResponse> resp ;
    public SpeechToTextService(Consumer<String> onTranscription ) throws IOException {
        SpeechClient client = SpeechClient.create();
        resp = new ResponseObserver<StreamingRecognizeResponse>(){

            @Override
            public void onStart(StreamController streamController) {
                  logger.info("Debut de Transcription  ");
            }

            @Override
            public void onResponse(StreamingRecognizeResponse streamingRecognizeResponse) {
                StreamingRecognitionResult result = streamingRecognizeResponse.getResultsList().get(0);
                SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
                onTranscription.accept(alternative.getTranscript());
            }

            @Override
            public void onError(Throwable throwable) {
               logger.error("Error : {}", throwable);
            }

            @Override
            public void onComplete() {
               logger.info("Fin de Transcription");
            }
        };
        req = client.streamingRecognizeCallable().splitCall(resp);
        RecognitionConfig recognitionConfig =
                RecognitionConfig.newBuilder()
                .setEncoding(RecognitionConfig.AudioEncoding.MULAW)
                .setLanguageCode("en-US")
                .setSampleRateHertz(8000)
                .build();
        StreamingRecognitionConfig streamingRecognitionConfig =
                StreamingRecognitionConfig.newBuilder()
                .setConfig(recognitionConfig)
                .setInterimResults(true)
                .build();
        StreamingRecognizeRequest streamingRecognizeRequest =
                StreamingRecognizeRequest.newBuilder()
                .setStreamingConfig(streamingRecognitionConfig)
                .build();
        req.send(streamingRecognizeRequest);
    }
    public void send(String message)  {
        try {
            JSONObject j = new JSONObject(message);
            if (!j.getString("event").equals("media")){
                return;
            }
            String payload = j.getJSONObject("media").getString("payload");
            byte[] donnee = Base64.getDecoder().decode(payload);
            StreamingRecognizeRequest request =
                    StreamingRecognizeRequest.newBuilder()
                    .setAudioContent(ByteString.copyFrom(donnee))
                    .build();
            req.send(request);
        } catch(JSONException e ){
            logger.error("Format json invalide");
            e.printStackTrace();
        }
    }
    public void close(){
        logger.info("Fin de connexion");
        resp.onComplete();
    }

}
