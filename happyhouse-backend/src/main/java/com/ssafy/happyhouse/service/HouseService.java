package com.ssafy.happyhouse.service;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.happyhouse.controller.dto.house.HouseDetailDto;
import com.ssafy.happyhouse.controller.dto.house.HouseDetailDto.HouseDetail;
import com.ssafy.happyhouse.controller.dto.house.HouseDetailSearchQuery;

@Service
public class HouseService {

    private final static Logger logger = LoggerFactory.getLogger(HouseService.class);
    private static final Class<HouseDetailDto> responseDtoClass = HouseDetailDto.class;
    private final ObjectMapper mapper;

    public List<HouseDetail> houses(HouseDetailSearchQuery searchQuery) {
        final URL url = searchQuery.toUrl();
        logger.info(url.toString());
        try {
            final HouseDetailDto houseDetailResponse = mapper.readValue(url, responseDtoClass);
            return houseDetailResponse.getItems();
        } catch (IOException e) {
            logger.error(e.getMessage());
            return Collections.emptyList();
        }
    }

    @Autowired
    public HouseService() {
        mapper = new ObjectMapper();
    }
}
