package com.bot.bottest.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class Payload {

    private String object;
    private List<Entry> entry;

}
