public static boolean authenticate_(String salt, String encryptedPassword, 
        char[] plainTextPassword ) throws NoSuchAlgorithmException, UnsupportedEncodingException {

        // This was ok
        String saltPlusPlainTextPassword = salt + new String(plainTextPassword);    

        MessageDigest sha = MessageDigest.getInstance("SHA-512");

        // must specify "UTF-8" encoding
        sha.update(saltPlusPlainTextPassword.getBytes("UTF-8"));
        byte[] hashedByteArray = sha.digest();

        // Use Base64 encoding here -->
        String hashed = org.apache.commons.codec.binary.Base64.encodeBase64URLSafeString(hashedByteArray);

        return hashed.equals(encryptedPassword);
    }
