private Cipher aesCipherForDecryption;
String strDecryptedText = new String();

public String decryptAES(final String ciphertext) {

    try {

        aesCipherForDecryption = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        aesCipherForDecryption.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iV));
        byte[] byteDecryptedText = aesCipherForDecryption.doFinal(byteCipherText);
        strDecryptedText = new String(byteDecryptedText);

    } catch (IllegalBlockSizeException e) {
        System.out.print("IllegalBlockSizeException " +e);
    } catch (BadPaddingException e) {
        System.out.print("BadPaddingException "+e);
    } catch (NoSuchAlgorithmException e) {
        System.out.print("NoSuchAlgorithmException "+ e);
    } catch (NoSuchPaddingException e) {
        System.out.print("NoSuchPaddingException "+e);
    } catch (InvalidKeyException e) {
        System.out.print("InvalidKeyException "+e);
    } catch (InvalidAlgorithmParameterException e) {
        System.out.print("InvalidAlgorithmParameterException "+e);
    }

    System.out.println("\nDecrypted Text message is " + strDecryptedText);
    return strDecryptedText;
}
