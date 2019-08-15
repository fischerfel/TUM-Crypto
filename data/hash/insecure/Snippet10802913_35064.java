FileInputStream input = new FileInputStream("/path/to/file.csv");
MessageDigest md5 = MessageDigest.getInstance("MD5");
byte[] buffer = new byte[10240];

for (int length = 0; (length = input.read(buffer)) > 0;) {
    md5.update(buffer, 0, length);
}     

byte[] hash = digest.digest();
