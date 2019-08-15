try {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(128);
        SecretKey secretKey = keyGen.generateKey();

        final int AES_KEYLENGTH = 128;  
        byte[] iv = new byte[AES_KEYLENGTH / 8];    
        SecureRandom prng = new SecureRandom();
        prng.nextBytes(iv);

        Cipher aesCipherForEncryption = Cipher.getInstance("AES/CBC/PKCS5PADDING");

        aesCipherForEncryption.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(iv));

        BigInteger bigIntToEncrypt = new BigInteger("123465746443654687461161655434494984631323");
        byte[] byteDataToEncrypt = bigIntToEncrypt.toByteArray();
        byte[] byteCipherText = aesCipherForEncryption.doFinal(byteDataToEncrypt);

        BigInteger chipherBigInt = new BigInteger(byteCipherText);
        System.out.println("Cipher Int generated using AES is " + chipherBigInt);


        Cipher aesCipherForDecryption = Cipher.getInstance("AES/CBC/PKCS5PADDING"); 

        aesCipherForDecryption.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));
        byte[] byteDecryptedText = aesCipherForDecryption.doFinal(byteCipherText);
        BigInteger decryptedInt = new BigInteger(byteDecryptedText);

        System.out.println(" Decrypted Text message is " + decryptedInt);

    } catch (NoSuchAlgorithmException noSuchAlgo) {
        System.out.println(" No Such Algorithm exists " + noSuchAlgo);
    } catch (NoSuchPaddingException noSuchPad) {
        System.out.println(" No Such Padding exists " + noSuchPad);
    } catch (InvalidKeyException invalidKey) {
        System.out.println(" Invalid Key " + invalidKey);
    } catch (BadPaddingException badPadding) {
        System.out.println(" Bad Padding " + badPadding);
    } catch (IllegalBlockSizeException illegalBlockSize) {
        System.out.println(" Illegal Block Size " + illegalBlockSize);
    } catch (InvalidAlgorithmParameterException invalidParam) {
        System.out.println(" Invalid Parameter " + invalidParam);
    }
