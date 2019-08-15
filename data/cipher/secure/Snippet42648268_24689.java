package com.heyho.rest;

import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;

import trabe.AbePrivateKey;
import trabe.AbeSecretMasterKey;
import trabe.Cpabe;
import trabe.policyparser.ParseException;

@Path("/service")
public class JSONService {


    @POST
    @Path("/userkey")
    @Consumes("application/json")
    public Response createEncryptedKey(UserInput input) {

        File key = new File("key/smKey.txt");
        Map<String, Object> result = new HashMap<String, Object>();
        byte[] encryptedSecret = null;
        byte[] privateKeyByte = null;
        Gson gson = new Gson();
        try {
            AbeSecretMasterKey secretKey = AbeSecretMasterKey.readFromFile(key);
            secretKey.counter=input.getCounter();
            AbePrivateKey privateKey = Cpabe.keygenSingle(secretKey, input.getAttribute());
            privateKeyByte = privateKey.getAsByteArray();
            KeyFactory factory = KeyFactory.getInstance("RSA");
            PublicKey pubKey = factory.generatePublic(new X509EncodedKeySpec(input.getPublicKey()));
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            encryptedSecret = Encryption.blockCipher(privateKeyByte, 1, cipher);

        } catch (IOException | ParseException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException | InvalidKeyException | InvalidKeySpecException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        result.put("encryptedKey", encryptedSecret.toString());
        result.put("keyHash", Encryption.generateHash(privateKeyByte));
        String jsonResult = gson.toJson(result);

        return Response.status(201).entity(jsonResult).build();
    }

}
