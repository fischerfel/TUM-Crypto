Cipher cipher = null;
try {
    cipher = Cipher.getInstance("RSA/ECB/NoPadding");
} catch (NoSuchAlgorithmException e) {
    cipher = Cipher.getInstance("RSA/NONE/NoPadding");
}
