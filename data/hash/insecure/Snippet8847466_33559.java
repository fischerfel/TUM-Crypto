HttpSession session1 = request.getSession(false);
     PrintWriter out = response.getWriter();
      try{
   if(request.getMethod().equalsIgnoreCase("POST")){
String user="";
String timenow="";
String strQuery="";
String today="";
String tour="";
try{
String useridfinal = (String)request.getParameter("userid");
    String userpassfinal = (String)request.getParameter("userpassword");
         Pattern p10 = Pattern.compile("[A-Z0-9a-z]+");// XSS checking
Matcher m10 = p10.matcher(useridfinal);
boolean b10 = m10.matches();
Pattern p11 = Pattern.compile("[A-Z!_,.a-z0-9]+");// XSS checking
Matcher m11 = p11.matcher(userpassfinal);
boolean b11 = m11.matches();
if(useridfinal == null || b10==false){
session1.setAttribute("errorlogin", "Invalid Login ID or userpassword");
response.sendRedirect("login.jsp");
        }
else if(userpassfinal == null || b11==false){
session1.setAttribute("errorlogin", "Invalid Login ID or userpassword");
response.sendRedirect("login.jsp");
}
else{
try {
  dbconnection db= new dbconnection();
 db.getConnection();
PreparedStatement ps=null;
     PreparedStatement ps2=null;
 ResultSet  rs=null;
ResultSet  rs1=null;
String ipadd="";
     try {
ipadd= request.getRemoteAddr();//tracking IP address
}
catch(Exception e) {
}
               SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MM-yy HH:mm:ss");         
Date now = new Date();
String strDate = sdfDate.format(now);
    if(request.getParameter("userid")!=null &&
        (request.getParameter("userid") == null ? "" != null : !request.getParameter("userid").equals("")) && request.getParameter("userpassword")!=null &&
        (request.getParameter("userpassword") == null ? "" != null : !request.getParameter("userpassword").equals("")))
    {
 if ( session1 != null) {
               session1.invalidate(); }
 session1 = request.getSession(true);
    String s=(String)useridfinal;
       MessageDigest m=MessageDigest.getInstance("MD5");
       m.update(s.getBytes(),0,s.length());
       String encuseridfinal=(new BigInteger(1,m.digest()).toString(16));
      String s1=(String)userpassfinal;
       MessageDigest m1=MessageDigest.getInstance("MD5");
       m1.update(s1.getBytes(),0,s1.length());
       String encuserpassfinal=(new BigInteger(1,m1.digest()).toString(16));
ps= db.con.prepareStatement("select * from login where loginid=? and  loginpass=? ");
ps.setString(1, useridfinal);
ps.setString(2, encuserpassfinal);// encrypted userpassword
     try {
      rs=ps.executeQuery();
       } catch (SQLException ex) { 
        }
      int count=0;
      while(rs.next())
      {
 count++;
try {
  //Initialize SecureRandom
  //This is a lengthy operation, to be done only upon
  //initialization of the application
  SecureRandom prng = SecureRandom.getInstance("SHA1PRNG");
  //generate a random number
  String randomNum = new Integer( prng.nextInt() ).toString();
  //get its digest
  MessageDigest sha = MessageDigest.getInstance("SHA-1");
  byte[] result =  sha.digest( randomNum.getBytes() );
String csrf="";

csrf=hexEncode(result);
                try{
Calendar calendar = new GregorianCalendar();
int hour = calendar.get(Calendar.HOUR_OF_DAY);
int minute = calendar.get(Calendar.MINUTE);
int second = calendar.get(Calendar.SECOND);
today=(+hour+":"+minute+":"+second +"");
Date date = new Date();
SimpleDateFormat sdf = null;
String strDate1 = "";
sdf = new SimpleDateFormat("dd-MM-yy");
strDate1 = sdf.format(date);
tour=strDate1+" "+today;
               try{
           ps2 = db.con.prepareStatement("insert into logindetails (login_id, login_dt, login_ipaddress) values (?, to_date(?, 'DD-MM-YY hh24:mi:ss'),?) ");
               }
               catch(Exception e){
               }
ps2.setString(1, useridfinal);
try{
ps2.setString(2, tour);
}
catch(Exception e){}
try{
ps2.setString(3, ipadd);
}
catch(Exception e){}
try {
 rs1=ps2.executeQuery();
}
catch(SQLException ex){
}
 rs1.close();
 ps2.close();
  } 
               catch(Exception e){
               }
         session1.setAttribute("useridfinal", useridfinal);
      session1.setAttribute("csrftoken", csrf); //csrf token generation      
    response.sendRedirect("home.jsp");
  session1.setAttribute("authenticated", true);  
}
catch(Exception e){}
 }
 try{
       rs.close();
       }
       catch(Exception e){}
       try{
       ps.close();
       }
       catch(Exception e){}
     try{
    db.removeConnection();
     }
 catch(Exception e){}
      if(count==0)
      {
 session1.setAttribute("errorlogin", "Invalid Login ID or userpassword");
 response.sendRedirect("login.jsp");
      }
    }
    else {   
 session1.setAttribute("errorlogin", "Invalid Login ID or userpassword");
 response.sendRedirect("login.jsp");
}
  } catch (Exception e) {
  }
 }
} catch (Exception e) {
      out.println("please try later");
  }
   }
else{
response.sendRedirect("login.jsp");
}
    } catch (Exception e) {
   response.sendRedirect("login.jsp");
   }
processRequest(request, response);
}
function() {// For generating secure token
return token;
}
