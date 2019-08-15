 public FunctionClass() {  
        try {  
        keyGenerator = KeyGenerator.getInstance("Blowfish");  
        secretKey = keyGenerator.generateKey();  
        cipher = Cipher.getInstance("Blowfish");  
    } catch (NoSuchPaddingException ex) {  
        System.out.println(ex);  
    } catch (NoSuchAlgorithmException ex) {  
        System.out.println(ex);  
    }  
}

 public void encrypt(String srcPath, String destPath) {  
    File rawFile = new File(srcPath);  
    File encryptedFile = new File(destPath);  
    InputStream inStream = null;  
    OutputStream outStream = null;  
    try {  
        /** 
         * Initialize the cipher for encryption 
         */  
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);  
        /** 
         * Initialize input and output streams 
         */  
        inStream = new FileInputStream(rawFile);  
        outStream = new FileOutputStream(encryptedFile);  
        byte[] buffer = new byte[1024];  
        int len;  
        while ((len = inStream.read(buffer)) > 0) {  
            outStream.write(cipher.update(buffer, 0, len));  
            outStream.flush();  
        }  
        outStream.write(cipher.doFinal());  
        inStream.close();  
        outStream.close();  
    } catch (IllegalBlockSizeException ex) {  
        System.out.println(ex);  
    } catch (BadPaddingException ex) {  
        System.out.println(ex);  
    } catch (InvalidKeyException ex) {  
        System.out.println(ex);  
    } catch (FileNotFoundException ex) {  
        System.out.println(ex);  
    } catch (IOException ex) {  
        System.out.println(ex);  
    }  
}
