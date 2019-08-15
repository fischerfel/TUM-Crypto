package com.cb.hiveudf;

import java.nio.ByteBuffer; 
import java.nio.ByteOrder; 
import java.security.MessageDigest; 
import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.LongWritable;


/**
 * Implementation of the HashBytes UDF found in many databases.
 */

@Description(name = "hashBytes", 
    value = "_FUNC_(Charset, Value) - assigns a unique Biginterger to each string to which it is applied",
    extended = "Example:\n "
    + "  > SELECT name, _FUNC_(\"UTF-16LE\", value) hashkey FROM src" + "  ")

public class UDFHashBytes extends UDF {
 private final LongWritable result = new LongWritable(); 

    public LongWritable evaluate(Text input) throws Exception {
        if (input == null) 
        {
              return null;
        } 
        else 
        {
            String hashstring = input.toString();
            byte[] buf = hashstring.getBytes("UTF-8");
            MessageDigest algorithm=null;
            algorithm = MessageDigest.getInstance("SHA-1");          
            algorithm.reset();
            algorithm.update(buf);
            byte[] digest = algorithm.digest();  
            if(java.nio.ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) {
              for (int i = 0, j = digest.length - 1; i < j; i++, j--)  
              {  
                byte b = digest[i];  
                digest[i] = digest[j];  
                digest[j] = b;  
              } 
            }    
            ByteBuffer bb = ByteBuffer.wrap( digest );
            if(java.nio.ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) 
            {
              bb.order(ByteOrder.LITTLE_ENDIAN);
            }    

            result.set(bb.getLong());
            return result;
        }
    }
}
