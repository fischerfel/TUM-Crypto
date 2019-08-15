private byte[] generateIv(int size) {
    byte[] iv = new byte[size];
    randomSecureRandom.nextBytes(iv);
    return iv;
}

@Override
public byte[] encryptData(byte[] iv, byte[] in, Key key) throws CryptoException {
    try {
        Cipher c = Cipher.getInstance("AES/CTR/PKCS5Padding");
        c.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
        return c.doFinal(in);
    } catch (Exception ex) {
        throw new CryptoException(ex);
    }
}

@Override
public byte[] decryptData(byte[] iv, byte[] in, Key key) throws CryptoException {
    try {
        Cipher c = Cipher.getInstance("AES/CTR/PKCS5Padding");
        c.init(Cipher.DECRYPT_MODE,key, new IvParameterSpec(iv));
        return c.doFinal(in);
    } catch(Exception ex) {
        throw new CryptoException(ex);
    }
}

@Override
public byte[] createHMAC(byte[] pauload, Key sigKey) throws CryptoException {
    try {
        Mac mac = Mac.getInstance("HMACSHA256");  
        mac.init(sigKey);
        byte[] digest = mac.doFinal(pauload);
        return digest;
    } catch (Exception e) {
        throw new CryptoException("unable to create HMAC",e);
    }
}
