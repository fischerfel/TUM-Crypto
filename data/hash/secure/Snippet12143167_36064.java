try {
    byte[] b = "test".getBytes("ASCII");
    MessageDigest md = MessageDigest.getInstance("SHA-256");
    byte[] hashBytes = md.digest(b);
    StringBuffer hexString = new StringBuffer();
    for (int i = 0; i < hashBytes.length; i++) {
        hexString.append(Integer.toHexString(0xFF & hashBytes[i]));
    }
    System.out.println(hexString);
} catch (Exception e) {
    e.printStackTrace();
}
