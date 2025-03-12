package com.microservice.image.dto;

import lombok.Data;

@Data
public class ImageDto {

    private final byte[] imageData;
    private final String contentType;
    private final String fileExtension;
}
