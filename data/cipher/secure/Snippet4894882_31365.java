public static void encryptDecryptFile(String srcFileName, String destFileName, Key key, int cipherMode) throws Exception
    {
        OutputStream outputWriter = null;
        InputStream inputReader = null;
        try
        {
            Cipher cipher = Cipher.getInstance("ElGamal/None/NoPadding", "BC"");
            String textLine = null;
    //buffer(my chunks) depends wether it is encyption or decryption
            byte[] buf = (cipherMode == Cipher.ENCRYPT_MODE? new byte[50] : new byte[64]);
            int bufl;
            // init the Cipher object for Encryption...
            cipher.init(cipherMode, key);

            // start FileIO
            outputWriter = new FileOutputStream(destFileName);
            inputReader = new FileInputStream(srcFileName);
            while ( (bufl = inputReader.read(buf)) != -1)
            {
                byte[] encText = null;
                if (cipherMode == Cipher.ENCRYPT_MODE)
                {
                      encText = encrypt(copyBytes(buf,bufl),(PublicKey)key);
                }
                else
                {
                    if (_log.isDebugEnabled())
                    {
                        System.out.println("buf = " + new String(buf));
                    }
                    encText = decrypt(copyBytes(buf,bufl),(PrivateKey)key);
                }
                outputWriter.write(encText);
                if (_log.isDebugEnabled())
                {
                    System.out.println("encText = " + new String(encText));
                }
            }
            outputWriter.flush();

        }
        catch (Exception e)
        {
            _log.error(e,e);
            throw e;
        }
        finally
        {
            try
            {
                if (outputWriter != null)
                {
                    outputWriter.close();
                }
                if (inputReader != null)
                {
                    inputReader.close();
                }
            }
            catch (Exception e)
            {
                // do nothing...
            } // end of inner try, catch (Exception)...
        }
    }
