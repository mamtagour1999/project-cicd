package com.microservice.image.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.microservice.image.modal.Image;

public interface ImageRepository extends MongoRepository<Image, String> {
}
