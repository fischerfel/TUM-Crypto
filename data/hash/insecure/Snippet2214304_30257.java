FileInputStream f = new FileInputStream(new File("bigFile.txt"));
MessageDigest digest = MessageDigest.getInstance("md5");
byte[] buffer = new byte[8192];
int len = 0;
while (-1 != (len = f.read(buffer))) {
   digest.update(buffer,0,len);
}
byte[] md5hash = digest.digest();
