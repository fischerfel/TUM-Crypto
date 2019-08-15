package prestaAmazon;

import java.net.URLEncoder;
import java.security.SignatureException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;







public class AccessAmazon {

    public static void main(String[] args) throws SignatureException, UnsupportedEncodingException {
        String timeStamp = getCurrentTimeStamp();
                System.out.println(timeStamp);
        String data = "GET"+"\n"+"webservices.amazon.co.uk"+"\n"+"/onca/xml"+"\n"+"AWSAccessKeyId=MY_AWS_KEY&AssociateTag=MY_TAG&Condition=New&IdType=ASIN&ItemId=B0087Y7E5K&Operation=ItemLookup&ResponseGroup=Offers&Service=AWSECommerceService&Timestamp=2013-07-03T18%3A42%3A00.000Z&Version=2011-08-01";
        String key = "MY_SECRET_KEY";

        String signature = calculateHMAC(data, key);        
        System.out.println(signature);

        String restURL = "http://webservices.amazon.co.uk/onca/xml?AWSAccessKeyId=MY_AWS_KEY&AssociateTag=MY_TAG&Condition=New&IdType=ASIN&ItemId=B0087Y7E5K&Operation=ItemLookup&ResponseGroup=Offers&Service=AWSECommerceService&Timestamp=2013-07-03T18%3A42%3A00.000Z&Version=2011-08-01&Signature=" + signature;
        System.out.println(restURL);

    }

    private static final String HMAC_SHA_ALGORITHM = "HmacSHA256";


    public static String calculateHMAC(String data, String key)throws java.security.SignatureException{
        try {

        // get an hmac_sha256 key from the raw key bytes
        SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), HMAC_SHA_ALGORITHM);

        // get an hmac_sha256 Mac instance and initialize with the signing key
        Mac mac = Mac.getInstance(HMAC_SHA_ALGORITHM);
        mac.init(signingKey);

        // compute the hmac256 on input data bytes
        byte[] rawHmac = mac.doFinal(data.getBytes());

        // base64-encode the hmac256
        String result = new String(Base64.encodeBase64(rawHmac));
        return URLEncoder.encode(result, "UTF-8").replace("+", "%20").replace("*", "%2A").replace("%7E", "~");

        } catch (Exception e) {
            throw new SignatureException("Failed to generate HMAC : " + e.getMessage());
        }

        }



    public static String getCurrentTimeStamp() throws UnsupportedEncodingException {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS ");
        sdfDate.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date now = new Date();
        String strDate = sdfDate.format(now);
        return strDate.replaceFirst(" ", "T").replaceFirst(" ", "Z").replaceAll(":", "%3A");
    }

}
