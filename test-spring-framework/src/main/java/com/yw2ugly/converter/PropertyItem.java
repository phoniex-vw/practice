package com.yw2ugly.converter;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@ToString
@Getter
@Setter
public class PropertyItem {
    private int age;
    private String name;
    private Date d1;
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private Date d2;
}
