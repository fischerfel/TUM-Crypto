public class LastNameEncryption implements AttributeConverter<String,String> {

    private static SecretKeySpec secretKey;
    private final static String peselKey = "somekey";

    @Override
    public String convertToDatabaseColumn(String attribute) {
        try
        {
            setKey();
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(attribute.getBytes("UTF-8")));
        }
        catch (Exception e)
        {
            System.out.println("Error while encrypting: " + e.toString());
        }
        return null;

    }

    public String convertToEntityAttribute(String dbData) {
        try {
            setKey();
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(dbData)));
        }
        catch (Exception e)
        {
            System.out.println("Error while decrypting: " + e.toString());
        }
        return null;

    }

    public static void setKey() {
        MessageDigest sha = null;
        byte[] key;
        try {
            key = peselKey.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, "AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }  catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

}
