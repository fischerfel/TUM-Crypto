        KeyGenerator keyGenerator = KeyGenerator.getInstance("Blowfish");
        SecretKey secretKey = keyGenerator.generateKey();
        Cipher cipher = Cipher.getInstance("Blowfish"); 
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        String input = "password";
        byte encrypted[] = cipher.doFinal(input.getBytes());
        String s = new String(encrypted);
        System.out.println(s);
