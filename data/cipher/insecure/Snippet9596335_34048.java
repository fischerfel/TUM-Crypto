// get cipher object for password-based encryption
Cipher cipher1 = Cipher.getInstance("PBEWithMD5AndDES");//You have to pass here algorithm name which PGP uses. May be you have to find and init provider for it.

// initialize cipher for decryption, using one of the 
// init() methods that takes an AlgorithmParameters 
// object, and pass it the algParams object from above
cipher1.init(Cipher.DECRYPT_MODE, myKey, algParams);


FileInputStream fis;
FileOutputStream fos;
CipherInputStream cis;

fis = new FileInputStream("/tmp/a.txt");
cis = new CipherInputStream(fis, cipher1);
fos = new FileOutputStream("/tmp/b.txt");
byte[] b = new byte[8];
int i = cis.read(b);
while (i != -1) {
    fos.write(b, 0, i);
    i = cis.read(b);
}
fos.close();
