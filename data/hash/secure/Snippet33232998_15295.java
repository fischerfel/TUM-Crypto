public class HMACSHA {

private MessageDigest md = null;
private static final byte IPAD = (byte) 0x36;
private static final byte OPAD = (byte) 0x5c;
private byte[] secretIpad;
private byte[] secretOpad;
private byte[] secretKey;
private short outSize = 20;
private short blockSize = 64;
private short ctr = 0;

/**
 * Init HMAC algo from RFC-2104. Setup the blocksize of the algo. Default SHA-1.
 *
 * @param hashAlgo
 * @param hmacKey
 */
public void init(byte hashAlgo, byte[] hmacKey) {
    md = MessageDigest.getInstance(hashAlgo, false);

    if (hashAlgo == 4) {
        outSize = (short) 32; // SHA-256
    } else if (hashAlgo == 5) {
        outSize = (short) 48; // SHA-384            
        blockSize = (short) 128;
    } else if (hashAlgo == 6) {
        outSize = (short) 64; // SHA-512            
        blockSize = (short) 128;
    }

    secretIpad = JCSystem.makeTransientByteArray((short) blockSize, JCSystem.CLEAR_ON_RESET);
    secretOpad = JCSystem.makeTransientByteArray((short) blockSize, JCSystem.CLEAR_ON_RESET);
    secretKey = JCSystem.makeTransientByteArray((short) blockSize, JCSystem.CLEAR_ON_RESET);

    // Block size == key size. Adjust key.
    if ((short) hmacKey.length > blockSize) {
        md.reset();
        md.doFinal(hmacKey, (short) 0, (short) hmacKey.length, secretKey, (short) 0);
    } else {
        ArrayLogic.arrayCopyRepack(hmacKey, (short) 0, (short) hmacKey.length, secretKey, (short) 0);
    }

    // Setup IPAD & OPAD secrets
    for (ctr = (short) 0; ctr < blockSize; ctr++) {
        secretIpad[ctr] = (byte) (secretKey[ctr] ^ IPAD);
        secretOpad[ctr] = (byte) (secretKey[ctr] ^ OPAD);
    }
    ctr = (short) 0;
}

public void doFinal(byte[] msg, short offset, short length, byte[] workBuff, short workOffset, byte[] outMsg, short outOffset) {
    if (md != null) {
        // hash(i_key_pad ∥ message)
        md.reset();
        ArrayLogic.arrayCopyRepack(secretIpad, (short) 0, (short) secretIpad.length, workBuff, workOffset);
        ArrayLogic.arrayCopyRepack(msg, offset, length, workBuff, (short) (workOffset + secretIpad.length));
        md.doFinal(workBuff, workOffset, (short) (secretIpad.length + length), outMsg, outOffset);

        //hash(o_key_pad ∥ i_pad-hashed)
        md.reset();
        ArrayLogic.arrayCopyRepack(secretOpad, (short) 0, (short) secretOpad.length, workBuff, workOffset);
        ArrayLogic.arrayCopyRepack(outMsg, outOffset, (short) outSize, workBuff, (short) (workOffset + secretOpad.length));
        md.doFinal(workBuff, workOffset, (short) (secretOpad.length + outSize), outMsg, outOffset);
    }
}
