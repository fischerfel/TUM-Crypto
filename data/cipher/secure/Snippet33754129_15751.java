if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M){
            c =  Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
        }else{
            c =  Cipher.getInstance("RSA/ECB/PKCS1Padding");
        }
