package com.bot.bottest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

@RestController
public class BotController {

    @GetMapping("/callback")
    public String callback(@RequestParam(value = "hub.mode", required = false) String hubMode,
                           @RequestParam(value = "hub.challenge", required = false) String hubChallenge,
                           @RequestParam(value = "hub.verify_token", required = false) String verifyToken) {
        return hubChallenge;
    }

    @PostMapping("/webhook")
    public ResponseEntity<Void> receiveMessage(@RequestBody Payload data) {
        // Make sure this is a page subscription
        if ("page".equals(data.getObject())) {

            // Iterate over each entry - there may be multiple if batched
            data.getEntry().stream().forEach(entry -> {
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


    private void receivedMessage(Messaging event) {
        String senderID = event.getSender().getId();
        String recipientID = event.getRecipient().getId();
        Date timeOfMessage = event.getTimestamp();
        Message message = event.getMessage();

        // console.log("Received message for user %d and page %d at %d with message:",
        //   senderID, recipientID, timeOfMessage);
        //console.log(JSON.stringify(message));

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
            sendTextMessage(senderID, "Message with attachment received");
        }
    }

    private void sendTextMessage(String recipientId, String messageText) {
        Messaging messageData = new Messaging();
        Recipient recipient = new Recipient(recipientId);
        messageData.setRecipient(recipient);
        Message message = new Message();
        message.setText(messageText);
        messageData.setMessage(message);
        callSendAPI(messageData);
    }

    private void callSendAPI(Messaging messageData) {

        RestTemplate restTemplate = new RestTemplate();
        String uri = "https://graph.facebook.com/v2.6/me/messages";
        String accessToken =
            "EAAGgZBpa7WYkBAEqlrvtVt5hd0q8Tb2dhZCjmLNXk72HRcj4A34kkMrMxSUoUEhpP3LZCRDRsDQVyWXbBLhJmZBujxgvixcbBEueBZBXCyeX0koKGSO4JaLSQPeApfqR6LTbkHZCeUAON6M4HxzoPMMmiZBabZCAowrMhnUdQvTtfQZDZD";
        restTemplate.postForEntity(uri, messageData, Messaging.class);
    }

    private void sendGenericMessage(String recipientId) {
       /* var messageData = {
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
                                payload: "Payload for first bubble",
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
                                payload: "Payload for second bubble",
                        }]
                    }]
                }
            }
        }
  };

        callSendAPI(messageData);*/
    }

}
