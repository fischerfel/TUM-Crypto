public void myMethod(...) throws MyModuleException {
  try {
    final Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
    cipher.init(Cipher.ENCRYPT_MODE, key, iv);
    cipher.doFinal(*something*);
  } catch(Crypto1Ex ex){
    throw new MyModuleException("something is wrong", ex); //ex added, so it is not lost and visible in stacktraceses
  } catch(Crypto1Ex ex){
    throw new MyModuleException("something is wrong", ex);
  } //etc.
}
