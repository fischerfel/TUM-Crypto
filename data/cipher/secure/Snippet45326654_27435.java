import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;


private int cryptographyKeySize=0;
private String cryptographyMethod=null;
private     KeyGenerator kgen = null;
private int secretKeyFieldIndex=0;
private int myDecryptedSecretFieldIndex=0;

private int encryptedTextFieldIndex1=0;
private int encryptedTextFieldIndex2=0;


private int COL1Index1 =0;
private int COL2Index1 =0;
private int COL1Index2 =0;
private int COL2Index2 =0;
private String symmKey = "9d6ea4d3e6f8c4f8";
private String ivSpec = "1c5dd32d7ba54bdd";


public boolean processRow(StepMetaInterface smi, StepDataInterface sdi) throws KettleException 
{
    Object[] r = getRow(); 
    byte[] decrypted = null;
    byte[] raw = null;
    SecretKeySpec skeySpec = null;
    Cipher cipher = null;
    String exMessage = null;
    boolean isException = false;

    if (r==null)
    {
        setOutputDone();
        return false;
    }


    if(!isException){
        Object[] outputRowData = createOutputRow(r, data.outputRowMeta.size());


        try {

            raw = symmKey.getBytes("UTF-8");        
            skeySpec = new SecretKeySpec(raw, "AES");   
            cipher = Cipher.getInstance("AES/CBC/ISO10126PADDING");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(ivSpec.getBytes("UTF-8"));
            cipher.init(Cipher.DECRYPT_MODE,skeySpec,ivParameterSpec);  
            byte[] decordedValue = new BASE64Decoder().decodeBuffer((String)r[encryptedTextFieldIndex1]);
            decrypted = cipher.doFinal(decordedValue);                  
            outputRowData[myDecryptedSecretFieldIndex]=new String(decrypted);
            outputRowData[COL1Index2]=r[COL1Index1];
            outputRowData[COL2Index2]=r[COL2Index1];
            outputRowData[encryptedTextFieldIndex2]=r[encryptedTextFieldIndex1];

            putRow(data.outputRowMeta, outputRowData);
        } catch (Exception e) {
            exMessage = "failed during decryption::"+(String)r[encryptedTextFieldIndex1];
            isException = true;
        }

    }

    return true;
}
