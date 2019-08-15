private String getShaKey() {
        //fucnion para saber si esta bien registrado el codigo de googlemaps
        //ME SALE EXCEPTION DE NOMBRE NO ENCONTRADO?¿?¿
        String strRet="";
         try {
         PackageInfo info = getPackageManager().getPackageInfo("your.package.name",
         PackageManager.GET_SIGNATURES);
         for (Signature signature : info.signatures) {
         MessageDigest md = MessageDigest.getInstance("SHA");
         md.update(signature.toByteArray());
         //Log.v(TAG, "KeyHash:" + Base64.encodeToString(md.digest(),
         strRet="KeyHash:" + Base64.encodeToString(md.digest(),Base64.DEFAULT);

         }

         } catch (NameNotFoundException e) {
            //e.printStackTrace();
         strRet="EXCEPTION NOMBRE NO ENCONTRADO";
             } catch (NoSuchAlgorithmException  e) {
         //e.printStackTrace();
         strRet="EXCEPTION ALGORITMO NO";
         }
        return strRet;

         }
