import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

String secret = "<storage-account-key>";
// Date for string to sign
Calendar calendar = Calendar.getInstance();
SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss 'GMT'", Locale.US);
sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
String date = sdf.format(calendar.getTime());
// canonicalizedResource, such as "/testaccount1/Tables"
String canonicalizedResource = "<Canonicalized-Resource>";
String stringToSign = date + "\n" + canonicalizedResource;
System.out.println(stringToSign);
// HMAC-SHA@%^
Mac sha256HMAC = Mac.getInstance("HmacSHA256");
SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
sha256HMAC.init(secretKey);
String hash = Base64.encodeBase64String(sha256HMAC.doFinal(stringToSign.getBytes()));
System.out.println(hash);
