/**
 * Perform SHA-256 hash on the given string.
 * It returns a hashed string as Base64 string.
 * @param str String to be hashed in SHA-256
 * @return Base64 string if hashed successfully, else NULL
 */
public static String getHashSHA256(String str){
    String hash = null;

    try{
        MessageDigest digest = null;

        try { digest = MessageDigest.getInstance("SHA-256"); }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return hash;
        }

        digest.reset();
        hash = Base64.encodeToString(digest.digest(str.getBytes()),
                  Base64.DEFAULT).trim();
        digest = null;
    }
    catch (Exception e) {
        Log.e("SHA-256", "Error in getHashSHA256() due to -> " + e.toString());
    }

    return hash;
}
