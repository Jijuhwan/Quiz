package com.fire.quiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.ads.MobileAds;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnSolo = (Button)findViewById(R.id.btn_solo);
        Button btnDuo = (Button)findViewById(R.id.btn_duo);
        Button btnSetting = (Button)findViewById(R.id.btn_setting);

        //AdMob 선언
        MobileAds.initialize(this, getString(R.string.admob_app_id));

        //AdMob Code


        btnSolo.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), SoloQuizActivity.class);
                intent.putExtra("flag","Solo Mode");
                startActivity(intent);
            }
        });

        btnDuo.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), DuoSettingActivity.class);
                startActivity(intent);
            }
        });

        btnSetting.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
                startActivity(intent);
            }
        });

    }
}
