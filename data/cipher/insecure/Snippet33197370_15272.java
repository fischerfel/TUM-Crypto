try
{ 
    Class.forName("com.mysql.jdbc.Driver");
    //connection for database
    Connection conn = (Connection)
            //root and username and password for access to the database
    DriverManager.getConnection("jdbc:mysql://localhost:3306/salventri","root","password");
    //create the statement that will be used
    Statement stmt=conn.createStatement();
    //executes the statement
    ResultSet rs = stmt.executeQuery(getCat); 

    int user = Integer.parseInt(txtUsername.getText());
    String pwd = new String(txtPassword.getPassword());
    Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    SecretKeySpec key = new SecretKeySpec(keyBytes, "DES"); 
    IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
    cipher = Cipher.getInstance("DES/CTR/NoPadding", "BC");



    while (rs.next())
    {
        int uname = rs.getInt("username");
        //Username is the coloumn name in the database table 
        String password = rs.getString("password");
        byte [] pass = password.getBytes();
        cipherText = pass;
        cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
        byte[] plainText = new byte[cipher.getOutputSize(ctLength)];
        int ptLength = cipher.update(cipherText, 0, ctLength, plainText, 0);
        ptLength += cipher.doFinal(plainText, ptLength);
        String upassword = String.valueOf(ptLength);


        if (user == uname) 
        {
            JOptionPane.showMessageDialog(null, "username  " + uname + " Password " + cipherText + "\n password as string " + password);
            //...
        }
    }
}
catch (Exception e)
{
    //exception handled
    JOptionPane.showMessageDialog(null, e);
}
