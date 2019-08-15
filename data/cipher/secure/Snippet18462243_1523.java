import java.util.HashMap;
import java.util.Map;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import net.sf.json.JSONObject;
import org.apache.commons.codec.binary.Hex;

class Crypt {
    public static void main(String[] args) throws Exception {
        Map<String, String> node = new HashMap<String, String>();
        node.put("timestamp", "1377499097199");
        JSONObject jsonObject = JSONObject.fromObject(node);
        String json = jsonObject.toString();
        System.out.println(json);

        //key
        String skeyString = "97128424897797a166913557a6f4cc8e";
        byte[] skey = Hex.decodeHex(skeyString.toCharArray());
        System.out.println("key : " + skeyString);

        //iv
        String ivString = "84e8c3ea8859a0e293941d1cb00a39c3";
        byte[] iv = Hex.decodeHex(ivString.toCharArray());
        System.out.println("iv : " + ivString);

        //encrypt
        SecretKeySpec skeySpec1 = new SecretKeySpec(skey, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec1, new IvParameterSpec(iv));
        byte[] encrypted = cipher.doFinal(json.getBytes());
        String encryptedString = Hex.encodeHexString(encrypted);
        System.out.println("=============>");
        System.out.println("encrypted string: " + encryptedString);
        }
}
