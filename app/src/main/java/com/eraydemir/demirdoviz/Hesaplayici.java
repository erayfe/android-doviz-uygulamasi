package com.eraydemir.demirdoviz;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;

public class Hesaplayici extends AppCompatActivity {

    EditText editText;
    Button button;
    TextView textView;
    TextView textView2;
    Float alis;
    Float hesap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hesaplayici);
        getSupportActionBar().setTitle(MainActivity.birim);

        editText = findViewById(R.id.editText);
        button = findViewById(R.id.button);
        textView = findViewById(R.id.textView);
        textView2 = findViewById(R.id.textView2);
        alis = SecondActivity.bugunAlis;

        DecimalFormat decf = new DecimalFormat("#.####");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hesap = Float.parseFloat(String.valueOf(editText.getText())) * alis;
                textView.setText(editText.getText()+" "+SecondActivity.birim);
                textView2.setText("= "+decf.format(hesap)+" TL");
            }
        });
    }
}
