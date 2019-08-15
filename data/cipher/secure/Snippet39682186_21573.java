FileInputStream fis = new FileInputStream(PATH_BKP);

FileOutputStream fos = new FileOutputStream(PATH_DB);

byte[] s = Arrays.copyOf(KEY_DATABASE.getBytes(),16);
SecretKeySpec sks = new SecretKeySpec(s, "AES");

Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
cipher.init(Cipher.DECRYPT_MODE, sks);

CipherInputStream cis = new CipherInputStream (fis, cipher);

byte[] buffer = new byte[1024];
int length;
while ((length = cis.read(buffer)) != -1) {
    fos.write(buffer, 0, length);
}

fos.flush();
fos.close();
cis.close();
