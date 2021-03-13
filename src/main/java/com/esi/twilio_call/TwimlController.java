package com.esi.twilio_call;

import com.twilio.twiml.VoiceResponse;
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
                .say(new Say.Builder("Hello Tou Mohammed Start Talking ").build())
                .start(new Start.Builder().stream(new Stream.Builder().url(wsUrl).build()).build())
    .build().toXml();
    }
}
