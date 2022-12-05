package com.ssafy.happyhouse.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.happyhouse.controller.dto.house.HouseDetailDto;
import com.ssafy.happyhouse.controller.dto.house.HouseDetailDto.HouseDetail;
import com.ssafy.happyhouse.controller.dto.house.HouseDetailSearchQuery;
import com.ssafy.happyhouse.service.HouseService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "주택 거래 정보")
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/house")
public class HouseController {

    private final HouseService houseService;

    @Operation(summary = "주택 거래 정보 조회")
    @GetMapping
    public ResponseEntity<HouseDetailDto> houses(
            HouseDetailSearchQuery searchQuery
    ) {
        try {
            final List<HouseDetail> houseDetails = houseService.houses(searchQuery);
            final HouseDetailDto houseDetailDto = HouseDetailDto.builder()
                                                                .message("SUCCESS")
                                                                .items(houseDetails)
                                                                .build();
            return ResponseEntity.ok(houseDetailDto);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    public HouseController(HouseService houseService) {
        this.houseService = houseService;
    }
}
