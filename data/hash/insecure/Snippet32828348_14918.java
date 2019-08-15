import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.util.Base64;

import java.security.MessageDigest;
import java.io.File.*;
import java.io.*;
import java.net.URL;

public class Base64Plugin extends CordovaPlugin{
int REQUEST_CAMERA = 0, SELECT_FILE = 1;
CallbackContext callbackContext;

public boolean execute(String action, JSONArray args,
        CallbackContext callbackContext) throws JSONException {

    System.out.println(action);
    System.out.println("param:::"+args.getString(0));
    System.out.println("param:::"+args.getString(1));
    if (action.equals("download")) {

        this.callbackContext = callbackContext;
        FileOutputStream fileOutputStream = null;
        int buffer = 1024;
        int SIZE = args.getString(2).length();
        int start = 0;
        int end = buffer;

        JSONObject res = new JSONObject();
        //byte[] data = new byte[buffer];//Base64.decode(args.getString(0), Base64.DEFAULT);
        File file = new File("/storage/emulated/0/Pictures/Screenshots/", args.getString(0));
        try {
            fileOutputStream = new FileOutputStream(file);
            while(start < SIZE) { 
                System.out.println("start:"+start+" \n end:"+end);
                byte[] data = Base64.decode(args.getString(2).substring(start, end), Base64.DEFAULT);
                fileOutputStream.write(data);
                start = end;
                if(start + buffer<= SIZE){
                    end = start + buffer;
                }else{
                    end = SIZE;
                }
                data = null;
            }

            res.put("Result", "File Write Successfule");

            this.callbackContext.success(res);
            //data = null;
            fileOutputStream = null;
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            res.put("Error", e.getMessage());
            this.callbackContext.error(res);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            res.put("Error", e.getMessage());
            this.callbackContext.error(res);
        }

    }else if(action.equals("upload")){
        this.callbackContext = callbackContext;
        String encodedImage = null;
        String selectedImagePath = "";
        StringBuffer sb = new StringBuffer("");
        try {

            System.out.println("creating variables..");
            FileInputStream fileInputStream = null;

            File file = new File("/storage/emulated/0/Pictures/Screenshots/"+args.getString(0));
            byte[] bFile = new byte[(int) file.length()];

            fileInputStream = new FileInputStream(file);
            BufferedInputStream inputStream = new BufferedInputStream(fileInputStream);
            System.out.println("reading file...");
            inputStream.read(bFile);

            MessageDigest md1 = MessageDigest.getInstance("MD5");

            int nread = 0;
            System.out.println("reading bytes....");
            while ((nread = fileInputStream.read(bFile)) != -1) {
                md1.update(bFile, 0, nread);
            };

            byte[] mdbytes = md1.digest(bFile);

            // convert the byte to hex format
            System.out.println("generating checksum....");
            for (int i = 0; i < mdbytes.length; i++) {
                sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100,
                        16).substring(1));
            }

            System.out.println("generating base64....");
            encodedImage = Base64.encodeToString(bFile, Base64.DEFAULT);

        } catch (Exception e) {

        }
        // System.out.println("base64:"+encodedImage);

        JSONObject res = new JSONObject();
        try {
            System.out.println("adding checksum and base64 to result....");
            res.put("Result", encodedImage);
            res.put("Checksum", sb);

            res.put("path", args.getString(0));
            System.out.println("sending result....");
            this.callbackContext.success(res);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            encodedImage = null;
            //bFile = null;
        }

        return true;

    }
    return false;
}

}
