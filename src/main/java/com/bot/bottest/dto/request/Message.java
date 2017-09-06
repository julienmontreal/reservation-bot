package com.bot.bottest.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Message {
    private String mid;
    private String text;
    private String attachements;
}