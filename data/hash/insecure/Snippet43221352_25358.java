try {
        MessageDigest md = MessageDigest.getInstance("MD5");     
        md.update(Password.getBytes());
        byte[] messageDigest = md.digest(Password.getBytes());
        BigInteger number = new BigInteger(1, messageDigest);
        String hashtext = number.toString(16);
        // Now we need to zero pad it if you actually want the full 32 chars.
        while (hashtext.length() < 32) {
            hashtext = "0" + hashtext;
        }

    } catch (NoSuchAlgorithmException e) {
        throw new RuntimeException(e);
    }
