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
				<form class="board-form" method="post" action="/mysite/boardwrite" enctype="multipart/form-data">
					<input type="hidden" name="a" value="modify" />
					<input type="hidden" name="no" value="${boardVo.no}" />
					<input type="hidden" name="nowPage" value="${nowPage}" />
				
					<table class="tbl-ex">
						<tr>
							<th colspan="2">글수정</th>
						</tr>
						<tr>
							<td class="label">제목</td>
							<td><input type="text" name="title" value="${boardVo.title}"></td>
						</tr>
						<tr>
							<td class="label">내용</td>
							<td>
								<textarea id="content" name="content">${boardVo.content}</textarea>
							</td>
						</tr>
							<tr>
							    <td class="label">현재 첨부 파일</td>
							    <td>
							        <c:if test="${not empty boardVo.filename}">
							            ${boardVo.filename} (파일 크기: ${boardVo.filesize} bytes)
							        </c:if>
						        <c:if test="${empty boardVo.filename}">
							            첨부된 파일 없음 
						        </c:if>
					            </td>
					            <td>
      							<c:if test="${not empty boardVo.filename2}">
							            ${boardVo.filename2} (파일 크기: ${boardVo.filesize2} bytes)
							        </c:if>
						        <c:if test="${empty boardVo.filename2}">
							            첨부된 파일 없음
						        </c:if>
							    </td>
							</tr>
							<tr>
							    <td class="label">새 파일 업로드</td>
							    <td><input type="file" name="file" size="50" maxlength="50"></td>
							</tr>
														<tr>
							    <td class="label">새 파일 업로드2</td>
							    <td><input type="file" name="file2" size="50" maxlength="50"></td>
							</tr>
						
					</table>
				
					<div class="bottom">
						<a href="/mysite/board?a=read&no=${boardVo.no}&nowPage=${nowPage}">취소</a>
						<input type="submit" value="수정">
					</div>
				</form>				
			</div>
		</div>

		<c:import url="/WEB-INF/views/includes/footer.jsp"></c:import>
		
	</div><!-- /container -->
</body>
</html>		
		
