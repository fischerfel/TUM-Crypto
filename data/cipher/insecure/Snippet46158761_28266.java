    public void doSendGmail(){
    Connection con = Functions.ConnectToDB();
    try {
        Statement stmt = con.createStatement();
        String sqlQuery = "select * from settings";
        ResultSet rs = stmt.executeQuery(sqlQuery);

        while(rs.next()){
            String email = rs.getString("Email");
            //String pass = rs.getString("Password");
            byte [] pass = rs.getBytes("Password");

            cipher = Cipher.getInstance("DES/CTR/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, keySpec,ivspec);
            byte [] plain_text = cipher.doFinal(pass);

            from = email;
            password = new String(plain_text);
        }

        con.close();// close the connection

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error retrieving email address and password\n"+e.toString(),
                "Error",JOptionPane.ERROR_MESSAGE);
    }

    to = txtTo.getText();
    cc = txtCC.getText();
    bcc = txtBCC.getText();
    subject = txtSubject.getText();
    message_body = jtaMessage.getText();

    Properties props = new Properties();
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.host", "smtp.gmail.com");
    props.put("mail.smtp.port", "587");


    /*use authenticator as username and password are supplied 'on demand' i.e queried from database
    or supplied via a login dialog*/
    Session session = Session.getInstance(props,new javax.mail.Authenticator() {
        protected PasswordAuthentication getPasswordAuthentication(){
            return new PasswordAuthentication(from, password);
        }
    });

    try {            
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(to));
        if(!cc.equals("")){
            message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(cc));
        }
        if(!bcc.equals("")){
            message.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(bcc));
        }
        message.setSubject(subject);

        if(filePathList.isEmpty()){// if a file(s) have not been attached...
            message.setText(message_body);
            Transport.send(message);
        }
        else{// if a file(s) have been attached
            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText(message_body);// actual message
            Multipart multipart = new MimeMultipart();// create multipart message
            multipart.addBodyPart(textPart);//add the text message to the multipart

            for(int i =0; i<filePathList.size(); i++){// use for loop to attach file(s)
                MimeBodyPart attachmentPart = new MimeBodyPart();
                DataSource source = new FileDataSource((String)filePathList.get(i));
                attachmentPart.setDataHandler(new DataHandler(source));
                attachmentPart.setFileName((String)fileList.get(i));
                multipart.addBodyPart(attachmentPart);// add the attachment to the multipart
                message.setContent(multipart);// add the multipart to the message
            }
            Transport.send(message);
        }

        JOptionPane.showMessageDialog(this, "Message Sent!","Sent",JOptionPane.INFORMATION_MESSAGE);

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error sending email message\n"+e.toString(),
                "Error",JOptionPane.ERROR_MESSAGE);
    }
} 
