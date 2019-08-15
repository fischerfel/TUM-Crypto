        byte[] key = content.getBytes();


        MessageDigest md = MessageDigest.getInstance("SHA1");

        byte[] hash = md.digest(key);

        String result = "";
        for (byte b : hash) {
            result += Integer.toHexString(b & 255);
        }
        return result.toUpperCase();
