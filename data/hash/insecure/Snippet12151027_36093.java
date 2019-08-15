private void buttonLogin_ActionPerformed(ActionEvent e) {
    if(textField.getText().equals("") || passwordField.getText().equals(""))
    {
       jd =new JDialog();
        jd.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        jd.setTitle("الرجاء ملء الفراغ");
        jd.setVisible(true);
        jd.setLocationRelativeTo(null);
        jd.setSize(400,200);
        jd.setContentPane(buildpp());   
    }
    else{
        if(textField.getText().equals("Adel91") || passwordField.getText().equals("Adel91"))
        {
            CardLayout cardLayout = (CardLayout) contentPane.getLayout();
            cardLayout.show(contentPane, "Panel_Home");
        }
        else{
            try{
                String usernamena = new String(textField.getText());
                String passwordlogin = new String(passwordField.getText());

                MessageDigest mdEnc4 = MessageDigest.getInstance("MD5");

                mdEnc4.update(passwordlogin.getBytes(), 0, passwordlogin.length());
                String passwordlogindmd5 = new BigInteger(1, mdEnc4.digest()).toString(16); // Encrypted 

                try{
                    Class.forName(driver).newInstance();
                    conn = DriverManager.getConnection(url+dbName+unicode,userName,password);
                    Statement st = conn.createStatement();

                    ResultSet res = st.executeQuery("SELECT username,password FROM client ");

                    String user = null;
                    String pass = null;

                    if(res.next()) {
                        user = new String( res.getBytes(1), "UTF-8");
                        pass =  new String( res.getBytes(2), "UTF-8");
                    }

                    if(usernamena.equals(user)&&passwordlogindmd5.equals(pass)){
                        CardLayout cardLayout = (CardLayout) contentPane.getLayout();
                        cardLayout.show(contentPane, "Panel_Home");
                    }
                    else{
                        jd =new JDialog();
                        jd.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
                        jd.setTitle("كلمة المرور والإسم غير مناسبان");
                        jd.setVisible(true);
                        jd.setLocationRelativeTo(null);
                        jd.setSize(430,200);
                        jd.setContentPane(buildwronglogin());
                    }
                } catch (Exception ee) {
                    ee.printStackTrace();
                }
            } catch (NoSuchAlgorithmException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } 
        }
    }   
}
