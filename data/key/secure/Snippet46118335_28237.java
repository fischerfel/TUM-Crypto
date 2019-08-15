@Transactional
public ResponseEntity <Client> getClientByUsername(String username) throws Exception {
  Client loggedClient = clientDAO.findByUsername(username);
  String data = loggedClient.getCreditCardNo();
  if (null != data) {
    @SuppressWarnings("static-access")
    byte[] encrypted = base64.decodeBase64(data);
    SecretKeySpec secretKeySpec = new SecretKeySpec(encryptionKey.getBytes(), algorithm);
    Cipher cipher = Cipher.getInstance(algorithm);
    cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
    decrypted = cipher.doFinal(encrypted);
    loggedClient.setCreditCardNo(new String(decrypted));
  }
  return new ResponseEntity < Client > (loggedClient, HttpStatus.OK);
}
