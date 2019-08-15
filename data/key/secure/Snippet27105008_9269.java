@Service
public class PersonServiceImpl implements PersonService {

    private static final String HMAC_ALGO = "HmacSHA256";
    private static final String TOKEN_SEPARATOR = ":";
    private static final long MAX_AGE = 1_000 * 60 * 60 * 24; // 24h
     private static final String signKey = "secretvalue";
 @Override
    public void createToken(String username){
        long timestamp = System.currentTimeMillis();
        StringBuilder sb = new StringBuilder();
        sb.append(generateTokenStringPublicPart(username, timestamp));
        sb.append(TOKEN_SEPARATOR);
        try {
            sb.append(computeSignature(username, timestamp, signKey));
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
}
 private static String generateTokenStringPublicPart(String username, long timestamp) {
        StringBuilder sb = new StringBuilder();
        sb.append(username);
        sb.append(TOKEN_SEPARATOR);
        sb.append(timestamp);
        return sb.toString();
    }

    private static String computeSignature(String username, long timestamp, String secretKey) throws InvalidKeyException, NoSuchAlgorithmException {
        StringBuilder sb = new StringBuilder();
        sb.append(generateTokenStringPublicPart(username, timestamp));
        SecretKeySpec sks = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), HMAC_ALGO);
        Mac hmac = Mac.getInstance(HMAC_ALGO);
        hmac.init(sks);
     return Base64.encodeBase64URLSafeString(hmac.doFinal(sb.toString().getBytes(StandardCharsets.UTF_8)));
    }

 public static boolean verifyToken(String token) throws InvalidKeyException, NoSuchAlgorithmException {
        String[] parts = token.split(TOKEN_SEPARATOR);
        boolean result = false;
        if (parts.length == 3) {
            String username = parts[0];
            System.out.println("username in verify token is"+username);
            Long timestamp = Long.valueOf(parts[1]);
            System.out.println("Current timestamp of token is"+timestamp);
            String signature = parts[2];
            if (signature.equals(computeSignature(username, timestamp, signKey))) {
                if (System.currentTimeMillis() - timestamp < MAX_AGE) {  // It fails here
                    result = true;
                }else {
                    System.out.println("Timestamp remaining is"+(System.currentTimeMillis() - timestamp));
                    System.out.println("Time verificaiton failed");
                }
            }
            else {
                System.out.println("Signature is not equal");
            }
        } else{
            System.out.println("Token parts are not 3"+parts.length);
        }

        return result;
    }
