   import javax.xml.bind.DatatypeConverter;
   public static boolean encrypt(byte[] text){

    Boolean yorn = false;
    try{
        myDesKey = KeyGenerator.getInstance("DES").generateKey();

        //myDeskKey = myDesKey.toString();
        Cipher desCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        desCipher.init(Cipher.ENCRYPT_MODE, myDesKey);

//I felt it would be better seeing the secret key as "woatDnBJLAg="  instead of  "com.sun.crypto.provider.DESKey@18765"

        if (myDesKey != null) {
           stringKey = DatatypeConverter.printBase64Binary(myDesKey.getEncoded());

            System.out.println("actual secret_key:" + myDesKey);

            byte[] encodedKey = DatatypeConverter.parseBase64Binary(stringKey);

            myDesKey = new SecretKeySpec(encodedKey, 0, encodedKey.length,
                    "DES");
            System.out.println("after encode & decode secret_key:"
                    + DatatypeConverter.printBase64Binary(myDesKey.getEncoded()));
            }
        textEncrypted = desCipher.doFinal(text);

    yorn = true;
    JTextArea textArea = new JTextArea(2,50);
    textArea.setText("Your encryption key is:  " +  stringKey + " . Ensure you store it in a safe place" );// + DatatypeConverter.printBase64Binary(myDesKey.getEncoded()));
    textArea.setEditable(false);
    JOptionPane.showMessageDialog(null, new JScrollPane(textArea), "RESULT", JOptionPane.INFORMATION_MESSAGE);


    }catch(Exception e)
    {
        System.out.println("There has been an error encrypting the file");
        yorn = false;
    }
        return yorn;
