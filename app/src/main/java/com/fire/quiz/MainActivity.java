package com.fire.quiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.fire.quiz.adapter.IntroThread;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AdView adView;

    //갤러리 다중 선택
    int PICK_FROM_ALBUM = 1;
    public static ArrayList uri = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnSolo = (Button)findViewById(R.id.btn_solo);
        Button btnDuo = (Button)findViewById(R.id.btn_duo);
        Button btnSetting = (Button)findViewById(R.id.btn_setting);

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

        btnSolo.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), SoloQuizActivity.class);
                intent.putExtra("flag","Solo Mode");
                startActivity(intent);
                finish();
            }
        });

        btnDuo.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view){
                getImage();
            }
        });

        btnSetting.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
    
    //이미지 불러오는 메소드
    private void getImage(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            try{
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                Toast.makeText(getApplication(),"길게 누르시면 다중 선택이 가능하며,\n선택은 최대 10개까지 가능합니다.",Toast.LENGTH_LONG).show();
                intent.setType("image/*");
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if(intent.resolveActivity(getPackageManager()) != null ){
                    startActivityForResult(intent.createChooser(intent,"2개 이상의 이미지를 원하시면\n'포토'를 이용해주세요."),PICK_FROM_ALBUM);
                }
            }catch (Exception e){
                Log.e("Permission Error...",e.toString());
            }
        }
        else{
            Log.e("Kitkat under", "..");
        }

    }

    //이미지 불러왔을 경우 결과 값 받는 부분
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_FROM_ALBUM && resultCode == RESULT_OK && null != data){
            if(data.getClipData() == null ){
                //리스트 초기화
                uri.clear();

                uri.add(data.getData());

                Intent intent = new Intent(getApplicationContext(), DuoSettingActivity.class);
                intent.putStringArrayListExtra("picture",uri);
                startActivity(intent);
            }else{
                ClipData clipData = data.getClipData();

                if(clipData.getItemCount() > 10){
                    Toast.makeText(MainActivity.this,"사진은 최대 10개까지만 선택 가능합니다.",Toast.LENGTH_SHORT).show();
                }else if(clipData.getItemCount() == 1){
                    //리스트 초기화
                    uri.clear();

                    Uri picture = clipData.getItemAt(0).getUri();
                    uri.add(picture);

                    Intent intent = new Intent(getApplicationContext(), DuoSettingActivity.class);
                    intent.putStringArrayListExtra("picture",uri);
                    startActivity(intent);
                }else if(clipData.getItemCount() > 1 && clipData.getItemCount() <= 10){
                    //리스트 초기화
                    uri.clear();
                    for(int i =0; i < clipData.getItemCount(); i++){
                        uri.add(clipData.getItemAt(i).getUri());
                    }
                    Intent intent = new Intent(getApplicationContext(), DuoSettingActivity.class);
                    intent.putStringArrayListExtra("picture",uri);
                    startActivity(intent);
                    finish();
                }
            }
        }
        else{
            Toast.makeText(this,"이전 화면으로 돌아갑니다.",Toast.LENGTH_SHORT).show();
        }
    }
}
