package com.fire.quiz;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.fire.quiz.adapter.PictureAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DuoSettingActivity extends AppCompatActivity {

    private AdView adView;
    ImageView imgStroage;

    //뷰페이저
    private ViewPager viewPager;
    private PictureAdapter pictureAdapter;

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_duo_setting);

        Button btnDuoMain = (Button)findViewById(R.id.btn_duomain);
        Button btnStart = (Button)findViewById(R.id.btn_start);

        viewPager = (ViewPager)findViewById(R.id.view);
        pictureAdapter = new PictureAdapter(this);
        viewPager.setAdapter(pictureAdapter);

        //메인화면으로 이동
        btnDuoMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DuoSettingActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                }
        });

        //Duo 게임 시작
        btnStart.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(DuoSettingActivity.this, DuoQuizActivity.class);
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

    private void permissionCheck(){
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {

            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {

            }
        };
        TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setRationaleMessage(getResources().getString(R.string.permission_2))
                .setDeniedMessage(getResources().getString(R.string.permission_1))
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE).check();
    }
}
