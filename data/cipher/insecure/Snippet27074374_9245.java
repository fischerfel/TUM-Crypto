    public class SecurityAES {
    private final static String encoding = "UTF-8"; 
    public static void main(String[] args) {
        String str = encryptAES("18382360986%2Cqq200600","uu24sfsd8sdggs");

        System.out.println(str);
        String uriAPI = "http://xxxxxx.com/do/httpapi!apiUserInfo.shtml";

        Map parameters=new HashMap();

        parameters.put("token", "uu24sfsd8sdggs");
        parameters.put("formVals", str);

        System.out.println(post(uriAPI, parameters, true));

    }
    public static String post(String urlStr, Map parameters, boolean flag) {
        try {
            String content = "";
            String result = "";
            URL url = null;
            URLConnection conn = null;
            OutputStreamWriter writer = null;
            StringBuffer params = new StringBuffer();

            for (Iterator iter = parameters.entrySet().iterator(); iter.hasNext();) {
                Entry element = (Entry) iter.next();
                params.append(element.getKey().toString());
                params.append("=");
                params.append(element.getValue().toString());
                params.append("&");
            }

            if (params.length() > 0) {
                params = params.deleteCharAt(params.length() - 1);
            }

            try {
                url = new URL(urlStr);
                conn = url.openConnection();
                conn.setDoOutput(true);
                conn.setRequestProperty("Referer", "");
                conn.setConnectTimeout(3000);// 设置连接主机超时（单位：毫秒）
                conn.setReadTimeout(3000);// 设置从主机读取数据超时（单位：毫秒）

                writer = new OutputStreamWriter(conn.getOutputStream());
                writer.write(params.toString());
                writer.flush();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (writer != null) {
                        writer.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            InputStreamReader reder = null;
            BufferedReader breader = null;
            try {
                reder = new InputStreamReader(conn.getInputStream(), "utf-8");
                breader = new BufferedReader(reder);
                while ((content = breader.readLine()) != null) {
                    result += content;
                }
            } catch (Exception e) {
            } finally {
                try {
                    if (reder != null) {
                        reder.close();
                    }
                    if (breader != null) {
                        breader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            if (result == null || result.equals("")) {
                result = "|";
            }
            System.out.println(result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }
    /**
     * 
     * @param content
     * @param password
     * @return
     */
    public static String encryptAES(String content, String password) {
        byte[] encryptResult = encrypt(content, password);
        String encryptResultStr = parseByte2HexStr(encryptResult);
        encryptResultStr = ebotongEncrypto(encryptResultStr);
        return encryptResultStr;
    }

    /**
     * 
     * @param encryptResultStr
     * @param password
     * @return
     */
    public static String decrypt(String encryptResultStr, String password) {
        String decrpt = ebotongDecrypto(encryptResultStr);
        byte[] decryptFrom = parseHexStr2Byte(decrpt);
        byte[] decryptResult = decrypt(decryptFrom, password);
        return new String(decryptResult);
    }

    /**
     */
    public static String ebotongEncrypto(String str) {
        BASE64Encoder base64encoder = new BASE64Encoder();
        String result = str;
        if (str != null && str.length() > 0) {
            try {
                byte[] encodeByte = str.getBytes(encoding);
                result = base64encoder.encode(encodeByte);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result.replaceAll("\r\n", "").replaceAll("\r", "").replaceAll("\n", "");
    }

    /**
     */
    public static String ebotongDecrypto(String str) {
        BASE64Decoder base64decoder = new BASE64Decoder();
        try {
            byte[] encodeByte = base64decoder.decodeBuffer(str);
            return new String(encodeByte);
        } catch (IOException e) {
            e.printStackTrace();
            return str;
        }
    }
    /**  
     *   
     * @return  
     */  
    private static byte[] encrypt(String content, String password) {   
            try {              
                    KeyGenerator kgen = KeyGenerator.getInstance("AES"); 
                    SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG" );   
                    secureRandom.setSeed(password.getBytes());   
                    kgen.init(128, secureRandom);
                    //kgen.init(128, new SecureRandom(password.getBytes()));   
                    SecretKey secretKey = kgen.generateKey();   
                    byte[] enCodeFormat = secretKey.getEncoded();   
                    SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");   
                    Cipher cipher = Cipher.getInstance("AES");// 
                    byte[] byteContent = content.getBytes("utf-8");   
                    cipher.init(Cipher.ENCRYPT_MODE, key);
                    byte[] result = cipher.doFinal(byteContent);   
                    return result; 
            } catch (NoSuchAlgorithmException e) {   
                    e.printStackTrace();   
            } catch (NoSuchPaddingException e) {   
                    e.printStackTrace();   
            } catch (InvalidKeyException e) {   
                    e.printStackTrace();   
            } catch (UnsupportedEncodingException e) {   
                    e.printStackTrace();   
            } catch (IllegalBlockSizeException e) {   
                    e.printStackTrace();   
            } catch (BadPaddingException e) {   
                    e.printStackTrace();   
            }   
            return null;   
    }  


    private static byte[] decrypt(byte[] content, String password) {   
            try {   
                     KeyGenerator kgen = KeyGenerator.getInstance("AES"); 

                     SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG" );   
                     secureRandom.setSeed(password.getBytes());   
                     kgen.init(128, secureRandom);
                     //kgen.init(128, new SecureRandom(password.getBytes()));   
                     SecretKey secretKey = kgen.generateKey();   
                     byte[] enCodeFormat = secretKey.getEncoded();   
                     SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");               
                     Cipher cipher = Cipher.getInstance("AES"); 
                    cipher.init(Cipher.DECRYPT_MODE, key);  
                    byte[] result = cipher.doFinal(content);   
                    return result; //
            } catch (NoSuchAlgorithmException e) {   
            } catch (NoSuchPaddingException e) {   
            } catch (InvalidKeyException e) {   
            } catch (IllegalBlockSizeException e) {   
            } catch (BadPaddingException e) {   
            }   
            return null;   
    }  

    public static String parseByte2HexStr(byte buf[]) {   
            StringBuffer sb = new StringBuffer();   
            for (int i = 0; i < buf.length; i++) {   
                    String hex = Integer.toHexString(buf[i] & 0xFF);   
                    if (hex.length() == 1) {   
                            hex = '0' + hex;   
                    }   
                    sb.append(hex.toUpperCase());   
            }   
            return sb.toString();   
    }  

    public static byte[] parseHexStr2Byte(String hexStr) {   
            if (hexStr.length() < 1)   
                    return null;   
            byte[] result = new byte[hexStr.length()/2];   
            for (int i = 0;i< hexStr.length()/2; i++) {   
                    int high = Integer.parseInt(hexStr.substring(i*2, i*2+1), 16);   
                    int low = Integer.parseInt(hexStr.substring(i*2+1, i*2+2), 16);   
                    result[i] = (byte) (high * 16 + low);   
            }   
            return result;   
    }  
}
