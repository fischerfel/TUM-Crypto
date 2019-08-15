import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
* RSA加密解密工具类
* User: shijinkui
* Date: 14-2-24
* Time: 上午11:20
* To change this template use File | Settings | File Templates.
*/
public class RSAUtil {
public static final String KEY_ALGORTHM = "RSA";
public static final String CIPHER_ALGORTHM = "RSA/ECB/PKCS1Padding";
public static final String SIGNATURE_ALGORITHM = "MD5withRSA";

private static RSAPublicKey publicKey;
private static RSAPrivateKey privateKey;

static {
    initKey();
}

/**
 * 初始化密钥
 *
 * @return
 * @throws Exception
 */
public static void initKey() {
    KeyPairGenerator keyPairGenerator;
    try {
        keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORTHM);
        keyPairGenerator.initialize(1024);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        publicKey = (RSAPublicKey) keyPair.getPublic();
        privateKey = (RSAPrivateKey) keyPair.getPrivate();
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    }
}


/**
 * 取得公钥，并转化为String类型
 *
 * @return
 * @throws Exception
 */
public static String getPublicKey() throws Exception {
    return base64Encode(publicKey.getEncoded());
}

/**
 * 取得私钥，并转化为String类型
 *
 * @return
 * @throws Exception
 */
public static String getPrivateKey() throws Exception {
    return base64Encode(privateKey.getEncoded());
}

/**
 * BASE64解密
 *
 * @param key
 * @return
 * @throws Exception
 */
public static byte[] base64Decode(String key) throws Exception {
    return Base64.decodeBase64(key);
}

/**
 * BASE64加密
 *
 * @param key
 * @return
 * @throws Exception
 */
public static String base64Encode(byte[] key) throws Exception {
    return Base64.encodeBase64String(key);
}

/**
 * 用私钥加密
 *
 * @param plainText 加密数据
 * @return
 * @throws Exception
 */
public static String encryptByPrivateKey(String plainText) throws Exception {
    byte[] encodedData = base64Decode(plainText);
    byte[] ret = rsaByPrivateKey(encodedData, Cipher.ENCRYPT_MODE);
    return base64Encode(ret);
}

/**
 * 用私钥解密
 *
 * @return
 * @throws Exception
 */
public static String decryptByPrivateKey(String data) throws Exception {
    byte[] encodedData = base64Decode(data);
    byte[] ret = rsaByPrivateKey(encodedData, Cipher.DECRYPT_MODE);

    return base64Encode(ret);
}

public static byte[] rsaByPrivateKey(byte[] data, int mode) throws Exception {
    byte[] keyBytes = privateKey.getEncoded();

    PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
    KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM);
    Key privateKey2 = keyFactory.generatePrivate(keySpec);

    Cipher cipher = Cipher.getInstance(CIPHER_ALGORTHM);
    cipher.init(mode, privateKey2);

    return cipher.doFinal(data, 0, data.length);
}


/**
 * 用公钥加密
 *
 * @param data 加密数据
 * @return
 * @throws Exception
 */
public static String encryptByPublicKey(String data) throws Exception {
    byte[] encodedData = base64Decode(data);
    byte[] ret = rsaByPublicKey(encodedData, Cipher.ENCRYPT_MODE);
    return base64Encode(ret);
}

/**
 * 用公钥解密
 *
 * @param data 加密数据
 * @return
 * @throws Exception
 */
public static String decryptByPublicKey(String data) throws Exception {
    byte[] base64Data = base64Decode(data);
    byte[] ret = rsaByPublicKey(base64Data, Cipher.DECRYPT_MODE);
    return base64Encode(ret);
}

public static byte[] rsaByPublicKey(byte[] data, int mode) throws Exception {
    byte[] keyBytes = publicKey.getEncoded();
    X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
    KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM);
    Key publicKey = keyFactory.generatePublic(keySpec);

    Cipher cipher = Cipher.getInstance(CIPHER_ALGORTHM);
    cipher.init(mode, publicKey);

    return cipher.doFinal(data, 0, data.length);
}


/**
 * 用私钥对信息生成数字签名
 *
 * @param data 要加密数据
 * @return
 * @throws Exception
 */
public static String sign(byte[] data) throws Exception {
    byte[] keyBytes = privateKey.getEncoded();
    PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);    //构造PKCS8EncodedKeySpec对象
    KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM);       //指定加密算法
    PrivateKey privateKey2 = keyFactory.generatePrivate(keySpec);       //取私钥匙对象

    Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);   //用私钥对信息生成数字签名
    signature.initSign(privateKey2);
    signature.update(data);

    return base64Encode(signature.sign());
}

/**
 * 校验数字签名
 *
 * @param data 加密数据
 * @param sign 数字签名
 * @return
 * @throws Exception
 */
public static boolean verify(byte[] data, String sign) throws Exception {
    byte[] keyBytes = publicKey.getEncoded();
    X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);  //构造X509EncodedKeySpec对象
    KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM);   //指定加密算法
    PublicKey publicKey2 = keyFactory.generatePublic(keySpec);      //取公钥匙对象

    Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
    signature.initVerify(publicKey2);
    signature.update(data);

    //验证签名是否正常
    return signature.verify(base64Decode(sign));
}

private static void test() throws Exception {
    String str = "1234";
    for (int i = 0; i < 1000000; i++) {
        String r1 = encryptByPrivateKey(str + i);
        String d1 = decryptByPublicKey(r1);
        String r2 = encryptByPublicKey(str + i);
        String d2 = decryptByPrivateKey(r2);

        System.out.println(str + i + ", 公钥解密：" + d1 + "||私钥解密:" + d2);
    }
}

public static void main(String[] args) throws Exception {
//      test();
    String str = "11";
    String r1 = encryptByPrivateKey(str);
    String d1 = decryptByPublicKey(r1);
    System.out.println(r1);
    System.out.println(d1);
}

}
