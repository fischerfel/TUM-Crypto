import java.io.IOException;
import java.util.*;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.Cipher;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.InvalidKeyException;
import java.security.InvalidAlgorithmParameterException;

import javax.crypto.NoSuchPaddingException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

import sun.misc.BASE64Encoder;

public class Enc {

      public static class Map extends Mapper<LongWritable, Text, LongWritable, Text> {
        private Text word = new Text();
        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

            try {
            String strDataToEncrypt = new String();
            String strCipherText = new String();

            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(128);
            SecretKey secretKey = keyGen.generateKey();

            Cipher aesCipher = Cipher.getInstance("AES");
            aesCipher.init(Cipher.ENCRYPT_MODE,secretKey);
            strDataToEncrypt = value.toString();

            byte[] byteDataToEncrypt = strDataToEncrypt.getBytes();
            byte[] byteCipherText = aesCipher.doFinal(byteDataToEncrypt); 
            strCipherText = new BASE64Encoder().encode(byteCipherText);
            System.out.println("cipher text: " +strCipherText);

            String cipherString =  new String(strCipherText);
            context.write(key, new Text(cipherString));
            }
            catch (NoSuchAlgorithmException noSuchAlgo)
            {
                System.out.println(" No Such Algorithm exists " + noSuchAlgo);
            }

                catch (NoSuchPaddingException noSuchPad)
                {
                    System.out.println(" No Such Padding exists " + noSuchPad);
                }

                    catch (InvalidKeyException invalidKey)
                    {
                        System.out.println(" Invalid Key " + invalidKey);
                    }

                    catch (BadPaddingException badPadding)
                    {
                        System.out.println(" Bad Padding " + badPadding);
                    }

                    catch (IllegalBlockSizeException illegalBlockSize)
                    {
                        System.out.println(" Illegal Block Size " + illegalBlockSize);
                    }


        }
    } 


    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();

        Job job = new Job(conf, "Enc");
        job.setJarByClass(Enc.class);

        job.setOutputKeyClass(LongWritable.class);
        job.setOutputValueClass(Text.class);

        job.setMapperClass(Map.class);
        //job.setCombinerClass(Reduce.class);
        //job.setReducerClass(Reduce.class);


        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        job.waitForCompletion(true);
    }        
}
