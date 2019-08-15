private BufferedImage load(String s){
BufferedImage image;
        try{
            image = ImageIO.read(getDecryptedStream(Buffers.class.getResourceAsStream(s)));
            return image;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
}

private InputStream getDecryptedStream(InputStream inputStream) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException{
    Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
    cipher.init(Cipher.DECRYPT_MODE, this.key);
    CipherInputStream input = new CipherInputStream(inputStream, cipher);

    return input;
}
