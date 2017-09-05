package com.bot.bottest;

import lombok.Data;

import java.util.Date;

@Data
public class Messaging {
    Sender sender;
    Recipient recipient;
    Date timestamp;//TODO change for LocalDateTime
    Message message;

    @Data
    public class Sender {
        private String id;
    }




}

