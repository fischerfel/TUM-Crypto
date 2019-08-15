/**
 * Perform MD-5 hash on the given string.
 * It returns a hashed string as Base64 string.
 * @param str String to be hashed in MD5
 * @return Base64 string if hashed successfully, else NULL
 */
public static String getHashMD5(String str){
    String hash = null;

    try{
        MessageDigest digest = null;

        try { digest = MessageDigest.getInstance("MD5"); }
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
        Log.e("MD5", "Error in getHashMD5() due to -> " + e.toString());
    }

    return hash;
}
