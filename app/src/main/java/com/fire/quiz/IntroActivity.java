package com.fire.quiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.fire.quiz.adapter.IntroThread;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        IntroThread introThread = new IntroThread(handler);
        introThread.start();
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            if(msg.what == 1){

                int readCheck = ContextCompat.checkSelfPermission(IntroActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
                int writeCheck = ContextCompat.checkSelfPermission(IntroActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                int coarseCheck = ContextCompat.checkSelfPermission(IntroActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION);
                int fineCheck = ContextCompat.checkSelfPermission(IntroActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);

                if(readCheck == PackageManager.PERMISSION_DENIED && writeCheck == PackageManager.PERMISSION_DENIED && coarseCheck == PackageManager.PERMISSION_DENIED && fineCheck == PackageManager.PERMISSION_DENIED){
                    ActivityCompat.requestPermissions(IntroActivity.this, new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION
                    },0);
                }else{
                    Intent intent = new Intent(IntroActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }
    };

    //Permission 결과 값 받는 메소드
    @Override
    public void onRequestPermissionsResult(int requestCode, String permission[], int[] grantResults){
        if(requestCode ==0){
            if(grantResults[0] == 0 && grantResults[1] == 0 && grantResults[2] == 0 && grantResults[3] == 0){
                Toast.makeText(this, "권한을 승인 하셨습니다.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(IntroActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }else{
                Toast.makeText(this, "내부 저장소 권한을 거절하셨습니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}
