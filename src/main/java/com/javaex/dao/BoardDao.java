package com.javaex.dao;

import java.util.List;

import com.javaex.vo.AttachmentVo;
import com.javaex.vo.BoardVo;

public interface BoardDao {
	public List<BoardVo> getList();  // 게시물 전체 목록 조회
	public List<BoardVo> getSubList(int startNum,int pagePerNum,String option,String kwd); //한 페이지의 게시물 목록 조회
	public int getTotalRecord(String keyField,String keyWord);//게시물 총 갯수 가져오기
	public BoardVo getBoard(int no); // 게시물 상세 조회
	public int insert(BoardVo vo);   // 게시물 등록
	public int delete(int no);       // 게시물 삭제
	public int update(BoardVo vo);   // 게시물 수정
	public List<BoardVo> search(String str, String option);  // 게시물 검색
	public int hitUp(BoardVo vo); // 조회수 증가
//	public void insertAttachment(AttachmentVo attachment);
//	public List<AttachmentVo> getAttachmentsByBoardNo(int boardNo);
}
