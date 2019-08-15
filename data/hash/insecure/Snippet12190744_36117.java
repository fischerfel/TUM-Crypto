public class AmazonS3Activity extends Activity 
{
private String Tag = "Downlaod";

Button btnDownload;

Context myContext;

String product_code = "pro_code";

String bucketName = "bucketnamw";

String appDirPath = Environment.getExternalStorageDirectory().getName() +"/foldername/";

/** Called when the activity is first created. */
@Override
public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    myContext = this;

    btnDownload = (Button) findViewById(R.id.btnDownload);      
}

public void Download(View button)
{
    try 
    {
        String imgName = product_code+"1.jpg";
        String md5Name = getMD5HasCode(product_code);

        String productCode = md5Name+".app/";

        String filePath = getFullFilePath(imgName);

        String accessKey = "accessKey";
        String secretKey = "secretKey";
        AWSCredentials credential = new BasicAWSCredentials(accessKey, secretKey);

        Log.e(Tag, "FileName : " + filePath, null);

        String strObjectKey = productCode + imgName.replace(product_code,md5Name);

        Log.e(Tag, "Object Key : " + strObjectKey, null);

        GetObjectRequest objRequest = new GetObjectRequest(bucketName,strObjectKey);

        AmazonS3Client myS3Client = new AmazonS3Client(credential);

        S3Object s3Object = myS3Client.getObject(objRequest);

        long fileSize = s3Object.getObjectMetadata().getContentLength();
        final InputStream input = s3Object.getObjectContent();
        final FileOutputStream fos = new FileOutputStream(filePath);

        long total = 0;
        int len=0;

        byte[] buf = new byte[1024];

        while((len = input.read(buf)) > 0)
        {
            fos.write(buf,0,len);

            total = total + len;
        }

        if(len == -1 && total==fileSize)
        {
            Thread.currentThread().interrupt();

            input.close();
            fos.close();

        }

    } 
    catch (Exception e) 
    {
        e.printStackTrace();
        Log.e(Tag, "Error Key : " + e.getMessage(), null);
    }

}

public String getMD5HasCode(String strMessage)
{
    try
    {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        byte[] messageDigestBytes = messageDigest.digest(strMessage.getBytes());
        BigInteger hasNumber = new BigInteger(1,messageDigestBytes);
        String md5 = hasNumber.toString(16);

        while(md5.length()<32)
        {
            md5 = "0" + md5;
        }

        return md5;
    }
    catch (Exception ex) 
    {
        Toast.makeText(myContext, "ERROR in HasCode : "+ex.toString(), Toast.LENGTH_SHORT).show();  
        return null;
    }
}

public String getFullFilePath(String filename)
{
    File dir = new File(appDirPath);
    if(!dir.exists())
    {
        dir.mkdir();
    }
    return appDirPath+filename;
}
 }
