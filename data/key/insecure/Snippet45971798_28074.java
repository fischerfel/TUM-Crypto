public static void test() {
    String stringToEncrypt = "R";
    stringToEncrypt= Encryption.encrypt(stringToEncrypt);
    System.out.println(stringToEncrypt);
    stringToEncrypt= Encryption.decrypt(stringToEncrypt);
    System.out.println(stringToEncrypt);
}

public class Encryption {
    private static final String key = "ert25424o";

    public static String encrypt(String password){
        try
        {
        Key clef = new SecretKeySpec(key.getBytes("ISO-8859-2"),"Blowfish");
        Cipher cipher=Cipher.getInstance("Blowfish");
        cipher.init(Cipher.ENCRYPT_MODE,clef);
        return new String(cipher.doFinal(password.getBytes()));
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public static String decrypt(String password){
        try
        {
            Key clef = new SecretKeySpec(key.getBytes("ISO-8859-2"),"Blowfish");
            Cipher cipher=Cipher.getInstance("Blowfish");
            cipher.init(Cipher.DECRYPT_MODE,clef);
            return new String(cipher.doFinal(password.getBytes()));
        }
        catch (Exception e)
        {
            System.out.println(e);
            return null;
        }
    }
}
