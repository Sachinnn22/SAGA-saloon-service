package com.example.salonservice.controller;

import com.example.salonservice.dto.ApiResponse;
import com.example.salonservice.dto.SalonRequestDto;
import com.example.salonservice.dto.SalonResponseDto;
import com.example.salonservice.service.SalonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/salons")
@RequiredArgsConstructor
public class SalonController {

    private final SalonService salonService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<SalonResponseDto>> registerSalon(@RequestBody SalonRequestDto requestDto) {
        SalonResponseDto responseDto = salonService.registerSalon(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Salon registered successfully", responseDto));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<SalonResponseDto>>> getAllSalons() {
        List<SalonResponseDto> salons = salonService.getAllSalons();
        return ResponseEntity.ok(ApiResponse.success("Salons fetched successfully", salons));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SalonResponseDto>> getSalonById(@PathVariable Long id) {
        SalonResponseDto salon = salonService.getSalonById(id);
        return ResponseEntity.ok(ApiResponse.success("Salon fetched successfully", salon));
    }
}