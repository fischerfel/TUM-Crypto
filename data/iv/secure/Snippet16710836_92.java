void seek(long pos) {
    // calculate the block number that contains the byte we need to seek to
    long block = pos / BLOCK_SIZE;
    // allocate a 16-byte buffer
    ByteBuffer buffer = ByteBuffer.allocate(BLOCK_SIZE);
    // fill the first 12 bytes with the original IV (the iv minus the actual counter value)
    buffer.put(cipher.getIV(), 0, BLOCK_SIZE - 4);
    // set the counter of the IV to the calculated block index + 1 (counter starts at 1)
    buffer.putInt(block + 1);
    IvParameterSpec iv = new IvParameterSpec(buffer.array());
    // re-init the Cipher instance with the new IV
    cipher.init(Cipher.ENCRYPT_MODE, key, iv);
    // seek the delegate wrapper (like seek() in a RandomAccessFile and 
    // recreate the delegate stream to read from the new location)
    // recreate the input stream we're serving reads from
    input = new CipherInputStream(delegate, cipher);
    // next read will be at the block boundary, need to skip some bytes to arrive at pos
    int toSkip = (int) (pos % BLOCK_SIZE);
    byte[] garbage = new byte[toSkip];
    // read bytes into a garbage array for as long as we need (should be max BLOCK_SIZE
    // bytes
    int skipped = input.read(garbage, 0, toSkip);
    while (skipped < toSkip) {
        skipped += input.read(garbage, 0, toSkip - skipped);
    }

    // at this point, the CipherStream is positioned at pos, next read will serve the 
    // plain byte at pos
}
