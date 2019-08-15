

private static byte[] encryptingAFile(List<Employee> list) {
    byte[] empList, textEncrypted = null;

    try {
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        ObjectOutputStream object = new ObjectOutputStream(byteArray);

        KeyGenerator keygenerator = KeyGenerator.getInstance("AES");
        SecretKey myDesKey = keygenerator.generateKey();

        Cipher desCipher;
        desCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        String s;

        for (Employee employee: list) {
            object.writeObject(employee);
        }

        empList = byteArray.toByteArray();
        desCipher.init(Cipher.ENCRYPT_MODE, myDesKey, new IvParameterSpec(new byte[16]));
        textEncrypted = desCipher.doFinal(empList);

        Files.write(Paths.get("Encrypt.txt"), textEncrypted);

        desCipher.init(Cipher.DECRYPT_MODE, myDesKey, new IvParameterSpec(new byte[16]));
        byte[] textDecrypted = desCipher.doFinal(textEncrypted);

        ByteArrayInputStream bis = new ByteArrayInputStream(textDecrypted);
        ObjectInputStream ois = new ObjectInputStream(bis);
        List<Employee > result = (List<Employee>) ois.readObject();

        System.out.println(result.toString());

    }
    catch (InvalidKeyException in) {
        System.out.println(in);
    }
    catch (Exception e) {
        System.out.println(e);
    }
    return textEncrypted;
}
