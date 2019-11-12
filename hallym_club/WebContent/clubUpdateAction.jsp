<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="utf-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

	<%request.setCharacterEncoding("UTF-8");
String student_id = request.getParameter("student_id");

%>

	<jsp:useBean id="CV" class="exam.jdbc.ClubVO" />
	<jsp:setProperty property="*" name="CV" />
	<jsp:useBean id="CV_dao" class="exam.jdbc.JDBC_clubDAO" />


	<%	    
 
	
	int result = CV_dao.updateClub(CV);
    
    if (result == -1) {
    	out.print("<script>");
        out.print("alert('정상적으로 완료되지 않았습니다.');");  
        out.print("history.back();");          
        out.print("</script>");     
        } else {
        	out.print("<script>");
            out.print("alert('수정을 완료했습니다.');"); 
			out.print("history.back();");
            out.print("</script>");
     }
 %>
</body>
</html>