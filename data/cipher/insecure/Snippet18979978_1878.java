        // Sensitive information - message to be encrypted
        byte[] date_of_exp = "032019".getBytes(); // Date of Expiration in form MMYYYY

        //System.out.println("Card Number : " + card_number); // Print original message

        // Encrypt the text
       byte[] date_of_expEncrypted = desCipher.doFinal(date_of_exp);

        System.out.println("");
        System.out.println("Date of Expiration Encrypted : " + date_of_expEncrypted); // Print the encrypted message
        System.out.println("");

        // Initialize the same cipher for decryption
        desCipher.init(Cipher.DECRYPT_MODE, myDesKey_2);

        String date_of_expEncrypted_;
        date_of_expEncrypted_ = DatatypeConverter.printBase64Binary(date_of_expEncrypted); 
        // SecretKey card_numberEncrypted_key;
        // card_numberEncrypted_key = stringToSecretKey (card_numberEncrypted_, "DES");
        SecretKey date_of_expEncrypted_key;
        date_of_expEncrypted_key = new SecretKeySpec(date_of_expEncrypted, 0, 8, "DES");
        System.out.println("");
        System.out.println("Date of expiration as secret key :" + date_of_expEncrypted_key);
        System.out.println("");

        // Decrypt the text
        byte[] date_of_expDecrypted = desCipher.doFinal(date_of_expEncrypted);

        System.out.println("Original Date of Expiration (decrypted) : " + new String(date_of_expDecrypted)); // Print the decrypted Text
        System.out.println("");
        System.out.println("");
        System.out.println("-----------------------------------------------------------------------------------");
        System.out.println("Further to Step 3"); // Print the decrypted Text
        System.out.println("-----------------------------------------------------------------------------------"); // Print the decrypted Text
        System.out.println("");
        System.out.println("");




    SecretKey myDesKey_3 = date_of_expEncrypted_key;

    //Cipher desCipher_2; // New Cipher for iteration 2

        // Create the cipher 
        //desCipher_2 = Cipher.getInstance("DES/ECB/PKCS5Padding");

        // Initialize the cipher for encryption
        desCipher.init(Cipher.ENCRYPT_MODE, myDesKey_3);

        // Sensitive information - message to be encrypted
        byte[] service_code = "318".getBytes(); 

       // Encrypt the text
       byte[] service_codeEncrypted = desCipher.doFinal(service_code);
        System.out.println("");
        System.out.println("Service Code Encrypted : " + service_codeEncrypted); // Print the encrypted message
        System.out.println("");
        // Initialize the same cipher for decryption
        desCipher.init(Cipher.DECRYPT_MODE, myDesKey_3);

        // Decrypt the text
        byte[] service_codeDecrypted = desCipher.doFinal(service_codeEncrypted);

        System.out.println("Service Code decrypted : " + new String(service_codeDecrypted)); // Print the decrypted Text
        System.out.println("");
        System.out.println("");
        System.out.println("-----------------------------------------------------------------------------------");
        System.out.println("Finish!!!"); // Print the decrypted Text
        System.out.println("-----------------------------------------------------------------------------------"); // Print the decrypted Text
        System.out.println("");
        System.out.println("");


        //Integer bigInt = new Integer("Bwwhw34".getBytes());
        // int service_codeEncrypted_hashed = service_codeEncrypted.hashCode();
        // System.out.println("hash code for Service Code Encrypted : " + service_codeEncrypted_hashed);
        // int service_codeEncrypted_hashed_2 = service_codeEncrypted_hashed.hashCode();

        // byte[] service_code__ = service_codeEncrypted.getBytes(); 
        //  System.out.println("hash code for Service Code Encrypted and baseD  : " + service_code__);



    }catch(NoSuchAlgorithmException e){
        e.printStackTrace();
    }catch(NoSuchPaddingException e){
        e.printStackTrace();
    }catch(InvalidKeyException e){
        e.printStackTrace();
    }catch(IllegalBlockSizeException e){
        e.printStackTrace();
    }catch(BadPaddingException e){
        e.printStackTrace();
    } 

}
