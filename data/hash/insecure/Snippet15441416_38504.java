import java.security.DigestInputStream;
...
...

MessageDigest md_1 = MessageDigest.getInstance("MD5");
MessageDigest md_2 = MessageDigest.getInstance("MD5");
InputStream is_1 = new FileInputStream("file1.txt");
InputStream is_2 = new FileInputStream("file2.txt");
try {
  is_1 = new DigestInputStream(is_1, md_1);
  is_2 = new DigestInputStream(is_2, md_2);
}
finally {
  is_1.close();
  is_2.close();
}
byte[] digest_1 = md_1.digest();
byte[] digest_2 = md_2.digest();

// compare digest_1 and digest_2
