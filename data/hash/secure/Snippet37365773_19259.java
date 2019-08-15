public static String calculateHash(String hash) throws NoSuchAlgorithmException, UnsupportedEncodingException{      
    MessageDigest md = MessageDigest.getInstance("SHA-256");

    // 16 characters used in the seed, 8 length. Possibilities : 8 * 8 * 8... (16 times) = 8 ^ 16
    long convinations = (long) Math.pow(8, 16);
    String hex, formatted;

    for (long i = 0; i < (long) convinations; i++) {

        hex = String.format("%08X", i);

        // This takes CPU time, only uncomment for debug purposses
        //System.out.println(hex);

        // Calculate hash of "hex"
        md.update(hex.getBytes("UTF-8")); 
        byte[] digest = md.digest();

        // Compare results
        formatted = String.format("%064x", new BigInteger(1, digest));
        if (formatted.equals(hash)) {
            return hex;
        }
    }

    return "";
}
