${
 import sun.misc.BASE64Encoder;
 import javax.crypto.Mac; 
 import javax.crypto.spec.SecretKeySpec;
 import java.security.InvalidKeyException; 
 import java.security.NoSuchAlgorithmException; 
 import java.io.UnsupportedEncodingException; 
 import java.util.ArrayList; 
 import java.util.Iterator;

    private String sign(HashMap params) throws InvalidKeyException, NoSuchAlgorithmException,    {
        return sign(buildDataToSign(params), "111111111111111");
    }

    private String sign(String data, String secretKey) throws InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException {
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(secretKeySpec);
        byte[] rawHmac = mac.doFinal(data.getBytes("UTF-8"));
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encodeBuffer(rawHmac).replace("\n", "");
    }

    private String buildDataToSign(HashMap params) {
        String[] signedFieldNames = String.valueOf(params.get("signed_field_names")).split(",");
        ArrayList<String> dataToSign = new ArrayList<String>();
        for (String signedFieldName : signedFieldNames) {
            dataToSign.add(signedFieldName + "=" + String.valueOf(params.get(signedFieldName)));
        }
        return commaSeparate(dataToSign);
    }

    private String commaSeparate(ArrayList<String> dataToSign) {
        StringBuilder csv = new StringBuilder();
        for (Iterator<String> it = dataToSign.iterator(); it.hasNext(); ) {
            csv.append(it.next());
            if (it.hasNext()) {
                csv.append(",");
            }
        }
         return csv.toString();
    }
}$  
