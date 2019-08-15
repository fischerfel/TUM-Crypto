private Optional<Cipher> getCipher(int mode, byte[] iv) {
  // where mode is either 1 -> encrypt or 2 -> decrypt
  Optional<Cipher> result = Optional.empty();

  Cipher cipher = null;
  try {
    cipher = Cipher.getInstance(this.algorithmMode);

    IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
    AlgorithmParameters parameters =
      AlgorithmParameters.getInstance(this.algorithm);
    parameters.init(ivParameterSpec);

    cipher.init(mode, this.key, parameters);
    result = Optional.of(cipher);
  } catch (NoSuchAlgorithmException e) {
    log.error("Could not find cipher mode: `{}`", e.getMessage());
    log.debug(e.toString());
  } catch (NoSuchPaddingException e) {
    log.error("Could not find padding type: `{}`", e.getMessage());
    log.debug(e.toString());
  } catch (InvalidKeyException e) {
    log.error("Encryption key is invalid: `{}`", e.getMessage());
    log.debug(e.toString());
  } catch (InvalidParameterSpecException e) {
    log.error("Algorithm parameter spec invalid: `{}`", e.getMessage());
    log.debug(e.toString());
  } catch (InvalidAlgorithmParameterException e) {
    log.error("Algorithm parameters invalid: `{}`", e.getMessage());
    log.debug(e.toString());
  }

  return result;
}
