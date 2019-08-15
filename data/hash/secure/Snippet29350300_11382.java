bitmap1 = BitmapFactory.decodeFile(picturePath1,opt);

ImageView view1 = (ImageView) findViewById(R.id.gambar5);
view1.setImageBitmap(bitmap1);

ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
bitmap1.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

byte[] data1 = outputStream.toByteArray();

MessageDigest md = MessageDigest.getInstance("SHA-256");
md.update(data1);

byte[] hash = md.digest();
String result = returnHex(hash);

EditText et1 = (EditText) findViewById(R.id.editText1);
et1.setText(result);
