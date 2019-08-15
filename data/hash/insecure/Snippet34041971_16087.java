public byte[] createSha1(File file) throws Exception  {
MessageDigest digest = MessageDigest.getInstance("SHA-1");
InputStream fis = new FileInputStream(file);
int n = 0;
byte[] buffer = new byte[8192];
while (n != -1) {
    n = fis.read(buffer);
    if (n > 0) {
        digest.update(buffer, 0, n);
    }
}
return digest.digest();
}
