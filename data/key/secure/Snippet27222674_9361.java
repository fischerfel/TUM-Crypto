import org.apache.commons.codec.binary.Base64;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

private JSONObject parseFBSignedRequest(String signedRequest, String secret) throws UnsupportedEncodingException, Exception {


    //split request into signature and data
    String[] signedRequests = signedRequest.split("\\.", 2);
    //parse signature
    String sig = signedRequests[0];

    //parse data and convert to json object
    String data = signedRequests[1];

    //I assumed it is UTF8
    JSONObject jsonData = new JSONObject(new String(Base64.decodeBase64(data), "UTF-8"));
    //check signature algorithm
    if(!jsonData.getString("algorithm").equals("HMAC-SHA256")) {
        //unknown algorithm is used
        return null;
    }

    //check if data is signed correctly
    if(!hmacSHA256(signedRequests[1], secret).equals(sig)) {
        //signature is not correct, possibly the data was tampered with
        return null;
    }
    return jsonData;

}

//HmacSHA256 implementation 
private String hmacSHA256(String data, String key) throws Exception {
    SecretKeySpec secretKey = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
    Mac mac = Mac.getInstance("HmacSHA256");
    mac.init(secretKey);
    byte[] hmacData = mac.doFinal(data.getBytes("UTF-8"));
    return new String(Base64.encodeBase64URLSafe(hmacData), "UTF-8");
}
