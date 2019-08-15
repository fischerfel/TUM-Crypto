    public class AESCrypto
        {
            /// <summary>
            /// Encrpyts the sourceString, returns this result as an Aes encrpyted, BASE64 encoded string
            /// </summary>
            /// <param name="plainSourceStringToEncrypt">a plain, Framework string (ASCII, null terminated)</param>
            /// <param name="passPhrase">The pass phrase.</param>
            /// <returns>
            /// returns an Aes encrypted, BASE64 encoded string
            /// </returns>
            public static string EncryptString(string plainSourceStringToEncrypt, string passPhrase)
            {
                //Set up the encryption objects
                using (AesCryptoServiceProvider acsp = GetProvider(Encoding.Default.GetBytes(passPhrase)))
                {
                    byte[] sourceBytes = Encoding.ASCII.GetBytes(plainSourceStringToEncrypt);
                    ICryptoTransform ictE = acsp.CreateEncryptor();

                    //Set up stream to contain the encryption
                    MemoryStream msS = new MemoryStream();

                    //Perform the encrpytion, storing output into the stream
                    CryptoStream csS = new CryptoStream(msS, ictE, CryptoStreamMode.Write);
                    csS.Write(sourceBytes, 0, sourceBytes.Length);
                    csS.FlushFinalBlock();

                    //sourceBytes are now encrypted as an array of secure bytes
                    byte[] encryptedBytes = msS.ToArray(); //.ToArray() is important, don't mess with the buffer

                    //return the encrypted bytes as a BASE64 encoded string
                    return Convert.ToBase64String(encryptedBytes);
                }
            }


            /// <summary>
            /// Decrypts a BASE64 encoded string of encrypted data, returns a plain string
            /// </summary>
            /// <param name="base64StringToDecrypt">an Aes encrypted AND base64 encoded string</param>
            /// <param name="passphrase">The passphrase.</param>
            /// <returns>returns a plain string</returns>
            public static string DecryptString(string base64StringToDecrypt, string passphrase)
            {
                //Set up the encryption objects
                using (AesCryptoServiceProvider acsp = GetProvider(Encoding.Default.GetBytes(passphrase)))
                {
                    byte[] RawBytes = Convert.FromBase64String(base64StringToDecrypt);
                    ICryptoTransform ictD = acsp.CreateDecryptor();

                    //RawBytes now contains original byte array, still in Encrypted state

                    //Decrypt into stream
                    MemoryStream msD = new MemoryStream(RawBytes, 0, RawBytes.Length);
                    CryptoStream csD = new CryptoStream(msD, ictD, CryptoStreamMode.Read);
                    //csD now contains original byte array, fully decrypted

                    //return the content of msD as a regular string
                    return (new StreamReader(csD)).ReadToEnd();
                }
            }

            private static AesCryptoServiceProvider GetProvider(byte[] key)
            {
                AesCryptoServiceProvider result = new AesCryptoServiceProvider();
                result.BlockSize = 128;
                result.KeySize = 128;
                result.Mode = CipherMode.CBC;
                result.Padding = PaddingMode.PKCS7;

                result.GenerateIV();
                result.IV = new byte[] {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};

                byte[] RealKey = GetKey(key, result);
                result.Key = RealKey;
               // result.IV = RealKey;
                return result;
            }

            private static byte[] GetKey(byte[] suggestedKey, SymmetricAlgorithm p)
            {
                byte[] kRaw = suggestedKey;
                List<byte> kList = new List<byte>();

                for (int i = 0; i < p.LegalKeySizes[0].MinSize; i += 8)
                {
                    kList.Add(kRaw[(i / 8) % kRaw.Length]);
                }
                byte[] k = kList.ToArray();
                return k;
            }   
        }

**JAVA -Andorid**

public class AESHelper {

    /*This method use to encript the sting  */
    public static String encrypt(String inputString) throws Exception {
        byte[] result = encrypt(inputString.getBytes());
        String result1 = Base64.encodeToString(result,
                Base64.NO_WRAP);//Instead of Default
        return result1;
    }

    public static String decrypt(String encrypted) throws Exception {
        byte[] data = Base64.decode(encrypted, Base64.NO_WRAP);
        byte[] result = decrypt(data);
        String original = new String(result, "UTF-8");

        return new String(original);
    }

    /*This method return encrypted text with byte code*/
    private static byte[] encrypt( byte[] message) throws Exception {
        byte[] KEY = ConstantData.key.getBytes();//Key for encryption

        SecretKeySpec skeySpec = new SecretKeySpec(KEY, "AES");
        byte[] ivx =  new byte[] {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};//make same as web team
        IvParameterSpec ivSpec = new IvParameterSpec(ivx);
        Cipher ecipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        ecipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivSpec);

        byte[] encrypted = ecipher.doFinal(message);
        return encrypted;
    }

    private static byte[] decrypt(byte[] encrypted) throws Exception {
        byte[] KEY = ConstantData.key.getBytes();//Key for decrypt

        SecretKeySpec skeySpec = new SecretKeySpec(KEY, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        byte[] ivx =  new byte[] {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};//make same as web team
        IvParameterSpec ivSpec = new IvParameterSpec(ivx);

        cipher.init(Cipher.DECRYPT_MODE, skeySpec,ivSpec);
        byte[] decrypted = cipher.doFinal(encrypted);
        return decrypted;
    }
