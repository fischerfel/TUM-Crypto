public String getMD5(String data) {
        String result = null;
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
            md.update(data.getBytes(Charset.forName("UTF-8")));
            result = String.format(Locale.ROOT, "%032x", new BigInteger(1, md.digest()));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
        return result;
}
