    String hash = "";
    String name = nameText.getText();
    try{
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update((name + score + HASH_SALT).getBytes());
        byte byteData[] = md.digest();

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
        hash = sb.toString();
    } catch (Exception e) {
        hash = "";
    }


    try {
        String urlParameters = PARAM_NAME + name + PARAM_SCORE + score + PARAM_HASH + hash;
        URL url = new URL(HIGHSCORES_ADD + urlParameters);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.getInputStream();
        urlConnection.disconnect();
    } catch (Exception e){
        Gdx.app.log( "HighScores", "Could not submit score" + e.getMessage());
    } finally {
        ((Game)Gdx.app.getApplicationListener()).setScreen(new MainMenu());
    }
