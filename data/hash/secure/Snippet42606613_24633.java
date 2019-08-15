package net.sourceforge.globalplatform.jc.helloworld;
import javacard.framework.*;
import javacard.security.*;
import javacardx.crypto.Cipher;

import javax.print.attribute.standard.MediaSize;
import java.util.logging.Level;

public class HelloWorldApplet extends Applet {

final static byte  APPLET_CLA    = (byte)0x80;
final static byte  HASH          = (byte)0x05;

public static byte[] Message;

MessageDigest mDig = MessageDigest.getInstance(MessageDigest.ALG_MD5, true);

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

            case HASH:
                hash_message(apdu);
                break;

            default:
                ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
        }
    } else {
        ISOException.throwIt(ISO7816.SW_CLA_NOT_SUPPORTED);
    }
}

public void hash_message(APDU apdu) {
    byte[] buffer = apdu.getBuffer();
    short mLen = apdu.setIncomingAndReceive();
    mDig.reset();
    mDig.doFinal(buffer, (short) ISO7816.OFFSET_CDATA, mLen, Message, (short) 0);
    Util.arrayCopy(Message,(short)0,buffer,(short)0, mLen);
    apdu.setOutgoingAndSend((short)0,mLen);
}

}
