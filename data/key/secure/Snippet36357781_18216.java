String mAesKey_string;
SecretKeySpec mAesKey= new SecretKeySpec(secretKey.getEncoded(), "AES");

//SecretKeySpec to String 
    byte[] byteaes=mAesKey.getEncoded();
    mAesKey_string=Base64.encodeToString(byteaes,Base64.NO_WRAP);

//String to SecretKeySpec
    byte[] aesByte = Base64.decode(mAesKey_string, Base64.NO_WRAP);
    mAesKey= new SecretKeySpec(aesByte, "AES");
