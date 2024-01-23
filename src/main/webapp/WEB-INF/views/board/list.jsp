<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<link href="/mysite/assets/css/board.css" rel="stylesheet" type="text/css">
<title>Mysite</title>
</head>
<body>

	<div id="container">
		
		<c:import url="/WEB-INF/views/includes/header.jsp"></c:import>
		<c:import url="/WEB-INF/views/includes/navigation.jsp"></c:import>
		
		<div id="content">
			<div id="board">
				<form id="search_form" action="/mysite/board" method="post">
				<input type="hidden" name="a" value="list">
					<select name="option">
        				<option value="name" selected>작성자</option>
        				<option value="reg_date">게시물 작성일시</option>
        				<option value="title">제목</option>
        				<option value="content">내용</option>
    				</select>					
					<input type="text" id="kwd" name="kwd" value="${d_kwd }">
					<input type="submit" value="찾기">
				</form>
				<table class="tbl-ex">
					<tr>
						<th>번호</th>
						<th>제목</th>
						<th>글쓴이</th>
						<th>조회수</th>
						<th>작성일</th>
						<th>&nbsp;</th>
					</tr>				
					<c:forEach items="${list}" var="vo">
						<tr>
							<td>${vo.no }</td>
							<td><a href="/mysite/board?a=read&no=${vo.no }"> ${vo.title } </a></td>
							<td>${vo.userName }</td>
							<td>${vo.hit }</td>
							<td>${vo.regDate }</td>
							<td>
								<c:if test="${authUser.no == vo.userNo }">
									<a href="/mysite/board?a=delete&no=${vo.no }" class="del">삭제</a>
								</c:if>
							</td>
						</tr>
					</c:forEach>
				</table>
				<div class="pager">
					<ul>
						<c:if test="${nowBlock > 1 }">
							<li><a href="/mysite/board?a=list&nowPage=${(nowBlock-1)*pagePerBlock}&d_kwd=${kwd}&d_option=${option}"><</a></li>
						</c:if>
						<c:forEach var="i" begin="${startPage}" end="${endPage > totalPage ? totalPage : endPage}">
						    <c:if test="${i <= totalPage}">
						        <c:set var="isSelected" value="${i eq nowPage}" />
						        <li class="${isSelected ? 'selected' : ''}">
						            <a href="/mysite/board?a=list&nowPage=${i}&d_kwd=${kwd}&d_option=${option}">
						                ${i}
						            </a>
						        </li>
						    </c:if>
						</c:forEach>


						<c:if test="${nowBlock < totalBlock }">
							<li><a href="/mysite/board?a=list&nowPage=${nowBlock * pagePerBlock + 1}&d_kwd=${kwd}&d_option=${option}">></a></li>
						</c:if>
					</ul>
				</div>				
				<c:if test="${authUser != null }">
					<div class="bottom">
						<a href="/mysite/board?a=writeform" id="new-book">글쓰기</a>
					</div>
				</c:if>			
			</div>
		</div>
		
		<c:import url="/WEB-INF/views/includes/footer.jsp"></c:import>
		
	</div><!-- /container -->
</body>
</html>		
		
