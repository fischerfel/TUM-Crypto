String passwordSalt = "somesalt";
       byte[] bsalt = base64ToByte(passwordSalt);
       byte[] thePasswordToDigestAsBytes = ("somepassword").getBytes("UTF-8");
       System.out.println("------------------------------"+passwordSalt);
       MessageDigest digest = MessageDigest.getInstance("SHA-512");
        digest.reset();
        digest.update(bsalt);
        byte[] input = digest.digest(thePasswordToDigestAsBytes);
        System.out.println("------------------------------"+byteToBase64(input));
