@Transactional
public boolean clientUpdate(String client) {
    str = updateclient.getCreditCardNo();
    if (null != str) {
      SecretKeySpec secretKeySpec = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), algorithm);
      Cipher cipher = Cipher.getInstance(algorithm);
      cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
      encrypted = cipher.doFinal(str.getBytes("UTF-8"));
      updateclient.setCreditCardNo(base64.encodeToString(encrypted));
      return clientDAO.updateProfileClient(updateclient);
    }
