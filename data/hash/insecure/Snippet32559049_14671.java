)

public static String getFileMD5(InputStream is) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
            int numOfBytesRead;

            byte[] buffer = new byte[8192];
            while ((numOfBytesRead = is.read(buffer)) > 0) {
                md.update(buffer, 0, numOfBytesRead);
            }
        } catch (NoSuchAlgorithmException e) {
            logStatic.error(e);
        } catch (IOException e) {
            logStatic.error(e);
        }

        byte[] digest = md.digest();

        BigInteger bigInt = new BigInteger(1, digest);
        String output = bigInt.toString(16);
        return output;
    }
