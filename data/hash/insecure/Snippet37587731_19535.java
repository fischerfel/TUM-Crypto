import android.util.Base64;
import android.util.Log;import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

MessageDigest md = null;
try {
  PackageInfo info = context.getPackageManager().getPackageInfo(
      context.getPackageName(),
      PackageManager.GET_SIGNATURES);
  for (Signature signature : info.signatures) {
    md = MessageDigest.getInstance("SHA");
    md.update(signature.toByteArray());
  }
} catch (PackageManager.NameNotFoundException e) {

} catch (NoSuchAlgorithmException e) {

}
Log.i("KeyHash = ",Base64.encodeToString(md.digest(), Base64.DEFAULT));
