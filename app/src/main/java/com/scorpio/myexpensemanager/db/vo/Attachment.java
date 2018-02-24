package com.scorpio.myexpensemanager.db.vo;

/**
 * Created by hkundu on 01-08-2016.
 * for My Expense Manager
 */
public class Attachment extends BaseVO {
    private Long id;
    private byte[] image;
    private String fileName = null;
    private Integer size;
    private String comments;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
