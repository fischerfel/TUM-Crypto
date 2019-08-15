public String getCurrency(String hash) {
    StringBuilder sb = new StringBuilder();
    try {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(hash.getBytes());
        byte byteData[] = md.digest();

        /** convert the byte to hex format */
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }              

        return sb.toString();
    } catch (NoSuchAlgorithmException ex) {
        ex.printStackTrace();
    }
    return sb.toString();                
}
