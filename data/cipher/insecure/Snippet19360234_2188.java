 public static void encrypt(String password, InputStream is, OutputStream os,String DelFile) throws Exception {

SecretKeySpec keySpec = new SecretKeySpec(password(password), "TripleDES");
Cipher cipher = Cipher.getInstance("TripleDES");
cipher.init(Cipher.ENCRYPT_MODE, keySpec);
byte[] buf = new byte[8096];
os = new CipherOutputStream(os, cipher);
int numRead = 0;
while ((numRead = is.read(buf)) >= 0) {
    os.write(buf, 0, numRead);
}
os.close();

// file deleting part...
File f = new File(DelFile);
f.delete();}
