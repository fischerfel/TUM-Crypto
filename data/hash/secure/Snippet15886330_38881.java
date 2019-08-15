    public CipherHandler {

        public String encryptPassword(char[] plaintext, String encoding) throws Exception {

            MessageDigest msgDigest = null;
            String hashValue = null;

            /* Convert char array plaintext to byte array */
            byte[] b = new byte[plaintext.length << 1];
            for (int i = 0; i < plaintext.length; i++) {
                b[i] = (byte) plaintext[i]; //will this work regardless of encoding?
            }

            try {
                msgDigest = MessageDigest.getInstance("SHA-256");
                msgDigest.update(b);
                byte rawByte[] = msgDigest.digest();
                hashValue = (new BASE64Encoder()).encode(rawByte);
            } catch (NoSuchAlgorithmException e) {
                System.out.println("No Such Algorithm Exists");
            }

            System.out.println(hashValue);
            return hashValue;
        }            
    }
