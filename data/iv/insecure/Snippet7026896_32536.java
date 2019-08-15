package com.custom.lazylist;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Stack;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import com.fedorvlasov.lazylist.R;
import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

public class ImageLoader extends Activity {

    //the simplest in-memory cache implementation. This should be replaced with something like SoftReference or BitmapOptions.inPurgeable(since 1.6)
    private HashMap<String, Bitmap> cache=new HashMap<String, Bitmap>();

    private File cacheDir;

    public ImageLoader(Context context){
        //Make the background thead low priority. This way it will not affect the UI performance
        photoLoaderThread.setPriority(Thread.NORM_PRIORITY-1);

        //Find the dir to save cached images
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
            cacheDir=new File(android.os.Environment.getExternalStorageDirectory(),"LazyList");
        else
            cacheDir=context.getCacheDir();
        if(!cacheDir.exists())
            cacheDir.mkdirs();
    }

    final int stub_id=R.drawable.stub;
    public void DisplayImage(String url, Activity activity, ImageView imageView)
    {
        if(cache.containsKey(url))
            imageView.setImageBitmap(cache.get(url));
        else
        {
            queuePhoto(url, activity, imageView);
            imageView.setImageResource(stub_id);
        }    
    }

    private void queuePhoto(String url, Activity activity, ImageView imageView)
    {
        //This ImageView may be used for other images before. So there may be some old tasks in the queue. We need to discard them. 
        photosQueue.Clean(imageView);
        PhotoToLoad p=new PhotoToLoad(url, imageView);
        synchronized(photosQueue.photosToLoad){
            photosQueue.photosToLoad.push(p);
            photosQueue.photosToLoad.notifyAll();
        }

        //start thread if it's not started yet
        if(photoLoaderThread.getState()==Thread.State.NEW)
            photoLoaderThread.start();
    }

    private Bitmap getBitmap(String src) {
        Bitmap myBitmap = null;
            //Decryption
            try {
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            SecretKeySpec keySpec = new SecretKeySpec("01234567890abcde".getBytes(), "AES");
            IvParameterSpec ivSpec = new IvParameterSpec("fedcba9876543210".getBytes());
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

            AssetManager is = this.getAssets();        
            InputStream input = is.open(src); //open file in asset manager
            CipherInputStream cis = new CipherInputStream(input, cipher);

            myBitmap = BitmapFactory.decodeStream(cis);

            }
            catch(Exception e){
                e.printStackTrace();
                Log.v("ERROR","Error : "+e);
            }


            return myBitmap;
        }

    //Task for the queue
    private class PhotoToLoad
    {
        public String url;
        public ImageView imageView;
        public PhotoToLoad(String u, ImageView i){
            url=u; 
            imageView=i;
        }
    }

    PhotosQueue photosQueue=new PhotosQueue();

    public void stopThread()
    {
        photoLoaderThread.interrupt();
    }

    //stores list of photos to download
    class PhotosQueue
    {
        private Stack<PhotoToLoad> photosToLoad=new Stack<PhotoToLoad>();

        //removes all instances of this ImageView
        public void Clean(ImageView image)
        {
            for(int j=0 ;j<photosToLoad.size();){
                if(photosToLoad.get(j).imageView==image)
                    photosToLoad.remove(j);
                else
                    ++j;
            }
        }
    }

    class PhotosLoader extends Thread {
        public void run() {
            try {
                while(true)
                {
                    //thread waits until there are any images to load in the queue
                    if(photosQueue.photosToLoad.size()==0)
                        synchronized(photosQueue.photosToLoad){
                            photosQueue.photosToLoad.wait();
                        }
                    if(photosQueue.photosToLoad.size()!=0)
                    {
                        PhotoToLoad photoToLoad;
                        synchronized(photosQueue.photosToLoad){
                            photoToLoad=photosQueue.photosToLoad.pop();
                        }
                        Bitmap bmp=getBitmap(photoToLoad.url);
                        cache.put(photoToLoad.url, bmp);
                        Object tag=photoToLoad.imageView.getTag();
                        if(tag!=null && ((String)tag).equals(photoToLoad.url)){
                            BitmapDisplayer bd=new BitmapDisplayer(bmp, photoToLoad.imageView);
                            Activity a=(Activity)photoToLoad.imageView.getContext();
                            a.runOnUiThread(bd);
                        }
                    }
                    if(Thread.interrupted())
                        break;
                }
            } catch (InterruptedException e) {
                //allow thread to exit
            }
        }
    }

    PhotosLoader photoLoaderThread=new PhotosLoader();

    //Used to display bitmap in the UI thread
    class BitmapDisplayer implements Runnable
    {
        Bitmap bitmap;
        ImageView imageView;
        public BitmapDisplayer(Bitmap b, ImageView i){bitmap=b;imageView=i;}
        public void run()
        {
            if(bitmap!=null)
                imageView.setImageBitmap(bitmap);
            else
                imageView.setImageResource(stub_id);
        }
    }

    public void clearCache() {
        //clear memory cache
        cache.clear();

        //clear SD cache
        File[] files=cacheDir.listFiles();
        for(File f:files)
            f.delete();
    }



}
