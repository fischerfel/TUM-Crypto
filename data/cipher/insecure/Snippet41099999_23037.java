    @Test
public void testGet() {
    Cipher cipher = null;
    try {
        SecretKey sks= getKeySpec(pass, salt);
        cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, sks);
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    } catch (NoSuchPaddingException e) {
        e.printStackTrace();
    } catch (InvalidKeySpecException e) {
        e.printStackTrace();
    } catch (InvalidKeyException e) {
        e.printStackTrace();
    }
}

public SecretKey getKeySpec(char[] pass, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {

    //generate key spec...
    return secretKeyFactory.generateSecret(keySpec);
}
