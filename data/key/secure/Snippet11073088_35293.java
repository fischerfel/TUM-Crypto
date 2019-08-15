package code.finalwork;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class FinalWorkActivity extends Activity {
    private String pref_file = "pref.xml";

    TextView pass;
    TextView pass_cnf;
    TextView err_msg;
    Button done;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        pass = (TextView) findViewById(R.id.pass);
        pass_cnf = (TextView) findViewById(R.id.pass_cnf);
        err_msg = (TextView) findViewById(R.id.error_pass);
        done = (Button) findViewById(R.id.btn_done);

        SharedPreferences pref = getSharedPreferences(pref_file,
                Context.MODE_PRIVATE);
        Boolean val = pref.getBoolean("firstuse", true);
        if (val) {
            SharedPreferences.Editor mod = pref.edit();
            mod.putBoolean("firstuse", false);
            mod.commit();

        }
    }

    // ///////////////////////////////////////////////////////////////////////
    public void onclick(View view) {
        switch (view.getId()) {
        case R.id.btn_done:
            String usrpass = pass.getText().toString();
            String cnfrmpass = pass_cnf.getText().toString();
            if (usrpass.equals(cnfrmpass)) {
                byte[] password = Base64.decode(usrpass, 0);
                byte[] key = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5,
                        6 };
                for (int i = 0; i < usrpass.length(); i++) {
                    key[i] = password[i];
                }
                try {
                    String passtostore = encrypt(usrpass, key);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                err_msg.setText("Password added");
                err_msg.setVisibility(View.VISIBLE);
            } else {
                err_msg.setText("Password Must Match");
                err_msg.setVisibility(View.VISIBLE);
            }
            break;
        }
    }

    // //////////////////////////////////////////////////////////////////////

    public String encrypt(String toencrypt, byte key[]) throws Exception {
        SecretKeySpec secret = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secret);
        byte[] encryptedbytes = cipher.doFinal(toencrypt.getBytes());
        String encrypted = Base64.encodeToString(encryptedbytes, 0);
        return encrypted;

    }
}  
