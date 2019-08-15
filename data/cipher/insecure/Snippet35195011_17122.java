private static synchronized Cipher getAesCipher() throws NoSuchAlgorithmException, NoSuchPaddingException{
    if (_aesCipher == null) {
        _aesCipher = Cipher.getInstance("AES");
    }

    return _aesCipher;
}
