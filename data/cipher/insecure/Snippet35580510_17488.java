byte[] passwd = Base64Util.decode(pwd);
bais = new ByteArrayInputStream(passwd);
baos = new ByteArrayOutputStream();
Cipher cipher = Cipher.getInstance("DESede");
cipher.init(Cipher.DECRYPT_MODE, key);
// Read bytes, decrypt, and write them out.
byte[] buffer = new byte[2048];
int bytesRead;
while ((bytesRead = bais.read(buffer)) != -1) {
    out.write(cipher.update(buffer, 0, bytesRead));
}
// Write out the final bunch of decrypted bytes
out.write(cipher.doFinal());
out.flush();
