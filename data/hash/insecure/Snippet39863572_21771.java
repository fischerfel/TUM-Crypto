 String password = "test";

        byte[] key = password.getBytes();

        MessageDigest md = MessageDigest.getInstance("SHA-1",new BouncyCastleProvider());

        byte[] hash = md.digest(key);

        String result = "";
        for (byte b : hash) {
            result += Integer.toHexString(b & 255);
        }
        return result;
