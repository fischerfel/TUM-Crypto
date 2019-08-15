 try {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        String text = "admin";
        md.update(text.getBytes("UTF-8")); // Change this to "UTF-16" if needed
        byte[] digest = md.digest();
        BigInteger bigInt = new BigInteger(1, digest);
        String output = bigInt.toString(16);

        System.out.println(output);

    } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
        Logger.getLogger(PasswordTest.class.getName()).log(Level.SEVERE, null, ex);

    }
