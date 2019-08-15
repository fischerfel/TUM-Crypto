KeyGenerator kg = KeyGenerator.getInstance("AES");
kg.init(256);
SecretKey sk = kg.generateKey();
Cipher aesCipher = Cipher.getInstance("AES");
aesCipher.init(Cipher.ENCRYPT_MODE, sk);
Credentials cred = new UsernamePasswordCredentials("username", "password");//no need for time field?
String eCred = Base64.encodeBase64String(aesCipher.doFinal(objectToByteArray(cred)));

...

private byte[] objectToByteArray(Object obj) {
    byte[] bytes = null;
    try (
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
    ) {
        oos.writeObject(obj);
        oos.flush();
        bytes = bos.toByteArray();
    }
    catch (IOException e) {
        e.printStackTrace();
    }

    return bytes;
}
