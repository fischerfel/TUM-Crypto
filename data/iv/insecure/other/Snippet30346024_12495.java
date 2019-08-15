public String encode(String text)
            throws NoPassGivenException, NoTextGivenException {

        if (text.length() == 0 || text == null) {
            throw new NoTextGivenException("Please give text");
        }

        try {
            SecretKeySpec skeySpec = getKey(KEY);

            System.out.println("-----Encoding Key-----"+skeySpec);
            byte[] clearText = text.getBytes("UTF8");

            //IMPORTANT TO GET SAME RESULTS ON iOS and ANDROID
            final byte[] iv = new byte[16];
            Arrays.fill(iv, (byte) 0x00);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

            // Cipher is not thread safe
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivParameterSpec);

//          IvParameterSpec ivspec = new IvParameterSpec(IV.getBytes());
//          cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivspec);

            String encrypedValue = Base64.encodeToString(
                    cipher.doFinal(clearText), Base64.DEFAULT);
            return new String(encrypedValue);

        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        return "";
    }

/**
     * Generates a SecretKeySpec for given password
     * @param password
     * @return SecretKeySpec
     * @throws UnsupportedEncodingException
     */
    public SecretKeySpec getKey(String password)
            throws UnsupportedEncodingException {


        int keyLength = 128;
        byte[] keyBytes = new byte[keyLength / 8];
        // explicitly fill with zeros
        Arrays.fill(keyBytes, (byte) 0x0);

        // if password is shorter then key length, it will be zero-padded
        // to key length
        byte[] passwordBytes = password.getBytes("UTF-8");
        int length = passwordBytes.length < keyBytes.length ?          passwordBytes.length
                : keyBytes.length;
        System.arraycopy(passwordBytes, 0, keyBytes, 0, length);
        SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
        return key;
    }
 .// PHP Code to decrypt
    public function decrypt($code) { 

      $decoded = base64_decode($code);
      $iv = mcrypt_create_iv(mcrypt_get_iv_size(MCRYPT_RIJNDAEL_128, MCRYPT_MODE_ECB), MCRYPT_RAND);
      $decrypted = trim(mcrypt_decrypt(MCRYPT_RIJNDAEL_128, $this->key, trim($decoded), MCRYPT_MODE_ECB, $iv));
      $blocksize = mcrypt_get_block_size(MCRYPT_RIJNDAEL_128, MCRYPT_MODE_CBC);

     return  $this->pkcs7unpad($decrypted,$blocksize);
    }
 function pkcs7unpad($padded, $blocksize)
    {
        $l = strlen($padded);

        if ($l % $blocksize != 0) 
        {
            throw new Exception("Padded plaintext cannot be divided by the block size");
        }

        $padsize = ord($padded[$l - 1]);

        if ($padsize === 0)
        {
            throw new Exception("Zero padding found instead of PKCS#7 padding");
        }    

        if ($padsize > $blocksize)
        {
            throw new Exception("Incorrect amount of PKCS#7 padding for blocksize");
        }

        // check the correctness of the padding bytes by counting the occurance
        $padding = substr($padded, -1 * $padsize);
        if (substr_count($padding, chr($padsize)) != $padsize)
        {
            throw new Exception("Invalid PKCS#7 padding encountered");
        }

        return substr($padded, 0, $l - $padsize);
    }
