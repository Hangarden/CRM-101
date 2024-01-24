package com.javaex.controller;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.javaex.dao.BoardDao;
import com.javaex.dao.BoardDaoImpl;
import com.javaex.util.WebUtil;
import com.javaex.vo.BoardVo;
import com.javaex.vo.UserVo;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;


@WebServlet("/boardwrite")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2,  // 2MB
        maxFileSize = 1024 * 1024 * 10,       // 10MB
        maxRequestSize = 1024 * 1024 * 50     // 50MB
)
public class BoardFileServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	// 파일저장소 경로
	private static final String SAVEFOLDER = "/javastudy/workspace/mysite/src/main/webapp/WEB-INF/uploadfile";
	private static final String ENCTYPE = "UTF-8";
	private static int MAXSIZE = 5 * 1024 * 1024;
	/* 파일첨부 끝 경성 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String filename = null;
		int filesize = 0;
		MultipartRequest multi = null;
		File file = null;
		try {
	        multi = new MultipartRequest(request, SAVEFOLDER, MAXSIZE, ENCTYPE, new DefaultFileRenamePolicy());
	        String actionName = multi.getParameter("a");
	        System.out.println("boardfile:" + actionName);
	        
	        if ("write".equals(actionName)) {
	        	System.out.println(multi);
	            UserVo authUser = getAuthUser(request);
	            int userNo = authUser.getNo();
	            String title = multi.getParameter("title");
	            String content = multi.getParameter("content");
	            BoardVo vo = new BoardVo(title, content, userNo);

	            // 파일 처리
	            file = new File(SAVEFOLDER);
	            if (!file.exists()) {
	                file.mkdirs();
	            }
	            if (multi.getFilesystemName("file") != null) {
	                filename = multi.getFilesystemName("file");
	                filesize = (int) multi.getFile("file").length();

	                // 파일 정보 설정
	                vo.setFilename(filename);
	                vo.setFilesize(filesize);
	            }

	            System.out.println("filename: " + filename);
	            System.out.println("filesize: " + filesize);

	            BoardDao dao = new BoardDaoImpl();
	            dao.insert(vo);
	            WebUtil.redirect(request, response, "/mysite/board?a=list");
	        } else if ("modify".equals(actionName)) {
				// 게시물 가져오기
//				String title = multi.getParameter("title");
//				String content = multi.getParameter("content");
//				System.out.println(multi.getParameter("no"));
//				int no = Integer.parseInt(multi.getParameter("no"));
//				
//				if (!file.exists())
//					file.mkdirs();
//				if (multi.getFilesystemName("file") != null) {
//					filename = multi.getFilesystemName("file");
//					filesize = (int) multi.getFile("file").length();
//				}
//				System.out.println("no : [" + no + "]");
//				System.out.println("title : [" + title + "]");
//				System.out.println("content : [" + content + "]");
//				System.out.println("filename: " + filename);
//				System.out.println("filesize: " + filesize);
//				
//				BoardVo vo = new BoardVo(no, title, content);
//				BoardDao dao = new BoardDaoImpl();
//				
//				// 아래 내용 set으로 값 넣기
//				vo.setFilename(filename);
//				vo.setFilesize(filesize);
//				
//				dao.update(vo);
//				WebUtil.redirect(request, response, "/mysite/board?a=list");
	            int no = Integer.parseInt(multi.getParameter("no"));
	            String title = multi.getParameter("title");
	            String content = multi.getParameter("content");
	            BoardVo vo = new BoardVo(no, title, content);

	            // 파일 저장소 경로 초기화
	            file = new File(SAVEFOLDER);
	            if (!file.exists()) {
	                file.mkdirs();
	            }

	            // 새로운 파일이 업로드 되었는지 확인
	            if (multi.getFilesystemName("file") != null) {
	                filename = multi.getFilesystemName("file");
	                filesize = (int) multi.getFile("file").length();

	                // 파일 정보 설정
	                vo.setFilename(filename);
	                vo.setFilesize(filesize);
	            }

	            // 데이터베이스 업데이트
	            BoardDao dao = new BoardDaoImpl();
	            dao.update(vo);
	            WebUtil.redirect(request, response, "/mysite/board?a=list");
			}
	        
		} catch (NullPointerException e) {
			System.out.println(e.toString());
		}
	}
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	// 로그인 되어 있는 정보를 가져온다.
	protected UserVo getAuthUser(HttpServletRequest request) {
		HttpSession session = request.getSession();
		UserVo authUser = (UserVo) session.getAttribute("authUser");
		return authUser;
	}
	
}
