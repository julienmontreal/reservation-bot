package com.bot.bottest.dto.request;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class Entry {
    private String id;
    private Date time;
    private List<Messaging> messaging;
}


