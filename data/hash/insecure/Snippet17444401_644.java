import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.os.Bundle;
import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.text.Editable;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {

    Button btn;
    EditText et;
    PackageInfo info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn=(Button)findViewById(R.id.button1);
        et=(EditText)findViewById(R.id.editText1);
        btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                try {
                    info = getPackageManager().getPackageInfo("com.example.id", PackageManager.GET_SIGNATURES);
                    for (Signature signature : info.signatures) {
                        MessageDigest md;
                        md = MessageDigest.getInstance("SHA");
                        md.update(signature.toByteArray());
                        String something = new String(Base64.encode(md.digest(), 0));
                        //String something = new String(Base64.encodeBytes(md.digest()));
                        et.setText("" + something);
                        Log.e("hash key", something);
                    }
                } catch (NameNotFoundException e1) {
                    Log.e("name not found", e1.toString());
                } catch (NoSuchAlgorithmException e) {
                    Log.e("no such an algorithm", e.toString());
                } catch (Exception e) {
                    Log.e("exception", e.toString());
                }
            }
        });
    }



}
