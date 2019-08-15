try {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("Blowfish");
        SecretKey secretKey = keyGenerator.generateKey();
        Cipher cipher = Cipher.getInstance("Blowfish"); 
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        String decryptSt = new String(cipher.doFinal(DatatypeConverter.parseHexBinary("f250d7a040859d66541e2ab4a83eb2225d4fff880f7d2506")));
        System.out.println(decryptSt);
    } catch (NoSuchAlgorithmException ex) {
        Logger.getLogger(Tester.class.getName()).log(Level.SEVERE, null, ex);
    } catch (NoSuchPaddingException ex) {
        Logger.getLogger(Tester.class.getName()).log(Level.SEVERE, null, ex);
    } catch (InvalidKeyException ex) {
        Logger.getLogger(Tester.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IllegalBlockSizeException ex) {
        Logger.getLogger(Tester.class.getName()).log(Level.SEVERE, null, ex);
    } catch (BadPaddingException ex) {
        Logger.getLogger(Tester.class.getName()).log(Level.SEVERE, null, ex);
    }
