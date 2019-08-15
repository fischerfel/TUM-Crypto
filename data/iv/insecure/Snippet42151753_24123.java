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

        byte[] newkey=(Base64.encode(aesKey.getEncoded())).getBytes("UTF-8");

        Cipher aesCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");


        aesCipher.init(Cipher.ENCRYPT_MODE, aesKey,new IvParameterSpec(new byte[16]));
        byte[] clear = pwd.getBytes("UTF-8");
        byte[] cipher = aesCipher.doFinal(clear);

        prop.setProperty("password", Arrays.toString(cipher));
        prop.setProperty("secKey", Arrays.toString(newkey));

        prop.store(outputStream, null);
        outputStream.flush();
        outputStream.close();

    } catch (Exception e) {
    System.out.println(e);
}
}

public static String dec(Properties prop, String fileName) {

    String decPwd = ABCD.map.get(fileName);
            try {
            byte[] newkey=prop.getProperty("secKey").getBytes("UTF-8");
            byte[] pwd;


            byte[] newkeybuff = Base64.decode(newkey);
            SecretKeySpec key = new SecretKeySpec(newkeybuff, "AES");

            Cipher aesCipher=Cipher.getInstance("AES/CBC/PKCS5Padding");
            aesCipher.init(Cipher.DECRYPT_MODE, key,new IvParameterSpec(new byte[16]));
            pwd = prop.getProperty("password").getBytes();

            byte[] cipher = aesCipher.doFinal(pwd);

            decPwd=new String(cipher);
            System.out.println("Decrypted pwd " + decPwd);

        } 
        catch (Exception e) {
    System.out.println(e);
}
        ABCD.map.put(fileName, decPwd);

    return decPwd;
}
