        String testString="someText";
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] messageDigest = md.digest(testString.getBytes());
        BigInteger number = new BigInteger(1, messageDigest);
        String hashtext = number.toString(16);
