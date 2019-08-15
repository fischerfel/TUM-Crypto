public class Enc {

public static class Map extends Mapper<LongWritable, Text, Text, IntWritable> {
private Text word = new Text();
public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
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
    } 

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();

        Job job = new Job(conf, "Enc");
        job.setJarByClass(Enc.class);

        job.setOutputKeyClass(LongWritable.class);
        job.setOutputValueClass(Text.class);

        job.setMapperClass(Map.class);

        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        job.waitForCompletion(true);
    }        
}
