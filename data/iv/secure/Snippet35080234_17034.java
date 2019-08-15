private static String decrypt(SecretKey aesKey, String encodedCiphertext) {
    try {
        // that's no base 64, that's base 64 over the UTF-8 encoding of the code points
        byte[] ciphertext = jsBase64Decode(encodedCiphertext);
        Cipher aesCTR = Cipher.getInstance("AES/CTR/NOPADDING");
        int n = aesCTR.getBlockSize();
        byte[] counter = new byte[n];
        int nonceSize = n / 2;
        System.arraycopy(ciphertext, 0, counter, 0, nonceSize);
        IvParameterSpec iv = new IvParameterSpec(counter);
        aesCTR.init(Cipher.DECRYPT_MODE, aesKey, iv);
        byte[] plaintext = aesCTR.doFinal(ciphertext, nonceSize, ciphertext.length - nonceSize);
        return new String(plaintext, "UTF-8");
        // that's no base 64, that's base 64 over the UTF-8 encoding of the code points

    } catch (Exception e) {
        e.printStackTrace();
    }
    return "";
}

private static byte[] jsBase64Decode(String encodedCiphertext) {
    byte[] ciphertext = null;
    try {
        byte[] utf8CT = Base64.decode(encodedCiphertext);

        String cts = new String(utf8CT, "UTF-8");
        ciphertext = new byte[cts.length()];
        for (int i = 0; i < cts.length(); i++) {
            ciphertext[i] = (byte) (cts.charAt(i) & 0xFF);
        }

    }catch (Exception e) {
        e.printStackTrace();
    }
    //Arrays.copyOfRange(new byte[100], 0, 99);
    return ciphertext;
}

// that should not be a singleton lazybones, it may contain state
private static SecretKey deriveKey(String password, int nBits) throws CharacterCodingException {
    try {
        Charset charset = Charset.forName("UTF-8");
        CharsetEncoder encoder = charset.newEncoder();

        ByteBuffer buf = encoder.encode(CharBuffer.wrap(password));
        //byte[] buf1 = password.getBytes();
        int nBytes = nBits / Byte.SIZE; // bits / Byte.SIZE;
        Cipher aesECB = Cipher.getInstance("AES/ECB/NoPadding");
        int n = aesECB.getBlockSize();
        byte[] pwBytes = new byte[nBytes];
        // so we only use those characters that fit in nBytes! oops!
        buf.get(pwBytes, 0, buf.remaining());
        //pwBytes = password.getBytes("UTF-8");
        SecretKey derivationKey = new SecretKeySpec(pwBytes, "AES");
        aesECB.init(Cipher.ENCRYPT_MODE, derivationKey);
        // and although the derivationKey is nBytes in size, we only encrypt 16 (the block size)
        byte[] partialKey = aesECB.doFinal(pwBytes, 0, n);
        byte[] key = new byte[nBytes];
        System.arraycopy(partialKey, 0, key, 0, n);
        // but now we have too few so we *copy* key bytes
        // so only the increased number of rounds is configured using nBits
        System.arraycopy(partialKey, 0, key, n, nBytes - n);
        SecretKey derivatedKey = new SecretKeySpec(key, "AES");
        return derivatedKey;
    } catch (Exception e) {
        throw new IllegalStateException("Key derivation should always finish", e);
    }
}


public static String main(){

    SecretKey key = null;
    try {
        key = deriveKey("aa2145f9e2a5daaa9c6a8ddc5f5c1a39", 256);


    } catch (Exception e) {
        e.printStackTrace();
    }
    // ciphertext may vary in length depending on UTF-8 encoding
    String pt = decrypt(key, "eQDH+srPqlbh7Ml42g==");
    return pt;
}
