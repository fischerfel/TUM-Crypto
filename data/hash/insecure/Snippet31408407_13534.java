public class MD5 {
    public static String getMD5(String input) {
        try {
            byte[] thedigest = MessageDigest.getInstance("MD5").digest(input.getBytes("UTF-8"));
            BigInteger number = new BigInteger(1, thedigest);
            String hashtext = number.toString(16);
            // Now we need to zero pad it if you actually want the full 32 chars.
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
    public static boolean checksum(String filePath, String hashCode) {
        try {
            return getFileHash(filePath).equals(hashCode);
        } catch (Exception ex) {
            return false;
        }
    }
    public static String getFileHash(String filePath) {
        InputStream fis = null;
        try {
            fis = new FileInputStream(filePath);
            byte[] buffer = new byte[1024];
            MessageDigest complete = MessageDigest.getInstance("MD5");
            int numRead;
            do {
                numRead = fis.read(buffer);
                if (numRead > 0) {
                    complete.update(buffer, 0, numRead);
                }
            } while (numRead != -1);
            byte[] b = complete.digest();
            String result = "";
            for (int i = 0; i < b.length; i++) {
                result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
            }
            return result;
        } catch (IOException | NoSuchAlgorithmException ex) {
            Logger.getLogger(MD5.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(MD5.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
