package com.hs.works;

import org.apache.commons.codec.binary.Base64;
import org.eclipse.jetty.http.HttpStatus;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.security.KeyFactory;

import javax.crypto.Cipher;

public class CryptServlet extends HttpServlet {

    byte[] encrypt(byte[] pubKey, String text) {
        try {
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(pubKey);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            RSAPublicKey k = (RSAPublicKey) keyFactory.generatePublic(keySpec);
            final Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, k);
            return cipher.doFinal(text.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String key = req.getParameter("key");
        byte[] decoded_bytes = Base64.decodeBase64(key.getBytes());
        byte[] encoded = encrypt(decoded_bytes, "SECRET_TEXT");
        resp.setStatus(HttpStatus.OK_200);
        resp.getWriter().write(new String(Base64.encodeBase64(encoded)));
    }
}
