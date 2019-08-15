package com.example.cli;

import com.microsoft.azure.keyvault.KeyVaultClient;
import com.microsoft.azure.keyvault.KeyVaultClientService;
import com.microsoft.azure.keyvault.models.KeyOperationResult;
import com.microsoft.azure.keyvault.webkey.JsonWebKeySignatureAlgorithm;
import com.microsoft.windowsazure.Configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.security.*;

import java.util.Random;

import java.util.concurrent.Future;


public class Main {

    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(Main.class);
        logger.debug("Launched !");

        try {
            byte[] plainText = new byte[100];
            new Random(0x1234567L).nextBytes(plainText);
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(plainText);
            byte[] digest = md.digest();
            Configuration configuration = AzureKVCredentials.createConfiguration();
            KeyVaultClient keyVaultClient = KeyVaultClientService.create(configuration);
            Future<KeyOperationResult> keyOperationPromise;
            KeyOperationResult keyOperationResult;
            keyOperationPromise = keyVaultClient.signAsync("https://XXXXXXX.vault.azure.net/keys/XXXXXXX/XXXXXXX”,JsonWebKeySignatureAlgorithm.RS256,digest);
            keyOperationResult = keyOperationPromise.get();   // <=== THIS IS LINE 37 IN THE STACKTRACE   ;-)   <====
            byte[] res = keyOperationResult.getResult();
            String  b64 = java.util.Base64.getEncoder().encodeToString(res);
            logger.debug(b64);


        } catch (Exception e) {
            logger.error(null,e);
        }
    }
}
