try{
        //To generate the secret key
        KeyGenerator keyGen = KeyGenerator.getInstance("DES");
        sKey = keyGen.generateKey();
        //Initialize the cipher instance to use DES algorithm, ECB mode,
        //and PKCS#5 padding scheme.
        cipherObj = Cipher.getInstance("DES/ECB/PKCS5Padding");
    }
    catch(NoSuchAlgorithmException nsae){nsae.printStackTrace();

    }
    catch(NoSuchPaddingException nspe){nspe.printStackTrace();}
