String sha;
try {
    java.security.MessageDigest md = java.security.MessageDigest.getInstance(algoritmo);
    byte[] hash = md.digest(texto.getBytes());
    java.util.Formatter formatter = new java.util.Formatter();
    for (byte b : hash) {
        formatter.format("%02x", b);
    }
    sha = formatter.toString();
    formatter = null;
    hash = null;
    md = null;

} catch (java.security.NoSuchAlgorithmException e) {
    return "";

}
return sha;
