package com.bot.bottest;

import lombok.Data;

import java.util.Date;

@Data
public class Messaging {
    Sender sender;
    Recipient recipient;
    Date timestamp;
    String message;

    @Data
    class Sender {
        String id;
    }

    @Data
    class Recipient{
        String id;
    }
}

