public class Encryptor {
  File file;
  SecretKeySpec secretKeySpec;
  public void setFile(String filePath) throws Exception {
    this.file = new File(filePath);
    if(!file.isFile()){
      throw new Exception("The file you choosed is not valid");
    }
  }
  public void setKey(String keyword){
    try {
      MessageDigest sha = MessageDigest.getInstance("SHA-256");
      sha.update(keyword.getBytes("UTF-8"));
      byte[] key = sha.digest();
      secretKeySpec = new SecretKeySpec(key, "AES");
    } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
  }
  public void encrypt(){
    byte[] bFile = new byte[(int) file.length()];
    try {
      //adding portocol bytes to the file bytes
      //String portcol = "encryptor portocol";
      //byte[] decPortocol = portcol.getBytes();

      //convert file into array of bytes
      BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
      bufferedInputStream.read(bFile);
      bufferedInputStream.close();

      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      //outputStream.write(decPortocol);
      outputStream.write(bFile);

      byte[] cryptedFileBytes = outputStream.toByteArray();
      //Cipher and encrypting
      Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
      cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
      byte[] encryptedBytes = cipher.doFinal(cryptedFileBytes);

      //Write Encrypted File
      BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file,false));
      bufferedOutputStream.write(encryptedBytes);
      bufferedOutputStream.flush();
      bufferedOutputStream.close();
    }catch (Exception e){
      e.printStackTrace();
    }
  }

}
