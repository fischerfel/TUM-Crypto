import java.security.InvalidKeyException;

import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;

import javax.crypto.spec.SecretKeySpec;

import com.caucho.util.Base64;

import com.caucho.quercus.module.AbstractQuercusModule;

public class HmacSHA256 extends AbstractQuercusModule {

    public String compute(String baseString, String key) throws InvalidKeyException, NoSuchAlgorithmException {
        Mac mac;
        mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret = new SecretKeySpec(key.getBytes(), mac.getAlgorithm());
        mac.init(secret);
        byte[] digest = mac.doFinal(baseString.getBytes());
        return Base64.encode(digest);
    }
}
