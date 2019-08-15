package md5.android;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException; 
import java.util.ArrayList; 
import java.util.Collections;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class md5android extends Activity {

    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState);

        String s = "test";
        String  res = md5(s);
        TextView tv = new TextView(this);
        tv.setText(res);
        setContentView(tv);
    }

    public String md5(String s) { 
        try {
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            StringBuffer hexString = new StringBuffer();
            for (int i=0; i<messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));

            return hexString.toString();

        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

}
