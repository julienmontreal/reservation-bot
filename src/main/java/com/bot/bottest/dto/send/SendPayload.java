package com.bot.bottest.dto.send;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SendPayload {

    private SendRecipient recipient;
    private SendMessage message;

}
