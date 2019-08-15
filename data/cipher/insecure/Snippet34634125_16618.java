    public class AdminKeyGenerator {

    private static final String ALGORITHM = "AES";


    public static SecretKey generateKey() throws NoSuchAlgorithmException,
            NoSuchPaddingException {

        KeyGenerator genarator = KeyGenerator.getInstance(ALGORITHM);
        SecretKey secretkey = genarator.generateKey();

        return secretkey;

    }

    public static void saveKey(SecretKey key) {

        File keyFile = new File("/home/thamiz/workspace/keyFile.txt");
        try{

            keyFile.createNewFile();
            FileWriter keyWriter = new FileWriter(keyFile);
            BufferedWriter buffKey = new BufferedWriter(keyWriter);

            char[] hex = encodeHex( key.getEncoded() );

            buffKey.write(hex);
            buffKey.flush();
            buffKey.close();


        }catch(FileNotFoundException e){
            System.out.println("Decryption fails");

        } catch (IOException e) {
            System.out.println("Decryption fails");
        }

    }



    public static SecretKey loadKey() throws DecoderException{


        File keyFile = new File("/home/thamiz/workspace/keyFile.txt");
        String data = null;
        byte[] encoded = null;

        try{
        FileReader keyReader = new FileReader( keyFile );
        BufferedReader buffKeyRead = new BufferedReader(keyReader);

        data = buffKeyRead.readLine();
        encoded = decodeHex(data.toCharArray());
        buffKeyRead.close();


        }catch( IOException e ){
            System.out.println("Decryption fails");
        }


        return new SecretKeySpec(encoded, ALGORITHM);



    }


    public static void main(String[] args) throws NoSuchAlgorithmException,
            NoSuchPaddingException {

        SecretKey secretkey = AdminKeyGenerator.generateKey();

        AdminKeyGenerator.saveKey(secretkey);



    }


Following is the client side code. client used the admin key and encrypt his card details.


 private static String debitcardType;
 private static int debitCardNumber;
 private static int debitcardCVV;
     private static Date debitcardExpiryDate;


    public static void storeCardDetails() throws ParseException {

    Scanner in = new Scanner(System.in);
    boolean anotherCardDetail = false;
    SimpleDateFormat expiry = new SimpleDateFormat("dd-MM-yyyy");

    do {

        System.out.println("Enter your debit card type:");
        debitcardType = in.next();

        System.out.println("Enter your debit card number:");
        debitCardNumber = in.nextInt();

        System.out.println("Enter your debit card cvv number:");
        debitcardCVV = in.nextInt();

        System.out.println("Enter your debit card expiry date in the format of dd-MM-yyyy:");
        String date = in.next();
        debitcardExpiryDate = expiry.parse(date);

        System.out.println("Do you want to enter another card detail");
        System.out.println("Enter 1 for another card details and 0 for exit ");
        int option = in.nextInt();
        if (!(option == 1) && (option == 0)) {
            anotherCardDetail = true;
            System.out.println("Exit from entering card details");
        }

    } while (!anotherCardDetail);

}

        private static String encryptCardDetails(int cardNumber, int cardCVV,Date expiryDate, SecretKey key)
        {

    Cipher cipherencrypt = Cipher.getInstance("AES");

    byte[] plainCardNumber = String.valueOf(cardNumber).getBytes();
    byte[] plainCardCVV = String.valueOf(cardCVV).getBytes();
    byte[] plainExpiryDate = expiryDate.toString().getBytes();

    cipherencrypt.init( cipherencrypt.ENCRYPT_MODE, key);
    byte[] encryptedCardNumber = cipherencrypt.doFinal(plainCardNumber);
    byte[] encryptedCardCVV = cipherencrypt.doFinal(plainCardCVV);
    byte[] encryptedExpiryDate = cipherencrypt.doFinal(plainExpiryDate);

    String encryptedCard = Base64.encodeBase64String(encryptedCardNumber)
            + Base64.encodeBase64String(encryptedCardCVV)
            + Base64.encodeBase64String(encryptedExpiryDate);

    return encryptedCard;

}


   public static String decryptCardDetails( File file, SecretKey key ) {

    byte[] finalString1 = null;
    byte[] finalString2 = null;
    byte[] finalString3 = null;

    String final1 = null,final2 = null, final3 = null;
    Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");

    try{
    FileReader decryptFile = new FileReader(file);
    BufferedReader buff = new BufferedReader(decryptFile);

    String decryptcontent = buff.readLine();

    Scanner scan = new Scanner(decryptcontent).useDelimiter(",");

    String cardType = scan.next();
    System.out.println(cardType);
    String decryptString = scan.next();
    System.out.println(decryptString);

    byte[] decrypt1 = Base64.decodeBase64(decryptString);
    cipher.init(Cipher.DECRYPT_MODE, key);

    byte[] decryptedText = cipher.doFinal(decrypt1);

    final3 = new String(decryptedText, "UTF-8");

    decryptedCardNumber.toString()+decryptedCardCVV.toString()+decryptedExpiryDate.toString();

    }catch( IOException e ) {
        System.out.println("Decryption fails");
    }

    return final3;


}

public static void main(String[] args)  {

    File cardDetailsFile = new File("/home/thamiz/workspace/cardFile.txt");

    try {
        cardDetailsFile.createNewFile();

        FileWriter fileOut = new FileWriter(cardDetailsFile);
        BufferedWriter buffer = new BufferedWriter(fileOut);

        ClientCardDetails.storeCardDetails();

        AdminKeyGenerator.generateKey();


        String card = ClientCardDetails.encryptCardDetails(debitCardNumber, debitcardCVV,
                debitcardExpiryDate, AdminKeyGenerator.generateKey());

        buffer.write(debitcardType);
        buffer.write(",");
        buffer.write(card);
        buffer.newLine();
        buffer.flush();

        SecretKey key = AdminKeyGenerator.loadKey();
        String card1 = ClientCardDetails.decryptCardDetails(cardDetailsFile, key );
        System.out.println("Decryption sucessful");
        System.out.println(card1);


    } catch (ParseException e) {
        System.out.println("Enter incorrect card details");
    } catch (IOException e) {
        System.out.println("Encryption fails");
    }

}
