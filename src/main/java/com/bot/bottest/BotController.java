package com.bot.bottest;

import com.bot.bottest.dto.request.Message;
import com.bot.bottest.dto.request.Messaging;
import com.bot.bottest.dto.request.Payload;
import com.bot.bottest.dto.send.SendMessage;
import com.bot.bottest.dto.send.SendPayload;
import com.bot.bottest.dto.send.SendRecipient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.Objects;

@RestController
public class BotController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping("/callback")
    public String callback(@RequestParam(value = "hub.mode", required = false) String hubMode,
                           @RequestParam(value = "hub.challenge", required = false) String hubChallenge,
                           @RequestParam(value = "hub.verify_token", required = false) String verifyToken) {
        return hubChallenge;
    }

    @GetMapping("/health")
    public String healthCheck() {
        logger.info("/health called");

        return "OK";
    }

    @PostMapping("/callback")
    public ResponseEntity<Void> receiveMessage(@RequestBody Payload data) {

        logger.info("POST /callback called with payload " + data.toString());
        // Make sure this is a page subscription
        if ("page".equals(data.getObject())) {

            // Iterate over each entry - there may be multiple if batched
            data.getEntry().stream().forEach(entry -> {
                String pageID = entry.getId();
                Date timeOfEvent = entry.getTime();

                // Iterate over each messaging event
                entry.getMessaging().stream()
                    .filter(Objects::nonNull)
                    .forEach(event -> {
                    if (!StringUtils.isEmpty(event.getMessage())) {
                        receivedMessage(event);
                    } else {
                        logger.warn("Received unknown event: ", event);
                    }
                });
            });

            // Assume all went well.
            //
            // You must send back a 200, within 20 seconds, to let us know
            // you've successfully received the callback. Otherwise, the request


            // will time out and we will keep trying to resend.
            logger.info("POST /callback returned 200");
            return new ResponseEntity<Void>(HttpStatus.ACCEPTED);
        }

        logger.info("POST /callback returned 400");
        return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
    }


    public void receivedMessage(Messaging event) {
        String senderID = event.getSender().getId();
        String recipientID = event.getRecipient().getId();
        Date timeOfMessage = event.getTimestamp();
        Message message = event.getMessage();

        logger.info(String.format("Received message for user %s and page %s at %s with message: %s", senderID, recipientID, timeOfMessage, message.toString()));

        String messageId = message.getMid();

        String messageText = message.getText();
        String messageAttachments = message.getAttachements();

        if (!StringUtils.isEmpty(messageText)) {

            // If we receive a text message, check to see if it matches a keyword
            // and send back the example. Otherwise, just echo the text we received.
            switch (messageText) {
                case "generic":
                    sendGenericMessage(senderID);
                    break;

                default:
                    sendTextMessage(senderID, messageText);
            }
        } else if (!StringUtils.isEmpty(messageAttachments)) {
            sendTextMessage(senderID, "SendMessage with attachment received");
        }
    }

    private void sendTextMessage(String recipientId, String messageText) {
        SendRecipient recipient = new SendRecipient(recipientId);
        SendMessage message = new SendMessage();
        message.setText(messageText);
        SendPayload sendPayload = new SendPayload(recipient, message);
        callSendAPI(sendPayload);
    }

    public void callSendAPI(SendPayload sendPayload) {

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);


        String uri = "https://graph.facebook.com/v2.6/me/messages";
        String accessToken =
            "EAAGgZBpa7WYkBAEqlrvtVt5hd0q8Tb2dhZCjmLNXk72HRcj4A34kkMrMxSUoUEhpP3LZCRDRsDQVyWXbBLhJmZBujxgvixcbBEueBZBXCyeX0koKGSO4JaLSQPeApfqR6LTbkHZCeUAON6M4HxzoPMMmiZBabZCAowrMhnUdQvTtfQZDZD";
        uri = uri.concat("?access_token="+accessToken);
        ObjectMapper mapper = new ObjectMapper();
        String json = "";
        try{
            json = mapper.writeValueAsString(sendPayload);
            logger.info("sending response to facebook with payload: " + json);
        } catch (JsonProcessingException e) {
            logger.error("error while parsing data to json");
        }

        HttpEntity<String> entity = new HttpEntity<>(json, headers);
        ResponseEntity<Object> response = null;
        try {
            response = restTemplate.postForEntity(uri, entity, Object.class);
        } catch (HttpClientErrorException e) {
            logger.info(e.getResponseBodyAsString());
        }
    }

    private void sendGenericMessage(String recipientId) {
       /* SendMessage messageData = {
            recipient: {
            id: recipientId
        },
        message: {
            attachment: {
                type: "template",
                    payload: {
                    template_type: "generic",
                        elements: [{
                        title: "rift",
                            subtitle: "Next-generation virtual reality",
                            item_url: "https://www.oculus.com/en-us/rift/",
                            image_url: "http://messengerdemo.parseapp.com/img/rift.png",
                            buttons: [{
                            type: "web_url",
                                url: "https://www.oculus.com/en-us/rift/",
                                title: "Open Web URL"
                        }, {
                            type: "postback",
                                title: "Call Postback",
                                payload: "SendPayload for first bubble",
                        }],
                    }, {
                        title: "touch",
                            subtitle: "Your Hands, Now in VR",
                            item_url: "https://www.oculus.com/en-us/touch/",
                            image_url: "http://messengerdemo.parseapp.com/img/touch.png",
                            buttons: [{
                            type: "web_url",
                                url: "https://www.oculus.com/en-us/touch/",
                                title: "Open Web URL"
                        }, {
                            type: "postback",
                                title: "Call Postback",
                                payload: "SendPayload for second bubble",
                        }]
                    }]
                }
            }
        }
  };

        callSendAPI(messageData);*/
    }

}
