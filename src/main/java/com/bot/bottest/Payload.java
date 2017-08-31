package com.bot.bottest;

import lombok.Data;

import java.util.List;

@Data
public class Payload {

    String object;
    List<Entry> entry;

}
