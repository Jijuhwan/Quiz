package com.fire.quiz;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SoloQuizActivity extends AppCompatActivity {
    String flag;
    int count = 8;  //카운트 변수
    int num = 1; // 배열 인덱스
    int pre = 1; // 이전 인덱스 값 체크

    //파이어베이스 인스턴스 변수
    FirebaseStorage storage;
    StorageReference storageRef;
    StorageReference pathRef;

    //Main Var
    ImageView imageView;
    EditText editAnswer;
    Button btnAnswer;
    TextView tvCount;
    Animation animRotate;
    TextView tvProblem;
    private AdView adView;

    //랜덤 변수 선언
    Random rnd;
    int totalName[] = new int[52];

    //변수
    String urlAnswer;    //인물 정답 이름
    String decode;  //URL 인물 이름 디코드

    //카운트 다운 핸들러
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable(){
        @Override
        public void run() {
            //카운트 다운
            if(count == 8){
                pre = num;
                quiz(totalName[num]);
                tvCount.setText("게임 시작!");
                count -= 1;
                //1초뒤 다시 시작
                handler.postDelayed(runnable,1000);
            } else if((count < 0) && pre == num){
                handler.removeCallbacks(runnable);
                tvCount.setText("실패!");

                Toast.makeText(getApplicationContext(),"실패!",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), AnswerActivity.class);
                intent.putExtra("tvAnswer",decode);
                intent.putExtra("urlAnswer",urlAnswer);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK & Intent.FLAG_ACTIVITY_CLEAR_TOP & Intent.FLAG_ACTIVITY_NEW_TASK);
                handler.removeCallbacks(runnable);
                startActivity(intent);
                finish();
            }else if(pre != num ){
                pre += 1;
                count = 7;
                quiz(totalName[num]);
                tvCount.setText(count + "");
                tvCount.startAnimation(animRotate);
                count -= 1;
                //1초뒤 다시 시작
                handler.postDelayed(runnable,1000);
            } else{
                tvCount.setText(count + "");
                tvCount.startAnimation(animRotate);
                count -= 1;
                //1초뒤 다시 시작
                handler.postDelayed(runnable,1000);
            }
        }
    };

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solo_quiz);

        //Layout 변수 설정
        TextView tvMode = (TextView)findViewById(R.id.tv_mode);
        imageView = (ImageView)findViewById(R.id.imageView);
        tvCount = (TextView)findViewById(R.id.tv_count);
        animRotate = AnimationUtils.loadAnimation(this, R.animator.anim_rotate);
        btnAnswer = (Button)findViewById(R.id.btn_Answer);

        tvProblem = (TextView)findViewById(R.id.tv_problem);
        tvProblem.setText("1 / 50");

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

        //Image num Random 설정
        rnd = new Random();
        List<Integer> list = new ArrayList<>();

        for(int i = 1; i<=51; i++){
            totalName[i] = rnd.nextInt(51)+1;
            for(int j=1; j<i; j++){
                if(totalName[i] == totalName[j]){
                    i--;
                }
            }
        }

        for(int i = 1; i<=51; i++){
            Log.v("***** TotalName 값 *****", String.valueOf(totalName[i]));
        }

        //Mode 설정
        Intent intent = getIntent();
        flag = intent.getExtras().getString("flag");
        tvMode.setText(flag);

        //파이어베이스 인스턴스 생성
        storage = FirebaseStorage.getInstance("gs://quiz-3bf9d.appspot.com/");
        storageRef = storage.getReference().child("Naver/");

        //게임을 시작하지.
        start();

    }

    public void start(){
        handler.post(runnable);
    }

    public String quiz(int i){
        //각 인물 별 Name 지정
        Log.v("***** i 값 *****", String.valueOf(i));
        if(i == 1){
            pathRef = storageRef.child("아이유.png");
        }else if(i == 2){
            pathRef = storageRef.child("백현.png");
        }else if(i == 3){
            pathRef = storageRef.child("강소라.png");
        }else if(i == 4){
            pathRef = storageRef.child("강태오.png");
        }else if(i == 5){
            pathRef = storageRef.child("강한나.png");
        }else if(i == 6){
            pathRef = storageRef.child("거미.png");
        }else if(i == 7){
            pathRef = storageRef.child("고민시.png");
        }else if(i == 8){
            pathRef = storageRef.child("금새록.png");
        }else if(i == 9){
            pathRef = storageRef.child("기영이.png");
        }else if(i == 10){
            pathRef = storageRef.child("이국주.png");
        }else if(i == 11){
            pathRef = storageRef.child("김상호.png");
        }else if(i == 12){
            pathRef = storageRef.child("김성오.png");
        }else if(i == 13){
            pathRef = storageRef.child("다현.png");
        }else if(i == 14){
            pathRef = storageRef.child("디오.png");
        }else if(i == 15){
            pathRef = storageRef.child("미나.png");
        }else if(i == 16){
            pathRef = storageRef.child("박유나.png");
        }else if(i == 17){
            pathRef = storageRef.child("박지현.png");
        }else if(i == 18){
            pathRef = storageRef.child("박초롱.png");
        }else if(i == 19){
            pathRef = storageRef.child("뷔.png");
        }else if(i == 20){
            pathRef = storageRef.child("서은수.png");
        }else if(i == 21){
            pathRef = storageRef.child("선미.png");
        }else if(i == 22){
            pathRef = storageRef.child("손예진.png");
        }else if(i == 23){
            pathRef = storageRef.child("세훈.png");
        }else if(i == 24){
            pathRef = storageRef.child("수호.png");
        }else if(i == 25){
            pathRef = storageRef.child("슬기.png");
        }else if(i == 26){
            pathRef = storageRef.child("시현.png");
        }else if(i == 27){
            pathRef = storageRef.child("영심이.png");
        }else if(i == 28){
            pathRef = storageRef.child("안재홍.png");
        }else if(i == 29){
            pathRef = storageRef.child("요요미.png");
        }else if(i == 30){
            pathRef = storageRef.child("유병재.png");
        }else if(i == 31){
            pathRef = storageRef.child("유아.png");
        }else if(i == 32){
            pathRef = storageRef.child("유재석.png");
        }else if(i == 33){
            pathRef = storageRef.child("이주영.png");
        }else if(i == 34){
            pathRef = storageRef.child("이희진.png");
        }else if(i == 35){
            pathRef = storageRef.child("임형준.png");
        }else if(i == 36){
            pathRef = storageRef.child("장예원.png");
        }else if(i == 37){
            pathRef = storageRef.child("전여빈.png");
        }else if(i == 38){
            pathRef = storageRef.child("전효성.png");
        }else if(i == 39){
            pathRef = storageRef.child("진영.png");
        }else if(i == 40){
            pathRef = storageRef.child("쯔위.png");
        }else if(i == 41){
            pathRef = storageRef.child("찬열.png");
        }else if(i == 42){
            pathRef = storageRef.child("최리.png");
        }else if(i == 43){
            pathRef = storageRef.child("카이.png");
        }else if(i == 44){
            pathRef = storageRef.child("허경환.png");
        }else if(i == 45){
            pathRef = storageRef.child("현아.png");
        }else if(i == 46){
            pathRef = storageRef.child("홍현희.png");
        }else if(i == 47){
            pathRef = storageRef.child("황보라.png");
        }else if(i == 48){
            pathRef = storageRef.child("황치열.png");
        }else if(i == 49){
            pathRef = storageRef.child("김종국.png");
        }else if(i == 50){
            pathRef = storageRef.child("태연.png");
        }else{
            pathRef = storageRef.child("아이린.png");
        }

        Log.v("*****경로 위치1*****", String.valueOf(pathRef));
        pathRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    //이미지 불러오기
                    Log.v("*****이미지 위치*****", String.valueOf(task.getResult()));
                    urlAnswer = String.valueOf(task.getResult());
                    String str = String.valueOf(task.getResult());
                    str = str.substring(77,str.length()-57);
                    try {
                        decode = URLDecoder.decode(str,"UTF-8");
                        Log.v("***** 디코딩 값 *****", decode);
                    } catch (UnsupportedEncodingException e) {
                        Toast.makeText(getApplicationContext(),"디코딩 실패",Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                    Glide.with(SoloQuizActivity.this).load(task.getResult()).override(1024, 980).into(imageView);

                }else {
                    Toast.makeText(getApplicationContext(), "이미지 불러오기 실패", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return decode;
    }

    public void Answer(View view){
        //정답
        editAnswer = (EditText)findViewById(R.id.edit_Answer);
        String ans = editAnswer.getText().toString();

            if(decode.equals(ans)){
                Toast.makeText(getApplicationContext(),"정답입니다~",Toast.LENGTH_SHORT).show();
                editAnswer.setText("");
                num += 1;
                tvProblem.setText(num + " / 50");
            }else{
                Toast.makeText(getApplicationContext(),"실패!",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), AnswerActivity.class);
                intent.putExtra("tvAnswer",decode);
                intent.putExtra("urlAnswer",urlAnswer);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK & Intent.FLAG_ACTIVITY_CLEAR_TOP & Intent.FLAG_ACTIVITY_NEW_TASK);
                handler.removeCallbacks(runnable);
                startActivity(intent);
                finish();
            }
        }
    }






