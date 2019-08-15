class NaiveHmacSigner {

public static byte[] hmac(String algorithm, byte[] secret, String data)
    throws NoSuchAlgorithmException, InvalidKeyException {

    final Mac mac = Mac.getInstance(algorithm);
    final SecretKeySpec spec = new SecretKeySpec(secret, algorithm);
    mac.init(spec);
    return mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
}


private final String identity;
private final byte[] secret;

public NaiveHmacSigner(String id, String key) {
    this.identity = String.valueOf(id);
    this.secret = Base64.getDecoder().decode(key);
}

public Map<String, String> newSignature(String method, String path)
    throws InvalidKeyException, NoSuchAlgorithmException {

    final String date = OffsetDateTime.now().format(RFC_1123_DATE_TIME);
    final String nonce = String.valueOf(System.currentTimeMillis());

    final String data = method + path + date + nonce;

    final String digest = Base64.getEncoder().encodeToString(
        hmac("HmacSHA256", this.secret, data)
    );

    final Map<String, String> result = new HashMap<>();
    result.put("Date", date);
    result.put("Authentication", String.format("hmac %s:%s:%s", this.identity, nonce, digest));
    return result;
}
}

public class SignTest {
/**
 * @param args the command line arguments
 */
public static void main(String[] args) throws InvalidKeyException, NoSuchAlgorithmException, IOException {
     NaiveHmacSigner signer = new NaiveHmacSigner("45000007551554", "3M0VjbYDkSXJFZbqdGEjpI/kOCB22IsBxG1BslUDrwU=");
    Map<String, String> signature = signer.newSignature("GET", "/api/client/mobile/1.0/history");

    System.out.println(signature);
}
}
