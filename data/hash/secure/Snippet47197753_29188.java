public class Seclib {


      private static String algorithm = "AES";
      private static Key key = null;
      private static Cipher cipher = null;

    public Seclib(){
        key = KeyGenerator.getInstance(algorithm).generateKey();
        cipher = Cipher.getInstance(algorithm);
    }

    public static String messageHash(String message) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(message.getBytes());
        return md.digest(message.getBytes()).toString();
    }

    public static byte[] encryptMessage(String input) throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] inputBytes = input.getBytes();
        return cipher.doFinal(inputBytes);
    }   

    private static String decrypt(byte[] encryptionBytes) throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] recoveredBytes = 
        cipher.doFinal(encryptionBytes);
        String recovered = new String(recoveredBytes);
        return recovered;
    }   

    public static String initializeSecurityParameters(Scanner reader, int securityArray[]){

        int flagC = 0;
        int flagI = 0;
        int flagA = 0;

        String securityArrayString = "";

        String s;

        while(flagC == 0){

            System.out.println("Does this session require confidentiality? Y/N");
            System.out.println();
            s = reader.next();
            System.out.println();

            if(s.equals("Y") || s.equals("y") || s.equals("N") || s.equals("n")){

                if(s.equals("Y") || s.equals("y")){
                    securityArray[0] = 1;
                    flagC = 1;
                }else{
                    securityArray[0] = 0;
                    flagC = 2;
                }
            }

            if(flagC == 0){
                System.out.println("Invalid entry, must be one of Y,y,N,n");
                System.out.println();
            }
        }

        while(flagI == 0){

            System.out.println("Does this session require integrity? Y/N");
            System.out.println();
            s = reader.next();
            System.out.println();

            if(s.equals("Y") || s.equals("y") || s.equals("N") || s.equals("n")){
                flagI = 1;
                if(s.equals("Y") || s.equals("y")){
                    securityArray[1] = 1;
                    flagI = 1;
                }else{
                    securityArray[1] = 0;
                    flagI = 2;
                }
            }

            if(flagI == 0){
                System.out.println("Invalid entry, must be one of Y,y,N,n");
                System.out.println();
            }
        }

        while(flagA == 0){

            System.out.println("Does this session require authentication? Y/N");
            System.out.println();
            s = reader.next();
            System.out.println();

            if(s.equals("Y") || s.equals("y") || s.equals("N") || s.equals("n")){

                if(s.equals("Y") || s.equals("y")){
                    securityArray[2] = 1;
                    flagA = 1;
                }else{
                    securityArray[2] = 0;
                    flagA = 2;
                }
            }

            if(flagA == 0){
                System.out.println("Invalid entry, must be one of Y,y,N,n");
                System.out.println();
            }
        }

        //create security string

        for(int i = 0; i < 3; i++){
            securityArrayString = securityArrayString + String.valueOf(securityArray[i]);
        }

        //Use security array values to create key, etc based on need

        if(securityArray[0] == 1){
            System.out.println("C operations needed");
        }

        if(securityArray[1] == 1){
            System.out.println("I operations needed");
        }

        if(securityArray[2] == 1){
            System.out.println("A operations needed");
        }       

        return securityArrayString;
    }







}
