private void loginButtonActionPerformed(ActionEvent e) {

    String password = "";
        password=loginPasswordField.getText();
        String md5hashdatabase = "3f197eedfeaf826ad2af0bac49ded752";            
        try
        {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            System.out.println(new BigInteger(1, md.digest()).toString(16));
            String md5hashes = new BigInteger(1, md.digest()).toString(16);
            String m = md5hashes;

            String myDriver = "org.gjt.mm.mysql.Driver";
            String myUrl = "jdbc:mysql://experimentalx.com/exper482_social";
            Class.forName(myDriver);
            Connection conn = DriverManager.getConnection(myUrl, "admin", "test");

            String query = "SELECT password FROM social_users";

            Statement st = conn.createStatement();

            ResultSet rs = st.executeQuery(query);

            while (rs.next())
            {
                String encryptedPassWord = rs.getString("password");
                System.out.format("%s\n", encryptedPassWord);

                if(m.equals(encryptedPassWord)) 
                {
                    System.out.println("Its exactly the same!");
                }
                else if(!m.equals(encryptedPassWord)) 
                {
                    System.out.println("Its not the same!");
                }
                else
                {
                    System.out.println("For some reason, this is just not wanting to work!");
                }           
          }
            st.close();
        }
        catch (Exception e1)
        {
            System.err.println("Got an exception! ");
            System.err.println(e1.getMessage());
        }   
    }
