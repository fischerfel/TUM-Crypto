private static final MessageDigest MD5_DIGEST;
static {
   try {
      MD5_DIGEST = MessageDigest.getInstance("MD5");
   ///CLOVER:OFF
   } catch (Exception e) {
      // can't happen since MD5 is a known digest
   }
   ///CLOVER:ON
}

public MessageDigest getMessageDigest() {
   return MD5_DIGEST;
}
