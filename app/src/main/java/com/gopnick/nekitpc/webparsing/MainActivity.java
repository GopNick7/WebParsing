package com.gopnick.nekitpc.webparsing;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Elements value;
    private Elements type;
    private List<String> valueList = new ArrayList<>();
    private List<String> typeList = new ArrayList<>();
    private List<String> dataList = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView);

        new JSOUPtask().execute();

        adapter = new ArrayAdapter<>(this, R.layout.list_item, R.id.dataName, dataList);

    }

    public class JSOUPtask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            Document doc;

            try {
                doc = Jsoup.connect("https://kurs.censor.net.ua/").get();
                value = doc.select(".cur-value");
                type = doc.select(".cur-type");

                valueList.clear();
                typeList.clear();

                for (Element values : value) {
                    valueList.add(values.text());
                }
                for (Element types : type) {
                    typeList.add(types.text());
                }

                for (int i = 0; i < type.size(); i++) {
                    dataList.add(valueList.get(i) + " - " + typeList.get(i));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            listView.setAdapter(adapter);
        }
    }

}
