//ABCD is class name
public static void enc(String fileName, String pwd) {
    try {
        Properties prop = new Properties();
        InputStream input = ABCD.class.getClassLoader().getResourceAsStream(fileName);
        prop.load(input);
        input.close();
        URL url = ABCD.class.getClassLoader().getResource(fileName);

        FileOutputStream outputStream = new FileOutputStream(url.getPath());
        KeyGenerator key = KeyGenerator.getInstance("AES");

        key.init(128);
        SecretKey aesKey = key.generateKey();
        String newkey = new  String(Base64.encode(aesKey.getEncoded()).getBytes("UTF-8"));

        Cipher aesCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        aesCipher.init(Cipher.ENCRYPT_MODE, aesKey);
        byte[] clear = pwd.getBytes("UTF-8");
        byte[] cipher = aesCipher.doFinal(clear);
        String encPwd = new String(cipher);
        prop.setProperty("password", encPwd);
        prop.setProperty("secKey", newkey);
        prop.store(outputStream, null);
        outputStream.close();
    } catch (Exception e) {
        System.out.println(e);
    }
}

public static String dec(Properties prop, String fileName) {

    String decPwd = ABCD.map.get(fileName);
            try {
            String newkey = prop.getProperty("secKey");
            StringBuilder pwd;
            byte[] newkeybuff = Base64.decode(newkey.getBytes("UTF-8"));
            SecretKey key = new SecretKeySpec(newkeyuff, "AES");
            Cipher aesCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            aesCipher.init(Cipher.DECRYPT_MODE, key);
            pwd = new StringBuilder(prop.getProperty("password"));
            byte[] cipher = aesCipher.doFinal(pwd.toString().getBytes());
            decPwd = new String(cipher);
        } catch (Exception e) {
            System.out.println(e);
        }
        ABCD.map.put(fileName, decPwd);
    return decPwd;
}
