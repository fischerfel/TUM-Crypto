import javax.crypto.*
import javax.crypto.spec.*

import org.apache.commons.codec.binary.*
import org.scribe.exceptions.*
import org.scribe.services.SignatureService
import org.scribe.utils.*

public class HMACSha256SignatureService implements SignatureService {
    private static final String EMPTY_STRING = "";
    private static final String CARRIAGE_RETURN = "\r\n";
    private static final String UTF8 = "UTF-8";
    private static final String HMAC_SHA256 = "HMACSHA256";
    private static final String METHOD = "HMAC-SHA256";

    /**
     * {@inheritDoc}
     */
    public String getSignature(String baseString, String apiSecret, String tokenSecret) {
        try {
            println baseString
            Preconditions.checkEmptyString(baseString, "Base string cant be null or empty string");
            Preconditions.checkEmptyString(apiSecret, "Api secret cant be null or empty string");
            return doSign(baseString, OAuthEncoder.encode(apiSecret) + '&' + OAuthEncoder.encode(tokenSecret));
        }
        catch (Exception e) {
            throw new OAuthSignatureException(baseString, e);
        }
    }

    private String doSign(String toSign, String keyString) throws Exception {
        SecretKeySpec key = new SecretKeySpec((keyString).getBytes(UTF8), HMAC_SHA256);
        Mac mac = Mac.getInstance(HMAC_SHA256);
        mac.init(key);
        byte[] bytes = mac.doFinal(toSign.getBytes(UTF8));
        String a = new String(Base64.encodeBase64(bytes)).replace(CARRIAGE_RETURN, EMPTY_STRING)
        println a
        return a;
    }

    public String getSignatureMethod() {
        return METHOD;
    }
}
