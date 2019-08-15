SecretKeySpec   key = new SecretKeySpec(keyBytes, "AES");
IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
Cipher          cipher = Cipher.getInstance("AES/GCM/NoPadding", "BC");
byte[] block = new byte[1048576];
int i;
long st,et;

cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);

BufferedInputStream bIn=new BufferedInputStream(new ProgressMonitorInputStream(null,"Encrypting ...",new FileInputStream("input")));
CipherInputStream       cIn = new CipherInputStream(bIn, cipher);
BufferedOutputStream bOut=new BufferedOutputStream(new FileOutputStream("output.enc"));

int ch;
while ((i = cIn.read(block)) != -1) {
    bOut.write(block, 0, i);
}
cIn.close();
bOut.close();

Thread.sleep(5000);

cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);

BufferedInputStream fis=new BufferedInputStream(new ProgressMonitorInputStream(null,"Decrypting ...",new FileInputStream("output.enc")));
//FileInputStream fis=new FileInputStream("output.enc");
//FileOutputStream ro=new FileOutputStream("regen.plain");
BufferedOutputStream ro=new BufferedOutputStream(new FileOutputStream("regen.plain"));

CipherInputStream dcIn = new CipherInputStream(fis, cipher);

while ((i = dcIn.read(block)) != -1) {
        ro.write(block, 0, i);
}

dcIn.close();
ro.close();
