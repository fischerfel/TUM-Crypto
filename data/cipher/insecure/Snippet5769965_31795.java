 //Load Key
 FileInputStream fis2= new FileInputStream("a.key");
 File f=new File("a.key");
 long l=f.length();
 byte[] b1=new byte[(int)l];
 fis2.read(b1, 0, (int)l);



 SecretKeySpec ks2=new SecretKeySpec(b1,"AES");

  Cipher c1 = Cipher.getInstance("AES");
  c1.init(Cipher.DECRYPT_MODE, ks2);
 FileInputStream fis1=new FileInputStream("Encrypted.file");
 CipherInputStream in= new CipherInputStream(fis1,c1);
 FileOutputStream fos0 =new FileOutputStream("decrypted.file");
 byte[] b3=new byte[1];
 int ia=in.read(b3);
 while (ia >=0)
 {
    c1.update(b3); //<-------remove this
    fos0.write(b3, 0, ia);
    ia=in.read(b3);
 }
in.close();
fos0.flush();
fos0.close();
