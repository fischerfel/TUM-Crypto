cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

public String crypterMessage(String message) {
        cipher.init(Cipher.ENCRYPT_MODE, key);
        String messageCrypte = new String(Hex.encodeHex(cipher.doFinal(message.getBytes())));
        mIv = cipher.getIV();
        return messageCrypte;
}

public String decrypterMessage(String messageCrypte) {
        IvParameterSpec ivParameterSpec = new IvParameterSpec(mIv);
        cipher.init(Cipher.DECRYPT_MODE, obtenirCleSecrete(), ivParameterSpec);
        return new String(cipher.doFinal(Hex.decodeHex(messageCrypte.toCharArray())));
}
