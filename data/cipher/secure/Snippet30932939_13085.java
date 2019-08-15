 String url = "https://someurl.com"

 String token = createToken(bookNumber, invNumber, cusNumber)

 url += '?ref=' + token

class AesEncryptor {

    static byte[] encrypt(String clearText) {
            byte[] encrypted = null
            try {
                byte[] iv = new byte[16]
                Arrays.fill(iv, (byte) 0)

                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
                encrypted = cipher.doFinal(clearText.getBytes("UTF-8"))
            }
            catch (Exception e) {
                log.error "An error occurred when encrypting", e
            }
            encrypted
        }



    /**
         * Creates a token.
         * @return
         */
        static String createToken(final String bookNumber, final String invNumber, final String cusNumber) {
            String data =  bookNumber + invNumber + cusNumber
            String token = URLEncoder.encode(Base64.encodeBase64String(encrypt(data)), "UTF-8")
            token
        }
}
