//imports:

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;
import com.facebook.*;
import com.facebook.model.*;

@Override
 public void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  setContentView(R.layout.activity_main);


    try {
       **//be careful to put the correct package name, it was mmy first mistake**
       PackageInfo info = getPackageManager().getPackageInfo("[your package name, e.g.    com.yourcompany.yourapp]", PackageManager.GET_SIGNATURES);
       for (Signature signature : info.signatures) {
           MessageDigest md = MessageDigest.getInstance("SHA");
           md.update(signature.toByteArray());
           Log.d("Hash Key:","Hash Key " +Base64.encodeToString(md.digest(), Base64.DEFAULT));

        }
        } catch (NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

    }
   ...
  }
