public static byte[] decodeFile(byte[] key, byte[] fileData)
{
    byte[] decrypted=new byte[0];
    try {
        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES");

        cipher.init(Cipher.DECRYPT_MODE, skeySpec);

         decrypted= cipher.doFinal(fileData);
    } catch (IllegalBlockSizeException e) {
        e.printStackTrace();
    } catch (InvalidKeyException e) {
        e.printStackTrace();
    } catch (BadPaddingException e) {
        e.printStackTrace();
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    } catch (NoSuchPaddingException e) {
        e.printStackTrace();
    }

    return decrypted;
}
