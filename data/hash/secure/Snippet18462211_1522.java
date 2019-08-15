private String crypt(double x, ByteArrayOutputStream baos) throws UnsupportedEncodingException, NoSuchAlgorithmException{
    MessageDigest md = MessageDigest.getInstance("SHA-256");
    md.update(String.valueOf(x).getBytes("UTF-8"));
    md.update(String.valueOf(baos).getBytes("UTF-8"));
    byte[] digest = md.digest();

    StringBuffer sb = new StringBuffer();
    for(byte d:digest){
        sb.append(Integer.toHexString(0xFF & d));
    }
    return sb.toString();
}
