byte data[] = new byte[1024];

            String seed = "password";

            byte[] rawKey = getRawKey(seed.getBytes());
            SecretKeySpec skeySpec = new SecretKeySpec(rawKey, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

            output = new CipherOutputStream(output, cipher);


            long total = 0;

            while ((count = input.read(data)) != -1) {
                total += count;
                publishProgress("" + (int) ((total * 100) / lenghtOfFile));


                output.write(data, 0, count);

            }
