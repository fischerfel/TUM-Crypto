public static String calc(InputStream is ) {
        String output;
        int read;
        byte[] buffer = new byte[8192];

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256"); //"MD5");
            while ((read = is.read(buffer)) > 0) {
                digest.update(buffer, 0, read);
            }
            byte[] hash = digest.digest();
            BigInteger bigInt = new BigInteger(1, hash);
            output = bigInt.toString(16);

        } 
        catch (Exception e) {
            e.printStackTrace( System.err );
            return null;
        }
        return output;
    }
