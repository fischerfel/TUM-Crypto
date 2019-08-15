try {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("Blowfish");
        SecretKey secretKey = keyGenerator.generateKey();
        Cipher cipher = Cipher.getInstance("Blowfish"); 
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        String input = "tester";
        byte encrypted[] = cipher.doFinal(input.getBytes());

        // PRINT ENCRYPTED TEXT

        System.out.println(new String(Base64.encodeBytes(encrypted))); 
    } catch (NoSuchAlgorithmException ex) {
        Logger.getLogger(JavaApplication1.class.getName()).log(Level.SEVERE, null, ex);
    } catch (NoSuchPaddingException ex) {
        Logger.getLogger(JavaApplication1.class.getName()).log(Level.SEVERE, null, ex);
    } catch (InvalidKeyException ex) {
        Logger.getLogger(JavaApplication1.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IllegalBlockSizeException ex) {
        Logger.getLogger(JavaApplication1.class.getName()).log(Level.SEVERE, null, ex);
    } catch (BadPaddingException ex) {
        Logger.getLogger(JavaApplication1.class.getName()).log(Level.SEVERE, null, ex);
    }
