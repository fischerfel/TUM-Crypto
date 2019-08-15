public class ImageEncrypt {

  Cipher cipher; 

  public static byte[] convertImageToByteArray(String sourcePath) {
    byte[] imageInByte = null;
    try{

      BufferedImage originalImage = ImageIO.read(new File(
      sourcePath));

      // convert BufferedImage to byte array
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      ImageIO.write(originalImage, "jpg", baos);
      baos.flush();
      imageInByte = baos.toByteArray();
      baos.close();
    }catch(Exception e){
      e.printStackTrace();
    }
    return imageInByte;
  }

  public static void convertByteArrayToImage(byte[] b , String path) {

    try{

      InputStream in = new ByteArrayInputStream(b);
      BufferedImage bImageFromConvert = ImageIO.read(in);

      ImageIO.write(bImageFromConvert, "jpg", new File(
      path));

    }catch(Exception e)
    {
      e.printStackTrace();
    }
  }


  public static void main(String args []){
    final String strPassword = "password12345678";

    SecretKeySpec initVector = new SecretKeySpec(strPassword.getBytes(), "AES");
    AlgorithmParameterSpec paramSpec = new IvParameterSpec(strPassword.getBytes()); 

    try{
      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding"); 

      cipher.init(Cipher.ENCRYPT_MODE, initVector, paramSpec); 

      byte[] ecrypted = cipher.doFinal(convertImageToByteArray("C:/Users/user/Desktop/a.jpg"));

      convertByteArrayToImage(ecrypted,"C:/Users/user/user/enc.jpg");

      System.out.println("converted to encrypted file .... ");
    }catch(Exception e){
      e.printStackTrace();
    }
  }
