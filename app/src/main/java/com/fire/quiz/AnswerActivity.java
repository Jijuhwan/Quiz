package com.fire.quiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class AnswerActivity extends AppCompatActivity {

    private AdView adView;
    TextView tvAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);

        tvAnswer = (TextView)findViewById(R.id.tv_answer);
        ImageView ivAnswer = (ImageView)findViewById(R.id.iv_answer);
        Button btnMain = (Button)findViewById(R.id.btn_main);

        //정답 Text 받기
        Intent intent = getIntent();
        String answer = intent.getExtras().getString("tvAnswer");
        String urlAnswer = intent.getExtras().getString("urlAnswer");
        tvAnswer.setText(answer);

        Glide.with(AnswerActivity.this).load(urlAnswer).override(1024, 980).into(ivAnswer);

        Log.v("URL 값",urlAnswer);

        //메인으로 이동
        btnMain.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent goMain = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(goMain);
                finish();
            }
        });



        //AdMob 선언
        MobileAds.initialize(this, getString(R.string.admob_app_id));

        adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        //AdMob Code
        adView.setAdListener(new AdListener(){
            @Override
            public void onAdLoaded(){
                Log.d("광고 출력","onAdLoaded");
            }

            @Override
            public void onAdFailedToLoad(int errorCode){
                Log.d("광고 실패한 이유 : ","onAdFailedToLoad" + errorCode);
            }

            @Override
            public void onAdOpened() {
                Log.d("광고 오픈 ","onAdOpened");
            }

            @Override
            public void onAdClicked() {
                Log.d("광고 클릭 ","onAdClicked");
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });
    }
}
