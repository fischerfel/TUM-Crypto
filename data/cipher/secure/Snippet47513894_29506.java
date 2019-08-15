import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

public class Test {

public static void main(String[] args) {
    try {
        runEncryption();
    } catch (Exception e) {
        e.printStackTrace();
    }
}

private static void runEncryption() throws Exception
{
    //String to be encrypted
    String plainText = "abcd@1234\n";

    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

    // IV text
    String iv = "C837E1B6C3D3A7E28F47719DE0C182C9";

    // getting 16 characters of iv text
    iv = iv.substring(0,16);

    // Value of key
    String key = "FB4FF1BA6F1FCC1A11B8B3910342CBD3A2BEAEB8F52E8910D9B25C0C96280EEA";

    // Logic for converting 16 Digits of IV into HEX
    StringBuffer hexString = new StringBuffer();
    for (int i=0;i<iv.getBytes().length;i++) {
        String hex=Integer.toHexString(0xff & iv.getBytes()[i]);
        if(hex.length()==1) hexString.append('0');
        hexString.append(hex);
    }

    // Seems something wrong here because if i am passing all the bytes to keySpe like key.getBytes() it is producing exception  so i am passing 16 bytes as previous code was doing in SO
    SecretKeySpec keySpec = new SecretKeySpec(hexToBytes(key), 0, 16, "AES");
    IvParameterSpec ivSpec = new IvParameterSpec(hexToBytes(hexString.toString()));

    cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
    byte[] encrypted = cipher.doFinal(plainText.getBytes("UTF-8"));

    String encryptedBase64 = new String(DatatypeConverter.printBase64Binary(encrypted));

    System.out.println("");
    System.out.println("Encrypted base64 = " + encryptedBase64);
}

private static byte[] hexToBytes(String s)
{
    int len = s.length();
    byte[] data = new byte[len / 2];

    for (int i = 0; i < len; i += 2)
        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));

    return data;
}    
}
