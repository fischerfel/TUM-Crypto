MessageDigest algorithm = MessageDigest.getInstance("SHA1");
FileInputStream fis = new FileInputStream("path/to/file.exe");
BufferedInputStream bis = new BufferedInputStream(fis);
DigestInputStream   dis = new DigestInputStream(bis, algorithm);

// read the file and update the hash calculation
while (dis.read() != -1);

 // get the hash value as byte array
byte[] hash = algorithm.digest();
