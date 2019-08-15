private static final String md5(String string) {
    try {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update(string.getBytes());
        byte[] arrby = messageDigest.digest();
        StringBuffer stringBuffer = new StringBuffer();
        int n = 0;
        block3 : do {
            if (n >= arrby.length) {
                return stringBuffer.toString();
            }
            String string2 = Integer.toHexString(255 & arrby[n]);
            do {
                String string3;
                if (string2.length() >= 2) {
                    stringBuffer.append(string2);
                    ++n;
                    continue block3;
                }
                string2 = string3 = "0" + string2;
            } while (true);

        } while (true);
    }
    catch (NoSuchAlgorithmException var1_7) {
        var1_7.printStackTrace();
return "";
    }
}
