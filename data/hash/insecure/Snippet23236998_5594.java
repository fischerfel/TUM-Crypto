public static  String md5(String inputString) {
    try {
        // Create MD5 Hash
        MessageDigest msgDigest = java.security.MessageDigest.getInstance("MD5");
        msgDigest.update(inputString.getBytes());
        byte msgDigestBytes[] = msgDigest.digest();

        // Create Hex String
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < msgDigestBytes.length; i++) {
            String h = Integer.toHexString(0xFF & msgDigestBytes[i]);
            while (h.length() < 2)
                h = "0" + h;
            hexString.append(h);
        }
        return hexString.toString();

    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    }
    return "";
}
