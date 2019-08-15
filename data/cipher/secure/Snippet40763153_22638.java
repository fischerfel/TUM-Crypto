public void testEncryptionKey(){
    MessageDigest digest = null;
    try {

        digest = MessageDigest.getInstance("SHA-256");
        digest.update("Hello".getBytes("UTF-8"));
        byte[] keyBytes = new byte[32];
        keyBytes = digest.digest();
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        SecretKeySpec key = new SecretKeySpec(keyBytes,"AES");
        AlgorithmParameterSpec spec = getIV();
        cipher.init(Cipher.ENCRYPT_MODE, key, spec);

    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
    } catch (InvalidAlgorithmParameterException e) {
        e.printStackTrace();
    } catch (NoSuchPaddingException e) {
        e.printStackTrace();
    } catch (InvalidKeyException e) {
        e.printStackTrace();
    }
}

public AlgorithmParameterSpec getIV()
{
    byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    IvParameterSpec ivParameterSpec;
    ivParameterSpec = new IvParameterSpec(iv);
    byte[] ivv = ivParameterSpec.getIV();
    return ivParameterSpec;
}
