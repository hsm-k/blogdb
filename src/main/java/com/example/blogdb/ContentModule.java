package com.example.blogdb;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "content_module")
public class ContentModule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 콘텐츠 모듈 N개 : 게시글 1개 (FK는 content_module.post_id)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Enumerated(EnumType.STRING)
    private ContentModuleType type;

    @Lob
    private String text;

    private String location;

    private Integer displayOrder;

    public Long getId() { return id; }
    public Post getPost() { return post; }
    public ContentModuleType getType() { return type; }
    public String getText() { return text; }
    public String getLocation() { return location; }
    public Integer getDisplayOrder() { return displayOrder; }
    public void setPost(Post post) { this.post = post; }
    public void setType(ContentModuleType type) { this.type = type; }
    public void setText(String text) { this.text = text; }
    public void setLocation(String location) { this.location = location; }
    public void setDisplayOrder(Integer displayOrder) { this.displayOrder = displayOrder; }
}
