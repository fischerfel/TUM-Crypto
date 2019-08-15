<%
String CONN_STRING = "jdbc:mysql://localhost/pmsdb";
String USERNAME = "dbuser";
String PASSWORD = "dbpassword";

Connection conn=null;
PreparedStatement pst=null;
ResultSet rs =null;

 //variables to encrypt  
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
    String user = request.getParameter("Username");
    session.putValue("Username",user);
    String pwd = request.getParameter("Password");


    //checking with pwd encryptd in db
      Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
      input = pwd.getBytes();
      key = new SecretKeySpec(keyBytes, "DES");
      ivSpec = new IvParameterSpec(ivBytes);
      cipher = Cipher.getInstance("DES/CTR/NoPadding","BC");

      cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
      cipherText = new byte[cipher.getOutputSize(input.length)];

      ctLength+=cipher.update(input, 0, input.length, cipherText, 0);

      ctLength+= cipher.doFinal(cipherText, ctLength);
      String epwd = new String(cipherText);


    String sql = "select * from websupplierinfo where username=? and password=?";
    pst = conn.prepareStatement(sql);
    pst.setString(1,user);
    pst.setString(2,epwd);
    rs = pst.executeQuery();
    if(rs.next()){
        if(rs.getString("password").equals(epwd)){
            //wantto go to supplier home page
            response.sendRedirect("supplierpg-status.jsp");
        }else{
        %>  
        <script language="JavaScript">
            alert("Incorrect Login Details.. Try again");
        </script>
        <%            
        } 
    }
    %>
