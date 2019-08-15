 try {
        PrintWriter arq = new PrintWriter(jTextField1.getText()+".txt");
        arq.println("Username: " + jTextField1.getText());
        arq.println("Email: " + jTextField2.getText());




        String algorithm = "SHA";

        byte[] plainText = jPasswordField1.getText().getBytes();

    MessageDigest md = null;

    try {       
        md = MessageDigest.getInstance(algorithm);
    } catch (NoSuchAlgorithmException e) {
    }

    md.reset();     
    md.update(plainText);
    byte[] encodedPassword = md.digest();

    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < encodedPassword.length; i++) {
        if ((encodedPassword[i] & 0xff) < 0x10) {
            sb.append("0");
        }

        sb.append(Long.toString(encodedPassword[i] & 0xff, 16));
    }

        arq.println("Password: " + sb.toString());

        arq.close();
        if(!jTextField2.getText().equals(jTextField3.getText()) 
   ||!jPasswordField1.getText().equals(jPasswordField2.getText())){
            JOptionPane.showMessageDialog(null, "Either your email or 
   password are not corresponding. Please fix the issue.");
        }
        else{
            JOptionPane.showMessageDialog(null, "Account created!");
        }

    } catch (HeadlessException | FileNotFoundException erro) {
        JOptionPane.showMessageDialog(null, "Error creating Account. Please 
   try again.");
    }
