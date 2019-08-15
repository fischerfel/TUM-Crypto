public String decrypt(String text) throws DecryptionException{
        try{

        //byte[] decordedValue = new BASE64Decoder().decodeBuffer(text);
        // tried too use it but with poor outcome
        //c = Cipher.getInstance("AES/ECB/PKCS5Padding");
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] tmp1 = text.getBytes("UTF-8");
        //byte[] tmp = c.doFinal("aaaaaaaaaaaaaaaa".getBytes());
        //this returns BadPaddingException
        byte[] tmp = c.doFinal(text.getBytes());
        return new String(tmp, "UTF-8");

    }catch(IllegalBlockSizeException e){
    throw new DecryptionException();
    }
}
