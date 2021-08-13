package com.eraydemir.demirdoviz;

import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.BufferedInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class TarihSec extends AppCompatActivity {
    DatePicker datePicker;
    Button button;
    TextView textView;
    int yil,ay,gun;
    Calendar calendar;
    String tarih1, tarih2, tarih3, tarihUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tarih);
        getSupportActionBar().setTitle(MainActivity.birim);

        datePicker = findViewById(R.id.datePicker);
        button = findViewById(R.id.button2);
        textView = findViewById(R.id.textView3);

        DateFormat df1 = new SimpleDateFormat("yyyyMM");
        DateFormat df2 = new SimpleDateFormat("ddMMyyyy");
        DateFormat df3 = new SimpleDateFormat("dd/MM/yyyy");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yil = datePicker.getYear();
                ay = datePicker.getMonth();
                gun = datePicker.getDayOfMonth();

                calendar = Calendar.getInstance();
                calendar.set(yil, ay, gun);

                tarih3 = df3.format(calendar.getTime());

                if(calendar.get(Calendar.DAY_OF_WEEK) == 7)
                    calendar.add(Calendar.DATE, -1);
                if(calendar.get(Calendar.DAY_OF_WEEK) == 1)
                    calendar.add(Calendar.DATE, -2);

                tarih1 = df1.format(calendar.getTime());
                tarih2 = df2.format(calendar.getTime());

                tarihUrl = "https://www.tcmb.gov.tr/kurlar/"+tarih1+"/"+tarih2+".xml";
                doldur(tarihUrl);
            }
        });

    }

    private void doldur (String urls){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        HttpURLConnection baglanti = null;

        try {
            URL url = new URL(urls);
            baglanti = (HttpURLConnection) url.openConnection();

            int durum = baglanti.getResponseCode();
            if(durum == HttpURLConnection.HTTP_OK){
                BufferedInputStream stream = new BufferedInputStream(baglanti.getInputStream());
                DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

                Document document = documentBuilder.parse(stream);

                NodeList bugunNodeList = document.getElementsByTagName("Currency");

                int i = MainActivity.pozisyon;
                Element element = (Element) bugunNodeList.item(i);

                NodeList nodeListBirim = element.getElementsByTagName("Unit");
                NodeList nodeListParaBirimi = element.getElementsByTagName("Isim");
                NodeList nodeListAlis = element.getElementsByTagName("ForexBuying");

                String Birim = nodeListBirim.item(0).getFirstChild().getNodeValue();
                String paraBirimi = nodeListParaBirimi.item(0).getFirstChild().getNodeValue();
                Float a = Float.parseFloat(nodeListAlis.item(0).getFirstChild().getNodeValue());

                textView.setText(tarih3+"\n"+Birim+" "+paraBirimi+"\n= "+a+" TL");

            }

        } catch (Exception e) {
            Log.e("HATA",e.getMessage().toString());
        } finally {
            if (baglanti != null)
                baglanti.disconnect();
        }

    }
}
