import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Date;
import java.util.Random;

import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1StreamParser;
import org.bouncycastle.asn1.DERBoolean;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.tsp.MessageImprint;
import org.bouncycastle.asn1.tsp.TimeStampReq;
import org.bouncycastle.asn1.tsp.TimeStampResp;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.tsp.TimeStampResponse;
import org.bouncycastle.tsp.TimeStampToken;

public class TimeStampGenerationSample {

    public static void main(String args[]) throws Exception{

        // for this sample we will use SHA1 to perform the hashes
        // however feel free to use another algorithm since sha1 is weakness
        String sha1Oid = "1.3.14.3.2.26";
        // data to be timestamped
        byte[] data = "some sample data... or your signature...".getBytes();

        // perform the hash of your data
        byte[] digestData = MessageDigest.getInstance(sha1Oid, new BouncyCastleProvider()).digest(data);
        // generate random data to perform your ts, it's optional depends on your ts service
        Random rand = new Random(new Date().getTime()); 
        String nonce = BigInteger.valueOf(rand.nextLong()).toString();          
        // require cert optional (default false... so use false)
        boolean requireCert = false;
        // timestampPolicy it's an oid to identify a policy, if it's required
        // must be provided by your ts service... it's optional so we put null
        String timestampPolicy = null;      

        TimeStampReq ts_req = createTimeStampRequest(digestData, nonce, requireCert, sha1Oid, timestampPolicy);

        // the data to be send to the service
        byte[] dataToSend = ts_req.getEncoded();

        // simply send your data using POST method
        // don't forget to specify http-header content-type as "application/timestamp-query"
        byte[] response = // send the request as you want
        // parse the response 
        ASN1StreamParser asn1Sp = new ASN1StreamParser(response);
        TimeStampResp tspResp = new TimeStampResp((ASN1Sequence)asn1Sp.readObject());
        TimeStampResponse tsr = new TimeStampResponse(tspResp);
        // and get the timestamp token :)
        TimeStampToken token = tsr.getTimeStampToken();
    }

    /**
     * Create the timestamp request
     * @param hashedData
     * @param nonce
     * @param requireCert
     * @param digestAlgorithm
     * @param timestampPolicy
     * @return
     * @throws TimeStampGenerationException
     */
    public static TimeStampReq createTimeStampRequest(byte[] hashedData, String nonce, boolean requireCert, String digestAlgorithm, String timestampPolicy) throws TimeStampGenerationException {

        MessageImprint imprint = new MessageImprint(new AlgorithmIdentifier(digestAlgorithm), hashedData);

        TimeStampReq request = new TimeStampReq(
                imprint, 
                timestampPolicy!=null?new DERObjectIdentifier(timestampPolicy):null, 
                nonce!=null?new DERInteger(nonce.getBytes()):null, 
                new DERBoolean(requireCert), 
                null
        );      

        return request;
    }
}
