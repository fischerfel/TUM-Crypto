public class Encryptor {

public static byte[] encryptData(byte[] credentials, String key){

    Cipher c;
    SecretKeySpec k;
    byte[] byteCredentials = null;
    byte[] encryptedCredentials = null;
    byte[] byteSharedKey = null;

    try {

        byteCredentials = getBytesFromObject(credentials);
        byteSharedKey = getByteKey(key);

        c = Cipher.getInstance("AES");
        k = new SecretKeySpec(byteSharedKey, "AES");
        c.init(Cipher.ENCRYPT_MODE, k);
        encryptedCredentials = c.doFinal(byteCredentials);

    } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
        e.printStackTrace();
    } catch (InvalidKeyException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    } catch (IllegalBlockSizeException e) {
        e.printStackTrace();
    } catch (BadPaddingException e) {
        e.printStackTrace();
    }

    return encryptedCredentials;

}

public static byte[] getBytesFromObject(Object credentials) throws IOException{

    //Hmmm.... now I'm thinking I should make generic type for both: Token and ITU_Credentials object, that would have this getBytes and getObject methods.
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    ObjectOutput out = null;
    byte[] newBytes = null;

    try {

      out = new ObjectOutputStream(bos);   
      out.writeObject(credentials);
      newBytes = bos.toByteArray();

    } catch (IOException e) {
        e.printStackTrace();
    } finally {
      out.close();
      bos.close();
    }
    return newBytes;
}

private static byte[] getByteKey(String key) throws UnsupportedEncodingException, NoSuchAlgorithmException{

    //Converting key to SHA-1 and trimming to mach maximum lenght of key

    byte[] bkey = key.getBytes("UTF-8");
    MessageDigest sha = MessageDigest.getInstance("SHA-1");
    bkey = sha.digest(bkey);
    bkey = Arrays.copyOf(bkey, 16);

    return bkey;
}
