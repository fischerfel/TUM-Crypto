public void actionPerformed(ActionEvent e) {
    try {
        String input = (textField.getText());
        //ENCRYPTION
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update("So What's Up Doc?".getBytes());

        SecretKeySpec key = new SecretKeySpec(md5.digest(), "AES");

        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);

        byte encryptedMessage[] = cipher.doFinal(input.getBytes());
        //Sends the encrypted version of message
        System.out.println(encryptedMessage);
        out.println(encryptedMessage);
        //Clears the input box
        textField.setText("");
    } catch (    NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
        Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
    }
    }
