byte[] salt = { (byte) 0xc7, (byte) 0x73, (byte) 0x21, (byte) 0x8c,
        (byte) 0x7e, (byte) 0xc8, (byte) 0xee, (byte) 0x99 };
fileinputstrm = new FileInputStream(path);

PBEKeySpec pbeKeySpec = new PBEKeySpec("pass".toCharArray());

PBEParameterSpec pbeParamSpec = new PBEParameterSpec(salt, 20);
SecretKeyFactory keyFac = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
SecretKey pbeKey = keyFac.generateSecret(pbeKeySpec);

Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
pbeCipher.init(Cipher.DECRYPT_MODE, pbeKey, pbeParamSpec);

FileInputStream fis=new FileInputStream(path);
CipherInputStream cis=new CipherInputStream(fis, pbeCipher);
BufferedInputStream bfi=new BufferedInputStream(cis);
bfi.read();
cis.close();
FileOutputStream output1 = new FileOutputStream(path+".jpeg");
ByteArrayOutputStream baos=new ByteArrayOutputStream();
BufferedOutputStream bfo=new BufferedOutputStream(output1);
output1.write(baos.toByteArray());
