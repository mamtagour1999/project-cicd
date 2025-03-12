package com.microservice.image.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.microservice.image.dto.ImageDto;
import com.microservice.image.service.ImageService;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/images")
public class ImageController {

    @Autowired
    private ImageService imageService;

    /*
     * Api to upload image in three size (small, medium, large) in base64
     */
    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        log.info("uploading image");
        try {
            return imageService.uploadImage(file);
        } catch (IOException e) {
            log.error("Error uploading image", e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error uploading image: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Unexpected error during image upload", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error during image upload",
                    e);
        }
    }

    /*
     * Api to get image by it specific size(small, medium, large)
     */
    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getImageBySize(@PathVariable String id, @RequestParam(value = "size", required = false) String size) {
        log.info("downloading image with id: {} and size: {}", id, size);
        try {
            String imageSize = size != null ? size : "small";
            ImageDto info = imageService.processImageDownload(id, imageSize);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(info.getContentType()));
            headers.setContentDisposition(ContentDisposition.inline().filename("image." + info.getFileExtension()).build());
            return new ResponseEntity<>(info.getImageData(), headers, HttpStatus.OK);
        } catch (ResponseStatusException e){
            throw e;
        }
        catch (Exception e) {
            log.error("Error retrieving image", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
        }
    }
}
