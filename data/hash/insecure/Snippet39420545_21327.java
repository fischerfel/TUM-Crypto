public static String md5(String input) {
    try {
        MessageDigest md = MessageDigest.getInstance("MD5");

        StringBuilder hexString = new StringBuilder();
        for (byte digestByte : md.digest(input.getBytes()))
            hexString.append(String.format("%02X", digestByte));

        return hexString.toString();
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
        return null;
    }
}
