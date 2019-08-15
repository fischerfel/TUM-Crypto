  /**
    * Returns a md5 hash.
    * @param pw password string
    * @return hash
    */
   public static String md5(final String pw) {
     try {
       final MessageDigest md = MessageDigest.getInstance("MD5");
       md.update(Token.token(pw));
       final TokenBuilder tb = new TokenBuilder();
       for(final byte b : md.digest()) {
         final int h = b >> 4 & 0x0F;
         tb.add((byte) (h + (h > 9 ? 0x57 : 0x30)));
         final int l = b & 0x0F;
         tb.add((byte) (l + (l > 9 ? 0x57 : 0x30)));
       }
       return tb.toString();
     } catch(final Exception ex) {
       Main.notexpected(ex);
       return pw;
     }
   }
