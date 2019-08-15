<%@ page import="java.sql.*" %>
<%@ page import="java.io.*" %>
<%@ page import="java.security.*" %>
<%@ page import="javax.crypto.*" %>
<%@ page import="javax.crypto.spec.*" %>
<%@ page import="java.lang.*" %>

<HTML>
<HEAD>
<TITLE>Simple JSP/Oracle Query Example</TITLE>
</HEAD>
<BODY>

<%
   Class.forName("oracle.jdbc.OracleDriver");

   Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@xxx:xxxx:xxxx","xxxxxx","xxxxxx");
                        // @//machineName:port:SID,   userid,  password

    Statement st=conn.createStatement();

    ResultSet rs=st.executeQuery("Select * from Cxxxxxxx");

    while(rs.next()){
        String name=rs.getString("user_id");
        String p=rs.getString("password");
        out.println(name+":"+p);
        out.println("</br>");


    String algorithm1 = "DES";//magical mystery constant
    String algorithm2 = "DES/CBC/NoPadding";//magical mystery constant
    IvParameterSpec iv = new IvParameterSpec( new byte [] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 } );//magical mystery constant
    Cipher cipher;
    SecretKey key;
    String k="12345abc";
    key = new SecretKeySpec( k.getBytes("UTF-8"), algorithm1 );
    cipher = Cipher.getInstance( algorithm2 );

    String str="test4abc";

    cipher.init( Cipher.ENCRYPT_MODE, key, iv ); //normally you could leave out the IvParameterSpec argument, but not with Oracle

    byte[] bytes=str.getBytes("UTF-8");

    byte[] encrypted = cipher.doFinal( bytes );

    //Problem is in the statement BELOW --->
    String encoded = new String( Hex.encodeHex( encrypted ) );
    }
%>  
</BODY>
</HTML>
