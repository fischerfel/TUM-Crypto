public String computeHash(String input) throws NoSuchAlgorithmException, UnsupportedEncodingException{
    MessageDigest digest = MessageDigest.getInstance("SHA-256");
    digest.reset();

    byte[] byteData = digest.digest(input.getBytes("UTF-8"));
    StringBuffer sb = new StringBuffer();

    for (int i = 0; i < byteData.length; i++){
      sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
    }
    return sb.toString();
}
