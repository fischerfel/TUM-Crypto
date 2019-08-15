public static String encodePassword(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String result;
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(password.getBytes("iso-8859-1"), 0, password.length());
        byte[] sha1hash = md.digest();
        result = Base64.encodeToString(sha1hash, Base64.DEFAULT);
        result = result.substring(0, result.length()-1);
        return result;
    }
