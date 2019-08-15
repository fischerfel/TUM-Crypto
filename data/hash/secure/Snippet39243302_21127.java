RSAPrivateKey thePrivateKey = (RSAPrivateKey) KeyBuilder.buildKey(KeyBuilder.TYPE_RSA_PRIVATE, KeyBuilder.LENGTH_RSA_1024, NO_EXTERNAL_ACCESS);
    RSAPublicKey thePublickKey = (RSAPublicKey) KeyBuilder.buildKey(KeyBuilder.TYPE_RSA_PUBLIC, KeyBuilder.LENGTH_RSA_1024, NO_EXTERNAL_ACCESS);

    public void  generatesignature(APDU apdu)
   {

    if(!Pin.isValidated())
          ISOException.throwIt (ISO7816.SW_SECURITY_STATUS_NOT_SATISFIED);


    byte[] buffer=apdu.getBuffer();


    // data field of the command APDU
    short numdata=(short) buffer[ISO7816.OFFSET_LC];

    byte p1=(byte)buffer[ISO7816.OFFSET_P1];

    thePrivateKey=(RSAPrivateKey)PrivateKeyArray[p1];
    thePublickKey=(RSAPublicKey)PublicKeyArray[p1];


    // receive data starting from the offset
    // ISO.OFFSET_CDATA
    short inputlength= (short) apdu.setIncomingAndReceive();

     // it is an error if the number of data bytes
     // read does not match the number in Lc byte
    if (inputlength == 0)
       ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);

    try
    {         
    //convert input to hash

    MessageDigest digest=MessageDigest.getInstance(MessageDigest.ALG_SHA,false );

  short hashlength=digest.doFinal(buffer,ISO7816.OFFSET_CDATA,numdata,Input_Hash,(short)0);


    Signature    signature=Signature.getInstance(Signature.ALG_RSA_SHA_PKCS1,false);
    signature.init(thePrivateKey,Signature.MODE_SIGN);

     short hashlength=signature.sign(Input_Hash,(short)0,hashlength,Input_Sign, (short)0);



      Util.arrayCopy(Input_Sign,(short)0, buffer, (short)0, signLength);  
      apdu.setOutgoingAndSend((short)0, ((short)signLength));


    }
    catch (CryptoException c) {
     short reason = c.getReason();
     ISOException.throwIt(reason);       // for check
     }  
  }   
