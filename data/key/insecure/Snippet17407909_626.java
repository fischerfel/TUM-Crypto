final byte[] key = {(byte)0x49, (byte)0x45, (byte)0x4D, (byte)0x4B, (byte)0x41, (byte)0x45, (byte)0x52, (byte)0x42, (byte)0x21, (byte)0x4E, (byte)0x41, (byte)0x43, (byte)0x55, (byte)0x4F, (byte)0x59, (byte)0x46};
final byte[] encRndB = {(byte)0x33, (byte)0xD4, (byte)0x8E, (byte)0xAF, (byte)0x75, (byte)0x70, (byte)0x40, (byte)0x5E};

final SecretKeySpec keySpec = new SecretKeySpec(key, "DESede");
final byte[] zeroInitVector = { 0, 0, 0, 0, 0, 0, 0, 0 };

final byte[] rndB      = tripleDes(encRndB, Cipher.DECRYPT_MODE, keySpec, zeroInitVector);
