public String getHash(String target) throws NoSuchAlgorithmException {
    MessageDigest sh = MessageDigest.getInstance("SHA-512");
    sh.update(target.getBytes());
    StringBuilder sb = new StringBuilder();
    for (byte b : sh.digest()) {
        sb.append(Integer.toHexString(0xff & b));
    }

    return sb.toString();
}
