import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.codec.binary.Base64;

//import sun.misc.BASE64Decoder;

/**
 *
 * @author root
 */
public class rsaClient extends HttpServlet {
    private byte[] decryptedText;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */

                InputStream inStream = this.getServletContext().getResourceAsStream("/private.key");
                BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));


                reader.close();

                String encSS = request.getParameter("encSS");

                byte[] decodedBytes = Base64.decodeBase64(encSS);

                ObjectInputStream inputStream = new ObjectInputStream(inStream);
                final PrivateKey privateKey = (PrivateKey) inputStream.readObject();

                 //



                    try {
                    // get an RSA cipher object and print the provider
                    final Cipher cipher = Cipher.getInstance("RSA");

                    // decrypt the text using the private key
                    cipher.init(Cipher.DECRYPT_MODE, privateKey);
                    decryptedText = cipher.doFinal(decodedBytes);

                    } catch (Exception ex) {
                  ex.printStackTrace();
                     }



                 out.println(decryptedText);



        } catch (ClassNotFoundException ex) {
            Logger.getLogger(rsaClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
