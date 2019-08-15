Bitmap bmp = new Bitmap(); // load your bitmap...
ByteArrayOutputStream stream = new ByteArrayOutputStream();
bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
byte[] byteArray = stream.toByteArray();

MessageDigest digest = MessageDigest.getInstance("SHA-256");  
digest.update(byteArray);
byte[] keyBytes = digest.digest(byteArray);
