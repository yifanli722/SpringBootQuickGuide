package com.SpringQuickGuide.ImageCrud.controller;

import com.SpringQuickGuide.ImageCrud.service.ImageService;
import com.SpringQuickGuide.ImageCrud.service.Result;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/*
Controller classes, Map Url -> Method
Mark Methods with @GetMapping, @PostMapping, @PutMapping, or @DeleteMapping
*/
@RestController
@RequestMapping("/api")
public class ImageController {

    /*
    Example of Spring constructor injection
    This class + its dependency (ImageService) is handled by Spring.
    Autowired not needed when 1 constructor
     */
    private final ImageService imageService;
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    // Handles a GET at api/RetrieveImage/{imageHash}, imageHash is a path parameter and is retrieved with @PathVariable
    @GetMapping("/RetrieveImage/{imageHash}")
    public ResponseEntity<?> getImage(@PathVariable String imageHash) {
        Result<byte[]> retrievedImageResult = imageService.handleImageRetrieval(imageHash);
        if(retrievedImageResult.getResult() == null) {
            Map<String, String> response = new HashMap<>();
            response.put("Error", retrievedImageResult.getErrorMsg());
            return ResponseEntity.status(retrievedImageResult.getStatusCode()).body(response);
        }
        ByteArrayResource resource = new ByteArrayResource(retrievedImageResult.getResult());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }

    // Handles a Put at api/UploadImage, expects the post body to contain binary data
    @PostMapping("/UploadImage")
    public ResponseEntity<Map<String, String>> uploadImage(@RequestBody byte[] imageData) {
        Map<String, String> response = new HashMap<>();

        Result<String> processImageResult = imageService.processImageUpload(imageData);
        ResponseEntity.BodyBuilder builder = ResponseEntity.status(processImageResult.getStatusCode());
        if(processImageResult.getResult() == null) {
            response.put("Error", processImageResult.getErrorMsg());
            return builder.body(response);
        }
        response.put("sha256", processImageResult.getResult());
        return builder.body(response);
    }
}
