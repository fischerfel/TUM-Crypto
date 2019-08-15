    private static String encryptionKey="ERVwiiYMFlDcZ0wp";
    private static String decryptionKey="ERVwiiYMFlDcZ0wp";
    JSONObject resultJSON=new JSONObject();
    private static byte[] initializationVector = new byte[] { 0x01, 0x02, 0x03,
        0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e,
        0x0f, 0x10 };

    public JSONObject encryptMethod(JSONObject jObject)
    {
        try {
            textToEncrypt = jObject.getString("text");      
            textToEncrypt = replaceSlashes(textToEncrypt);
            Log.d("STATE", "textToEncrypt: "+textToEncrypt);
            try {
                cipherText = encryptData(textToEncrypt);
        Log.d("STATE", "cipherText: "+cipherText);
                resultType=true;
                resultData=cipherText;
                resultMessage="Encryption success";
            }
            catch(Exception e)
            {
                e.printStackTrace();
                resultMessage="Encryption is Not Possible, Please try some other text";
            }

        }
        catch(Exception e)
        {
            e.printStackTrace();
            resultMessage="Wrong Text Input, Please try again";
        }
        try {
            resultJSON.put("type", resultType);
            resultJSON.put("message", resultMessage);
            resultJSON.put("data", resultData);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return resultJSON;      
    }

  public static String encryptData(String plainText) {


        String cipherText = null;
        byte[] encryptedBytes = null;
        String skeySpecString = null;

        if (plainText == null || "".equals(plainText)) {
            return null;
        }

        try {       
            if(encryptionKey==null || encryptionKey.equals("")) {
                return null;
            }
            SecretKeySpec skeySpec = new SecretKeySpec(encryptionKey.getBytes(), "AES");
            IvParameterSpec ivps = new IvParameterSpec(initializationVector);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivps);
            encryptedBytes = cipher.doFinal(plainText.getBytes());
            encryptedBytes = Base64.encode(encryptedBytes, 0);
            cipherText = new String(encryptedBytes);

        } catch (BadPaddingException bpe) {
            Log.e("Plain", "<===== BadPaddingException =====>", bpe);
            return null;
        } catch (IllegalBlockSizeException ibse) {
            Log.e("Plain", "<===== BadPaddingException =====>", ibse);
            return null;
        } catch (InvalidAlgorithmParameterException iape) {
            Log.e("Plain", "<===== InvalidAlgorithmParameterException =====>",
                    iape);
            return null;
        } catch (InvalidKeyException ike) {
            Log.e("Plain", "<===== InvalidKeyException =====>", ike);
            return null;
        } catch (NoSuchAlgorithmException nae) {
            Log.e("Plain", "<===== NoSuchAlgorithmException =====>", nae);
            return null;
        } catch (NoSuchPaddingException nspe) {
            Log.e("Plain", "<===== NoSuchPaddingException =====>", nspe);
            return null;
        } catch (Exception ex) {
            Log.e("Plain", "<===== Exception: {0} =====>", ex);
            return null;
        }
        return cipherText;
    }
