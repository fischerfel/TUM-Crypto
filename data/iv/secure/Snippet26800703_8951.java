try {
    String input = simple_text.getText();

    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

    byte[] iv = new byte[cipher.getBlockSize()];
    new SecureRandom().nextBytes(iv);
    IvParameterSpec ivSpec = new IvParameterSpec(iv);

    MessageDigest digest = MessageDigest.getInstance("SHA-256");
    digest.update(keyString.getBytes());
    byte[] key = new byte[16];
    System.arraycopy(digest.digest(), 0, key, 0, key.length);
    SecretKeySpec keySpec = new SecretKeySpec(key, "AES");

    // encrypt
    cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
    byte[] encrypted = cipher.doFinal(input.getBytes("UTF-8"));
    System.out.println("encrypted: " + new String(encrypted));
    encrypt_text .setText(new String(encrypted));
} catch (Exception e2) {
    JOptionPane.showMessageDialog(null, e2);
}

try {
    String sql = "INSERT INTO user(username,password) VALUES(?,?) ON DUPLICATE KEY UPDATE username=VALUES(username),password=VALUES(password)";

    pst=conn.prepareStatement(sql);

    pst.setString(1, fLoginName.getText());
    pst.setString(2, encrypt_text.getText());

    pst.execute();

    JOptionPane.showMessageDialog(null, "saved");
} catch (Exception e2) {
    JOptionPane.showMessageDialog(null, e2);
}
