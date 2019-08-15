public class CryptoKoreanSeed extends Applet {

    //Abbreviations
    private static final boolean NO_EXTERNAL_ACCESS = false;

    // Array for the encryption/decryption key
    private byte[] the_Key = new byte[128];

    // Defining required key
    KoreanSEEDKey koreanSeedKey = (KoreanSEEDKey) KeyBuilder.buildKey(KeyBuilder.TYPE_KOREAN_SEED, KeyBuilder.LENGTH_KOREAN_SEED_128, NO_EXTERNAL_ACCESS);

    // Defining required cipher
    Cipher myCipher;

    // Defining switch case variables for supported instructions = INS in APDU command
    final byte SET_KEY = (byte) 0xC0;
    final byte DO_CRYPTO = (byte) 0xC2;

    // Defining switch case variables for cipher algorithms = P1 in APDU command
    final byte ALG_KOREAN_SEED_CBC_NOPAD = (byte) 0x00;
    final byte ALG_KOREAN_SEED_ECB_NOPAD = (byte) 0x01;

    // Defining switch case variables for crytptography mode = P2 in APDU command
    final byte ENCRYPT_MODE = (byte) 0x00;
    final byte DECRYPT_MODE = (byte) 0x01;

    public static void install(byte[] bArray, short bOffset, byte bLength) {
        new CryptoKoreanSeed();
    }

    protected CryptoKoreanSeed() {
        register();
    }

    public void process(APDU apdu) {
        if (selectingApplet()) {
            return;
        }

        byte[] buffer = apdu.getBuffer();

        // Checking the CLA field in the APDU command.
        if (buffer[ISO7816.OFFSET_CLA] != 0) {
            ISOException.throwIt(ISO7816.SW_CLA_NOT_SUPPORTED);
        }

        // Checking the P1 and P2 fields in the APDU command.
        if (buffer[ISO7816.OFFSET_P1] != 0x00 || buffer[ISO7816.OFFSET_P2] > 1) {
            ISOException.throwIt(ISO7816.SW_INCORRECT_P1P2);
        }

        try {

            switch (buffer[ISO7816.OFFSET_INS]) {

                case SET_KEY:
                    setCryptoKeyAndInitCipher(apdu);
                    break;

                case DO_CRYPTO:
                    doIt(apdu);
                    break;

                default:
                    ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
            }

        } catch (CryptoException e) {
            ISOException.throwIt(((CryptoException) e).getReason());
        }

    }

    public void setCryptoKeyAndInitCipher(APDU apdu)
            throws ISOException {
        byte[] buffer = apdu.getBuffer();

        // Key must has 16 bytes length
        if (buffer[ISO7816.OFFSET_LC] == 16) {
            Util.arrayCopyNonAtomic(buffer, ISO7816.OFFSET_CDATA, the_Key,
                    (short) 0, buffer[ISO7816.OFFSET_LC]);
            koreanSeedKey.setKey(the_Key, (short) 0);

        } else {
            ISOException.throwIt(ISO7816.SW_DATA_INVALID);
        }

    }

    public void doIt(APDU apdu)
            throws ISOException {

        byte[] buffer = apdu.getBuffer();

        switch (buffer[ISO7816.OFFSET_P1]) {
            case ALG_KOREAN_SEED_CBC_NOPAD:
                myCipher = Cipher.getInstance(Cipher.ALG_KOREAN_SEED_CBC_NOPAD, NO_EXTERNAL_ACCESS);
                break;
            case ALG_KOREAN_SEED_ECB_NOPAD:
                myCipher = Cipher.getInstance(Cipher.ALG_KOREAN_SEED_ECB_NOPAD, NO_EXTERNAL_ACCESS);
                break;
        }

        if (buffer[ISO7816.OFFSET_P2] == ENCRYPT_MODE) {
            myCipher.init(koreanSeedKey, Cipher.MODE_ENCRYPT);
        } else if (buffer[ISO7816.OFFSET_P2] == DECRYPT_MODE) {
            myCipher.init(koreanSeedKey, Cipher.MODE_DECRYPT);
        }

        byte[] CipheredData = JCSystem.makeTransientByteArray((short) 32,
                JCSystem.CLEAR_ON_DESELECT);

        short datalen = apdu.setIncomingAndReceive();
        if ((datalen % 16) != 0) {
            ISOException.throwIt(ISO7816.SW_DATA_INVALID);
        }

        short cipheredDataLength = myCipher.doFinal(buffer, (short) ISO7816.OFFSET_CDATA, datalen, CipheredData, (short) 0);
        Util.arrayCopyNonAtomic(CipheredData, (short) 0, buffer, (short) 0,
                cipheredDataLength);
        apdu.setOutgoingAndSend((short) 0, cipheredDataLength);
    }
}
