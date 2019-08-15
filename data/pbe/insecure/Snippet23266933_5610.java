fileinputstrm=new FileInputStream(path);
BufferedInputStream input=new BufferedInputStream(fileinputstrm);
Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
SecretKeyFactory keyFac = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
PBEKeySpec pbeKeySpec = new PBEKeySpec("pass".toCharArray());          

PBEParameterSpec pbeParamSpec = new PBEParameterSpec(salt, 20);
SecretKey pbeKey = keyFac.generateSecret(pbeKeySpec);
Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
pbeCipher.init(Cipher.ENCRYPT_MODE, pbeKey, pbeParamSpec);

FileOutputStream output = new FileOutputStream(path + ".icrpt");
CipherOutputStream cos = new CipherOutputStream(output, pbeCipher);

ByteArrayOutputStream bytes = new ByteArrayOutputStream();
output.write(bytes.toByteArray());
cos.close();
