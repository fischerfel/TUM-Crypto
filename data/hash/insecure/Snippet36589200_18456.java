public String getSHA1Hash(String input) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String SHA1Hash = null;
        MessageDigest md = MessageDigest.getInstance("SHA1");
        md.reset();
        byte[] buffer = input.getBytes("UTF-8");
        md.update(buffer);
        byte[] digest = md.digest();
        String hexStr = "";
        for (int i = 0; i < digest.length; i++) {
            hexStr += Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1);
            SHA1Hash = hexStr;
        }
        return SHA1Hash;
