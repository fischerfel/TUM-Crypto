 public class decryption {
   private static Cipher encrypt;

private static Cipher decrypt;


private static final byte[] initialization_vector = { 22, 33, 11, 44, 55, 99, 66, 77 };




/**
 * @param args the command line arguments
 */
public static void main(String[] args) {
    // TODO code application logic here


    String encryptedFile = "D:/ashok/aso.txt";

    String decryptedFile = "D:/ashok/amii.txt";

    try {



        SecretKey secret_key = KeyGenerator.getInstance("DES")

                .generateKey();

        AlgorithmParameterSpec alogrithm_specs = new IvParameterSpec(

                initialization_vector);
          // set encryption mode ...

        // set decryption mode

        decrypt = Cipher.getInstance("DES/CBC/PKCS5Padding");

        decrypt.init(Cipher.DECRYPT_MODE, secret_key, alogrithm_specs);

        // encrypt file

    // decrypt file

      decrypt(new FileInputStream(encryptedFile), new FileOutputStream(

                decryptedFile));

        System.out.println("End of Encryption/Decryption procedure!");


    } catch (NoSuchAlgorithmException | NoSuchPaddingException

            | InvalidKeyException | InvalidAlgorithmParameterException

            | IOException e) {

        e.printStackTrace();

    }

}



private static void decrypt(InputStream input, OutputStream output)

        throws IOException {



    input = new CipherInputStream(input, decrypt);

    writeBytes(input, output);

}



private static void writeBytes(InputStream input, OutputStream output)

        throws IOException {

    byte[] writeBuffer = new byte[512];

   int readBytes = 0;



    while ((readBytes = input.read(writeBuffer)) >= 0) {

        output.write(writeBuffer, 0, readBytes);

     }



     output.close();

     input.close();

   }

 }
