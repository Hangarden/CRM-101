package com.javaex.vo;

public class AttachmentVo {
    private int id;
    private String fileName;
    private String fileType;
    private String filePath;
    private int userNo;
    private int boardNo;

//    public AttachmentVo(int id, String fileName, String fileType, String filePath, int userNo, int boardNo) {
//        this.id = id;
//        this.fileName = fileName;
//        this.fileType = fileType;
//        this.filePath = filePath;
//        this.userNo = userNo;
//        this.boardNo = boardNo;
//    }

    // 기본 생성자
    public AttachmentVo() {
    }

    // 기존의 매개변수를 받는 생성자
    public AttachmentVo(int id, String fileName, String fileType, String filePath, int userNo, int boardNo) {
        this.id = id;
        this.fileName = fileName;
        this.fileType = fileType;
        this.filePath = filePath;
        this.userNo = userNo;
        this.boardNo = boardNo;
    }




    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getUserNo() {
        return userNo;
    }

    public void setUserNo(int userNo) {
        this.userNo = userNo;
    }

    public int getBoardNo() {
        return boardNo;
    }

    public void setBoardNo(int boardNo) {
        this.boardNo = boardNo;
    }



    // 생성자, getter, setter ...
}
