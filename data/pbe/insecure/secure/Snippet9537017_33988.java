byte[] salt = {
    (byte)0xc7, (byte)0x73, (byte)0x21, (byte)0x8c,
    (byte)0x7e, (byte)0xc8, (byte)0xee, (byte)0x99
};

int count = 20;

PBEParameterSpec pbeParamSpec = new PBEParameterSpec(salt, count);
PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray());
SecretKeyFactory keyFac = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
SecretKey pbeKey = keyFac.generateSecret(pbeKeySpec);

Cipher cipher = Cipher.getInstance("PBEWithMD5AndDES");
cipher.init(Cipher.ENCRYPT_MODE, pbeKey, pbeParamSpec);

SealedObject sealed = new SealedObject(object, cipher);
...
