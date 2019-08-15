   String token_id =  "ceb589c0-8a01-4513-96d9-6ed10d6f8c77";

        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
        }
        catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("MD5 algorithm not available.  Fatal (should be in the JDK).");
        }

        try {
            byte[] bytes = digest.digest(token_id.getBytes("UTF-8"));
            System.out.println(String.format("%032x", new BigInteger(1, bytes))) ;
        }
        catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("UTF-8 encoding not available.  Fatal (should be in the JDK).");
        }
