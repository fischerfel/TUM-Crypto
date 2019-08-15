File dbFile = new File(PATH_DB);

FileInputStream fileInputStream = new FileInputStream(PATH_DB);

FileOutputStream outputStream = new FileOutputStream(PATH_BKP);

byte[] s = Arrays.copyOf(KEY_DATABASE.getBytes(),16);
SecretKeySpec sks = new SecretKeySpec(s, "AES");

Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
cipher.init(Cipher.ENCRYPT_MODE, sks);

CipherOutputStream cos = new CipherOutputStream(outputStream, cipher);

//Transferencia dos dados do inputfile para o output
byte[] buffer = new byte[1024];
int length;
while ((length = fileInputStream.read(buffer))!= -1) {
    cos.write(buffer,0,length);
}

//Fecha as streams
cos.flush();
cos.close();
fileInputStream.close();
