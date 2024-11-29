package com.openclassroom.projet_spring_api_chatopcg.utils;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Component
public class ToolBoxUtils {

    public String DateTimeFormatterFrench() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return LocalDate.now().format(formatter);
    }
}
