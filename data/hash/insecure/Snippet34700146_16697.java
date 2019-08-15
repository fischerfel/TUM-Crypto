 final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

 public byte[] md5x16(String text) {
    try {
        MessageDigest digester = MessageDigest.getInstance("MD5");
        digester.update(text.getBytes());
        byte[] md5Bytes = digester.digest();
        String md5Text = new String(md5Bytes); // if you need in String format
        // better use md5Bytes if applying further processing to the generated md5.
        // Otherwise it may give undesired results.
        return md5Bytes;

    }
    catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}
