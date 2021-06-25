package aeonlabs.common.libraries.Libraries;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.AsyncTask;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class ReadKMLfile  extends AsyncTask<String, Void, Void> {

    HashMap<String,String> points;


    private final Activity activity;

    public ReadKMLfile(Activity _activity){
        activity=_activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        points= new HashMap<>();
    }

    @Override
    protected Void doInBackground(String... params) {
        AssetManager assetManager = activity.getAssets();

        try {
            InputStream inputStream = assetManager.open("map.kml");
            DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = docBuilder.parse(inputStream);
            NodeList coordList;

            if (document == null) return null;

            coordList = document.getElementsByTagName("coordinates");

            for (int i = 0; i < coordList.getLength(); i++) {
                String[] coordinatePairs = coordList.item(i).getFirstChild().getNodeValue().trim().split(" ");
                for (String coord : coordinatePairs) {
                    points.put(coord.split(",")[1], coord.split(",")[0]);
                }
            }
        } catch (IOException | SAXException | ParserConfigurationException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);


    }
}