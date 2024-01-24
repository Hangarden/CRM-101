package com.javaex.vo;

public class BoardVo {

	private int no;
	private String title;
	private String content;
	private int hit;
	private String regDate;
	private int userNo;
	private String userName;
    private String filename;  // 파일 이름
    private int filesize;    // 파일 크기
	private String filename2;
	private int filesize2;
	
	public BoardVo(String title) {
		this.title = title;
	}

	public BoardVo() {
	}
	
	public BoardVo(int no, String title, String content) {
		this.no = no;		
		this.title = title;
		this.content = content;
	}

	public BoardVo(String title, String content, int userNo) {
		this.title = title;
		this.content = content;
		this.userNo = userNo;
	}
	
	public BoardVo(int no, String title, int hit, String regDate, int userNo, String userName) {
		this.no = no;
		this.title = title;
		this.hit = hit;
		this.regDate = regDate;
		this.userNo = userNo;
		this.userName = userName;
	}
	
	public BoardVo(int no, String title, String content, int hit, String regDate, int userNo, String userName, int filesize, String filename, int filesize2, String filename2 ) {
		this(no, title, hit, regDate, userNo, userName);
		this.content = content;
        this.filename = filename;
        this.filesize = filesize;
        this.filename2 = filename2;
        this.filesize2 = filesize2;
        System.out.println("이거임?");
	}
	
    // 새로운 생성자: 파일 이름과 파일 크기를 포함
//	public BoardVo(int no, String title, String content,int hit, String regDate, int userNo, String userName, String filename, int filesize) {
//		this.no = no;
//		this.title = title;
//		this.content = content;
//		this.hit = hit;
//		this.regDate = regDate;
//		this.userNo = userNo;
//		this.userName = userName;
//        this.filename = filename;
//        this.filesize = filesize;
//	}
	

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getHit() {
		return hit;
	}

	public void setHit(int hit) {
		this.hit = hit;
	}

	public String getRegDate() {
		return regDate;
	}

	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}

	public int getUserNo() {
		return userNo;
	}

	public void setUserNo(int userNo) {
		this.userNo = userNo;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	
	public int getFilesize() {
		return filesize;
	}

	public String getFilename() {
		// TODO Auto-generated method stub
		return filename;
	}

	public void setFilename(String filename) {

		this.filename = filename;
		
	}

	public void setFilesize(int filesize) {
		this.filesize = filesize;
		
	}
	
	
	public int getFilesize2() {
		return filesize2;
	}

	public String getFilename2() {
		// TODO Auto-generated method stub
		return filename2;
	}

	public void setFilename2(String filename2) {
		this.filename2 = filename2;
	}

	public void setFilesize2(int filesize2) {
		this.filesize2 = filesize2;
	}
	
	
	
	
	
	@Override
	public String toString() {
		return "BoardVo [no=" + no + ", title=" + title + ", content=" + content + ", hit=" + hit + ", regDate="
				+ regDate + ", userNo=" + userNo + ", userName=" + userName + ", filename= " + filename +  ", filesize= " + filesize +", filename2= \" + filename2 +  \", filesize2= \" + filesize2 +\" ]";
	}



}
