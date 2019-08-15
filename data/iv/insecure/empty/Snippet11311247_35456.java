byte[] bKey = Hex.decode("C67DDB0CE47D27FAF6F32ECA5C99E8AF")
byte[] bMsg = Hex.decode("ff00")

byte[] keyBytes = Arrays.copyOf(sKey.bytes, 24)
int j = 0, k = 16
while (j < 8) {
    keyBytes[k++] = keyBytes[j++]
}

SecretKey key3 = new SecretKeySpec(keyBytes, "DESede")
IvParameterSpec iv3 = new IvParameterSpec(new byte[8])
Cipher cipher3 = Cipher.getInstance("DESede/CBC/PKCS5Padding")
cipher3.init(Cipher.ENCRYPT_MODE, key3, iv3)

byte[] bMac = cipher3.doFinal(bMsg)
println new String(Hex.encode(bMac))
