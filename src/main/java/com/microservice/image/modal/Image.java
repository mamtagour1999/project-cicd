package com.microservice.image.modal;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "images")
public class Image {
    @Id
    private String id;
    private String name;
    private String smallImage;   // Base64 encoded string
    private String mediumImage;  // Base64 encoded string
    private String largeImage;   // Base64 encoded string
}

