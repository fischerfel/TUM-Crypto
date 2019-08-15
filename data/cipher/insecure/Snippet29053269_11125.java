Protected String ScrambledPic(String ImageNow, String key)
{
try{  

        File ImageOld = new File(Environment.getExternalStorageDirectory(), ImageNow);
        FileInputStream file = new FileInputStream(ImageOld);       
        Bitmap bm=null;
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        File NewPhoto = new File(Environment.getExternalStorageDirectory()
               + File.separator + "picEnc.jpg");
        NewPhoto.createNewFile();
        FileOutputStream outStream = new FileOutputStream(NewPhoto);
        byte k[]= key.getBytes();
        SecretKeySpec KEYY=new SecretKeySpec(k, "DES");
        Cipher enc = Cipher.getInstance("DES");
        enc.init(Cipher.ENCRYPT_MODE, KEYY);
        CipherOutputStream cos = new CipherOutputStream(outStream,enc);
        byte[] buffer = new byte[1024];
        int read;
        while((read=file.read(buffer))!=-1){
            cos.write(buffer, 0, read);
        }
        file.close();
        outStream.flush();
        cos.close();
        Toast.makeText(getBaseContext(), "Photo Encrypted", Toast.LENGTH_LONG).show();
        NewImageUri = Uri.fromFile(NewPhoto);
        ImageView scrambled = (ImageView)findViewById(R.id.scrambled);

        return NewImageUri.toString();

    }

catch(FileNotFoundException e) {
Toast.makeText(getBaseContext(), "File Not Found", Toast.LENGTH_LONG).show();
return null;
}
    catch (IOException e){
        Toast.makeText(getBaseContext(), "IOException", Toast.LENGTH_LONG).show();
        return ImageNow;
    }
    catch (NoSuchPaddingException e){
        Toast.makeText(getBaseContext(), "Padding Error", Toast.LENGTH_LONG).show();
        return ImageNow;
    }
    catch (NoSuchAlgorithmException e){
        Toast.makeText(getBaseContext(), "No Such Algorithm", Toast.LENGTH_LONG).show();
        return ImageNow;
    }
    catch (InvalidKeyException e){
        Toast.makeText(getBaseContext(), "Invalid Key", Toast.LENGTH_LONG).show();
        return ImageNow;
    }
}
