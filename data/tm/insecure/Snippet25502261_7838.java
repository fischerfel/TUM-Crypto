package testproject;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.junit.*;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.Select;

public class testClass {
  private WebDriver driver;
  @Before
  public void setUp() throws Exception {
      //"C:\\Users\\c.farkas\\AppData\Local\\Mozilla Firefox\\Firefox.exe
      System.setProperty("webdriver.firefox.bin","C:\\Users\\c.farkas\\AppData\\Local\\Mozilla Firefox\\Firefox.exe");
    driver = new FirefoxDriver();
  }


  @Test
  public void testtestclass() throws Exception {
      driver.get("http://tudakozo.telekom.hu/main?xml=main&xsl=main");
      driver.findElement(By.xpath("id('session_name')")).sendKeys("Szabó Gábor");
      driver.findElement(By.xpath("id('session_location')")).sendKeys("Gyula");
      System.out.println("cica");
      WebElement img = driver.findElement(By.xpath("//form[@id='searchByName']/table/tbody/tr/td/img")); // or xpath whichever you prefer
      String src = img.getAttribute("src");

   // Create a new trust manager that trust all certificates
      TrustManager[] trustAllCerts = new TrustManager[]{
          new X509TrustManager() {
              public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                  return null;
              }
              public void checkClientTrusted(
                  java.security.cert.X509Certificate[] certs, String authType) {
              }
              public void checkServerTrusted(
                  java.security.cert.X509Certificate[] certs, String authType) {
              }
          }
      };

      // Activate the new trust manager
      try {
          SSLContext sc = SSLContext.getInstance("SSL");
          sc.init(null, trustAllCerts, new java.security.SecureRandom());
          HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
      } catch (Exception e) {
      }


      URL url = new URL(src);
      URLConnection connection = url.openConnection();
      InputStream is = connection.getInputStream();
      BufferedImage bufImgOne = ImageIO.read(url);
      ImageIO.write(bufImgOne, "jpg", new File("test.jpg"));

      // .. then download the file
 /*    System.out.println(src);
      URI uri = new URI(src);
      URL url = uri.toURL();
      BufferedImage bufImgOne = ImageIO.read(url);
      ImageIO.write(bufImgOne, "jph", new File("test.png"));*/
 //     System.out.println(cheesecakes.size() + " cheesecakes:");
   /*   for (int i=0; i<cheesecakes.size(); i++) {
          System.out.println(i+1 + ". " + cheesecakes.get(i).getText());
      }*/
  }

  @After
  public void tearDown() throws Exception {
    driver.quit();
    }
  }
