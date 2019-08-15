String Password = PasswordText.getText();
SecretKeySpec SKC = new SecretKeySpec(Password.getBytes(), "DES");
PBEKeySpec PKS = new PBEKeySpec(Password.toCharArray());
SecretKeyFactory SKF = null;
try{
    SKF = SecretKeyFactory.getInstance("DES");
} catch(NoSuchAlgorithmException AlgorithmFail) {
    return;
}
SecretKey CipherKey = null;
try{
    CipherKey = SKF.generateSecret(SKC);
} catch(InvalidKeySpecException KeyFail) {
    return;
}
