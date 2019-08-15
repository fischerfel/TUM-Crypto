public void decryptCreditCard() {
  Key key = new SecretKeySpec(KEY, "AES");
  try {
    String ccNumber = this.cardNumber;
    if (ccNumber == null || ccNumber.length() == 0) {
      return;
    }
    Cipher c = Cipher.getInstance(ALGORITHM);
    c.init(Cipher.DECRYPT_MODE, key);
    byte[] ccENCBytes = Base64.decodeBase64(ccNumber);
    byte[] ccDECBytes = c.doFinal(ccENCBytes);
    this.plainCardNumber = new String(ccDECBytes);
    this.last4CreditCard =             plainCardNumber.substring(this.plainCardNumber.length() - 4);
  } catch (Exception e) {
    throw new RuntimeException(e);
  }
}
