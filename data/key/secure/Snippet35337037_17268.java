package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Hex;
import org.aspectj.weaver.patterns.ThrowsPattern;

import com.amazonaws.auth.policy.Policy;
import com.amazonaws.util.Base64;
import com.fasterxml.jackson.core.JsonEncoding;

/**
 * Servlet implementation class UploadCon2
 */
@WebServlet("/UploadCon2")
@MultipartConfig
public class UploadCon2 extends HttpServlet {
    private static final long serialVersionUID = 1L;
//  private static final String JSON = null;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public UploadCon2() {
        super();
        // TODO Auto-generated constructor stub
    }

        /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        String bucketName = "thehexa4";
        String access_key_id = "myaccesskey";
        String secret_access_key = "secretaccesskey";


        String policy_doc =
                  "{\"expiration\": \"2016-12-30T12:00:00.000Z\"," +
                    "\"conditions\": [" +
                      "{\"bucket\": \"thehexa4\"}," +
                      "[\"starts-with\", \"$key\", \"\"]," +
                      "{\"acl\": \"public-read\"}," +
                      "{\"success_action_redirect\": \"http://localhost/\"}," +
                      "[\"starts-with\", \"$Content-Type\", \"\"]," +

                      "[\"content-length-range\", 0, 10485760]" +
                      "{\"x-amz-credential\": \"myaccesskey/20160211/us-east-1/s3/aws4_request\"}," +
                      "{\"x-amz-algorithm\": \"AWS4-HMAC-SHA256\"}," +
                      "{\"x-amz-date\": \"20160211T000000Z\" }" +
                    "]" +
                  "}";
        System.out.print(policy_doc);
        // Calculate policy and signature values from the given policy document and AWS credentials.
        String policy = new String(Base64.encode(policy_doc.getBytes("UTF-8")), "ASCII");

        //HMAC SHA 256 calculation
        String signingkey = "";
        String signature = "";
        try {
            signingkey = getSignatureKey(secret_access_key,"20160211" , "us-east-1", "s3").toString();
            //signature = Base64.encodeAsString((HmacSHA256(signingkey, policy.getBytes())));
            signature = Hex.encodeHexString(HmacSHA256(signingkey, policy.getBytes()));

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        PrintWriter out = response.getWriter();
        out.write(bucketName+" ");
        out.write(policy+" ");
        out.write(signature+" ");   
        out.write(access_key_id+" ");
    }

        private byte[] getSignatureKey(String key, String dateStamp, String regionName, String serviceName) throws Exception{
             byte[] kSecret = ("AWS4" + key).getBytes("UTF8");
             byte[] kDate    = HmacSHA256(dateStamp, kSecret);
             byte[] kRegion  = HmacSHA256(regionName, kDate);
             byte[] kService = HmacSHA256(serviceName, kRegion);
             byte[] kSigning = HmacSHA256("aws4_request", kService);
             return kSigning;
        }


        private byte[] HmacSHA256(String data, byte[] key) throws Exception {
            // TODO Auto-generated method stub
            String algorithm="HmacSHA256";               
             Mac mac = Mac.getInstance(algorithm);
             mac.init(new SecretKeySpec(key, algorithm));
             return mac.doFinal(data.getBytes("UTF8"));
        }   

}
