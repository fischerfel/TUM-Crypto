public class AES
{
static String encryptionKey = "48C3B4286FF421A4A328E68AD9E542A4";
    static String clearText = "00000000000000000000000000000000";

    public static void main(String[] args) throws UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException
    {
        encr();
    }

    public static String toHexString(byte[] ba)
    {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ba.length; i++)
        {
            sb.append(String.format("%02X ", ba[i]));
        }
        return sb.toString();
    }

    public static void encr() throws InvalidKeyException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException
    {
        //Security.addProvider(new com.sun.crypto.provider.SunJCE());
        SecretKeySpec secretKey = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), "AES");

        byte[] clearTextBytes = clearText.getBytes("UTF8");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] cipherBytes = cipher.doFinal(clearTextBytes);

        System.out.print("enc1:  ");
        for (int i = 0; i < cipherBytes.length; i++)
        {
            System.out.print(cipherBytes[i]);
        }
        System.out.println("");

        String cipherText = new String(cipherBytes, "UTF8");
        System.out.println("enc2: " + cipherText);

        System.out.println("enc3: " + toHexString(cipherText.getBytes("UTF-8")));
    }
}
