private String encryptPassword(String password) throws NoSuchAlgorithmException{

    MessageDigest md = MessageDigest.getInstance("MD5");
    md.update(password.getBytes());
    byte[] digest = md.digest();
    StringBuilder stringBuilder = new StringBuilder();
    for (byte b : digest) {
        stringBuilder.append(String.format("%02x", b & 0xff));
    }

    return stringBuilder.toString();
}
