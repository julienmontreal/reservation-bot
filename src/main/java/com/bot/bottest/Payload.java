package com.bot.bottest;

import lombok.Data;

import java.util.List;

@Data
public class Payload {

    private String object;
    private List<Entry> entry;

}
