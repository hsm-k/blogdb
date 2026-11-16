package com.example.blogdb.dto;

import com.example.blogdb.ContentModuleType;

public class ContentModuleDTO {
    private final Long id;
    private final ContentModuleType type;
    private final String text;
    private final String location;
    private final int displayOrder;

    public ContentModuleDTO(Long id, ContentModuleType type, String text, String location, int displayOrder) {
        this.id = id;
        this.type = type;
        this.text = text;
        this.location = location;
        this.displayOrder = displayOrder;
    }

    public Long getId() { return id; }
    public ContentModuleType getType() { return type; }
    public String getText() { return text; }
    public String getLocation() { return location; }
    public int getDisplayOrder() { return displayOrder; }
}
