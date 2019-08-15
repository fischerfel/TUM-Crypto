    try{
    String msgout = "";
    msgout = msg_text.getText().trim();
    aesKey = new SecretKeySpec(key.getBytes(), "AES");
        cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, aesKey);
        encrypted = cipher.doFinal(msgout.getBytes());
    String msgout1;
        msgout1 = String.valueOf(encrypted);
    dout.writeUTF(msgout1);
    msg_area.setText(msg_area.getText().trim()+"\nYou:\t"+msgout);
    }catch(Exception e){

    }
