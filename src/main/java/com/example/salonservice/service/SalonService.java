package com.example.salonservice.service;

import com.example.salonservice.dto.SalonRequestDto;
import com.example.salonservice.dto.SalonResponseDto;

import java.util.List;

public interface SalonService {
    SalonResponseDto registerSalon(SalonRequestDto requestDto);
    List<SalonResponseDto> getAllSalons();
    SalonResponseDto getSalonById(Long id);
    SalonResponseDto updateSalon(Long id, SalonRequestDto requestDto); 
    void deleteSalon(Long id); 
}