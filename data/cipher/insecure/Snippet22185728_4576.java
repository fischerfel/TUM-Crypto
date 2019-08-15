String f="A000000000000000";
FileInputStream fis = new FileInputStream("C:\\Users\\original.txt");
byte[] bytes = DatatypeConverter.parseHexBinary(f);
SecretKey key = new SecretKeySpec(bytes, 0, bytes.length, "DES");

String strDataToEncrypt = new String();
String strCipherText = new String();
String strDecryptedText = new String();

    try{

    Cipher desCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
    desCipher.init(Cipher.ENCRYPT_MODE,key);

            //read from file and transform to String
            try{
            builder = new StringBuilder();
            int ch;
            while((ch = fis.read()) != -1){
            builder.append((char)ch);
            }
            }catch (IOException e){

            }

    byte[] byteDataToEncrypt = builder.toString().getBytes();
    byte[] byteCipherText = desCipher.doFinal(byteDataToEncrypt); 
    strCipherText = new BASE64Encoder().encode(byteCipherText);

    System.out.println(strCipherText);
