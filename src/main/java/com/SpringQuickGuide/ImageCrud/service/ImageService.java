package com.SpringQuickGuide.ImageCrud.service;

import com.SpringQuickGuide.ImageCrud.model.ImageEntity;
import com.SpringQuickGuide.ImageCrud.repository.ImageRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/*
- Services contain code to handle REST requests intercepted by a controller.
- May interact with repository, a separate class for handling DB operations
 */
@Service
public class ImageService {
    private final ImageRepository imageRepository;
    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public Result<String> processImageUpload(byte[] imageData) {
        try {
            String sha256 = calculateSHA256(imageData);
            if(sha256 == null) {
                return new Result<>(null, "Error calculating sha256", HttpStatus.INTERNAL_SERVER_ERROR);
            }

            if(imageRepository.existsBySha256(sha256)) {
                return new Result<>(sha256, null, HttpStatus.OK);
            }

            Result<byte[]> jpegConversionResult = convertToJPEG(imageData);
            if(jpegConversionResult.result == null) {
                return new Result<>(null, jpegConversionResult.errorMsg, jpegConversionResult.responseCode);
            }
            try {
                ImageEntity entity = imageRepository.save(new ImageEntity(sha256, imageData));
                if(entity == null) {
                    return new Result<>(null, "Failed to persist into DB, entity is null", HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } catch (DataAccessException ex) {
                return new Result<>(null, "Failed to persist into DB, DataAccessException", HttpStatus.INTERNAL_SERVER_ERROR);
            }

            return new Result<>(sha256, null, HttpStatus.OK);
        } catch (IOException e) {
            return new Result<>(null, "Unable to close stream resources", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public Result<byte[]> handleImageRetrieval(String sha256) {
        if(!imageRepository.existsBySha256(sha256)) {
            return new Result<>(null, String.format("No media found with key %s", sha256), HttpStatus.NOT_FOUND);
        }
        ImageEntity retrievedImage = imageRepository.findBySha256(sha256);
        return new Result<>(retrievedImage.getData(), null, HttpStatus.OK);
    }

    public Result<Boolean> handleImageDeletion(String sha256) {
        try {
            imageRepository.deleteById(sha256);
            return new Result<>(true, null, HttpStatus.OK);
        } catch (Exception e) {
            return new Result<>(false, e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Result<byte[]> convertToJPEG(byte[] imageData) throws IOException {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(imageData);
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            BufferedImage image;
            try {
                image = ImageIO.read(inputStream);
                //If PNG has alpha channel, must remove before converting to jpeg
                if(image.getType() == BufferedImage.TYPE_4BYTE_ABGR) {
                    image = removeImageAlphaChannel(image);
                }
            } catch (IOException e) {
                return new Result<>(null, "Input file is not an of type Jpeg/Jpg/Png/Gif", HttpStatus.UNSUPPORTED_MEDIA_TYPE);
            }

            if (image == null) {
                return new Result<>(null, "Invalid image data", HttpStatus.BAD_REQUEST);
            }

            try {
                ImageIO.write(image, "jpg", outputStream);
            } catch (IOException e) {
                return new Result<>(null, "Error converting to JPEG", HttpStatus.INTERNAL_SERVER_ERROR);
            }

            return new Result<>(outputStream.toByteArray(), null, HttpStatus.OK);
        }
    }

    private BufferedImage removeImageAlphaChannel(BufferedImage image) throws IOException {
        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_3BYTE_BGR);

        Graphics2D g2d = newImage.createGraphics();
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(newImage, "png", outputStream);

        // Convert the byte array back to a BufferedImage
        ByteArrayInputStream outputInputStream = new ByteArrayInputStream(outputStream.toByteArray());

        return ImageIO.read(outputInputStream);
    }

    private String calculateSHA256(byte[] data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(data);

            StringBuilder stringBuilder = new StringBuilder();
            for (byte b : hash) {
                stringBuilder.append(String.format("%02x", b));
            }
            return stringBuilder.toString();

        } catch (NoSuchAlgorithmException e) {
            // Handle the exception appropriately
            e.printStackTrace();
            return null;
        }
    }
}

