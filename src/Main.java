import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Chris Wallace and Bradley Hoad on 10/01/2017.
 */
public class Main {
    private JTextField Authortext;
    private JTextField Pubdatetext;
    private JTextField Publishertext;
    private JTextField Publication;
    private JTextField Editortext;
    private JTextField Booktext;
    private JTextField Editiontext;
    private JButton searchButton;
    private JTextField ISBNtext;
    private JLabel Name;
    private JLabel ISBN;
    private JLabel Pubdate;
    private JLabel Author;
    private JLabel Editor;
    private JLabel Book;
    private JLabel Place;
    private JLabel Publisher;
    private JLabel Edition;
    private JEditorPane Generatedreference;
    private JButton Generate;
    private JPanel Harvardreferencer;

    /**Action for the generate button, includes if states so that if no information is inputted then
     * this will not input the , and leave a space
     */
    public Main() {
        Generate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String reference = "";
                reference = reference + Authortext.getText() + ", ";
                if (!Pubdatetext.getText().equals("")) {
                    reference = reference + "(" + Pubdatetext.getText() + "), ";
                }
                if (!Editortext.getText().equals("")) {
                    reference = reference + Editortext.getText() + ", ";
                }
                if (!Booktext.getText().equals("")) {
                    reference = reference + Booktext.getText() + ", ";
                }
                if (!Editiontext.getText().equals("")) {
                    reference = reference + Editiontext.getText() + ", ";
                }
                if (!Publication.getText().equals("")) {
                    reference = reference + Publication.getText() + ", ";
                }
                if (!Publishertext.getText().equals("")) {
                    reference = reference + Publishertext.getText();
                }
                System.out.println(reference);

                Generatedreference.setText(reference);
            }
        });

        // Create a trust manager that does not validate certificate chains
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

// Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
        }

        //this is for the search button to search for ISBNs
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    getISBN(ISBNtext.getText());

                }catch(Exception error){
                    System.out.println(error.toString());
                }
            }
        });
    }

    public void getISBN(String ISBN) throws Exception {

        URL url = new URL("https://www.googleapis.com/books/v1/volumes?q=ISBN_10:" + ISBN);

        URLConnection con = url.openConnection();

        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        con.getInputStream()
                )
        );

        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null)
            response.append(inputLine);
        in.close();

        System.out.println(response);

        parseJson(response.toString());
    }

    public void parseJson(String rawjson) throws JSONException {

        JSONObject obj = new JSONObject(rawjson);
        JSONArray items = obj.getJSONArray("items");
        System.out.println(items.toString());
        JSONObject item = items.getJSONObject(0);
        JSONObject volInfo = item.getJSONObject("volumeInfo");
        System.out.println(volInfo.getString("title"));
//        System.out.println(obj.getString("Auth"));
//        System.out.println(obj.getString("Datepub"));
//        System.out.println(obj.getString("Publish"));
//        System.out.println(obj.getString("public"));
//        System.out.println(obj.getString("Editor"));
//        System.out.println(obj.getString("Book"));
//        System.out.println(obj.getString("Edition"));
//
//        Authortext.setText(obj.getString("Auth"));
//        Pubdatetext.setText(obj.getString("Datepub"));
//        Publishertext.setText(obj.getString("Publish"));
//        Publication.setText(obj.getString("public"));
//        Editortext.setText(obj.getString("Editor"));
//        Booktext.setText(obj.getString("Book"));
//        Editiontext.setText(obj.getString("Edition"));


    }


    //GUI code below
    public static void main(String[] args) {
        JFrame frame = new JFrame("Main");
        frame.setContentPane(new Main().Harvardreferencer);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);



    }
}
