public static void main(String[] args) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.update("string".getBytes() );
        byte[] hash = digest.digest();
        BigInteger bi = new BigInteger( hash );
        System.out.println( "hex:" + bi.toString(16) + "\r\ndec:" + bi.toString() );
    }
