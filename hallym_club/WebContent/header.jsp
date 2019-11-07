<%@page import="sun.security.krb5.internal.PAEncTSEnc"%>
<%@page import="java.net.URLEncoder"%>
<%-- <%@page import="exam.jdbc.ClubVO"%> --%>
<%@page import="java.util.ArrayList"%>
<%-- <%@page import="exam.jdbc.JDBC_clubDAO"%> --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=1200">
<title>한림대학교 동아리</title>
<link rel="stylesheet" type="text/css" href="css/common.css">
</head>
<body>

	<%
      String username = null;
      if (session.getAttribute("username") != null) {
         username = (String) session.getAttribute("username");
      }
   %>

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
					<li><span class="txt"><%out.print((String) session.getAttribute("username") + " 님 환영합니다.");%></span></li>
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

</body>
</html>