 public static byte[] encrypt(String plainText, String encryptionKey) throws Exception {
Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding", "SunJCE");
SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), "AES");
cipher.init(Cipher.ENCRYPT_MODE, key,new IvParameterSpec(IV.getBytes("UTF-8")));
return cipher.doFinal(plainText.getBytes("UTF-8"));
}

private class HandleThat implements ActionListener{
 public void actionPerformed(ActionEvent eve){
 JTextField jtf; //user will enter string here
 JTextField jtf1; //this will show the encrypted text
 plaintext = jtf.getText();
 String error = "Error, you must provide some text"; 
        if(eve.getActionCommand().equals("Encrypt")){
            if(!jtf.getText().equals("")){
             try{   
             byte[] cipher = encrypt(plaintext, encryptionKey);
             for (int i=0; i<cipher.length; i++)

             jtf1.setText(cipher[i]); //here is where I get my error
            } catch (Exception e) {
            e.printStackTrace();}
        }else{
                label.setText(error);
        }
        }
