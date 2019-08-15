package hashPack;

import javacard.framework.*;
import javacard.security.CryptoException;
import javacard.security.MessageDigest;

public class HashMachine extends Applet {

    //outputArray
    byte[] hashedValue = new byte[64];

    //output Length
    short OLength = 0x0000;

    //Defining switch case variables for Hash algorithm commands
    final byte MD5 = (byte) 0x00;
    final byte RIPEMD160 = (byte) 0X01;
    final byte SHA = (byte) 0X02;
    final byte SHA224 = (byte) 0X03;
    final byte SHA256 = (byte) 0X04;
    final byte SHA384 = (byte) 0X05;
    final byte SHA512 = (byte) 0X06;

    public static void install(byte[] bArray, short bOffset, byte bLength) {
        new HashMachine();
    }

    protected HashMachine() {
        register();
    }

    public void process(APDU apdu) {
        if (selectingApplet()) {
            return;
        }

        byte[] buffer = apdu.getBuffer();
        try {
            switch (buffer[ISO7816.OFFSET_INS]) {
                case MD5: {
                    MessageDigest HashObj = MessageDigest.getInstance(MessageDigest.ALG_MD5, false);
                    HashObj.reset();
                    OLength = 16;

                    if (buffer[ISO7816.OFFSET_LC] > 0) {
                        doHash(apdu, HashObj, OLength);
                    } else {
                        ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);
                    }
                }
                break;

                case RIPEMD160: {
                    MessageDigest HashObj = MessageDigest.getInstance(MessageDigest.ALG_RIPEMD160, false);
                    HashObj.reset();
                    OLength = 20;

                    if (buffer[ISO7816.OFFSET_LC] > 0) {
                        doHash(apdu, HashObj, OLength);
                    } else {
                        ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);
                    }
                }
                break;

                case SHA: {
                    MessageDigest HashObj = MessageDigest.getInstance(MessageDigest.ALG_SHA, false);
                    HashObj.reset();
                    OLength = 20;

                    if (buffer[ISO7816.OFFSET_LC] > 0) {
                        doHash(apdu, HashObj, OLength);
                    } else {
                        ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);
                    }
                }
                break;

                case SHA224: {
                    MessageDigest HashObj = MessageDigest.getInstance(MessageDigest.ALG_SHA_224, false);
                    HashObj.reset();
                    OLength = 32;

                    if (buffer[ISO7816.OFFSET_LC] > 0) {
                        doHash(apdu, HashObj, OLength);
                    } else {
                        ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);
                    }
                }
                break;

                case SHA256: {
                    MessageDigest HashObj = MessageDigest.getInstance(MessageDigest.ALG_SHA_256, false);
                    HashObj.reset();
                    OLength = 32;
                    if (buffer[ISO7816.OFFSET_LC] > 0) {
                        doHash(apdu, HashObj, OLength);
                    } else {
                        ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);
                    }
                }
                break;

                case SHA384: {
                    MessageDigest HashObj = MessageDigest.getInstance(MessageDigest.ALG_SHA_384, false);
                    HashObj.reset();
                    OLength = 64;
                    if (buffer[ISO7816.OFFSET_LC] > 0) {
                        doHash(apdu, HashObj, OLength);
                    } else {
                        ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);
                    }
                }
                break;

                case SHA512: {
                    MessageDigest HashObj = MessageDigest.getInstance(MessageDigest.ALG_SHA_512, false);
                    HashObj.reset();
                    OLength = 64;
                    if (buffer[ISO7816.OFFSET_LC] > 0) {
                        doHash(apdu, HashObj, OLength);
                    } else {
                        ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);
                    }
                }
                break;

                default:
                    ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
            }
        } catch (CryptoException e) {
            ISOException.throwIt(((CryptoException) e).getReason());

        }

    }

    public void doHash(APDU apdu, MessageDigest HashObj, short OLength) {

        byte[] buffer = apdu.getBuffer();
        HashObj.update(buffer, ISO7816.OFFSET_CDATA, buffer[ISO7816.OFFSET_LC]);
        HashObj.doFinal(buffer, ISO7816.OFFSET_CDATA, buffer[ISO7816.OFFSET_LC], hashedValue, (short) 0);
        Util.arrayCopyNonAtomic(hashedValue, (short) 0, buffer, (short) 0, OLength);
        apdu.setOutgoingAndSend((short) 0, OLength);

    }
}
