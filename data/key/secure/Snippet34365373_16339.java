    SecretKeySpec skeySpec = new SecretKeySpec(skey, "AES");
    Cipher cipher;
    byte[] decryptedData=null;
    CipherInputStream cis=null;
    try {
        cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, new IvParameterSpec(iv));
        // Create CipherInputStream to read and decrypt the image data
        cis = new CipherInputStream(fis, cipher);
        // Write encrypted image data to ByteArrayOutputStream
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] data = new byte[2048];
        while ((cis.read(data)) != -1) {
            buffer.write(data);
        }
        buffer.flush();
        decryptedData=buffer.toByteArray();
    }catch(Exception e){
        e.printStackTrace();
    }
    finally{
        try {
            fis.close();
            cis.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    return decryptedData;
}
