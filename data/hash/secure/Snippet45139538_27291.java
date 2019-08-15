    import java.security.MessageDigest;
    import java.security.NoSuchAlgorithmException;
    import java.util.Base64;

    public class PackageCheckSumCalculationTEST
    {
        public static void main(String[] args) 
        {   
            String fileLocation =  "D:\\XXX\\YYYV1.iso";

            try
            {
                MessageDigest md = MessageDigest.getInstance("SHA-256");            
                md.reset();
                FileInputStream fis = new FileInputStream(fileLocation);

                byte[] dataBytes = new byte[1024];

                int nread = 0;
                while ((nread = fis.read(dataBytes)) != -1) {
                  md.update(dataBytes, 0, nread);
                };
                byte[] mdbytes = md.digest();

                String checkSum  = Base64.getEncoder().encodeToString(mdbytes);

                System.out.println(checkSum);
                md.reset();
            }
            catch (IOException | NoSuchAlgorithmException e)
            {
                e.printStackTrace();
            }
        }
    }
