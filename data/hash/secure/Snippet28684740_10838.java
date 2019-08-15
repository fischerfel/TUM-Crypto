        SecureRandom random = new SecureRandom();
        byte[] saltArr = new byte[64];
        random.nextBytes(saltArr);

        String salt = new String(saltArr, "Cp1252");
        System.out.println("SALT:"+salt);


        byte[] encodedBytes = Base64.encodeBase64(saltArr);
        System.out.println("Encoded SALT:" + new String(encodedBytes));

        byte[] decodedBytes = Base64.decodeBase64(encodedBytes);
        System.out.println("Decoded SALT:" + new String(decodedBytes, "Cp1252"));


        //SHA
        String target = "Test";
        MessageDigest sh = MessageDigest.getInstance("SHA-512");
        sh.update(target.getBytes());
        StringBuffer sb = new StringBuffer();
        for (byte b : sh.digest()) sb.append(Integer.toHexString(0xff & b));
        System.out.println("Hashed PWD:"+sb);

        //And then joining them together... 
