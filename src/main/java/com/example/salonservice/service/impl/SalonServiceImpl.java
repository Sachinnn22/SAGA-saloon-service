package com.example.salonservice.service.impl;

import com.example.salonservice.dto.SalonRequestDto;
import com.example.salonservice.dto.SalonResponseDto;
import com.example.salonservice.entity.Salon;
import com.example.salonservice.repository.SalonRepository;
import com.example.salonservice.config.GcpStorageService; 
import com.example.salonservice.service.SalonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SalonServiceImpl implements SalonService {

    private final SalonRepository salonRepository;
    private final GcpStorageService gcpStorageService; 

    @Override
    public SalonResponseDto registerSalon(SalonRequestDto requestDto) {
        String imageUrl = null;
        try {
            if (requestDto.getImage() != null && !requestDto.getImage().isEmpty()) {
                imageUrl = gcpStorageService.uploadFile(requestDto.getImage()); 
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image to GCP", e);
        }

        Salon salon = Salon.builder()
                .name(requestDto.getName())
                .address(requestDto.getAddress())
                .phone(requestDto.getPhone())
                .description(requestDto.getDescription())
                .ownerEmail(requestDto.getOwnerEmail())
                .imageUrl(imageUrl) 
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

    @Override
    public SalonResponseDto updateSalon(Long id, SalonRequestDto requestDto) {
        Salon existingSalon = salonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Salon not found with id: " + id));

        existingSalon.setName(requestDto.getName());
        existingSalon.setAddress(requestDto.getAddress());
        existingSalon.setPhone(requestDto.getPhone());
        existingSalon.setDescription(requestDto.getDescription());
        existingSalon.setOwnerEmail(requestDto.getOwnerEmail());

        try {
            if (requestDto.getImage() != null && !requestDto.getImage().isEmpty()) {
                String imageUrl = gcpStorageService.uploadFile(requestDto.getImage());
                existingSalon.setImageUrl(imageUrl);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to update image", e);
        }

        Salon updatedSalon = salonRepository.save(existingSalon);
        return mapToDto(updatedSalon);
    }

    @Override
    public void deleteSalon(Long id) {
        if (!salonRepository.existsById(id)) {
            throw new RuntimeException("Salon not found with id: " + id);
        }
        salonRepository.deleteById(id);
    }

    private SalonResponseDto mapToDto(Salon salon) {
        return SalonResponseDto.builder()
                .id(salon.getId())
                .name(salon.getName())
                .address(salon.getAddress())
                .phone(salon.getPhone())
                .description(salon.getDescription())
                .ownerEmail(salon.getOwnerEmail())
                .imageUrl(salon.getImageUrl()) 
                .build();
    }
}