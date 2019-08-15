Charset utf8Charset = Charset.forName("UTF-8");

byte[] bytesOfPhrase = phrase.getBytes(utf8Charset);
MessageDigest md = MessageDigest.getInstance("MD5");

byte[] thedigest = md.digest(bytesOfPhrase);
this._AuthenticationToken = new String(thedigest, utf8Charset);
