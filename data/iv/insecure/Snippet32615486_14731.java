package ngProjiect;
import java.security.Key;    
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
public class ThreeDes {

static IvParameterSpec iv = null;
private static Key key = null;

public static void main(String[] args) throws Exception {
            String deco = decode("j3YRYsmVWRnM2T2pN83tbQ==");
    String enco = encode("123456789");
     System.out.println("--:" + deco);
    System.out.println(":" + enco);

}

public static String decode(String encryptText) throws Exception {
    String v6;
    CryptoTools();
    Cipher v0 = Cipher.getInstance("DES/CBC/PKCS5Padding");
    v0.init(2, key, iv);
    v6 = new String(v0.doFinal(Base64.decode(encryptText)), "utf-8");
    return v6;
}

public static String encode(String plainText) throws Exception {
    String v7;
    CryptoTools();
    Cipher v0 = Cipher.getInstance("DES/CBC/PKCS5Padding");
    v0.init(1, key, iv);
    byte[] encryptData = v0.doFinal(plainText.getBytes("utf-8"));
    v7 = Base64.encode(encryptData);
    return v7;
}

private static void CryptoTools() throws Exception {
    byte[] DESkey = { 35, 12, 37, 49, 101, 91, 14, 36 };
    byte[] DESIV = { 19, 31, 97, 26, 10, 80, 11, 37 };
    DESKeySpec keySpec = new DESKeySpec(DESkey);// 设置密钥参数
    iv = new IvParameterSpec(DESIV);// 设置向量
    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");// 获得密钥工厂
    key = keyFactory.generateSecret(keySpec);// 得到密钥对象
}
