import javacard.framework.APDU;
import javacard.framework.APDUException;
import javacard.framework.Applet;
import javacard.framework.CardRuntimeException;
import javacard.framework.ISO7816;
import javacard.framework.ISOException;
import javacard.framework.UserException;
import javacard.framework.Util;
import javacard.security.CryptoException;
import javacardx.framework.math.BigNumber;
import javacard.security.MessageDigest;

public class SignatureGPS extends Applet {

    public static final byte CLA = (byte) 0xB0;
    public static final byte INS = (byte) 0x00;

    private BigNumber s;
    private BigNumber x; 

    private SignatureGPS() {
        try {
            s = new BigNumber((short)100);
            x = new BigNumber((short)100);
            byte[] tmp = {(byte) 0xc6, (byte) 0x85, (byte)0x8e, 0x06, (byte)0xb7, 0x04, 0x04, (byte)0xe9, (byte)0xcd, (byte)0x9e, 0x3e, (byte)0xcb, 0x66, 0x23, (byte)0x95, (byte)0xb4,
                    0x42, (byte)0x9c, 0x64, (byte)0x81, 0x39, 0x05, 0x3f, (byte)0xb5, 0x21, (byte)0xf8, 0x28, (byte)0xaf, 0x60, 0x6b, 0x4d, 0x3d, (byte)0xba, (byte)0xa1, 0x4b, 0x5e, 
                    0x77, (byte)0xef, (byte)0xe7, 0x59, 0x28, (byte)0xfe, 0x1d, (byte)0xc1, 0x27, (byte)0xa2, (byte)0xff, (byte)0xa8, (byte)0xde, 0x33, 0x48, (byte)0xb3, (byte)0xc1, 
                    (byte)0x85, 0x6a, 0x42, (byte)0x9b, (byte)0xf9, 0x7e, 0x7e, 0x31, (byte)0xc2, (byte)0xe5, (byte)0xbd, 0x66};
            x.init(tmp, (short) 0, (short) tmp.length, BigNumber.FORMAT_HEX);
            h = MessageDigest.getInstance(MessageDigest.ALG_SHA_256, false);
        }
        catch(ArithmeticException e){}
        catch(CryptoException e){CryptoException.throwIt(e.getReason());}
        catch(NullPointerException e){}
        catch(ArrayIndexOutOfBoundsException e){}
    }

    public static void install(byte bArray[], short bOffset, byte bLength) throws ISOException {
        new SignatureGPS().register();
    }

    public void process(APDU apdu) 
        throws ISOException, ArithmeticException, NullPointerException, ArrayIndexOutOfBoundsException, APDUException, {
        byte[] buffer = apdu.getBuffer();

        if (this.selectingApplet()) return;

        if (buffer[ISO7816.OFFSET_CLA] != CLA)
            ISOException.throwIt(ISO7816.SW_CLA_NOT_SUPPORTED);

        if (buffer[ISO7816.OFFSET_INS]!=0)
            ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);

        short LC = apdu.getIncomingLength();
        apdu.setIncomingAndReceive();
        s.init(buffer, (short)5, LC, BigNumber.FORMAT_HEX);
    }
}
