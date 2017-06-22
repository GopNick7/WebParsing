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

    // Экземпляры класса благодаря которым мы будет разбирать данные на куски
    private Elements value;
    private Elements type;
    // Массивы для хранения данных и передачи их адаптеру
    private List<String> valueList = new ArrayList<>();
    private List<String> typeList = new ArrayList<>();
    private List<String> dataList = new ArrayList<>();
    // Адаптер
    private ArrayAdapter<String> adapter;

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView);
        // Запрос к нашему потоку для выборки данных
        new JSOUPtask().execute();

        // Добавляем данные для ListView
        adapter = new ArrayAdapter<>(this, R.layout.list_item, R.id.dataName, dataList);

    }

    // Внутренний класс создающий запрос
    private class JSOUPtask extends AsyncTask<String, Void, String> {

        // Метод выполняющий запрос в фоне
        @Override
        protected String doInBackground(String... params) {

            // Экземпляр класса захватывающего страницу
            Document doc;

            try {
                // Страница откуда будем парсить
                doc = Jsoup.connect("https://kurs.censor.net.ua/").get();
                // задаем с какого места
                value = doc.select(".cur-value");
                type = doc.select(".cur-type");

                // Очищаем массивы
                valueList.clear();
                typeList.clear();

                // В циклах захватываем все данные которые есть и записываем их массивы
                for (Element values : value) {
                    valueList.add(values.text());
                }
                for (Element types : type) {
                    typeList.add(types.text());
                }

                // Заполняем массив данными из других массивов
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
            // после запроса обновляем листвью
            listView.setAdapter(adapter);
        }
    }

}
