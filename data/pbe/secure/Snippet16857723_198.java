private String cryptoAlgorithm = "PBEWITHSHA256AND128BITAES-CBC-BC";

Security.addProvider(new BouncyCastleProvider());

// load passPhrase from configured external file to char array.
char[] passPhrase = null;
try {
    passPhrase = loadPassPhrase(passPhraseFile);
} catch (FileNotFoundException e) {
    throw BeanHelper.logException(LOG, methodName, new EJBException("The file not found: " + passPhraseFile, e));
} catch (IOException e) {
    throw BeanHelper.logException(LOG, methodName, new EJBException("Error in reading file: " + passPhraseFile, e));
}

PBEKeySpec pbeKeySpec = new PBEKeySpec(passPhrase);

try {
    SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(cryptoAlgorithm);
    SecretKey newSecretKey = secretKeyFactory.generateSecret(pbeKeySpec);
    return newSecretKey;
} catch (NoSuchAlgorithmException e) {
    throw BeanHelper.logException(LOG, methodName, new EJBException("The algorithm is not found: " + cryptoAlgorithm, e));
} catch (InvalidKeySpecException e) {
    throw BeanHelper.logException(LOG, methodName, new EJBException("The key spec is invalid", e));
}
