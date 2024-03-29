package com.esi.twilio_call;

import com.twilio.twiml.VoiceResponse;
import com.twilio.twiml.voice.Pause;
import com.twilio.twiml.voice.Say;
import com.twilio.twiml.voice.Start;
import com.twilio.twiml.voice.Stream;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriComponentsBuilder;

@Controller
public class TwimlController {
    @GetMapping(value = "/twiml",produces = "application/xml")
    @ResponseBody
    public String getTwiml(UriComponentsBuilder uri){
        String wsUrl = "wss://"+uri.build().getHost()+ "/msgs";
        return new VoiceResponse.Builder()
                .say(new Say.Builder("Hello Mohammed Start talking and the live audio will be streamed to your app").build())
                .start(new Start.Builder().stream(new Stream.Builder().url(wsUrl).build()).build())
                .pause(new Pause.Builder().length(30).build())
                .build().toXml();
    }
}
/*
 export GOOGLE_APPLICATION_CREDENTIALS=/home/abdou/Downloads/spring-boot-k8s-demo1-4a676b4cdf59.json"

 */