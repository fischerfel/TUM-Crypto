public Core(String key) {
    try {
        this.SecretKey = key;
        this.ivspec = new IvParameterSpec(this.iv.getBytes());
        this.keyspec = new SecretKeySpec(SecretKey.getBytes("UTF-8"), "AES");
        try {
            this.cipher = Cipher.getInstance("AES/CBC/NoPadding");
        } catch (Exception e) {
            e.printStackTrace();
        }
    } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
    }
}
