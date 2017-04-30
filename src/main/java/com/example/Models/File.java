package com.example.Models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by baraa on 4/9/2017.
 */
@Entity
public class File {

    @Id
    @GeneratedValue
    private Long fileId;
    private String fileName;
    private String fileType;
    private Long caseId;
    private Long userId;
    private Long postId;
    private byte[] file;
public File(){}
    public File(String fileName, String fileType, Long caseId, Long userId,Long postId, byte[] file) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.caseId = caseId;
        this.userId = userId;
        this.postId = postId;
        this.file = file;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCaseId() {
        return caseId;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public void setCaseId(Long caseId) {
        this.caseId = caseId;
    }
}
