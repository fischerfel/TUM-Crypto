import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import static java.lang.Character.digit;

public class CryptoClass {

public static void main(String[] args) throws Exception {
    byte[] decryptByte = Decrypt();
    String hexString = ByteToHex(decryptByte);
    StringBuilder decryptedString = HexToString(hexString);
    System.out.println(decryptedString);
}

public static byte[] Decrypt() throws Exception {
    //
    String algorithm = "AES";
    String mode = "CTR";
    String padding = "NoPadding";
    byte[] ciphertextBytes = StringToByte("770b80259ec33beb2561358a9f2dc617e46218c0a53cbeca695ae45faa8952aa0e311bde9d4e01726d3184c34451");
    byte[] keyBytes = StringToByte("36f18357be4dbd77f050515c73fcf9f2");
    IvParameterSpec ivParamSpec = null;
    int ivSize = 16;
    byte[] iv = new byte[ivSize];

    SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
    secureRandom.nextBytes(iv);
    ivParamSpec = new IvParameterSpec(iv);
    SecretKey aesKey = new SecretKeySpec(keyBytes, "AES");

    Cipher cipher = Cipher.getInstance(algorithm + "/" + mode + "/" + padding, "JsafeJCE");
    cipher.init(Cipher.DECRYPT_MODE, aesKey, ivParamSpec);

    byte[] result = cipher.doFinal(ciphertextBytes);
    return result;
}

//convert ByteArray to Hex String
public static String ByteToHex(byte[] byteArray) {
    StringBuilder sb = new StringBuilder();
    for (byte b : byteArray)
    {
        sb.append(String.format("%02X", b));
    }
    return sb.toString();
}

//convert String to ByteArray
private static byte[] StringToByte(String input) {
    int length = input.length();
    byte[] output = new byte[length / 2];

    for (int i = 0; i < length; i += 2) {
        output[i / 2] = (byte) ((digit(input.charAt(i), 16) << 4) | digit(input.charAt(i+1), 16));
    }
    return output;
}
//changes a hex string into plain text
public static StringBuilder HexToString(String hex) throws Exception {
    StringBuilder output = new StringBuilder();
    for (int i = 0; i < hex.length(); i+=2) {
        String str = hex.substring(i, i+2);
        output.append((char)Integer.parseInt(str, 16));
    }
    return output;
  }
}
