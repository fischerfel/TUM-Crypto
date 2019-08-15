String name = "potato";

MessageDigest md = MessageDigest.getInstance("SHA-1");
md.update(name.getBytes("iso-8859-1"), 0 , name.getBytes( "iso-8859-1").length );
Bytes[] sha1hash = md.digest();

textview.setText(sha1hash.toString());
