package com.microservice.image.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

import org.apache.tomcat.util.http.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.microservice.image.dto.ImageDto;
import com.microservice.image.modal.Image;
import com.microservice.image.repository.ImageRepository;

import net.coobird.thumbnailator.Thumbnails;

@Service
public class ImageService {

    @Autowired
    private ImageRepository imageRepository;

    

    /*
     * method to get image by it size
     */
    public ImageDto processImageDownload(String id, String size) {
        return imageRepository.findById(id)
                .map(image -> {
                    String imageData;
                    switch (size.toLowerCase()) {
                        case "small":
                            imageData = image.getSmallImage();
                            break;
                        case "medium":
                            imageData = image.getMediumImage();
                            break;
                        case "large":
                            imageData = image.getLargeImage();
                            break;
                        default:
                            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid image size: " + size);
                    }
                    return createImageDownloadInfo(imageData);
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Image not found with id: " + id));
    }

    private ImageDto createImageDownloadInfo(String base64Data) {
        String[] parts = base64Data.split(",");
        String dataPart = parts[1];
        String metadata = parts[0];
        String format = metadata.substring(metadata.indexOf("/") + 1, metadata.indexOf(";"));
        byte[] imageData = Base64.getDecoder().decode(dataPart);
        return new ImageDto(imageData, "image/" + format, format);
    }

    /*
     * method to upload image
     */
    public ResponseEntity<String> uploadImage(MultipartFile imagefile) throws IOException {
        try {
            if (imagefile == null || imagefile.isEmpty()) {
                return ResponseEntity.badRequest().body("Image file is empty or null");
            }

            String originalFormat = this.getFormat(imagefile.getOriginalFilename());
            Image image = new Image();
            image.setLargeImage(image == null ? "" : this.resizeImage(imagefile, 1024, originalFormat));
            image.setMediumImage(image == null ? "" : this.resizeImage(imagefile, 512, originalFormat));
            image.setSmallImage(image == null ? "" : this.resizeImage(imagefile, 256, originalFormat));

            image.setName(imagefile.getName());
            imageRepository.save(image);

            return ResponseEntity.ok("Image Created Successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Bad Request: " + e.getMessage());
        }
    }

    /*
     * method to get image extention ("png", "jpg", "gif")
     */
    public String getFormat(String filename) {
        // Extract the format from the filename (e.g., "png", "jpg", "gif")
        String[] parts = filename.split("\\.");
        return parts[parts.length - 1].toLowerCase();
    }

    /*
     * method to resize image
     */
    public String resizeImage(MultipartFile imageFile, int width, String format) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Thumbnails.of(imageFile.getInputStream())
                .size(width, width)
                .outputFormat(format)
                .toOutputStream(outputStream); // Write to the output stream
        return base64Encode(outputStream.toByteArray(), format); // Get the byte array from the stream
    }

    /*
     * method to convert image to base64
     * You can add format-specific compression methods here
     */
    public String base64Encode(byte[] imageData, String format) {
        return "data:image/" + format + ";base64," + Base64.getEncoder().encodeToString(imageData);
    }

}
