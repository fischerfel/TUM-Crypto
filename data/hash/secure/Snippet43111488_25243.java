package net.sourceforge.globalplatform.jc.hasincard;

import javacard.framework.*;
import javacard.security.*;
import javacardx.crypto.*;

import java.security.acl.Owner;
import java.util.Random;


public class HasinCardApplet extends Applet {

   final static byte APPLET_CLA = (byte)0x80;
   final static byte INITIALIZE = (byte)0x00;
   final static byte INSERT_MESSAGE = (byte)0x01;
   final static byte CHECK_MESSAGE = (byte)0x02;
   final static byte GET_HASH = (byte)0x03;
   final static byte GET_SIGN = (byte)0x04;
   final static byte VERIFY_SIGN = (byte)0x05;
   final static byte VERIFY_PIN = (byte)0x06;
   final static byte CHANGE_PIN = (byte)0x07;
   final static byte UNLOCK_PIN = (byte)0x08;
   final static byte TEST = (byte)0x09;

   final static short SW_MESSAGE_NOT_INSERTED = (short)0x6300;
   final static short SW_KEY_IS_INITIALIZED = (short)0x6301;
   final static short SW_HASH_NOT_SET = (short)0x6302;
   final static short SW_SIGN_NOT_SET = (short)0x6303;
   final static short SW_CARD_IS_LOCKED = (short)0x6304;
   final static short SW_VERIFICATION_FAILED = (short)0x6305;
   final static short SW_PIN_VERIFICATION_REQUIRED = (short)0x6306;
   final static short SW_NEW_PIN_TOO_LONG = (short)0x6307;
   final static short SW_NEW_PIN_TOO_SHORT = (short)0x6308;
   final static short SW_PUK_VERIFICATION_REJECTED = (short)0x6309;
   final static short SW_PUK_VERIFICATION_FAILED = (short)0x630A;

   final static byte PIN_TRY_LIMIT = (byte)3;
   final static byte MAX_PIN_SIZE = (byte)16;
   final static byte MIN_PIN_SIZE = (byte)4;
   final static byte PUK_LEN = (byte)16;
   final static byte PUK_TRY_LIMIT = (byte)3;

   private static byte[] Message;
   private static byte[] Hash;
   private static byte[] Sign;
   private short message_len = 0;
   private short hash_len = 0;
   private short sign_len = 0;
   private static boolean key_initialization_flag = false;
   private static boolean insert_message_flag = false;
   private static boolean hash_set_flag = false;
   private static boolean sign_set_flag = false;
   MessageDigest mDigest;
   KeyPair rsaKey;
   Cipher rsaCipher;
   OwnerPIN userPin;
   RandomData rnd;
   byte[] PUK;
   boolean init_flag;
   byte puk_try_count;

   private HasinCardApplet (byte[] bArray,short bOffset,byte bLength)
   {
       Message         = new byte[512];
       Hash            = new byte[512];
       Sign            = new byte[512];
       userPin         = new OwnerPIN(PIN_TRY_LIMIT, MAX_PIN_SIZE);
       rsaKey          = new KeyPair(KeyPair.ALG_RSA,KeyBuilder.LENGTH_RSA_2048);
       rsaCipher       = Cipher.getInstance(Cipher.ALG_RSA_PKCS1, false);
       mDigest         = MessageDigest.getInstance(MessageDigest.ALG_SHA_256,false);
       rnd             = RandomData.getInstance(RandomData.ALG_SECURE_RANDOM);
       PUK             = new byte[PUK_LEN];
       generate_random(PUK, (byte) 16);
       init_flag       = false;
       puk_try_count   = (byte) 0;

       byte[] InstParam                = new byte[(byte)64];
       byte iLen                       = bArray[bOffset];
       bOffset                         = (short) (bOffset+iLen+1);
       byte cLen                       = bArray[bOffset];
       bOffset                         = (short) (bOffset+cLen+1);
       byte aLen                       = bArray[bOffset];
       Util.arrayCopy(bArray,(short) (bOffset +1),InstParam,(short)0,(short)aLen);
       byte CPassLen = InstParam[0];
       byte IPassLen = InstParam[CPassLen+1];
       userPin.update(InstParam,(short)1,CPassLen);

       register();

   }

   public static void install(byte[] bArray, short bOffset, byte bLength)
   {
       new HasinCardApplet(bArray, bOffset, bLength);
   }

   public boolean select()
   {
       Util.arrayFillNonAtomic(Message, (short) 0, (short)512, (byte) 0x00);
       Util.arrayFillNonAtomic(Hash, (short) 0, (short)512, (byte) 0x00);
       Util.arrayFillNonAtomic(Sign, (short) 0, (short)512, (byte) 0x00);
       return true;
   }

   public void deselect()
   {

   }

   public void process(APDU apdu)
   {
      if (selectingApplet())
      {
          if(!init_flag) {
              send_puk(apdu);
          }
          return;
      }

      byte[] buffer = apdu.getBuffer();
      if (buffer[ISO7816.OFFSET_CLA] == APPLET_CLA) {

          switch (buffer[ISO7816.OFFSET_INS]) {

              case VERIFY_PIN:
                  verify_pin(apdu);
                  break;

              case CHANGE_PIN:
                  change_pin(apdu);
                  break;

              case UNLOCK_PIN:
                  unlock_pin(apdu);
                  break;

              case INITIALIZE:
                  initialize();
                  break;

              case INSERT_MESSAGE:
                  insert_message(apdu);
                  break;

              case CHECK_MESSAGE:
                  check_message(apdu);
                  break;

              case GET_HASH:
                  hash_message();
                  get_hash(apdu);
                  break;

              case GET_SIGN:
                  hash_message();
                  sign_message();
                  get_sign(apdu);
                  break;

              case VERIFY_SIGN:
                  verify_sign(apdu);
                  break;

              case TEST:
                  test(apdu);
                  break;

              default:
                  ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
          }
      } else {
          ISOException.throwIt(ISO7816.SW_CLA_NOT_SUPPORTED);
      }
   }

   private void initialize() {
       if ( ! userPin.isValidated() )
           ISOException.throwIt(SW_PIN_VERIFICATION_REQUIRED);
       if(key_initialization_flag) {
           ISOException.throwIt(SW_KEY_IS_INITIALIZED);
       }
       rsaKey.genKeyPair();
       key_initialization_flag = true;
       init_flag = true;
   }

   private void insert_message(APDU apdu) {
       byte[] buffer = apdu.getBuffer();
       if ( ! userPin.isValidated() )
           ISOException.throwIt(SW_PIN_VERIFICATION_REQUIRED);
       if(buffer[ISO7816.OFFSET_P1] == (byte) 0x01) {
           // reset Message
           message_len = 0;
       }
       short LC = apdu.setIncomingAndReceive();
       Util.arrayCopy(buffer, (short) (ISO7816.OFFSET_CDATA), Message, message_len, LC);
       message_len = (short) (message_len + LC);
       insert_message_flag = true;
   }

   private void check_message(APDU apdu) {
       byte[] buffer = apdu.getBuffer();
       if ( ! userPin.isValidated() )
           ISOException.throwIt(SW_PIN_VERIFICATION_REQUIRED);
       if(insert_message_flag) {
           apdu.setIncomingAndReceive();
           Util.arrayCopy(Message, (short) 0, buffer, (short) 0, message_len);
           apdu.setOutgoingAndSend((short) 0, message_len);
       } else {
           ISOException.throwIt(SW_MESSAGE_NOT_INSERTED);
       }
   }

   private void hash_message() {
       if ( ! userPin.isValidated() )
           ISOException.throwIt(SW_PIN_VERIFICATION_REQUIRED);
       if(insert_message_flag) {
           mDigest.reset();
           hash_len = mDigest.doFinal(Message, (short) 0, message_len, Hash, (short) 0);
           hash_set_flag = true;
       } else {
           ISOException.throwIt(SW_MESSAGE_NOT_INSERTED);
       }
   }

   private void sign_message() {
       if ( ! userPin.isValidated() )
           ISOException.throwIt(SW_PIN_VERIFICATION_REQUIRED);
       if(insert_message_flag) {
           rsaCipher.init(rsaKey.getPrivate(), Cipher.MODE_ENCRYPT);
           sign_len = rsaCipher.doFinal(Hash, (short) 0, hash_len, Sign, (short) 0);
           sign_set_flag = true;
       } else {
           ISOException.throwIt(SW_MESSAGE_NOT_INSERTED);
       }
   }

   private void get_hash(APDU apdu) {
       if ( ! userPin.isValidated() )
           ISOException.throwIt(SW_PIN_VERIFICATION_REQUIRED);
       if(hash_set_flag) {
           byte[] buffer = apdu.getBuffer();
           Util.arrayCopy(Hash, (short) 0, buffer, (short) 0, hash_len);
           apdu.setOutgoingAndSend((short) 0, hash_len);
       } else {
           ISOException.throwIt(SW_HASH_NOT_SET);
       }
   }

   private void get_sign(APDU apdu) {
       if ( ! userPin.isValidated() )
           ISOException.throwIt(SW_PIN_VERIFICATION_REQUIRED);
       if(sign_set_flag) {
           byte[] buffer = apdu.getBuffer();
           Util.arrayCopy(Sign, (short) 0, buffer, (short) 0, sign_len);
           apdu.setOutgoingAndSend((short) 0, sign_len);
       } else {
           ISOException.throwIt(SW_SIGN_NOT_SET);
       }
   }

   private void verify_sign(APDU apdu) {
       byte[] buffer = apdu.getBuffer();
       if ( ! userPin.isValidated() )
           ISOException.throwIt(SW_PIN_VERIFICATION_REQUIRED);
       apdu.setIncomingAndReceive();
       rsaCipher.init(rsaKey.getPublic(), Cipher.MODE_DECRYPT);
       short LC = rsaCipher.doFinal(Sign, (short) 0, sign_len, buffer, (short)0);
       apdu.setOutgoingAndSend((short)0,LC);
   }

   private void verify_pin(APDU apdu) {
       byte[] buffer = apdu.getBuffer();
       if(userPin.getTriesRemaining() == (byte)0)
           ISOException.throwIt(SW_CARD_IS_LOCKED);
       short LC = (byte)(apdu.setIncomingAndReceive());
       if ( userPin.check(buffer, ISO7816.OFFSET_CDATA, (byte)LC) == false )
           ISOException.throwIt(SW_VERIFICATION_FAILED);
   }

   private void change_pin(APDU apdu) {
       byte[] buffer = apdu.getBuffer();
       if(userPin.getTriesRemaining() == (byte)0)
           ISOException.throwIt(SW_CARD_IS_LOCKED);
       if ( ! userPin.isValidated() )
           ISOException.throwIt(SW_PIN_VERIFICATION_REQUIRED);
       short LC = (byte)(apdu.setIncomingAndReceive());
       if(LC > MAX_PIN_SIZE)
           ISOException.throwIt(SW_NEW_PIN_TOO_LONG);
       if(LC < MIN_PIN_SIZE)
           ISOException.throwIt(SW_NEW_PIN_TOO_SHORT);
       userPin.update(buffer, (short) ISO7816.OFFSET_CDATA, (byte)LC);
   }

   private void unlock_pin(APDU apdu){
       if(puk_try_count >= PUK_TRY_LIMIT){
           ISOException.throwIt(SW_PUK_VERIFICATION_REJECTED);
       }
       byte[] buffer = apdu.getBuffer();
       apdu.setIncomingAndReceive();
       if (Util.arrayCompare(PUK, (short) 0, buffer, (short) (ISO7816.OFFSET_CDATA), (short) PUK_LEN) == 0) {
           userPin.resetAndUnblock();
           puk_try_count = (byte) 0;
           return;
       } else {
           puk_try_count = (byte)(puk_try_count + (byte)1);
           ISOException.throwIt(SW_PUK_VERIFICATION_FAILED);
       }
   }

   private void generate_random(byte[] buffer,byte len){
       rnd.generateData(buffer, (short) 0, (short) len);
   }

   private void send_puk(APDU apdu){
       apdu.setIncomingAndReceive();
       byte[] buffer = apdu.getBuffer();
       Util.arrayCopy(PUK,(short)0,buffer,(short)0,(short)PUK_LEN);
       apdu.setOutgoingAndSend((short) 0, (short) PUK_LEN);
   }

}
