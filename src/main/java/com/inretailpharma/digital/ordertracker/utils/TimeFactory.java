package com.inretailpharma.digital.ordertracker.utils;

import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TimeFactory {

    public Date currentDate(){
        return DateUtil.currentDate();
    }

}
