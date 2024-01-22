package com.javaex.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import com.javaex.dao.BoardDao;
import com.javaex.dao.BoardDaoImpl;
import com.javaex.util.WebUtil;
import com.javaex.vo.AttachmentVo;
import com.javaex.vo.BoardVo;
import com.javaex.vo.UserVo;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

@WebServlet("/board")
public class BoardServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		String actionName = request.getParameter("a"); // 멀티파트파일이 씹힌다.
		System.out.println("board:" + actionName);

		if ("list".equals(actionName)) {
			// 리스트 가져오기
			BoardDao dao = new BoardDaoImpl();
			List<BoardVo> list = dao.getList();

			System.out.println(list.toString());
			

			//필요한 인수들 생성 및 정의
			int totalRecord=0; //전체레코드수
			int numPerPage=10; // 페이지당 레코드 수 
			int pagePerBlock=5; //블럭당 페이지수 
			  
			int totalPage=0; //전체 페이지 수
			int totalBlock=0;  //전체 블럭수 
	
			int nowPage=1; // 현재페이지
			int nowBlock=1;  //현재블럭
			  
			int start=0; //디비의 select 시작번호
			int end=9; //시작번호로 부터 가져올 select 갯수
			
			totalRecord=list.size();
			if(request.getParameter("nowPage")!=null) {
				nowPage=Integer.parseInt((String)request.getParameter("nowPage"));
				System.out.println("nowPage:"+nowPage);
			}
			totalPage=(int)Math.ceil((double)totalRecord/numPerPage);
			totalBlock=(int)Math.ceil((double)totalPage/pagePerBlock);
			nowBlock=(int)Math.ceil((double)nowPage/pagePerBlock);
			
			start=numPerPage*(nowPage-1);
			//end=10;
			list=list.subList(start, Math.min(start+numPerPage,totalRecord));
			
			request.setAttribute("pagePerBlock", pagePerBlock);
			request.setAttribute("totalRecord", totalRecord);
			request.setAttribute("totalPage", totalPage);
			request.setAttribute("totalBlock", totalBlock);
			request.setAttribute("nowBlock", nowBlock);
			request.setAttribute("start", start);
			request.setAttribute("nowPage", nowPage);
			
			// 리스트 화면에 보내기
			request.setAttribute("list", list);
			//WebUtil.forward(request, response, "/WEB-INF/views/board/list.jsp");
			RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/board/list.jsp");
			rd.forward(request, response);
	    
		} else if ("read".equals(actionName)) {
			// 게시물 가져오기
			int no = Integer.parseInt(request.getParameter("no"));
			System.out.println(no);
			BoardDao dao = new BoardDaoImpl();
			BoardVo boardVo = dao.getBoard(no);
			System.out.println(boardVo);
			
			//조회수 올리기
			int hitCount = boardVo.getHit();
			hitCount++;
			boardVo.setHit(hitCount);
			dao.hitUp(boardVo);

			// 게시물 화면에 보내기
			request.setAttribute("boardVo", boardVo);
			WebUtil.forward(request, response, "/WEB-INF/views/board/read.jsp");
		} else if ("modifyform".equals(actionName)) {
			// 게시물 가져오기
			int no = Integer.parseInt(request.getParameter("no"));
			BoardDao dao = new BoardDaoImpl();
			BoardVo boardVo = dao.getBoard(no);

			// 게시물 화면에 보내기
			request.setAttribute("boardVo", boardVo);
			WebUtil.forward(request, response, "/WEB-INF/views/board/modifyform.jsp");
		} else if ("writeform".equals(actionName)) {
			// 로그인 여부체크
			UserVo authUser = getAuthUser(request);
			if (authUser != null) { // 로그인했으면 작성페이지로
				WebUtil.forward(request, response, "/WEB-INF/views/board/writeform.jsp");
			} else { // 로그인 안했으면 리스트로
				WebUtil.redirect(request, response, "/mysite/board?a=list");
			}

		} 
//		else if ("write".equals(actionName)) {
//			UserVo authUser = getAuthUser(request);
//
//			String title = request.getParameter("title");
//			String content = request.getParameter("content");
//			
//			int userNo = authUser.getNo();
//			System.out.println("userNo : ["+userNo+"]");
//			System.out.println("title : ["+title+"]");
//			System.out.println("content : ["+content+"]");
//
//			BoardVo vo = new BoardVo(title, content, userNo);
//			BoardDao dao = new BoardDaoImpl();
//			dao.insert(vo);
//
//			WebUtil.redirect(request, response, "/mysite/board?a=list");
//
//		} 
		
		else if ("delete".equals(actionName)) {
			int no = Integer.parseInt(request.getParameter("no"));

			BoardDao dao = new BoardDaoImpl();
			dao.delete(no);

			WebUtil.redirect(request, response, "/mysite/board?a=list");
			
		} else if ("search".equals(actionName)) {
			
			String str = request.getParameter("kwd");
			String option = request.getParameter("search_option");
						
			BoardDao dao = new BoardDaoImpl();
			List<BoardVo> list = dao.search(str, option);
						
			// 리스트 화면에 보내기
			request.setAttribute("list", list);
			request.setAttribute("str", str);
			
			RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/board/list.jsp");
		    rd.forward(request, response);

		} else {
			WebUtil.redirect(request, response, "/mysite/board?a=list");
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {
	    request.setCharacterEncoding("UTF-8");
	    String actionName = request.getParameter("a");
	    
	    // 파일 파트 로깅을 위한 코드 추가
	    try {
	        Part filePart = request.getPart("file");
	        if (filePart != null) {
	            System.out.println("File part received: " + filePart.getName());
	            System.out.println("File size: " + filePart.getSize());
	            System.out.println("File content type: " + filePart.getContentType());
	        } else {
	            System.out.println("No file part received.");
	        }
	    } catch (Exception e) {
	        System.out.println("Error retrieving file part.");
	        e.printStackTrace();
	    }

	    if ("write".equals(actionName)) {
	        UserVo authUser = getAuthUser(request);
	        String title = request.getParameter("title");
	        String content = request.getParameter("content");
	        int userNo = authUser.getNo();
	        BoardVo vo = new BoardVo(title, content, userNo);
	        BoardDao dao = new BoardDaoImpl();

	        WebUtil.redirect(request, response, "/mysite/board?a=list");
	    }
	}
	
//	protected void doPost(HttpServletRequest request, HttpServletResponse response)
//	        throws ServletException, IOException {
//	    request.setCharacterEncoding("UTF-8");
//
//	    // 파일 업로드 경로 설정
//	    String savePath = "upload";
//	    int uploadFileSizeLimit = 5 * 1024 * 1024; // 5MB
//	    String encType = "UTF-8";
//	    ServletContext context = getServletContext();
//	    String uploadFilePath = context.getRealPath(savePath);
//
//	    // MultipartRequest 객체 생성
//	    MultipartRequest multi = new MultipartRequest(
//	        request,
//	        uploadFilePath,
//	        uploadFileSizeLimit,
//	        encType,
//	        new DefaultFileRenamePolicy()
//	    );
//
//	    // 게시글 정보 처리
//	    String title = multi.getParameter("title");
//	    String content = multi.getParameter("content");
//	    UserVo authUser = getAuthUser(request);
//	    int userNo = authUser.getNo();
//
//	    // 데이터베이스에 게시글 정보 저장
//	    BoardVo boardVo = new BoardVo(title, content, userNo);
//	    BoardDao boardDao = new BoardDaoImpl();
//	    int boardNo = boardDao.insert(boardVo);
//	    
//	    
//
//	    // 디렉토리 생성 확인
//	    File uploadDir = new File(uploadFilePath);
//	    if (!uploadDir.exists()) {
//	        uploadDir.mkdirs(); // 디렉토리가 없다면 생성
//	    }
//
//	    // 파일 정보 처리
//	    String fileName = multi.getFilesystemName("uploadFile");
//	    if (fileName != null) {
//	        // 데이터베이스에 파일 정보 저장
//	        String filePath = uploadFilePath + File.separator + fileName;
//	        AttachmentVo attachmentVo = new AttachmentVo();
//	        attachmentVo.setFileName(fileName);
//	        attachmentVo.setFilePath(filePath);
//	        attachmentVo.setBoardNo(boardNo); // 게시글 번호 연결
//	        attachmentVo.setUserNo(userNo);
//
//	        // 파일 정보 저장 로직 구현
//	        BoardDao attachmentDao = new BoardDaoImpl(); // 혹은 AttachmentDao의 구현체
//	        attachmentDao.insertAttachment(attachmentVo);
//	    }
//
//	    // Redirect to board list
//	    WebUtil.redirect(request, response, "/mysite/board?a=list");
//	}


	// 로그인 되어 있는 정보를 가져온다.
	protected UserVo getAuthUser(HttpServletRequest request) {
		HttpSession session = request.getSession();
		UserVo authUser = (UserVo) session.getAttribute("authUser");

		return authUser;
	}
	
	private String extractFileName(Part part) {
	    String contentDisp = part.getHeader("content-disposition");
	    String[] items = contentDisp.split(";");
	    for (String s : items) {
	        if (s.trim().startsWith("filename")) {
	            return s.substring(s.indexOf("=") + 2, s.length() - 1);
	        }
	    }
	    return "";
	}
	
	private void saveFile(Part filePart, String fileName) throws IOException {
	    String directoryPath = "C:\\path\\to\\uploads"; // 실제 경로로 변경하세요
	    File directory = new File(directoryPath);
	    if (!directory.exists()) {
	        directory.mkdirs(); // 디렉토리가 존재하지 않는 경우 생성
	    }

	    String filePath = directoryPath + File.separator + fileName;
	    filePart.write(filePath);
	}


}