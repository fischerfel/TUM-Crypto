           connection = (HttpURLConnection) url.openConnection();

           if(connection instanceof HttpsURLConnection){
                ((HttpsURLConnection) connection).setHostnameVerifier(DO_NOT_VERIFY);
            }

              connection.setRequestProperty("Cookie",android.webkit.CookieManager.getInstance().getCookie(sUrl[0]));

                connection.setUseCaches(false);

                connection.setRequestProperty("Cookie", "User name = "+ this.User + "; Password = " + this.Password);


            connection.connect();

            System.setProperty("http.keepAlive", "false");
            int HTTPResponseCode = connection.getResponseCode();
