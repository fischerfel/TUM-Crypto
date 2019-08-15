public class Decryptor {


public static byte[] decryptData(byte[] encryptedCredentials, String key){

    Cipher c;
    SecretKeySpec k;
    byte[] byteSharedKey = null;
    byte[] byteObject = null;


    try {

        byteSharedKey = getByteKey(key);

        c = Cipher.getInstance("AES");
        k = new SecretKeySpec(byteSharedKey, "AES");
        c.init(Cipher.DECRYPT_MODE, k);
        byteObject = c.doFinal(encryptedCredentials);



    } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
        throw new RuntimeException(e);
    } catch (InvalidKeyException e) {
        throw new RuntimeException(e);
    } catch (IOException e) {
        throw new RuntimeException(e);
    } catch (IllegalBlockSizeException e) {
        throw new RuntimeException(e);
    } catch (BadPaddingException e) {
        throw new RuntimeException(e);
    }

    return byteObject;

}

public static Object getObjectFromBytes(byte[] credentials) throws IOException, ClassNotFoundException{

    ByteArrayInputStream bis = new ByteArrayInputStream(credentials);
    ObjectInput in = null;
    ITU_Credentials credentialsObj = null;

    try {

        in = new ObjectInputStream(bis);
        credentialsObj = (ITU_Credentials)in.readObject(); 

    } finally {
      bis.close();
      in.close();
    }
    return credentialsObj;
}


private static byte[] getByteKey(String key) throws UnsupportedEncodingException, NoSuchAlgorithmException{

    //Converting key to SHA-1 and trimming to mach maximum lenght of key

    byte[] bkey = key.getBytes("UTF-8");
    MessageDigest sha = MessageDigest.getInstance("SHA-1");
    bkey = sha.digest(bkey);
    bkey = Arrays.copyOf(bkey, 16);

    return bkey;
}

public static void main(String[] args) {
    new Encryptor();
}

}
