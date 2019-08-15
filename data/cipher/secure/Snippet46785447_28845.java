        Cipher cipher = Cipher.getInstance("RSA/NONE/PKCS1Padding", "BC");
        SecureRandom random = new SecureRandom();
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", "BC");
        generator.initialize(2048, random);
        KeyPair pair = generator.generateKeyPair();         
        Key publicKey = pair.getPublic();
        Key privateKey = pair.getPrivate();             
        System.out.println("publicKey : " + byteArrayToHexString(publicKey.getEncoded()));          
        System.out.println("privateKey : " + byteArrayToHexString(privateKey.getEncoded()));                    
