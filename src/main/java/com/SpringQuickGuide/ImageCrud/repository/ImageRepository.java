package com.SpringQuickGuide.ImageCrud.repository;

import com.SpringQuickGuide.ImageCrud.model.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/*
Interacts with Databases through CRUD operations
Implementations of methods are autofilled by Spring
*/
@Repository
public interface ImageRepository extends JpaRepository<ImageEntity, String> {
    boolean existsBySha256(String sha256);
    ImageEntity findBySha256(String sha256);
    ImageEntity save(ImageEntity imageEntity);
    @Override
    void deleteById(String sha256);
}
