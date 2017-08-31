package com.bot.bottest;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class Entry {
    String id;
    Date time;
    List<Messaging> messaging;
}


