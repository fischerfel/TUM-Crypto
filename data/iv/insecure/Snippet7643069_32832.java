

int BLOCK_SIZE = 16;
BigInteger MODULUS = BigInteger.ONE.shiftLeft(BLOCK_SIZE * 8);
...
/* Retrieve original IV. */
byte[] iv = ... ;

/* Compute the index of the block to which data will be appended. */
BigInteger block = BigInteger.valueOf(file.length() / BLOCK_SIZE);
/* Add the block to the nonce to find the current counter. */
BigInteger nonce = new BigInteger(1, iv);
byte[] tmp = nonce.add(block).mod(MODULUS).toByteArray();
/* Right-justify the counter value in a block-sized array. */
byte[] ctr = new byte[BLOCK_SIZE];
System.arraycopy(tmp, 0, ctr, BLOCK_SIZE - tmp.length, tmp.length);
/* Use this to initialize the appending cipher. */
IvParameterSpec param = new IvParameterSpec(ctr);
