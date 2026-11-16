package com.example.blogdb.dto;

public class PostRequestDTO {
    private String title;
    private String category;
    private String contentType;
    private String content;
    private byte[] imageData;
    private String imageContentType;

    public String getTitle() { return title; }
    public String getCategory() { return category; }
    public String getContentType() { return contentType; }
    public String getContent() { return content; }
    public byte[] getImageData() { return imageData; }
    public String getImageContentType() { return imageContentType; }
    public void setTitle(String title) { this.title = title; }
    public void setCategory(String category) { this.category = category; }
    public void setContentType(String contentType) { this.contentType = contentType; }
    public void setContent(String content) { this.content = content; }
    public void setImageData(byte[] imageData) { this.imageData = imageData; }
    public void setImageContentType(String imageContentType) { this.imageContentType = imageContentType; }
}
