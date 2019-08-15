byte[] buf = new byte[8192];
MessageDigest sha =  MessageDigest.getInstance("SHA1");

FileInputStream inp = new FileInputStream(new File("D:\\season4_mlp.rar"));

int n;
while((n = inp.read(buf)) > 0)
    sha.update(buf, 0, n);

byte hash[] = sha.digest();
