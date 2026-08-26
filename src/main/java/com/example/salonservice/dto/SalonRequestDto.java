package com.example.salonservice.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class SalonRequestDto {
    private String name;
    private String address;
    private String phone;
    private String description;
    private String ownerEmail;
    private MultipartFile image;
}