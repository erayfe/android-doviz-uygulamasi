package com.eraydemir.demirdoviz;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.BufferedInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class SecondActivity extends AppCompatActivity {
    List<String> liste = new ArrayList<String>();
    List<Float> gliste = new ArrayList<Float>();
    ListView liste_gorunum;
    String paraBirimi;
    public static Float bugunAlis;
    public static String birim;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        liste_gorunum = findViewById(R.id.listView2);

        DateFormat df1 = new SimpleDateFormat("yyyyMM");
        DateFormat df2 = new SimpleDateFormat("ddMMyyyy");


        Date bugun = new Date();


        Calendar cbugun = Calendar.getInstance();
        cbugun.setTime(bugun);
        if(cbugun.get(Calendar.DAY_OF_WEEK) == 7)
            cbugun.add(Calendar.DATE, -1);
        if(cbugun.get(Calendar.DAY_OF_WEEK) == 1)
            cbugun.add(Calendar.DATE, -2);

        String btarih1 = df1.format(cbugun.getTime());
        String btarih2 = df2.format(cbugun.getTime());

        String bugunUrl = "https://www.tcmb.gov.tr/kurlar/"+btarih1+"/"+btarih2+".xml";

        Calendar cdun = Calendar.getInstance();
        cdun.setTime(bugun);
        cdun.add(Calendar.DATE, -1);
        if(cdun.get(Calendar.DAY_OF_WEEK) == 7)
            cdun.add(Calendar.DATE, -1);
        if(cdun.get(Calendar.DAY_OF_WEEK) == 1)
            cdun.add(Calendar.DATE, -2);


        String dtarih1 = df1.format(cdun.getTime());
        String dtarih2 = df2.format(cdun.getTime());

        String dunUrl = "https://www.tcmb.gov.tr/kurlar/"+dtarih1+"/"+dtarih2+".xml";


        Calendar chafta = Calendar.getInstance();
        chafta.setTime(bugun);
        chafta.add(Calendar.DATE, -7);
        if(chafta.get(Calendar.DAY_OF_WEEK) == 7)
            chafta.add(Calendar.DATE, -1);
        else if(chafta.get(Calendar.DAY_OF_WEEK) == 1)
            chafta.add(Calendar.DATE, -2);

        String htarih1 = df1.format(chafta.getTime());
        String htarih2 = df2.format(chafta.getTime());

        String haftaUrl = "https://www.tcmb.gov.tr/kurlar/"+htarih1+"/"+htarih2+".xml";


        Calendar cay = Calendar.getInstance();
        cay.setTime(bugun);
        cay.add(Calendar.MONTH, -1);
        if(cay.get(Calendar.DAY_OF_WEEK) == 7)
            cay.add(Calendar.DATE, -1);
        else if(cay.get(Calendar.DAY_OF_WEEK) == 1)
            cay.add(Calendar.DATE, -2);

        String atarih1 = df1.format(cay.getTime());
        String atarih2 = df2.format(cay.getTime());

        String ayUrl = "https://www.tcmb.gov.tr/kurlar/"+atarih1+"/"+atarih2+".xml";


        Calendar cyil = Calendar.getInstance();
        cyil.setTime(bugun);
        cyil.add(Calendar.YEAR, -1);
        if(cyil.get(Calendar.DAY_OF_WEEK) == 7)
            cyil.add(Calendar.DATE, -1);
        else if(cyil.get(Calendar.DAY_OF_WEEK) == 1)
            cyil.add(Calendar.DATE, -2);

        String ytarih1 = df1.format(cyil.getTime());
        String ytarih2 = df2.format(cyil.getTime());

        String yilUrl = "https://www.tcmb.gov.tr/kurlar/"+ytarih1+"/"+ytarih2+".xml";

        doldur(bugunUrl);
        doldur(dunUrl);
        doldur(haftaUrl);
        doldur(ayUrl);
        doldur(yilUrl);

        getSupportActionBar().setTitle(MainActivity.birim);

        bugunAlis = gliste.get(0);
        birim = paraBirimi;
        DecimalFormat decf = new DecimalFormat("#.####");

        liste.add("BUGÜN:\n"+gliste.get(0)+" TL");
        liste.add("HESAPLAYICI");
        liste.add("1 GÜN ÖNCE:\n"+gliste.get(1)+" TL");
        liste.add("1 HAFTA ÖNCE:\n"+gliste.get(2)+" TL");
        liste.add("1 AY ÖNCE:\n"+gliste.get(3)+" TL");
        liste.add("1 YIL ÖNCE:\n"+gliste.get(4)+" TL");
        liste.add("TARİH SEÇ");
        liste.add("GÜNLÜK DEĞİŞİM:\n"+decf.format((gliste.get(0)-gliste.get(1)))+" TL");
        liste.add("HAFTALIK DEĞİŞİM:\n"+decf.format((gliste.get(0)-gliste.get(2)))+" TL");
        liste.add("AYLIK DEĞİŞİM:\n"+decf.format((gliste.get(0)-gliste.get(3)))+" TL");
        liste.add("YILLIK DEĞİŞİM:\n"+decf.format((gliste.get(0)-gliste.get(4)))+" TL");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, liste);
        liste_gorunum.setAdapter(adapter);

        liste_gorunum.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1) {
                    Intent intent = new Intent(SecondActivity.this, Hesaplayici.class);
                    startActivity(intent);
                }
                if (position == 6) {
                    Intent intent2 = new Intent(SecondActivity.this, TarihSec.class);
                    startActivity(intent2);
                }
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
                paraBirimi = nodeListParaBirimi.item(0).getFirstChild().getNodeValue();
                Float a = Float.parseFloat(nodeListAlis.item(0).getFirstChild().getNodeValue());

                gliste.add(a);

            }

        } catch (Exception e) {
            Log.e("HATA",e.getMessage().toString());
        } finally {
            if (baglanti != null)
                baglanti.disconnect();
        }

    }
}
