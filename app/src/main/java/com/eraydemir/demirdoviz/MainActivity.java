package com.eraydemir.demirdoviz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.BufferedInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class MainActivity extends AppCompatActivity {

    ListView bugunliste;
    List<String> birimListe = new ArrayList<String>();

    public static int pozisyon;
    public static String birim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("DEMİR DÖVİZ");

        bugunliste = findViewById(R.id.listView);
        bugunListeDoldur();

        bugunliste.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pozisyon = position;
                birim = birimListe.get(position);
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                startActivity(intent);
            }
        });
    }

    private void bugunListeDoldur(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        List<String> bugun_liste = new ArrayList<String>();
        String bugun_url = "https://www.tcmb.gov.tr/kurlar/today.xml";

        HttpURLConnection baglanti = null;

        //XML Parçalama işlemi
        try {
            URL b_url = new URL(bugun_url);
            baglanti = (HttpURLConnection) b_url.openConnection();

            int durum = baglanti.getResponseCode();
            if(durum == HttpURLConnection.HTTP_OK){
                BufferedInputStream stream = new BufferedInputStream(baglanti.getInputStream());
                DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

                Document document = documentBuilder.parse(stream);

                NodeList bugunNodeList = document.getElementsByTagName("Currency");

                for (int i=0 ; i<bugunNodeList.getLength()-1 ; i++){
                    Element element = (Element) bugunNodeList.item(i);

                    NodeList nodeListBirim = element.getElementsByTagName("Unit");
                    NodeList nodeListParaBirimi = element.getElementsByTagName("Isim");
                    NodeList nodeListAlis = element.getElementsByTagName("ForexBuying");

                    String Birim = nodeListBirim.item(0).getFirstChild().getNodeValue();
                    String paraBirimi = nodeListParaBirimi.item(0).getFirstChild().getNodeValue();
                    String alis = nodeListAlis.item(0).getFirstChild().getNodeValue();

                    bugun_liste.add(Birim+" "+paraBirimi+"\n= "+alis+" TL");
                    birimListe.add(paraBirimi);
                }
            }

        } catch (Exception e) {
            Log.e("HATA",e.getMessage().toString());
        } finally {
            if (baglanti != null)
                baglanti.disconnect();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, bugun_liste);
        bugunliste.setAdapter(adapter);


    }
}
