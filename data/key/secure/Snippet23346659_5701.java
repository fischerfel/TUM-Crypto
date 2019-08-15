SecretKeySpec keySpec = new SecretKeySpec(myKey "AES");
Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

FileInputStream fis = new FileInputStream(new File(pathToEncryptedFile));
CipherInputStream cis = new CipherInputStream(fis, cipher);

ZipInputStream zis = new ZipInputStream(new BufferedInputStream(cis));
ZipEntry ze = null;
while ((ze = zis.getNextEntry()) != null) {
    String filename = ze.getName();
    System.out.println("Found zip entry: " + filename);
}
