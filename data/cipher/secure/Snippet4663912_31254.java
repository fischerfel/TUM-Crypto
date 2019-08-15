long start = System.currentTimeMillis()/1000L;
            try {
                SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivspec);

                 android.util.Log.d("TEST", "Start decoding...." + String.valueOf(length));

                byte[] decrypted = cipher.doFinal(content);

                File file2 = new File(Environment.getExternalStorageDirectory().getPath() + "/test.mp3");
                OutputStream os = new FileOutputStream(file2);
                os.write(decrypted);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            long end = System.currentTimeMillis()/1000L;

            android.util.Log.d("TEST","Time "+ String.valueOf(end-start));
