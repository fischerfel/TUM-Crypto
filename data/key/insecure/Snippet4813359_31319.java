String strkey = "My key goes here";
SecretKeySpec key = new SecretKeySpec(strkey.getBytes("UTF-8"), "Blowfish");
