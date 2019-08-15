Security.addProvider(new BouncyCastleProvider());

final char[] password = "pass".toCharArray();
final int saltLength = 8;
final String saltedPrefix = "Salted__";

String[] files = { "file0.txt.enc", "file0.txt.enc.nosalt" };
for (String file : files) {
    byte[] encrypted = Files.readAllBytes(Paths.get("testData", "openssl", file));

    byte[] salt = new byte[0];
    int offset = 0;
    if (new String(encrypted, 0, saltLength, "ASCII").equals(saltedPrefix)) {
        salt = new byte[saltLength];
        System.arraycopy(encrypted, saltedPrefix.length(), salt, 0, saltLength);
        offset = saltedPrefix.length() + saltLength;
    }

    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWITHMD5AND256BITAES-CBC-OPENSSL", "BC");
    PBEKeySpec keySpec = new PBEKeySpec(password);
    PBEParameterSpec paramSpec = new PBEParameterSpec(salt, 0);
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "BC");
    cipher.init(Cipher.DECRYPT_MODE, keyFactory.generateSecret(keySpec), paramSpec);

    byte[] data = cipher.doFinal(encrypted, offset, encrypted.length- offset);
    System.out.println(new String(data));
}
