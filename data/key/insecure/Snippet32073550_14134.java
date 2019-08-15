private static String password = "AxkbK2jZ5PMaeNZWfn8XRLUWF2waGwH2EkAXxBDU6aZ";
private static String salt = "2#g+XK^Sc3\"4ABXbvwF8CPD%en%;9,c(";
private static String text = "Fm+Zfufqe3DjRQtWcYdw9g9oXriDjrAkRrBLhEfu7fCtT4BzD0gw7D+8KxrcbbgJm26peTUWHU2k4YJ4KqCSRQN3NPzuXwlJ4mC4444Edg3Q==";

public String decrypt(String pass, String encr) {

    try {
        int i = 0;

        String key = hash();
        byte[] iv = Base64.decodeBase64(text.substring(0, 22) + "==");

        Cipher cipher = Cipher.getInstance("DES");
        SecretKeySpec keySpec = new SecretKeySpec(password.getBytes(), "DES");
        IvParameterSpec ivSpec = new IvParameterSpec(salt.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        ByteArrayInputStream fis = new ByteArrayInputStream(iv);
        CipherInputStream cis = new CipherInputStream(fis, cipher);
        ByteArrayOutputStream fos = new ByteArrayOutputStream();
        // decrypting
        byte[] b = new byte[8];
        while ((i = cis.read(b)) != -1) {
            fos.write(b, 0, i);
        }
        fos.flush();
        fos.close();
        cis.close();
        fis.close();

        return fos.toString();
    } catch (Exception e) {
        e.printStackTrace();
    }

    return null;
}

private String hash() {
    StringBuffer sb = new StringBuffer();

    try {
        MessageDigest md = null;
        md = MessageDigest.getInstance("SHA-256");
        md.update((password + salt).getBytes());
        byte byteData[] = md.digest();

        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    } finally {
        return sb.toString();
    }

}
