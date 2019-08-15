public class HadoopIndexProject {

    private static SecretKey generateKey(int size, String Algorithm) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance(Algorithm);
        keyGen.init(size);
        return keyGen.generateKey();
    }

    private static IvParameterSpec generateIV() {
        byte[] b = new byte[16];
        new Random().nextBytes(b);
        return new IvParameterSpec(b);    
    }

    public static void saveKey(SecretKey key, IvParameterSpec IV, String path) throws IOException {
        FileOutputStream stream = new FileOutputStream(path);
        //FSDataOutputStream stream = fs.create(new Path(path));
        try {
            stream.write(key.getEncoded());
            stream.write(IV.getIV());
        } finally {
            stream.close();
        }
    }

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {
        // TODO code application logic here
        Configuration conf = new Configuration();
        //FileSystem fs = FileSystem.getLocal(conf);
        String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
        SecretKey KEY;
        IvParameterSpec IV;
        if (otherArgs.length != 2) {
            System.err.println("Usage: Index <in> <out>");
            System.exit(2);
        }
        try {
            if(! new File("key.dat").exists()) {
                KEY = generateKey(128, "AES");
                IV = generateIV();
                saveKey(KEY, IV, "key.dat");
            }
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(HadoopIndexMapper.class.getName()).log(Level.SEVERE, null, ex);
        }
        conf.set("mapred.textoutputformat.separator", ":");

        Job job = Job.getInstance(conf);
        job.setJobName("Index creator");
        job.setJarByClass(HadoopIndexProject.class);      
        job.setMapperClass(HadoopIndexMapper.class);
        job.setReducerClass(HadoopIndexReducer.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntArrayWritable.class);

        FileInputFormat.addInputPath(job, new Path(otherArgs[0]) {});
        FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }

}
