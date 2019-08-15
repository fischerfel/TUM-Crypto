import java.io.File;
import java.io.FileInputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Calendar;
import java.util.Random;

import javax.crypto.Cipher;

import android.util.Base64;
public static String decrypt(String inputString, byte[] keyBytes) {
    String resultStr = null;
    Calendar cal = Calendar.getInstance();
    int mDay = cal.get(Calendar.DAY_OF_MONTH);
    Random generator = new Random(mDay);
    int num = (generator.nextInt()) % 100;
    String salt = "XXwerr" + num;
    PrivateKey privateKey = null;
    try {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(keyBytes);
        privateKey = keyFactory.generatePrivate(privateKeySpec);
    } catch (Exception e) {
        System.out.println("Exception privateKey:::::::::::::::::  "
                + e.getMessage());
    }
    byte[] decodedBytes = null;
    try {
        Cipher c = Cipher.getInstance("RSA");
         //Also tried 
        // Cipher c = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        c.init(Cipher.DECRYPT_MODE, privateKey);
        decodedBytes = c.doFinal(Base64.decode(inputString, Base64.NO_CLOSE));

    } catch (Exception e) {
        System.out.println("Exception privateKey1:::::::::::::::::  "
                + e.getMessage());
        e.printStackTrace();
    }
    if (decodedBytes != null) {
        resultStr = new String(decodedBytes);
        System.out.println("resultStr:::" + resultStr + ":::::");
        resultStr = resultStr.replace(salt, "");
    }
    return resultStr;

}
