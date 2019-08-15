<%
//variable declaration for encrypt and decrypt
byte [] input ;
byte [] keyBytes = "12345678".getBytes();
byte [] ivBytes ="input123".getBytes();

SecretKeySpec key = new SecretKeySpec(keyBytes,"DES");
IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
Cipher cipher;
byte[] cipherText;
int ctLength=0;

Class.forName("com.mysql.jdbc.Driver");
conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);

if(request.getParameter("submit")!=null){
    String cuser=request.getParameter("currentusername"); 
    String user = request.getParameter("username");
    String pwd = request.getParameter("password");
    String cpwd = request.getParameter("confirmpassword");

    Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
      input = pwd.getBytes();
      key = new SecretKeySpec(keyBytes, "DES");
      ivSpec = new IvParameterSpec(ivBytes);
      cipher = Cipher.getInstance("DES/CTR/NoPadding","BC");

      cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
      cipherText = new byte[cipher.getOutputSize(input.length)];

      ctLength+=cipher.update(input, 0, input.length, cipherText, 0);

      ctLength+= cipher.doFinal(cipherText, ctLength);
      String enpwd = new String(cipherText);


     String sql2 = "update webadmin set username=? ,password=? where username='"+cuser+"' ";

     if((cuser!=null &&cuser.length()>0) 
        && (user!=null &&user.length()>0)  
        && (pwd!=null && pwd.length()>0)
        && cpwd!=null && cpwd.length()>0) {

         if((pwd.equals(cpwd))){
           pst =conn.prepareStatement(sql2);
           pst.setString(1, user);
           pst.setString(2, enpwd);

            pst.executeUpdate();
%>
 <script language="JavaScript">
     alert("Sucessfully Updated");
 </script>
 <%
         }else{
             %>
           <script language="JavaScript">
            alert("Passwords are not matching try again");
            </script>
           <%

     }
    }
  }
}

%> 
