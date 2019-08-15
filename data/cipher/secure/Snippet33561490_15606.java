        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA"); //generate key using RSA
        KeyPair keypair=keyPairGenerator.generateKeyPair(); //get generated key
        Cipher cipher =Cipher.getInstance("RSA/ECB/PKCS1Padding"); 
        SharedPreferences sharedPreferences=context.getSharedPreferences("rsakey", MODE_PRIVATE);//Initializing SharedPerference 



        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("public",keypair.getPublic().toString());
        editor.putString("private",keypair.getPrivate().toString());
        editor.commit();//store key in sharedpreference
        final String sampletext="abcde";

       //getting stored key
        String publicKey = sharedPreferences.getString("public", null);
        String privateKey = sharedPreferences.getString("private", null);



        //publicKey must of type "KEY", so i need to convert publicKey to KEY, But its not happening
        cipher.init(Cipher.ENCRYPT_MODE,publicKey);
        byte[] encryptedtext=cipher.doFinal(sampletext.getBytes());
        String encrypted_text=new String(Base64.encode(encryptedtext,Base64.NO_WRAP));


       //privateKey is string, it supposed to be of type KEY
         cipher.init(Cipher.DECRYPT_MODE,privateKey);
         encryptedtext=Base64.decode(encrypted_text.getBytes(), Base64.NO_WRAP);
         encryptedtext=cipher.doFinal(encryptedtext);

         String  decrypted_text=new String(encryptedtext);
