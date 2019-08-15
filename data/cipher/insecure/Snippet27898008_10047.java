class Endecryption {
   private static Cipher encrypt;

private static Cipher decrypt;



private static final byte[] initialization_vector = { 22, 33, 11, 44, 55, 99, 66, 77 };


 public static void main(String[] args) {
    // TODO code application logic here
    String normalFile = "D:/ashok/ashk.txt";
    String encryptedFile="D:/ashok/aso.txt";


    try {



        SecretKey secret_key = KeyGenerator.getInstance("DES")

                .generateKey();

        AlgorithmParameterSpec alogrithm_specs = new IvParameterSpec(

                initialization_vector);
  // set encryption mode ...

        encrypt = Cipher.getInstance("DES/CBC/PKCS5Padding");

        encrypt.init(Cipher.ENCRYPT_MODE, secret_key, alogrithm_specs);



        // set decryption mode

       // encrypt file

        encrypt(new FileInputStream(normalFile), new FileOutputStream(

                encryptedFile));

     System.out.println("End of Encryption/Decryption procedure!");


    } catch (NoSuchAlgorithmException | NoSuchPaddingException

            | InvalidKeyException | InvalidAlgorithmParameterException

            | IOException e) {

        e.printStackTrace();

    }

}

 private static void encrypt(InputStream input, OutputStream output)

        throws IOException {



    output = new CipherOutputStream(output, encrypt);

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
