package xuggler;

import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.MediaListenerAdapter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.mediatool.event.IVideoPictureEvent;
import com.xuggle.xuggler.Global;
import com.xuggle.xuggler.ICodec;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

import java.io.File;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import javax.imageio.ImageIO;


public class DecodeAndCaptureFrames extends MediaListenerAdapter
{

 // The number of seconds between frames.
      public static final double SECONDS_BETWEEN_FRAMES = 5;

  //The number of micro-seconds between frames.
  public static final long MICRO_SECONDS_BETWEEN_FRAMES =(long)      (Global.DEFAULT_PTS_PER_SECOND * SECONDS_BETWEEN_FRAMES);

  // Time of last frame write
  private static long mLastPtsWrite = Global.NO_PTS;

private static final double FRAME_RATE = 50;

private static final int SECONDS_TO_RUN_FOR = 20;

private static final String outputFilename = "D:\\K.mp4";

public static IMediaWriter writer = ToolFactory.makeWriter(outputFilename);
//receive BufferedImage and returns its byte data
    public static byte[] get_byte_data(BufferedImage image) {
    WritableRaster raster = image.getRaster();
    DataBufferByte buffer = (DataBufferByte) raster.getDataBuffer();
    return buffer.getData();
}


//create new_img with the attributes of image
public static BufferedImage user_space(BufferedImage image) {
    //create new_img with the attributes of image
    BufferedImage new_img = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
    Graphics2D graphics = new_img.createGraphics();
    graphics.drawRenderedImage(image, null);
    graphics.dispose(); //release all allocated memory for this image
    return new_img;
}

public static BufferedImage toImage(byte[] imagebytes, int width, int height) {
    DataBuffer buffer = new DataBufferByte(imagebytes, imagebytes.length);
    WritableRaster raster = Raster.createInterleavedRaster(buffer, width, height,
       3 * width, 3, new int[]{2, 1, 0}, (Point) null);

    ColorModel cm = new ComponentColorModel(ColorModel.getRGBdefault().getColorSpace(), 
            false, true, Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
    return new BufferedImage(cm, raster, true, null);
}

public static byte[] encrypt(byte[] orgnlbytes, String key) throws NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
    byte[] encbytes = null;
    try {
        Cipher cipher = Cipher.getInstance("AES");
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        // cryptograph. secure random
        random.setSeed(key.getBytes());

        keyGen.init(128, random);
        // for example
        SecretKey secretKey = keyGen.generateKey();
        try {
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(DecodeAndCaptureFrames.class.getName()).log(Level.SEVERE, null, ex);
        }
        encbytes = cipher.doFinal(orgnlbytes);
    }
    catch (NoSuchAlgorithmException ex) {
        Logger.getLogger(DecodeAndCaptureFrames.class.getName()).log(Level.SEVERE, null, ex);
    }        catch (NoSuchPaddingException ex)
    {
        System.out.print("can not encrypt buffer");
    }

    return encbytes;
}


  /**
   * The video stream index, used to ensure we display frames from one
   * and only one video stream from the media container.
   */

  private int mVideoStreamIndex = -1;

  /**
   * Takes a media container (file) as the first argument, opens it and
   *  writes some of it's video frames to PNG image files in the
   *  temporary directory.
   *
   * @param args must contain one string which represents a filename
   */

  public static void main(String[] args)
  {
    // create a new mr. decode and capture frames


    DecodeAndCaptureFrames decodeAndCaptureFrames;
    decodeAndCaptureFrames = new DecodeAndCaptureFrames("D:\\K.mp4");
  } 

  /** Construct a DecodeAndCaptureFrames which reads and captures
   * frames from a video file.
   *
   * @param filename the name of the media file to read
   */


  //makes reader to the file and read the data of it
  public DecodeAndCaptureFrames(String filename)
  {
    // create a media reader for processing video

   IMediaReader reader = ToolFactory.makeReader(filename);

// stipulate that we want BufferedImages created in BGR 24bit color space
reader.setBufferedImageTypeToGenerate(BufferedImage.TYPE_3BYTE_BGR);


// note that DecodeAndCaptureFrames is derived from
// MediaReader.ListenerAdapter and thus may be added as a listener
// to the MediaReader. DecodeAndCaptureFrames implements
// onVideoPicture().

reader.addListener(this);

// read out the contents of the media file, note that nothing else
// happens here.  action happens in the onVideoPicture() method
// which is called when complete video pictures are extracted from
// the media source

while (reader.readPacket() == null)
  do {} while(false);
  }

 /**
   * Called after a video frame has been decoded from a media stream.
   * Optionally a BufferedImage version of the frame may be passed
   * if the calling {@link IMediaReader} instance was configured to
   * create BufferedImages.
   *
   * This method blocks, so return quickly.
   */

  public void onVideoPicture(IVideoPictureEvent event)
  {
    try
    {
      // if the stream index does not match the selected stream index,
      // then have a closer look

  if (event.getStreamIndex() != mVideoStreamIndex)
  {
    // if the selected video stream id is not yet set, go ahead an
    // select this lucky video stream

    if (-1 == mVideoStreamIndex)
      mVideoStreamIndex = event.getStreamIndex();

    // otherwise return, no need to show frames from this video stream

    else
      return;
  }

  // if uninitialized, backdate mLastPtsWrite so we get the very
  // first frame

  if (mLastPtsWrite == Global.NO_PTS)
    mLastPtsWrite = event.getTimeStamp() - MICRO_SECONDS_BETWEEN_FRAMES;

  // if it's time to write the next frame

  if (event.getTimeStamp() - mLastPtsWrite >= MICRO_SECONDS_BETWEEN_FRAMES)
  {
    // Make a temporary file name

   // File file = File.createTempFile("frame", ".jpeg");

    // write out PNG

//        ImageIO.write(event.getImage(), "png", file);

    BufferedImage orgnlimage = event.getImage();
        orgnlimage = user_space(orgnlimage);
        byte[] orgnlimagebytes = get_byte_data(orgnlimage);
        byte[] encryptedbytes = encrypt(orgnlimagebytes, "abc");
        BufferedImage encryptedimage = toImage(encryptedbytes, orgnlimage.getWidth(), orgnlimage.getHeight());


        ImageIO.write(encryptedimage, "png", File.createTempFile("frame", ".png"));
//         indicate file written

    double seconds = ((double)event.getTimeStamp())
      / Global.DEFAULT_PTS_PER_SECOND;
//        System.out.printf("at elapsed time of %6.3f seconds wrote: %s\n",seconds, file);

    // update last write time

    mLastPtsWrite += MICRO_SECONDS_BETWEEN_FRAMES;
  }
}
catch (Exception e)
{
  e.printStackTrace();
}
  }

}
