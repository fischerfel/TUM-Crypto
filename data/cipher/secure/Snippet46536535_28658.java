  //ENCRYPT
try {
        Cipher c = Cipher.getInstance("ECIES",BouncyCastleProvider.PROVIDER_NAME);
        c.init(Cipher.ENCRYPT_MODE,publicKey);
        encodeBytes = c.doFinal(origin.getBytes());

        String encrypt = Base64.getEncoder().encodeToString(encodeBytes);

        System.out.println("Encrypt:"+ encrypt+"\n");


    } catch (Exception e) {
        e.printStackTrace();
    }

//////DECRYPT
    try
    {
        String abc = Base64.getDecoder().decode(encrypt);
        Cipher c = Cipher.getInstance("ECIES","BC");
        c.init(Cipher.DECRYPT_MODE,privateKey);
        //decodeBytes = c.doFinal(encodeBytes);
        decodeBytes = c.doFinal(abc);
        String deCrypt = new String(decodeBytes,"UTF-8");

        System.out.println("Decrypt:"+ deCrypt +"\n");
    }
    catch (Exception ex)
    {
        ex.printStackTrace();
    }
