import javacard.framework.APDU;
import javacard.framework.Applet;
import javacard.framework.ISO7816;
import javacard.framework.ISOException;
import javacard.framework.Util;
import javacardx.framework.math.BigNumber;
import javacard.security.CryptoException;
import javacard.security.MessageDigest;

public class SignatureGPS extends Applet {
    public static final byte CLA = (byte) 0xB0;
    public static final byte INS = (byte) 0x00;

    private BigNumber s;
    private BigNumber x;
    private MessageDigest h;

    private SignatureGPS() {
        s = new BigNumber((short) 100);
        x = new BigNumber((short) 100);
        try {
            h = MessageDigest.getInstance(MessageDigest.ALG_SHA_256, false);
        } catch (CryptoException e) {
            if (e.getReason() == CryptoException.NO_SUCH_ALGORITHM){
            }
        }
    }


    public static void install(byte bArray[], short bOffset, byte bLength) throws ISOException {
        new SignatureGPS().register();
    }


    public void process(APDU apdu) throws ISOException {
        byte[] buffer = apdu.getBuffer();

        if (this.selectingApplet()) return;

        if (buffer[ISO7816.OFFSET_CLA] != CLA)
            ISOException.throwIt(ISO7816.SW_CLA_NOT_SUPPORTED);

        ...
 }

}
