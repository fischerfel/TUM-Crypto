Login() throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException{

  //Check to make sure a username has been entered.

  if (!name.contains("[a-zA-Z0-9]") && name.length() > 0 ){

  //Find which line the user's data is stored on:

        try{
            int LineCount = 0;
            String line = "";

            BufferedReader bReader = new BufferedReader(new FileReader("resources/user_names.txt"));
             while ((line = bReader.readLine()) != null) {
                 LineCount ++;

            int posFound = line.indexOf(name);

            if (posFound > - 1) {
                System.out.println("Search word found at position " + posFound + " on line " + LineCount);


 //What do I do here to read my key and password from their text files from the line found above.

//The following code works when there is only one entry on each line of the text files.


            FileInputStream keyFis = new FileInputStream("resources/password_keys.txt");
            byte[] encKey = new byte[keyFis.available()];
            keyFis.read(encKey);
            keyFis.close();

            Key keyFromFile = new SecretKeySpec(encKey, "DES");


            FileInputStream encryptedTextFis = new FileInputStream("resources/user_data.txt");
            byte[] encText = new byte[encryptedTextFis.available()];
            encryptedTextFis.read(encText);
            encryptedTextFis.close();


            Cipher decrypter = Cipher.getInstance("DES/ECB/PKCS5Padding");
            decrypter.init(Cipher.DECRYPT_MODE, keyFromFile);
            byte[] decryptedText = decrypter.doFinal(encText);



            System.out.println("Decrypted Text: " + new String(decryptedText));


                      }else{

                 JOptionPane.showMessageDialog(null, String.format("The password you entered has not been created."),
                 "Missing account", JOptionPane.ERROR_MESSAGE);
                    }
             }

             bReader.close();

        }catch(IOException e){
            System.out.println("Error: " + e.toString());
        }

  }else{
JOptionPane.showMessageDialog(null, String.format("Please enter a valid username."),
"No Input", JOptionPane.ERROR_MESSAGE);

  }

}
