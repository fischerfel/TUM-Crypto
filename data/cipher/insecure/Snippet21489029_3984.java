try {
        //Conservazione delle chiavi!
        //Secure PRNG
        SecureRandom m = SecureRandom.getInstance("SHA1PRNG");
        //Secure HASH
        MessageDigest hash = MessageDigest.getInstance("SHA-1");
        //KeyGenerator 
        KeyGenerator keyGenerator = KeyGenerator.getInstance("DES");
        keyGenerator.init(m);
        //Want to obtain a random masterkey that need to encrypt
        Key key = keyGenerator.generateKey();
        //Get DES cipher
        Cipher cipher = Cipher.getInstance("DES");
        //AND NOW?
                    cipher.init(Cipher.ENCRYPT_MODE, KEY); // THAT's the problem.



    } catch (NoSuchAlgorithmException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (NoSuchPaddingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
