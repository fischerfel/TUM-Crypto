package com.example.om.imageviewer3;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.LruCache;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {
    String[]imgLoc;
    private LruCache<String,Bitmap> mMemoryCache;


    AsyncTaskLoadFiles myAsyncTaskLoadFiles;

    public class AsyncTaskLoadFiles extends AsyncTask<Void, String, Void> {

        File targetDirector;
        ImageAdapter myTaskAdapter;

        public AsyncTaskLoadFiles(ImageAdapter adapter) {
            myTaskAdapter = adapter;
        }

        @Override
        protected void onPreExecute() {
            String ExternalStorageDirectoryPath = Environment
                    .getExternalStorageDirectory().getAbsolutePath();

            String targetPath = ExternalStorageDirectoryPath + "/DCIM/Camera/";
            targetDirector = new File(targetPath);
            myTaskAdapter.clear();

            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {

            File[] files = targetDirector.listFiles();
            Arrays.sort(files);
            for (File file : files) {
                publishProgress(file.getAbsolutePath());
                if (isCancelled()) break;
            }
            int i=0;
            for(File file:files){
                i++;
            }
            int len=i;
            imgLoc=new String[len];
            i=0;
            for(File file:files){
                imgLoc[i]=file.getAbsolutePath();
                i++;
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            myTaskAdapter.add(values[0]);
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void result) {
            myTaskAdapter.notifyDataSetChanged();
            super.onPostExecute(result);
        }

    }

    public class ImageAdapter extends BaseAdapter {

        private Context mContext;
        ArrayList<String> itemList = new ArrayList<String>();

        public ImageAdapter(Context c) {
            mContext = c;
        }

        void add(String path) {
            itemList.add(path);
        }

        void clear() {
            itemList.clear();
        }

        void remove(int index){
            itemList.remove(index);
        }

        @Override
        public int getCount() {
            return itemList.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return itemList.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        //getView load bitmap ui thread
  /*
  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
   ImageView imageView;
   if (convertView == null) { // if it's not recycled, initialize some
          // attributes
    imageView = new ImageView(mContext);
    imageView.setLayoutParams(new GridView.LayoutParams(220, 220));
    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
    imageView.setPadding(8, 8, 8, 8);
   } else {
    imageView = (ImageView) convertView;
   }

   Bitmap bm = decodeSampledBitmapFromUri(itemList.get(position), 220,
     220);

   imageView.setImageBitmap(bm);
   return imageView;
  }
  */

        //getView load bitmap in AsyncTask
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {


            ImageView imageView;
            if (convertView == null) { // if it's not recycled, initialize some
                // attributes
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(220,220));
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                imageView.setPadding(20,0,20,0);
                ViewHolder dataHolder=new ViewHolder();
                dataHolder.image=imageView;
                imageView.setTag(dataHolder);

                convertView = imageView;

            }
                ViewHolder holder=(ViewHolder)convertView.getTag();
                holder.image.setImageBitmap(null);
                holder.position=position;

                //imageView = (ImageView) convertView;



            //Bitmap bm = decodeSampledBitmapFromUri(itemList.get(position), 220, 220);
            // Using an AsyncTask to load the slow images in a background thread
            new AsyncTask<ViewHolder, Void, Bitmap>() {
                private ViewHolder v;

                @Override
                protected Bitmap doInBackground(ViewHolder... params) {
                    v = params[0];
                    //String a=itemList.
                    String hash= getMD5Hash(itemList.get(position))+".jpg";
                    Bitmap bm=null;
                    synchronized (mMemoryCache){
                        bm=mMemoryCache.get(hash);
                    }
                    if(bm==null){
                        File cacheFile=new File(Environment.getDownloadCacheDirectory(),hash);
                        if(cacheFile.exists()){
                            bm=BitmapFactory.decodeFile(cacheFile.getAbsolutePath());
                            if(bm!=null){
                                synchronized (mMemoryCache){
                                    bm=mMemoryCache.put(hash,bm);
                                }
                                return bm;
                            }
                        }
                    }else{
                        return bm;
                    }
                    if(bm==null){
                        bm=decodeSampledBitmapFromUri(itemList.get(position),220,250);
                        FileOutputStream out = null;
                        try {
                            out = new FileOutputStream(Environment.getDownloadCacheDirectory());
                            bm.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
                            // PNG is a lossless format, the compression factor (100) is ignored
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                if (out != null) {
                                    out.close();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }


                    }
                    return bm;



                }
                public String getMD5Hash(String a){//throws NoSuchAlgorithmException{
                    try {
                        MessageDigest m = MessageDigest.getInstance("MD5");
                        m.reset();
                        m.update(a.getBytes());
                        return new BigInteger(1, m.digest()).toString(16);
                    }catch(NoSuchAlgorithmException e){}
                    return "Error";

                }

                @Override
                protected void onPostExecute(Bitmap result) {
                    super.onPostExecute(result);
                    //Not work for me!

           if (v.position == position) {
               // If this item hasn't been recycled already,
            // show the image
               v.image.setImageBitmap(result);
           }


                   //v.image.setImageBitmap(result);

                }
            }.execute(holder);

            //imageView.setImageBitmap(bm);
            //return imageView;
            return convertView;
        }

        public Bitmap decodeSampledBitmapFromUri(String path, int reqWidth,
                                                 int reqHeight) {

            Bitmap bm = null;
            // First decode with inJustDecodeBounds=true to check dimensions
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, options);

            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, reqWidth,
                    reqHeight);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            bm = BitmapFactory.decodeFile(path, options);

            return bm;
        }

        public int calculateInSampleSize(

                BitmapFactory.Options options, int reqWidth, int reqHeight) {
            // Raw height and width of image
            final int height = options.outHeight;
            final int width = options.outWidth;
            int inSampleSize = 1;

            if (height > reqHeight || width > reqWidth) {
                if (width > height) {
                    inSampleSize = Math.round((float) height
                            / (float) reqHeight);
                } else {
                    inSampleSize = Math.round((float) width / (float) reqWidth);
                }
            }

            return inSampleSize;
        }

        class ViewHolder {
            ImageView image;
            int position;
        }

    }

    ImageAdapter myImageAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final GridView gridview = (GridView) findViewById(R.id.gridview);
        myImageAdapter = new ImageAdapter(this);
        gridview.setAdapter(myImageAdapter);
        //grid.seto

  /*
   * Move to asyncTaskLoadFiles String ExternalStorageDirectoryPath =
   * Environment .getExternalStorageDirectory() .getAbsolutePath();
   *
   * String targetPath = ExternalStorageDirectoryPath + "/test/";
   *
   * Toast.makeText(getApplicationContext(), targetPath,
   * Toast.LENGTH_LONG).show(); File targetDirector = new
   * File(targetPath);
   *
   * File[] files = targetDirector.listFiles(); for (File file : files){
   * myImageAdapter.add(file.getAbsolutePath()); }
   */
        myAsyncTaskLoadFiles = new AsyncTaskLoadFiles(myImageAdapter);
        myAsyncTaskLoadFiles.execute();

        //gridview.setOnItemClickListener(myOnItemClickListener);

        /*Button buttonReload = (Button)findViewById(R.id.reload);
        buttonReload.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View arg0) {

                //Cancel the previous running task, if exist.
                myAsyncTaskLoadFiles.cancel(true);

                //new another ImageAdapter, to prevent the adapter have
                //mixed files
                myImageAdapter = new ImageAdapter(MainActivity.this);
                gridview.setAdapter(myImageAdapter);
                myAsyncTaskLoadFiles = new AsyncTaskLoadFiles(myImageAdapter);
                myAsyncTaskLoadFiles.execute();
            }});*/
        gridview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                seeImage(view,position);


            }
        });
        final int maxMemory=(int)(Runtime.getRuntime().maxMemory()/1024);
        final int cacheSize=maxMemory/8;
        mMemoryCache=new LruCache<String, Bitmap>(cacheSize){
            @Override
        protected int sizeOf(String key,Bitmap bitmap){
                return bitmap.getByteCount()/1024;
            }
        };



    }
    public void seeImage(View v, int position){
        Intent intent=new Intent(this.getApplicationContext(), SeeImage.class);
        intent.putExtra("getimgloc",""+imgLoc[position]);
        startActivity(intent);
    }
}
