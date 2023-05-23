package com.SpringQuickGuide.ImageCrud.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "image_storage")
public class ImageEntity {

    @Id
    @Column(name = "sha256")
    private String sha256;

    @Column(name = "data", columnDefinition = "bytea")
    private byte[] data;

    // Constructors, getters, and setters

    public ImageEntity() {
    }

    public ImageEntity(String sha256, byte[] data) {
        this.sha256 = sha256;
        this.data = data;
    }

    public String getSha256() {
        return sha256;
    }

    public void setSha256(String sha256) {
        this.sha256 = sha256;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}

