package com.bot.bottest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class BotController {

    @GetMapping("/callback")
    public String callback(
        @RequestParam(value = "hub.mode", required = false) String hubMode,
        @RequestParam(value = "hub.challenge", required = false) String hubChallenge,
        @RequestParam(value = "hub.verify_token", required = false) String verifyToken){
        return hubChallenge;
    }

    @PostMapping("/webhook")
    public ResponseEntity<Void> receiveMessage(@RequestBody Payload data){
        // Make sure this is a page subscription
        if ("page".equals(data.getObject())) {

            // Iterate over each entry - there may be multiple if batched
            data.getEntry().stream().forEach(
                entry -> {
                String pageID = entry.getId();
                Date timeOfEvent = entry.getTime();

                // Iterate over each messaging event
                entry.getMessaging().stream().forEach(event -> {
                    if (!StringUtils.isEmpty(event.getMessage())) {
                        //receivedMessage(event);
                    } else {
                        //console.log("Webhook received unknown event: ", event);
                    }
                });
            });

            // Assume all went well.
            //
            // You must send back a 200, within 20 seconds, to let us know
            // you've successfully received the callback. Otherwise, the request
            // will time out and we will keep trying to resend.
            return new ResponseEntity<Void>(HttpStatus.ACCEPTED);
        }

        return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
    }
}
