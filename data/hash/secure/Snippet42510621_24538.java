MessageDigest md = MessageDigest.getInstance(MessageDigest.ALG_SHA, false);
md.reset();
md.doFinal(toSign, bOffset, bLength, tempBuffer, (short) 0);`
