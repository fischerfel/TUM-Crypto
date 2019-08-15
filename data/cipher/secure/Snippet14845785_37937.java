Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
int blockSize = cipher.getBlockSize();

byte[] inputBytes = data.getBytes();
int byteLength = inputBytes.length;
if (byteLength % blockSize != 0) {
    byteLength = byteLength + (blockSize - (byteLength % blockSize));
}

byte[] paddedBytes = new byte[byteLength];

System.arraycopy(inputBytes, 0, paddedBytes, 0, inputBytes.length);

cipher.init(Cipher.ENCRYPT_MODE, newKey, ivSpec);
byte[] results = cipher.doFinal(paddedBytes);
