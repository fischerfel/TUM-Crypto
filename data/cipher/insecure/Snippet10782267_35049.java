SecretKeySpec skeySpec = new SecretKeySpec(y.getBytes(), "AES");

FileInputStream fis;
FileOutputStream fos;
CipherOutputStream cos;
// File you are reading from
fis = new FileInputStream("/tmp/a.txt");
// File output
fos = new FileOutputStream("/tmp/b.txt");

// Here the file is encrypted. The cipher1 has to be created.
// Key Length should be 128, 192 or 256 bit => i.e. 16 byte
SecretKeySpec skeySpec = new SecretKeySpec("MyDifficultPassw".getBytes(), "AES"); 
Cipher cipher1 = Cipher.getInstance("AES");  
cipher1.init(Cipher.ENCRYPT_MODE, skeySpec);
cos = new CipherOutputStream(fos, cipher1);
// Here you read from the file in fis and write to cos.
byte[] b = new byte[8];
int i = fis.read(b);
while (i != -1) {
    cos.write(b, 0, i);
    i = fis.read(b);
}
cos.flush();
