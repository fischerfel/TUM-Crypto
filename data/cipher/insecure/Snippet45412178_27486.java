package org.apache.drill.exec.fn.impl;

import com.google.common.base.Charsets;
import io.netty.buffer.DrillBuf;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.net.BCodec;
import org.apache.drill.exec.expr.DrillSimpleFunc;
import org.apache.drill.exec.expr.annotations.FunctionTemplate;
import org.apache.drill.exec.expr.annotations.Output;
import org.apache.drill.exec.expr.annotations.Param;
import org.apache.drill.exec.expr.annotations.Workspace;
import org.apache.drill.exec.expr.holders.VarCharHolder;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.inject.Inject;

@functiontemplate(
name = "decode_cid",
scope = FunctionTemplate.FunctionScope.SIMPLE,
nulls = FunctionTemplate.NullHandling.NULL_IF_NULL
)

public class DecodeCidFunction implements DrillSimpleFunc {

@Param
VarCharHolder raw_input;
// @Param
// VarCharHolder raw_key;

@Output
VarCharHolder out;

@Inject
DrillBuf buffer;
// @workspace
// KeyGenerator keygenerator;

@Workspace
SecretKey myDesKey;

@Workspace
Cipher desCipher;

@Workspace
BCodec bCodec;

@Override
public void setup() {
    try {
        String key = "this is a secret";
        javax.crypto.SecretKeyFactory factory = javax.crypto.SecretKeyFactory.getInstance("DES");
        myDesKey = factory.generateSecret(new javax.crypto.spec.DESKeySpec(key.getBytes())) ;
        System.out.println("myDesKey = "+myDesKey.toString());
        // Create the cipher
        desCipher = Cipher.getInstance("DES");

        // Initialize the cipher for encryption
        desCipher.init(Cipher.DECRYPT_MODE, myDesKey);

        bCodec =  new BCodec("UTF-8");

    } catch(Exception e) {
        System.out.println("may i come here");
        e.printStackTrace();
    }
}

@Override
public void eval() {
    String input = org.apache.drill.exec.expr.fn.impl.StringFunctionHelpers.toStringFromUTF8(raw_input.start, raw_input.end, raw_input.buffer);
    String output = "";
    System.out.println("input = " + input);
    if (input.startsWith("=?"))
    {
        try{
            output = bCodec.decode(input);
        }catch(Exception e){
            System.out.println("find an error :" +e.toString());
            output  = "";
        }
    }else{
        byte[] bts = new byte[input.length() / 2];
        for (int i = 0; i < bts.length; i++) {
            bts[i] = (byte) Integer.parseInt(input.substring(2*i, 2*i+2), 16);
        }
        System.out.println("bts = " +bts.toString());
        try{
            byte[] decodedString = desCipher.doFinal(bts) ;
            output = new String(decodedString, "utf-8");
        }catch(Exception e){
            System.out.println("i come here " + e.toString());
            output="";
        }
    }
    System.out.println("output = " + output);
    out.buffer = buffer;
    out.start = 0;
    out.end = output.getBytes().length;
    buffer.setBytes(0, output.getBytes());
}
}
