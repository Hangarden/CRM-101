package com.javaex.dao;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.List;
import com.javaex.vo.BoardVo;
public class BoardDaoTest {
    private BoardDao dao;
    @Before
    public void setUp() {
        dao = new BoardDaoImpl();
    }
    @Test
    public void testGetList() {
        List<BoardVo> list = dao.getList();
        assertNotNull("리스트 가져오기 실패", list);
        assertFalse("리스트가 비어있습니다", list.isEmpty());
    }
    @Test
    public void testGetSubList() {
        int nowPage = 1;
        int pagePerNum = 5;
        String option = "title"; // 예시로 title을 사용
        String kwd = "테스트"; // 검색 키워드 예시
        List<BoardVo> subList = dao.getSubList(nowPage, pagePerNum, option, kwd);
        assertNotNull("서브리스트 가져오기 실패", subList);
        // 추가적인 검증 로직을 구현할 수 있습니다.
    }
    @Test
    public void testGetTotalRecord() {
        String keyField = "title"; // 예시 필드
        String keyWord = "테스트"; // 예시 키워드
        int totalRecord = dao.getTotalRecord(keyField, keyWord);
        assertTrue("레코드 수가 0 이하입니다", totalRecord > 0);
    }
    @Test
    public void testGetBoard() {
        int no = 1; // 테스트할 게시글 번호
        BoardVo board = dao.getBoard(no);
        assertNotNull("게시글 가져오기 실패", board);
        assertEquals("번호가 일치하지 않습니다", no, board.getNo());
    }
    @Test
    public void testInsert() {
        BoardVo vo = new BoardVo("테스트 제목", "테스트 내용", 1);
        int count = dao.insert(vo);
        assertEquals("삽입 실패", 1, count);
    }
    @Test
    public void testDelete() {
        int no = 1; // 삭제할 게시글 번호
        int count = dao.delete(no);
        assertEquals("삭제 실패", 1, count);
    }
    @Test
    public void testUpdate() {
        BoardVo vo = new BoardVo(1, "수정된 제목", "수정된 내용");
        int count = dao.update(vo);
        assertEquals("수정 실패", 1, count);
    }
    @Test
    public void testSearch() {
        String str = "검색어";
        String option = "title";
        List<BoardVo> list = dao.search(str, option);
        assertNotNull("검색 실패", list);
        // 추가적인 검증 로직을 구현할 수 있습니다.
    }
    @Test
    public void testHitUp() {
        BoardVo vo = new BoardVo();
        vo.setNo(1); // 조회수를 증가시킬 게시글 번호
        vo.setHit(1); // 조회수 증가량
        int count = dao.hitUp(vo);
        assertEquals("조회수 증가 실패", 1, count);
    }
}
