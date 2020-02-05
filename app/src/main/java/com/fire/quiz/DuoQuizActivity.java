package com.fire.quiz;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class DuoQuizActivity extends AppCompatActivity {

    private AdView adView;
    ImageView ivMain;
    int index = 0;
    int total = MainActivity.uri.size();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duo_quiz);

        ivMain = (ImageView)findViewById(R.id.iv_DuoMain);
        ImageView btnO = (ImageView)findViewById(R.id.iv_o);
        ImageView btnX = (ImageView)findViewById(R.id.iv_x);
        final TextView tvDuoProblem = (TextView)findViewById(R.id.tv_DuoProblem);
        tvDuoProblem.setText("1 / "+total);

        //0번째 인덱스 이미지 표시
        ivMain.setImageURI((Uri) MainActivity.uri.get(0));

        btnO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((total-1) == index){
                    Toast.makeText(DuoQuizActivity.this,"모든 문제를 맞추셨습니다!",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(DuoQuizActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else if(total > index){
                    index += 1;
                    Toast.makeText(DuoQuizActivity.this,"정답입니다!",Toast.LENGTH_SHORT).show();
                    ivMain.setImageURI((Uri) MainActivity.uri.get(index));
                    tvDuoProblem.setText((index+1) + " / " + total);
                }

            }
        });

        btnX.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Toast.makeText(DuoQuizActivity.this,"틀리셨습니다! 메인화면으로 돌아갑니다.",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(DuoQuizActivity.this, MainActivity.class);
                startActivity(intent);
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
