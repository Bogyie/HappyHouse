package com.ssafy.happyhouse.controller.dto.house;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.StringJoiner;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;


@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class HouseDetailSearchQuery {
    String serviceType;
    Integer localCode;
    Integer dealYear;
    Integer dealMonth;
    Integer page;
    Integer numOfRows;

    public URL toUrl() throws IllegalArgumentException {

        if (serviceType == null || localCode == null) {
            throw  new IllegalArgumentException();
        }

        final String endpoint = "https://2dgat4qloindqd7pvbx62j66ey0bjgbj.lambda-url.ap-northeast-2.on.aws";
        final String serviceKey = "87%2BiGqOqBwTkpxgZWY%2BCEGLFOOYt6DupE%2FTiLbySb4y55POBkupjFc6K%2Bt7xLhflWDZt%2Fij2X%2BCDv2Sw%2BVbfKw%3D%3D";
        final String baseUrl = endpoint
                               + "?serviceKey=" + serviceKey
                               + "&serviceType=" + serviceType
                               + "&localCode=" + localCode;
        final StringJoiner url = new StringJoiner("&").add(baseUrl);


        if (dealYear != null) {
            url.add(String.format("dealYear=%d", dealYear));
        }

        if (dealMonth != null) {
            url.add(String.format("dealMonth=%d", dealMonth));
        }

        if (page != null) {
            url.add(String.format("page=%d", page));
        }

        if (numOfRows != null) {
            url.add(String.format("numOfRows=%d", numOfRows));
        }

        try {
            return new URL(url.toString());
        } catch (MalformedURLException e) {
            System.out.println(e.toString());
            return null;
        }
    }
}
