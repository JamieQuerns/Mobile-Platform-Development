/*
 Name: James Querns
 Matriculation Number: S2026518
 */
package com.example.trafficscotland;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlPullParserException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity implements
        OnClickListener {

    private String result = "";
    private TextView roadSearch;
    private Button Button1;
    private Button Button2;
    private Button Button3;
    private TextView dataDisplay;

    // Traffic Scotland Planned Roadworks XML link
    private String
            incUrl = "https://trafficscotland.org/rss/feeds/currentincidents.aspx";
    private String
            roadUrl = "https://trafficscotland.org/rss/feeds/roadworks.aspx";
    private String
            plannedUrl = "https://trafficscotland.org/rss/feeds/plannedroadworks.aspx";

    private LinkedList<Incedent> alist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dataDisplay = findViewById(R.id.dataDisplay);
        roadSearch = findViewById(R.id.search);
        Button1 = findViewById(R.id.showIncedents);
        Button1.setOnClickListener(this);
        Button2 = findViewById(R.id.showRoadworks);
        Button2.setOnClickListener(this);
        Button3 = findViewById(R.id.showPlannedRoadworks);
        Button3.setOnClickListener(this);
        if (!internetIsConnected()) {
            internetToast();
            finish();
        }
    }

    public boolean internetIsConnected() {
        try {
            String command = "ping -c 1 google.com";
            return (Runtime.getRuntime().exec(command).waitFor() == 0);
        } catch (Exception e) {
            return false;
        }
    }

    public void internetToast() {
        Toast toast = Toast.makeText(getApplicationContext(),
                "Internet access unavailable, please check connection",
                Toast.LENGTH_SHORT);
        toast.show();
    }

    private LinkedList<Incedent> parseXml(String dataToParse) {
        alist = new LinkedList<Incedent>();
        Incedent inc = new Incedent();

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();

            xpp.setInput(new StringReader(dataToParse));
            int eventType = xpp.getEventType();

            if (!alist.isEmpty()) {
                alist.clear();
            }
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    System.out.println("Start tag " + xpp.getName());

                    //Check which tag we have
                    if (xpp.getName().equalsIgnoreCase("channel")) {
                        alist = new LinkedList<Incedent>();
                    } else if (xpp.getName().equalsIgnoreCase("item")) {
                        Log.e("MyTag", "Item Start Tag Found");
                        inc = new Incedent();
                    } else if (xpp.getName().equalsIgnoreCase("title")) {

                        String temp = xpp.nextText();

                        Log.e("MyTag", "Title is " + temp);
                        inc.setTitle(temp);
                    } else if (xpp.getName().equalsIgnoreCase("description")) {

                        String temp = xpp.nextText();

                        Log.e("MyTag", "Description is " + temp);
                        temp = temp.replaceAll("<br />", "\n \n")
                                .replaceAll("Date: ", "Date: \n")
                                .replaceAll("Information: ", "Information: \n")
                                .replaceAll("Traffic Management:", "\n\nTraffic Management: \n")
                                .replaceAll("Works:", "Work Information: \n");
                        inc.setDescription(temp);
                    } else if (xpp.getName().equalsIgnoreCase("link")) {
                        String temp = xpp.nextText();
                        Log.e("MyTag", "Link is " + temp);
                        inc.setLink(temp);
                    } else if (xpp.getName().equalsIgnoreCase("georssPoint")) {
                        String temp = xpp.nextText();
                        Log.e("MyTag", "Geo Point is " + temp);
                        inc.setGeorssPoint(temp);
                    } else if (xpp.getName().equalsIgnoreCase("author")) {
                        String temp = xpp.nextText();
                        Log.e("MyTag", "Author is " + temp);
                        inc.setAuthor(temp);
                    } else if (xpp.getName().equalsIgnoreCase("comment")) {
                        String temp = xpp.nextText();
                        Log.e("MyTag", "Comment is " + temp);
                        inc.setComment(temp);
                    } else if (xpp.getName().equalsIgnoreCase("pubDate")) {
                        String temp = xpp.nextText();
                        Log.e("MyTag", "Pub Date is " + temp);
                        inc.setPubDate(temp);
                    }
                } else if (eventType == XmlPullParser.END_TAG) {
                    if (xpp.getName().equalsIgnoreCase("item")) {
                        Log.e("MyTag", "feed is " + inc.toString());
                        System.out.print("Feed Is: " + inc.toString());
                        if (inc.getTitle().contains(roadSearch.getText().toString()) || inc.getTitle().contains(roadSearch.getText().toString().toUpperCase())) {
                            alist.add(inc);
                        }
                    } else if (xpp.getName().equalsIgnoreCase("channel")) {
                        int size;
                        size = alist.size();
                        Log.e("MyTag", "channel size is " + size);
                    }
                }
                eventType = xpp.next();
            }

            System.out.println("End Document");

            return alist;
        } catch (XmlPullParserException ae1) {
            Log.e("MyTag", "Parsing error" + ae1.toString());
        } catch (IOException ae1) {
            Log.e("MyTag", "IO error during parsing");
        }

        Log.e("MyTag", "End document");

        return alist;
    }

    public void onClick(View aview) {
        startProgress(aview);
    }

    public void startProgress(View btn) {

        if (btn.getId() == R.id.showIncedents) {

            if (!internetIsConnected()) {
                internetToast();
            } else {
                new Thread(new Task(incUrl)).start();
            }

        } else if (btn.getId() == R.id.showRoadworks) {
            if (!internetIsConnected()) {
                internetToast();
            } else {
                new Thread(new Task(roadUrl)).start();
            }

        } else if (btn.getId() == R.id.showPlannedRoadworks) {
            if (!internetIsConnected()) {
                internetToast();
            } else {
                new Thread(new Task(plannedUrl)).start();
            }
        }
    }

    class Task implements Runnable {
        private String url;

        public Task(String aurl) {
            url = aurl;
        }

        @Override
        public void run() {

            URL aurl;
            URLConnection yc;
            BufferedReader in = null;
            String inputLine = "";

            Log.e("MyTag", "in run");

            try {
                Log.e("MyTag", "in try");
                aurl = new URL(url);
                yc = aurl.openConnection();
                in = new BufferedReader(new InputStreamReader(yc.getInputStream()));

                if (!result.equals("")) {
                    result = "";
                }
                while ((inputLine = in.readLine()) != null) {
                    result = result + inputLine;
                }
                in.close();
            } catch (IOException ae) {
                Log.e("MyTag", "ioexception");
            }

            final LinkedList<Incedent> alist;
            alist = parseXml(result);

            if (alist != null) {
                Log.e("MyTag", "List is not null");
                for (Object o : alist) {
                    Log.e("MyTag", o.toString());
                }
            } else {
                Log.e("MyTag", "List is null");
            }

            // but we are just getting started !
            MainActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    Log.d("UI thread", "I am the UI thread");

                    if (alist != null) {
                        dataDisplay.setText("");

                        for (Object o : alist) {
                            dataDisplay.append(o.toString());
                        }
                    }
                }
            });
        }
    }
}