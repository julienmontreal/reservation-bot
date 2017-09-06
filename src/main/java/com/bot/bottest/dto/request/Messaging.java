package com.bot.bottest.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
public class Messaging {
    Sender sender;
    Recipient recipient;
    Date timestamp;//TODO change for LocalDateTime
    Message message;

    @Data
    @AllArgsConstructor
    public static class Sender {
        private String id;
    }

}

