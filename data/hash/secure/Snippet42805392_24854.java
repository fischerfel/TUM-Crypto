 private static byte[] HmacSHA256(String data, byte[] key) throws Exception {
    String algorithm="HmacSHA256";
    Mac mac = Mac.getInstance(algorithm);
    mac.init(new SecretKeySpec(key, algorithm));
    return mac.doFinal(data.getBytes("UTF8"));
}

private static byte[] getSignatureKey(String key, String dateStamp, String regionName, String serviceName) throws Exception {
    byte[] kSecret = ("AWS4" + key).getBytes("UTF8");
    byte[] kDate = HmacSHA256(dateStamp, kSecret);
    byte[] kRegion = HmacSHA256(regionName, kDate);
    byte[] kService = HmacSHA256(serviceName, kRegion);
    byte[] kSigning = HmacSHA256("aws4_request", kService);
    return kSigning;
}

private String SHA256HEX(String content) throws Exception
{
    MessageDigest digest = MessageDigest.getInstance("SHA-256");
    byte[] hash = digest.digest(content.getBytes(Util.UTF_8));
    return Util.encodeToBase16(hash);
}

public String getSignature(String url,long date,String payLoad) throws Exception
{
    String signature = "";

    String amzDate = Util.convertDateTo(ISO8601_HUMAN_READABLE,date);
    String amzDateShort = Util.convertDateTo(ISO8601_HUMAN_READABLE,date).replace(":","").replace("-","").split("\\.")[0] + "Z";
    String dateStamp = Util.convertDateTo(SHORT_DATE,date);

    String separator = File.separator;
    separator += separator;
    String host = url.split(separator)[1];

    String canonicalHeaders = "accept:application/hal+json\nhost:" + host + "\nx-amz-date:" + amzDate + "\n";

    String signedHeaders = "accept;host;x-amz-date";

    String payLoadHash = SHA256HEX(payLoad);

    String canonicalRequest = "POST\n" + AMASender.CANONICAL_URI + "\n\n" + canonicalHeaders +"\n" + signedHeaders + "\n"+ payLoadHash;

    String algorithm = "AWS4-HMAC-SHA256";
    String credentialScope = dateStamp + "/" + AMASender.REGION + "/" + AMASender.SERVICE_NAME + "/aws4_request";
    String stringToSign = algorithm + "\n" +  amzDateShort + "\n" +  credentialScope + "\n" +  SHA256HEX(canonicalRequest);

    byte[] signingKey = getSignatureKey(mSecretKey,dateStamp,AMASender.REGION,AMASender.SERVICE_NAME);

    signature = Util.encodeToBase16(HmacSHA256(stringToSign,signingKey)) ;

    return signature;
} 

public class Util {

private static String TAG = Util.class.getSimpleName();

private static int LOG_PRIORITY = 3;

public final static String UTF_8 = "UTF-8";

public static int timeout = 10000;

public static DateFormat ISO8601_HUMAN_READABLE = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'") ;
public static DateFormat ISO8601 = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'") ;
public static DateFormat SHORT_DATE_SEPARATOR = new SimpleDateFormat("yyyy-MM-dd") ;
public static DateFormat SHORT_DATE = new SimpleDateFormat("yyyyMMdd") ;

public static String getUTCDate(DateFormat dateFormat)
{
    return convertDateTo(dateFormat,System.currentTimeMillis());
}

/**
 * Get the ISO 8601 Format of the current UTC Time
 * @return current time
 */
public static String getUTCDate()
{
  return getUTCDate(ISO8601);
}

public static String getUTCDateLong()
{
    return getUTCDate(ISO8601_HUMAN_READABLE);
}

public static String convertDateTo(DateFormat dateFormat,long date)
{
    TimeZone tz = TimeZone.getTimeZone("UTC");
    dateFormat.setTimeZone(tz);
    return dateFormat.format(new Date(date));
}

public static String encodeToBase16(byte[] byteArray) {

    final char[] HEX = new char[]{
            '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    StringBuffer hexBuffer = new StringBuffer(byteArray.length * 2);
    for (int i = 0; i < byteArray.length; i++)
        for (int j = 1; j >= 0; j--)
            hexBuffer.append(HEX[(byteArray[i] >> (j * 4)) & 0xF]);
    return hexBuffer.toString();
}
