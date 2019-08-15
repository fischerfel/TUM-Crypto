    public  byte[] sha256digest16(String[] list) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.reset();
        for(String s: list){
            digest.update(s.getBytes("UTF-8"));
        }
        byte[]  b = digest.digest();

        return String.format("%0" + (b.length) + 'x', new BigInteger(1, b)).getBytes("UTF-8");

    }
