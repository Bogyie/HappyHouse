package com.ssafy.happyhouse.controller.dto.house;

import java.util.List;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
public class HouseDetailDto {
    private String message;
    private List<HouseDetail> items;

    @Builder
    public HouseDetailDto(String message, List<HouseDetail> items) {
        this.message = message;
        this.items = items;
    }

    @Data
    public static class HouseDetail {
        private int buildYear;
        private String tradeDay;
        private String address;
        private int floor;
        private String name;
        private double area;
        private double area_kor;
        private int deposit;
        private int rental;
        private int price;
        private String roadAddress;
        private String jibunAddress;
        private String englishAddress;
        private double lon;
        private double lat;
    }
}
