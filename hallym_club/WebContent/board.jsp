<%@page import="javax.security.auth.callback.ConfirmationCallback"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.io.PrintWriter"%>
<%@ page import="bbs.BbsDAO"%>
<%@ page import="bbs.Bbs"%>
<%@ page import="java.io.PrintWriter"%>
<%@page import="exam.jdbc.ClubVO"%>
<%@page import="java.util.ArrayList"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=1200">
<link rel="stylesheet" type="text/css" href="css/common.css">
<link rel="stylesheet" type="text/css" href="css/board.css">
<link rel="stylesheet" type="text/css" href="css/page.css?after">
<title>한림대학교 동아리</title>
</head>

<body>
	<%
		request.setCharacterEncoding("UTF-8");

		String username = null;
		if (session.getAttribute("username") != null) {
			username = (String) session.getAttribute("username");
		}
		int pageNumber = 1; //기본 페이지 넘버
		String condition = "";
		//페이지넘버값이 있을때
		if (request.getParameter("pageNumber") != null) {
			pageNumber = Integer.parseInt(request.getParameter("pageNumber"));
		}
		if (request.getParameter("condition") != null) {
			condition = request.getParameter("condition");
		}
		int club_id = 1;
		String board_cd = "007001";
		if (request.getParameter("club_id") != null) {
			club_id = Integer.parseInt(request.getParameter("club_id"));
		}
		if (request.getParameter("board_cd") != null) {
			board_cd = request.getParameter("board_cd");
		}
	%>
	<div id="wrap">
		<div id="header">
			<h1>
				<a href="index.jsp"><img src="image/logo.gif" alt="한림대학교 동아리"></a>
			</h1>
			<a href="#content" class="skip">본문 바로가기</a>

			<%if (username == null) {%>
			<ul id="snb">
				<li><a href="login.jsp">로그인</a></li>
				<li><a href="index.html">회원가입</a></li>
			</ul>
			<%}else{ %>
			<ul id="snb">
				<li><span class="txt">
						<%out.print((String) session.getAttribute("username") + " 님 환영합니다.");%>
				</span></li>
				<li><a href='logoutAction.jsp'>로그아웃</a></li>
			</ul>
			<%} %>

			<ul id="gnb">
				<li><a href="club_search.jsp">동아리조회</a></li>
				<li><a href="top_club.jsp">우수동아리</a></li>
				<li><a href="board.jsp">공지사항</a></li>
				<li><a href="jido.jsp">주변음식점</a></li>
			</ul>
		</div>
		<div id="content">
			<div class="info_tit">
				<h1>공지사항</h1>
			</div>

			<div class="tbl-div">
				<table class="tbl">
					<tr>
						<th style="width: 80px">번호</th>
						<th style="width: 500px">제목</th>
						<th style="width: 100px">분류</th>
						<th style="width: 120px">작성자</th>
						<th style="width: 80px">조회수</th>
					</tr>
					<%
					BbsDAO bbsDAO = new BbsDAO();
					ArrayList<Bbs> board_list = bbsDAO.getclub_search(club_id,board_cd,pageNumber, condition);
					int totalcount = 0;
				
					if(board_list.isEmpty()){
					
					}else{
						totalcount = board_list.get(0).getRow_count();
						for (int i = 0; i < board_list.size(); i++) { %>
					<tr>
						<td><%=board_list.get(i).getBOARD_NO()%></td>
						<td><a
							href="view.jsp?BOARD_NO=<%=board_list.get(i).getBOARD_NO()%>&club_id=<%=club_id%>&board_cd=<%=board_cd%>">
								<%=board_list.get(i).getTITLE()%></a></td>
						<td>
							<%if(board_list.get(i).getBoard_cd().equals("007001"))
										out.println("공지사항");%>
						</td>
						<td><%=board_list.get(i).getINPUT_ID()%></td>
						<td><%=board_list.get(i).getOPEN_CNT()%></td>
					</tr>
					<%} %>
				<%} %>
					
					<%for(int i=0; i<7; i++){ %>
					<tr>
						<td>1</td>
						<td>공지사항 테스트 진행중입니다.</td>
						<td>공지사항</td>
						<td>관리자</td>
						<td>100</td>
					</tr>
					<%} %>
					

				</table>

				<div class="page_wrap">
					<div class="page_nation">
						<%
						int countList = 10;
						int countPage = 5;
						int totalPage = totalcount / countList;
						if (totalcount > countList * totalPage) {
							totalPage++;
						}
						//현재 페이지가 총 페이지 번호보다 클 경우 
						if (totalPage < pageNumber) {
							pageNumber = totalPage;
						}
						int startPage = ((pageNumber - 1) / countPage) * countPage + 1; //페이지 번호  시작
						int endPage = startPage + countPage - 1; //페이지 번호 끝
						if (endPage > totalPage) {
							endPage = totalPage;
						}

						if (startPage > 1) {
					%>
						<a class="arrow pprev" href="board.jsp?pageNumber=<%=1%>">처음</a>
						<%
						}
						if (pageNumber > 1) {
					%>
						<a class="arrow prev"
							href="board.jsp?pageNumber=<%=pageNumber - 1%>">이전</a>
						<%
						}
						for (int i = startPage; i <= endPage; i++) {
							if (i == pageNumber) {
					%>
						<a class="active" href="board.jsp?pageNumber=<%=i%>"><%=i%></a>
						<%
						} else {
					%>
						<a href="board.jsp?pageNumber=<%=i%>"><%=i%></a>
						<%
						}
						}
					%>

						<%
						if (pageNumber < totalPage) {
					%>
						<a class="arrow next"
							href="board.jsp?pageNumber=<%=pageNumber + 1%>">다음</a>
						<%
						}
					%>

						<%
						if (endPage < totalPage) {
					%>
						<a class="arrow nnext" href="board.jsp?pageNumber=<%=totalPage%>">끝</a>
						<%
						}
					%>
					</div>
				</div>

				<div class="category">
					<form>
						<ul>
							<li><select><option value="">제목</option></select></li>
							<li><input type="text" name="condition" size="30"
								value="<%=condition%>" onfocus="this.select()">
								<button type="submit" class="category-btn">검색</button></li>
						</ul>
					</form>
				</div>
			</div>



		</div>
		<hr>
		<div id="footer">
			<div class="copyright">
				<address>Copyright 2019. 김정인, 김진섭. All Rights Reserved.</address>
			</div>
		</div>

	</div>



</body>

</html>