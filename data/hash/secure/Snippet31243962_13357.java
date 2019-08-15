<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
   pageEncoding="ISO-8859-1" import="java.sql.*,java.security.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Test</title>
</head>
<body>
<%
  String user = request.getParameter("login_id");    
  String pwd = request.getParameter("password");
  String fname = request.getParameter("first_name");
  String lname = request.getParameter("last_name");
  String email = request.getParameter("email");
  String sql = "INSERT INTO MEMBERS(FIRST_NAME, LAST_NAME, EMAIL, USER_ID, PASSWORD, REG_DATE)"
        + " VALUES(?, ?, ?, ?, ?, SYSDATE)";
  try {
     Class.forName("oracle.jdbc.driver.OracleDriver");
     Connection con = DriverManager
           .getConnection("jdbc:oracle:thin:nishtha/FirstJ2ee@//localhost:1521/XE");
     con.setAutoCommit(false);
     PreparedStatement stmt = con.prepareStatement(sql);
     stmt.setString(1, fname);
     stmt.setString(2, lname);
     stmt.setString(3, email);
     stmt.setString(4, user);

     MessageDigest md = MessageDigest.getInstance("SHA-256");
     md.update(pwd.getBytes("UTF-8"));
     byte[] digest = md.digest();
     stmt.setString(5, new String(digest, "UTF-8"));

     int action = stmt.executeUpdate();
     if (action >= 1) {
        con.commit();
        out.println("saved");
     } else {
        out.println("not saved");
     }

  } catch (SQLException e) {
     e.printStackTrace();
     out.println("Error saving your details:<br/>");
     out.println(e.getMessage());
  }
%>
</body>
</html>
