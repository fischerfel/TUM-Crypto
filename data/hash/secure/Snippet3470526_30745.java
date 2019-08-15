public String encrypt(String password) {
        String hash = "";
        try {
            MessageDigest md5 = MessageDigest.getInstance("SHA-512");
            byte [] digest = md5.digest(password.getBytes("UTF-8"));
            hash = Arrays.toString(digest);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hash;
    }
