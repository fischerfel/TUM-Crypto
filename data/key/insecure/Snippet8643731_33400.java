File bufferFile = new File(ids.get(position));
        FileInputStream fis   = new FileInputStream(bufferFile);

        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        SecretKeySpec keySpec = new SecretKeySpec("01234567890abcde".getBytes(), "AES");
        IvParameterSpec ivSpec = new IvParameterSpec("fedcba9876543210".getBytes());
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        CipherInputStream cis = new CipherInputStream(fis, cipher);


        Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeStream(cis, null, options);

        Boolean scaleByHeight = Math.abs(options.outHeight - Cards.height) >= Math.abs(options.outWidth - Cards.width);

        if(options.outHeight * options.outWidth * 2 >= 200*200*2){
               // Load, scaling to smallest power of 2 that'll get it <= desired dimensions
              double sampleSize = scaleByHeight
                    ? options.outHeight / Cards.height
                    : options.outWidth / Cards.width;
              options.inSampleSize = 
                    (int)Math.pow(2d, Math.floor(
                    Math.log(sampleSize)/Math.log(2d)));
           }

              // Do the actual decoding
              options.inJustDecodeBounds = false;

              //cis.close();
              cis = new CipherInputStream(fis, cipher);
              Bitmap img = BitmapFactory.decodeStream(cis, null, options);
              cis.close();


       ((ImageView) convertView.findViewById(R.id.imgView)).setImageBitmap(img);

        fis.close();


        Runtime.getRuntime().gc();
