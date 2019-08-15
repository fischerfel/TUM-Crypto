public class TokenUtils{
    public static final String MAGIC_KEY = "obfuscate";

    public static String createToken(UserDetails userDetails){
        System.out.println(" ----- Create Token ------");
        /* Expires in one hour */
        long expires = System.currentTimeMillis() + 1000L * 60 * 60;

        StringBuilder tokenBuilder = new StringBuilder();
        tokenBuilder.append(userDetails.getUsername());
        tokenBuilder.append(":");
        tokenBuilder.append(expires);
        tokenBuilder.append(":");
        tokenBuilder.append(TokenUtils.computeSignature(userDetails, expires));

        return tokenBuilder.toString();
    }


    public static String computeSignature(UserDetails userDetails, long expires){
        System.out.println("------ Compute Signature ------");
        StringBuilder signatureBuilder = new StringBuilder();
        signatureBuilder.append(userDetails.getUsername());
        signatureBuilder.append(":");
        signatureBuilder.append(expires);
        signatureBuilder.append(":");
        signatureBuilder.append(userDetails.getPassword());
        signatureBuilder.append(":");
        signatureBuilder.append(TokenUtils.MAGIC_KEY);

        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("No MD5 algorithm available!");
        }
        System.out.println(new String(Hex.encode(digest.digest(signatureBuilder.toString().getBytes()))));
        return new String(Hex.encode(digest.digest(signatureBuilder.toString().getBytes())));
    }


    public static String getUserNameFromToken(String authToken){
        System.out.println("----- Get Username From TOken ----");
        if (null == authToken) {
            return null;
        }

        String[] parts = authToken.split(":");
        return parts[0];
    }


    public static boolean validateToken(String authToken, UserDetails userDetails)  {
        System.out.println("=== Validate Token ===");
        String[] parts = authToken.split(":");
        long expires = Long.parseLong(parts[1]);
        String signature = parts[2];

        if (expires < System.currentTimeMillis()) {
            return false;
        }
        System.out.println(signature.equals(TokenUtils.computeSignature(userDetails, expires)));
        return signature.equals(TokenUtils.computeSignature(userDetails, expires));
    }
}
