  public static String decrypt(byte[] cipherText, SecretKey key,String key1)
  {
    String plainText = "";
    try{
        SecretKey myDesKey = key;
        if(key == null){

        JOptionPane.showMessageDialog(null, "We were unable to find your decryption key. Please enter your decryption key below: ");
        JTextArea textBox = new JTextArea(1,15);
        JOptionPane.showMessageDialog(null, new JScrollPane(textBox),"Enter your decryption key ",JOptionPane.PLAIN_MESSAGE);
        //myDesKey = textBox.toSecretKey;
        }

    Cipher desCipher;
    desCipher = Cipher.getInstance("DES");
    desCipher.init(Cipher.DECRYPT_MODE, myDesKey);
    byte[] textDecrypted = desCipher.doFinal(cipherText);
    plainText = new String(textDecrypted);
    JOptionPane.showMessageDialog(null, plainText, "DECRYPTED MESSAGE", 0);

    }catch(Exception e)
    {
        System.out.println("There has been an error decrypting the file");
        System.out.println(e);
    }return plainText;
  }
}
