public static class TokenizerMapper extends Mapper<Object, Text, IntWritable, Text>
{
    private Text one = new Text();
    private static IntWritable word = new IntWritable(1);    
    public void map(Object key, Text value, Context context) throws IOException, InterruptedException
    {
        String plainText = value.toString();

        int leng =  plainText.length();
        int num = leng / 128;
        int i = 0;
        String[] arr;
        List<String> list = new ArrayList<String>();
        while (i<num)
        {
            list.add(plainText.substring(num*i, num*(i+1)-1));
             i++;
        }   
        Iterator iterator = list.iterator();
        while (iterator.hasNext()) 
        {
            try{
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(128);
            SecretKey secretKey = keyGenerator.generateKey();
            cipher = Cipher.getInstance("AES");
            ///////////////////////////////////
            String blocktext = (String) iterator.next();
            byte[] plainTextByte = blocktext.getBytes();
            /////////////////////////////////// 
            //byte[] plainTextByte = plainText.getBytes();
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedByte = cipher.doFinal(plainTextByte);
            Base64.Encoder encoder = Base64.getEncoder();
            String encryptedText = encoder.encodeToString(encryptedByte);
            one.set(encryptedText);
                context.write(word, one);
            }
            catch (Exception e){System.out.println(e);}
        }   
    }
}//end of TokenizerMapper class
