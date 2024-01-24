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
        String actionName = request.getParameter("a");
        System.out.println("---BoardSeverlet_acting---");
        System.out.println("board:" + actionName);

        if ("list".equals(actionName)) {
            BoardDao dao = new BoardDaoImpl();
            System.out.println("getParameter: "+request.getParameter("kwd"));
            System.out.println("getParameter: "+request.getParameter("d_kwd"));

            //검색어 가져오기
            String kwd="";
            String option="";
            if(request.getParameter("kwd")!=null) {
                kwd = request.getParameter("kwd");
                option = request.getParameter("option");
            }
            else if(request.getParameter("d_kwd")!=null) {
                kwd = request.getParameter("d_kwd");
                option =request.getParameter("d_option");
            }
            System.out.println("key word:"+kwd+"\nsearch option: "+option);
            //설정값 인수들
            int numPerPage=10; // 페이지당 레코드 수
            int pagePerBlock=5; //블럭당 페이지수
            int nowPage=1; // 현재페이지

            //현재 페이지 가져오기, null 일경우 기본값 1 적용
            if(request.getParameter("nowPage")!=null) {
                nowPage=Integer.parseInt((String)request.getParameter("nowPage"));
                System.out.println("nowPage:"+nowPage);
            }

            //필요 인수들 계산
            int totalRecord=dao.getTotalRecord(option, kwd);
            System.out.println("totalrecord: "+totalRecord);
            int totalPage=(int)Math.ceil((double)totalRecord/numPerPage);
            int totalBlock=(int)Math.ceil((double)totalPage/pagePerBlock);
            int nowBlock=(int)Math.ceil((double)nowPage/pagePerBlock);
            int endPage=nowBlock*pagePerBlock;
            int startPage=endPage-4;

            //페이지 하나의 리스트 가져오기
            List<BoardVo> list = dao.getSubList(nowPage, numPerPage,option,kwd);
            System.out.println(list.toString());

            request.setAttribute("pagePerBlock", pagePerBlock);
            request.setAttribute("totalRecord", totalRecord);
            request.setAttribute("totalPage", totalPage);
            request.setAttribute("totalBlock", totalBlock);
            request.setAttribute("nowBlock", nowBlock);
            request.setAttribute("nowPage", nowPage);
            request.setAttribute("endPage", endPage);
            request.setAttribute("startPage", startPage);

            request.setAttribute("kwd", kwd);
            request.setAttribute("option", option);

            // 리스트 화면에 보내기
            request.setAttribute("list", list);
            //WebUtil.forward(request, response, "/WEB-INF/views/board/list.jsp");
            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/board/list.jsp");
            rd.forward(request, response);

        } else if ("read".equals(actionName)) {
			// 게시물 가져오기
			int no = Integer.parseInt(request.getParameter("no"));
			BoardDao dao = new BoardDaoImpl();
			BoardVo boardVo = dao.getBoard(no);
			
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
		} else if ("modify".equals(actionName)) {
            // 게시물 가져오기
            String title = request.getParameter("title");
            String content = request.getParameter("content");
            int no = Integer.parseInt(request.getParameter("no"));

            BoardVo vo = new BoardVo(no, title, content);
            BoardDao dao = new BoardDaoImpl();

            dao.update(vo);

            WebUtil.redirect(request, response, "/mysite/board?a=list");
        } else if ("writeform".equals(actionName)) {
			// 로그인 여부체크
			UserVo authUser = getAuthUser(request);
			if (authUser != null) { // 로그인했으면 작성페이지로
				WebUtil.forward(request, response, "/WEB-INF/views/board/writeform.jsp");
			} else { // 로그인 안했으면 리스트로
				WebUtil.redirect(request, response, "/mysite/board?a=list");
			}

		}
//        else if ("write".equals(actionName)) {
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

            String kwd = request.getParameter("kwd");
            String option = request.getParameter("search_option");
            System.out.println("kwd: "+kwd);
            System.out.println("option: "+option);
            //BoardDao dao = new BoardDaoImpl();
            //List<BoardVo> list = dao.search(kwd, option);

            // 리스트 화면에 보내기
            //request.setAttribute("list", list);
            //request.setAttribute("kwd", kwd);

            //리스트섹션에 검색어 전달하기 위함
            request.setAttribute("r_option", option);
            request.setAttribute("r_kwd", kwd);

            //RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/board/list.jsp");//얘가 왜 jsp로 가지
            //RequestDispatcher rd = request.getRequestDispatcher("/mysite/board?a=list");

            // rd.forward(request, response);
            WebUtil.redirect(request, response, "/mysite/board?a=list");

        } else {
            WebUtil.redirect(request, response, "/mysite/board?a=list");
        }
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	// 로그인 되어 있는 정보를 가져온다.
	protected UserVo getAuthUser(HttpServletRequest request) {
		HttpSession session = request.getSession();
		UserVo authUser = (UserVo) session.getAttribute("authUser");

		return authUser;
	}

}
