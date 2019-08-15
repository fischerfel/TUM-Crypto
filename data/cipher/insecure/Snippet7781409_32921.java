public static byte[] encrypt(String message) throws Exception
{
    String salt = "1111111111111111";
    SecretKeySpec key = new SecretKeySpec(salt.getBytes(), "AES");
    Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding", "SunJCE");
    cipher.init(Cipher.ENCRYPT_MODE, key);
    return cipher.doFinal(message.getBytes());
}

public static void main (String[] args) throws Exception
{
    String hello = Arrays.toString(encrypt("hello"));
    System.out.println("hello:" + hello);
    String helloWorld = Arrays.toString(encrypt("hello world"));
    System.out.println("hello world:" + helloWorld);
}
