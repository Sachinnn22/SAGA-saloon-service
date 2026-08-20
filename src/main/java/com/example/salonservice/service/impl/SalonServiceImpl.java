package com.example.salonservice.service.impl;

import com.example.salonservice.dto.SalonRequestDto;
import com.example.salonservice.dto.SalonResponseDto;
import com.example.salonservice.entity.Salon;
import com.example.salonservice.repository.SalonRepository;
import com.example.salonservice.service.SalonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SalonServiceImpl implements SalonService {

    private final SalonRepository salonRepository;

    @Override
    public SalonResponseDto registerSalon(SalonRequestDto requestDto) {
        Salon salon = Salon.builder()
                .name(requestDto.getName())
                .address(requestDto.getAddress())
                .phone(requestDto.getPhone())
                .description(requestDto.getDescription())
                .ownerEmail(requestDto.getOwnerEmail())
                .build();

        Salon savedSalon = salonRepository.save(salon);
        return mapToDto(savedSalon);
    }

    @Override
    public List<SalonResponseDto> getAllSalons() {
        return salonRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public SalonResponseDto getSalonById(Long id) {
        Salon salon = salonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Salon not found with id: " + id));
        return mapToDto(salon);
    }

    private SalonResponseDto mapToDto(Salon salon) {
        return SalonResponseDto.builder()
                .id(salon.getId())
                .name(salon.getName())
                .address(salon.getAddress())
                .phone(salon.getPhone())
                .description(salon.getDescription())
                .ownerEmail(salon.getOwnerEmail())
                .build();
    }
}