    CipherInputStream cis;
    String salt = "1234567890123456";
    String password = "abcdEFGH";

    password = password.concat(salt);
    String validpassword = password.substring(0, 16);
    SecretKeySpec secretKey = new SecretKeySpec(validpassword.getBytes(),"AES");   
    AlgorithmParameterSpec paramSpec = new IvParameterSpec(salt.getBytes());

    try  {
        // Creation of Cipher objects
        Cipher encrypt = 
         Cipher.getInstance("AES/CFB8/NoPadding");
        encrypt.init(Cipher.ENCRYPT_MODE, secretKey,paramSpec);

        // Open the file
        try {
             fis = new FileInputStream(file);
        } catch(IOException err) {
             System.out.println("Cannot open file!");
             return null;
        }
        cis = new CipherInputStream(fis, encrypt);

        // Write to the Encrypted file
        fos = new FileOutputStream(desFile);
        byte[] b = new byte[256];
        int i = cis.read(b);
        while (i != -1) {
             fos.write(b, 0, i);
             i = cis.read(b);
        }
