 3167       /**
 3168        * Gets the requested finger print of the certificate.
 3169        */
 3170       private String getCertFingerPrint(String mdAlg, Certificate cert)
 3171           throws Exception
 3172       {
 3173           byte[] encCertInfo = cert.getEncoded();
 3174           MessageDigest md = MessageDigest.getInstance(mdAlg);
 3175           byte[] digest = md.digest(encCertInfo);
 3176           return toHexString(digest);
 3177       }
