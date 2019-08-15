package com.comviva.logic;

import java.io.IOException;
import javax.crypto.Cipher;
import org.apache.tomcat.util.IntrospectionUtils;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.comviva.util.Util;

    public class Decrypt implements IntrospectionUtils.PropertySource {

        static Log log = LogFactory.getLog(Decrypt.class);
        static int count =0;
        static String encrypted_password=null;
        private static byte[] key = {
            0x74, 0x68, 0x69, 0x73, 0x49, 0x73, 0x41, 0x53, 0x65, 0x63, 0x72, 0x65, 0x74, 0x4b, 0x65, 0x79
        };//"thisIsASecretKey";


        public static String decrypt(String strToDecrypt)
        {
            try
            {
                Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
                final SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
                cipher.init(Cipher.DECRYPT_MODE, secretKey);
                final String decryptedString = new String(cipher.doFinal(Base64.decodeBase64(strToDecrypt)));
                return decryptedString;
            }
            catch (Exception e)
            {
                log.error("Error while decrypting", e);
                return null;
            }

        }

        @Override
        public String getProperty(String arg0) {

            System.out.println("Count is " + count++);

            String fname=this.getClass().getClassLoader().getResource("").getFile();
            fname=fname.substring(0, fname.length()-4);
            String OS = System.getProperty("os.name").toLowerCase();
            if(OS.indexOf("win")>=0)
            {
                log.info("System Type is :"+OS);
                fname=fname.concat("conf\\catalina.properties\n");
                System.out.print("File Name is :"+fname);
            }
            else
            {
                log.info("System Type is :"+OS);
                fname=fname.concat("conf/catalina.properties\n");
                System.out.print("File Name is :"+fname);
            }
            log.info("File is read successfully");
            try {
                Util utility=new Util(fname);
                log.info("Hello");

                encrypted_password=utility.getValue(arg0);
                log.info("ecrypted password is :" + encrypted_password);
                System.out.println("ecrypted password is :" + encrypted_password);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return decrypt(encrypted_password);
        }

    }
