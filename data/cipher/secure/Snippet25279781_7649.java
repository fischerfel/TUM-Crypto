public static Cipher RSA1024Cipher(int mode)
        throws NoSuchAlgorithmException, NoSuchPaddingException,
        InvalidKeyException, InvalidKeySpecException,
        UnsupportedEncodingException {
    Cipher RsaCipher;
    RsaCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
    BigInteger modulus = new BigInteger(Base64.decode(RSAModulus,
            Base64.DEFAULT));//RSAModulus is provided out of this block of code.

    // Log.d("RSA Encryption:", "modulus:" + modulus);
    BigInteger exponent = new BigInteger(Base64.decode(RSAExponent,
            Base64.DEFAULT));//RSAExponent is provided out of this block of code.
    // Log.d("RSA Encryption:", "Exponent:" + exponent);
    RSAPublicKeySpec KeySpec = new RSAPublicKeySpec(modulus, exponent);
    KeyFactory keyFact = KeyFactory.getInstance("RSA");
    PublicKey pk = keyFact.generatePublic(KeySpec);
    RsaCipher.init(mode, pk);

    return RsaCipher;

}
// the data byte array should always be size of 8, UTF-8 based.
public static String RSA1024EncryptionToBase64String(final byte[] data) {
    try {

        Cipher RsaCipher = RSA1024Cipher(Cipher.ENCRYPT_MODE);

        System.out.println(RsaCipher.getBlockSize());//i got 117
        System.out.println(data.length);//this return 8
        // Log.d("Base64UnEncryptedByteData", new String(data));
        byte[] CipherText = RsaCipher.doFinal(data);
        System.out.println(CipherText.length);
        System.out.println(new String(CipherText));
        byte[] base64 = Base64.encode(CipherText, Base64.NO_WRAP);//NO_WRAP for sure
        // Log.d("Base64ByteData", new String(base64));
        return new String(base64);
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    } catch (NoSuchPaddingException e) {
        e.printStackTrace();
    } catch (InvalidKeyException e) {
        e.printStackTrace();
    } catch (InvalidKeySpecException e) {
        e.printStackTrace();
    } catch (IllegalBlockSizeException e) {
        e.printStackTrace();
    } catch (BadPaddingException e) {
        e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
    } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    return "";
}
