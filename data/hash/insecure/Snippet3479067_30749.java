 public static synchronized String getMD5_Base64(String input) {
        if (!isInited) {
            isInited = true;
            try {
                digest = MessageDigest.getInstance("MD5");
            } catch (Exception ex) {
            }
        }
        if (digest == null)
            return input;

        // now everything is ok, go ahead
        try {
            digest.update(input.getBytes("UTF-8"));
        } catch (java.io.UnsupportedEncodingException ex) {
        }
        byte[] rawData = digest.digest();
        byte[] encoded = Base64.encode(rawData);
        String retValue = new String(encoded);
        return retValue;
    }
}
