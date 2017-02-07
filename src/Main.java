import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

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
    private JButton button1;
    private JButton button2;

    /**
     * Action for the generate button, includes if states so that if no information is inputted then
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

                } catch (Exception error) {
                    System.out.println(error.toString());
                }
            }
        });
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Name.setFont(new Font("Arial", Font.PLAIN, 14));
                Pubdate.setFont(new Font("Arial", Font.PLAIN, 14));
                Author.setFont(new Font("Arial", Font.PLAIN, 14));
                ISBN.setFont(new Font("Arial", Font.PLAIN, 14));
                Editor.setFont(new Font("Arial", Font.PLAIN, 14));
                Book.setFont(new Font("Arial", Font.PLAIN, 14));
                Place.setFont(new Font("Arial", Font.PLAIN, 14));
                Publisher.setFont(new Font("Arial", Font.PLAIN, 14));
                Edition.setFont(new Font("Arial", Font.PLAIN, 14));
                Generate.setFont(new Font("Arial", Font.PLAIN, 14));
                Harvardreferencer.setFont(new Font("Arial", Font.PLAIN, 14));
                button2.setFont(new Font("Arial", Font.PLAIN, 14));
                button1.setFont(new Font("Arial", Font.PLAIN, 14));
//              each line of code changes the text within each instance to 14pt setting the font as Arial

            }
        });
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Name.setFont(new Font("Arial", Font.PLAIN, 12));
                Pubdate.setFont(new Font("Arial", Font.PLAIN, 12));
                Author.setFont(new Font("Arial", Font.PLAIN, 12));
                ISBN.setFont(new Font("Arial", Font.PLAIN, 12));
                Editor.setFont(new Font("Arial", Font.PLAIN, 12));
                Book.setFont(new Font("Arial", Font.PLAIN, 12));
                Place.setFont(new Font("Arial", Font.PLAIN, 12));
                Publisher.setFont(new Font("Arial", Font.PLAIN, 12));
                Edition.setFont(new Font("Arial", Font.PLAIN, 12));
                Generate.setFont(new Font("Arial", Font.PLAIN, 12));
                Harvardreferencer.setFont(new Font("Arial", Font.PLAIN, 12));
                button2.setFont(new Font("Arial", Font.PLAIN, 12));
                button1.setFont(new Font("Arial", Font.PLAIN, 12));

// each line of code changes the text within each instance back to 12pt keeping the font as Arial
            }
        });
    }

    public void getISBN(String ISBN) throws Exception {
// establishing a connection then checking the ISBN Database against the given ISBN
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

    // below data in taken from the API the Editor, Edition and Place of publication will need entering manually by the user.
    public void parseJson(String rawjson) throws JSONException {

        JSONObject obj = new JSONObject(rawjson);
        JSONArray items = obj.getJSONArray("items");
        System.out.println(items.toString());
        JSONObject item = items.getJSONObject(0);
        JSONObject volInfo = item.getJSONObject("volumeInfo");
        System.out.println(volInfo.getString("title"));
        System.out.println(volInfo.getString("publisher"));
        System.out.println(volInfo.getString("publishedDate"));
        JSONArray author = volInfo.getJSONArray("authors");
        System.out.println(author.get(0));

        //code for name to change into correct harvard referencing format

        String name = author.get(0).toString();
        String[] nameItems = name.split(" ");
        StringBuilder harvardName = new StringBuilder();
        List<String> nameParts = new ArrayList<String>();
        int parts = nameItems.length;
        System.out.println(parts);
        System.out.println(nameItems[parts - 1]);
        for (String Nitem : nameItems) {
            nameParts.add(Nitem);
        }
        Collections.reverse(nameParts);
        nameParts.forEach((section) -> {
            if (section.equals(nameItems[parts - 1])) {
                harvardName.append(section);
            } else {
                harvardName.append(". " + section.substring(0, 1));
            }
        });
        System.out.println(harvardName.toString());

        String year = null;
        try {
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//the date from json needs to go inside
            Date date = formatter.parse(volInfo.getString("publishedDate"));
            SimpleDateFormat outPut = new SimpleDateFormat("yyyy");
            year = outPut.format(date.getTime());
//this will output the year only - though you'll need to wrap it in ()
            System.out.println(year.toString());
        } catch (Exception e) {

        }


        //Sends data into the required text fields
        Authortext.setText(harvardName.toString());
        Pubdatetext.setText(year.toString());
        Publishertext.setText(volInfo.getString("publisher"));
        Booktext.setText(volInfo.getString("title"));
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