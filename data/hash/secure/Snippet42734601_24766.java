public class HelloWorldApplet extends Applet {

final static byte   APLET_CLA = (byte)0x80;
final static byte   INITIALIZE = (byte)0x00;
final static byte   SIGN = (byte)0x01;
final static byte   HASHVERIFY = (byte)0x07;
final static byte   GETHASH = (byte)0x08;

final static short SW_WRONG_DATA_LENGTH = 0x6300;
final static short SW_KEY_NOT_INITIALIZED = 0x6301;
final static short SW_KEY_IS_INITIALIZED = 0x6302;
final static short SW_KEY_IS_NOT_INITIALIZED = 0x6303;
final static short SW_INCORRECT_PARAMETER = 0x6304;

public static byte[] Message;
public static short message_len = 0;
public static short hashLen = 0;
public static short signLen = 0;
public static boolean key_initialization_flag256 = false;
public static boolean key_initialization_flag128 = false;

    // use unpadded RSA cipher for signing
    Cipher cipherRSA256 = Cipher.getInstance(Cipher.ALG_RSA_NOPAD, false);
    Cipher cipherRSA128 = Cipher.getInstance(Cipher.ALG_RSA_NOPAD, false);

    KeyPair rsaPair256 = new KeyPair(KeyPair.ALG_RSA_CRT, KeyBuilder.LENGTH_RSA_2048);
    KeyPair rsaPair128 = new KeyPair(KeyPair.ALG_RSA_CRT, KeyBuilder.LENGTH_RSA_1024);
    RSAPrivateCrtKey rsaKeyPriv256;
    RSAPrivateCrtKey rsaKeyPriv128;
    RSAPublicKey rsaKeyPub256;
    RSAPublicKey rsaKeyPub128;

    byte[] hashBuffer = JCSystem.makeTransientByteArray((short)256, JCSystem.CLEAR_ON_DESELECT);
    byte[] signBuffer = JCSystem.makeTransientByteArray((short)256, JCSystem.CLEAR_ON_DESELECT);

    byte[] dataBuffer = JCSystem.makeTransientByteArray((short)256, JCSystem.CLEAR_ON_DESELECT);

    MessageDigest md = MessageDigest.getInstance(MessageDigest.ALG_SHA_256, false);

    public static void install(byte[] bArray, short bOffset, byte bLength)
    {
        Message = new byte[256];
        new HelloWorldApplet().register(bArray, (short) (bOffset + 1), bArray[bOffset]);
    }

    public void process(APDU apdu)
    {
        if (selectingApplet())
        {
            return;
        }

        byte[] buffer = apdu.getBuffer();
        if (buffer[ISO7816.OFFSET_CLA] == APPLET_CLA) {

            switch (buffer[ISO7816.OFFSET_INS]) {

                case INITIALIZE:
                    // generate a new key
                    initialize(apdu);
                    break;

                case SIGN:
                    // sign a given incoming message
                    sign_message(apdu);
                    break;

                case HASHVERIFY:
                    verify_hash(apdu);
                    break;

                case GETHASH:
                    get_hash(apdu);
                    break;

                default:
                    ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
            }
        } else {
            ISOException.throwIt(ISO7816.SW_CLA_NOT_SUPPORTED);
        }
    }

    public void initialize(APDU apdu)
    {
        byte[] buffer = apdu.getBuffer();
        switch(buffer[ISO7816.OFFSET_P1]) {
            case 0x00: // gen 256 byte RSA key (P1=00)
                if (key_initialization_flag256)
                    ISOException.throwIt(SW_KEY_IS_INITIALIZED);
                rsaPair256.genKeyPair();
                rsaKeyPriv256 = (RSAPrivateCrtKey) rsaPair256.getPrivate();
                rsaKeyPub256 = (RSAPublicKey) rsaPair256.getPublic();
                key_initialization_flag256 = true;
                break;
            case 0x01: // gen 128 byte RSA key (P1=01)
                if (key_initialization_flag128)
                    ISOException.throwIt(SW_KEY_IS_INITIALIZED);
                rsaPair128.genKeyPair();
                rsaKeyPriv128 = (RSAPrivateCrtKey) rsaPair128.getPrivate();
                rsaKeyPub128 = (RSAPublicKey) rsaPair128.getPublic();
                key_initialization_flag128 = true;
                break;
        }
    }

    // P1=0 for modulus, P1=1 for exponent
    private void getPublicRSA(APDU apdu)
    {
        byte[] buffer = apdu.getBuffer();
        short length = 0;
        switch (buffer[ISO7816.OFFSET_P1])
        {
            case 0x00: // 256 byte RSA (P1)
                if (!key_initialization_flag256)
                    ISOException.throwIt(SW_KEY_IS_INITIALIZED);
                switch (buffer[ISO7816.OFFSET_P2]) {
                    case 0x00: // get the modulus (P2)
                        length = rsaKeyPub256.getModulus(buffer, (short) 0);
                        break;
                    case 0x01: // get the exponent (P2)
                        length = rsaKeyPub256.getExponent(buffer, (short) 0);
                        break;
                    default:
                        ISOException.throwIt(SW_INCORRECT_PARAMETER);
                        break;
                }
                break;
            case 0x01: // 128 byte RSA (P1)
                if (!key_initialization_flag128)
                    ISOException.throwIt(SW_KEY_IS_INITIALIZED);
                switch (buffer[ISO7816.OFFSET_P2]) {
                    case 0x00: // get the modulus (P2)
                        length = rsaKeyPub128.getModulus(buffer, (short) 0);
                        break;
                    case 0x01: // get the exponent (P2)
                        length = rsaKeyPub128.getExponent(buffer, (short) 0);
                        break;
                    default:
                        ISOException.throwIt(SW_INCORRECT_PARAMETER);
                        break;
                }
                break;
            default:
                ISOException.throwIt(SW_INCORRECT_PARAMETER);
        }
        apdu.setOutgoingAndSend((short) 0, length);
    }


    public void sign_message(APDU apdu)
    {
        byte[] buffer = apdu.getBuffer();
        switch(message_len) {
            case 256:
                if(!key_initialization_flag256)
                    ISOException.throwIt(SW_KEY_IS_NOT_INITIALIZED);
                cipherRSA256.init(rsaPair256.getPrivate(), Cipher.MODE_ENCRYPT);
                Util.arrayCopyNonAtomic(Message, (short) 0, dataBuffer, (short) 0, message_len);
                pkcs1_sha(dataBuffer, (short) 0, message_len, hashBuffer); // 32 Bytes
                signLen = cipherRSA256.doFinal(hashBuffer, (short) 0, message_len, signBuffer, (short) 0); // 128 Bytes
                Util.arrayCopy(signBuffer,(short)0,buffer,(short)0,signLen);
                apdu.setOutgoingAndSend((short) 0, signLen);
                break;
            case 128:
                if(!key_initialization_flag128)
                    ISOException.throwIt(SW_KEY_IS_NOT_INITIALIZED);
                cipherRSA128.init(rsaPair128.getPrivate(), Cipher.MODE_ENCRYPT);
                Util.arrayCopyNonAtomic(Message, (short) 0, dataBuffer, (short) 0, message_len);
                pkcs1_sha(dataBuffer, (short) 0, message_len, hashBuffer); // 32 Bytes
                signLen = cipherRSA128.doFinal(hashBuffer, (short) 0, message_len, signBuffer, (short) 0); // 128 Bytes
                Util.arrayCopy(signBuffer, (short) 0, buffer, (short) 0, signLen);
                apdu.setOutgoingAndSend((short) 0, signLen);
               break;
            default:
                ISOException.throwIt(SW_WRONG_DATA_LENGTH);
                break;
        }
    }

    public void verify_hash(APDU apdu) {
        byte[] buffer = apdu.getBuffer();
        switch(message_len) {
            case 256:
                if(!key_initialization_flag256)
                    ISOException.throwIt(SW_KEY_IS_INITIALIZED);
                cipherRSA256.init(rsaPair256.getPublic(), Cipher.MODE_DECRYPT);
                hashLen = cipherRSA256.doFinal(signBuffer, (short) 0, message_len, buffer, (short) 0);
                apdu.setOutgoingAndSend((short) 0, message_len);
                break;
            case 128:
                if(!key_initialization_flag128)
                    ISOException.throwIt(SW_KEY_IS_INITIALIZED);
                cipherRSA128.init(rsaPair128.getPublic(), Cipher.MODE_DECRYPT);
                hashLen = cipherRSA128.doFinal(signBuffer, (short) 0, message_len, buffer, (short) 0);
                apdu.setOutgoingAndSend((short) 0, message_len);
                break;
            default:
                ISOException.throwIt(SW_WRONG_DATA_LENGTH);
                break;
        }
    }

    public void get_hash(APDU apdu) {
        byte[] buffer = apdu.getBuffer();
        Util.arrayCopy(hashBuffer,(short)0,buffer,(short)0,message_len);
        apdu.setOutgoingAndSend((short)0,message_len);
    }

    // this function will leave tempBuffer with the data to be signed
    public void pkcs1_sha(byte[] toSign, short bOffset, short bLength,byte[] out)
    {
        md.reset();
        hashLen = md.doFinal(toSign, bOffset, bLength, out, (short) 0);
    }

}
