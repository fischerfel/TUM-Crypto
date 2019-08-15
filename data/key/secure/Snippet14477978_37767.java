    private RandomAccessFile in;
    private RandomAccessFile out;

    private Cipher cipher;
    private Mac hmac;
    private IvParameterSpec ivSpec2;
    private SecretKeySpec aesKey2;

    private Integer num_blocks;
    private Integer offset_read;
    private Integer offset_write;

    private AESCrypt parent;

    public HiloCrypt(String input, String output, IvParameterSpec ivSpec, SecretKeySpec aesKey, Integer offset_thread, Integer offset_write, Integer blocks, AESCrypt parent2) 
    {
        try
        {
                        // If i don't use RandomAccessFile there is a problem copying data
            this.in = new RandomAccessFile(input, "r");
            this.out = new RandomAccessFile(output, "rw");

            int total_offset_write = offset_write + offset_thread;

                        // Adjust the offset for reading and writing 
            this.out.seek(total_offset_write);
            this.in.seek(offset_thread);

            this.ivSpec2 = ivSpec;
            this.aesKey2 = aesKey;

            this.cipher = Cipher.getInstance(AESCrypt.CRYPT_TRANS);
            this.hmac = Mac.getInstance(AESCrypt.HMAC_ALG);

            this.num_blocks = blocks;
            this.offset_read = offset_thread;
            this.offset_write = total_offset_write;
            this.parent = parent2;

        } catch (Exception e)
        {
            System.err.println(e);
            return;
        }
    }


    public void run()
        {
        int len, last,block_counter,total = 0;
        byte[] text = new byte[AESCrypt.BLOCK_SIZE];

        try{
            // Start encryption objects
            this.cipher.init(Cipher.ENCRYPT_MODE, this.aesKey2, this.ivSpec2);
            this.hmac.init(new SecretKeySpec(this.aesKey2.getEncoded(), AESCrypt.HMAC_ALG));

            while ((len = this.in.read(text)) > 0 && block_counter < this.num_blocks) 
            {
                this.cipher.update(text, 0, AESCrypt.BLOCK_SIZE, text);
                this.hmac.update(text);

                // Write the block
                this.out.write(text);

                last = len;
                total+=len;

                block_counter++;
            }

            if (len < 0) // If it's the last block, calculate the HMAC
            {
                last &= 0x0f;
                this.out.write(last);

                this.out.seek(this.offset_write-this.offset_read);

                while ((len = this.out.read(text)) > 0) 
                {
                    this.hmac.update(text);
                }

                // write last block of HMAC
                text=this.hmac.doFinal();
                this.out.write(text);
            }

                        // Close streams
            this.in.close();
            this.out.close();

                        // Code to notify the end of the thread
        }
        catch(Exception e)
        {
            System.err.println("Hola!");
            System.err.println(e);
        }
    }
}
