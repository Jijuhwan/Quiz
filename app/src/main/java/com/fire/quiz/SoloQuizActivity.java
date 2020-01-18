package com.fire.quiz;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class SoloQuizActivity extends AppCompatActivity {
    String flag;

    //파이어베이스 인스턴스 변수
    FirebaseStorage storage;
    StorageReference storageRef;
    StorageReference pathRef;
    StorageReference gsRef;
    StorageReference httpsRef;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solo_quiz);

        //파이어베이스 인스턴스 생성
        //storage = FirebaseStorage.getInstance("gs://quiz-3bf9d.appspot.com");
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference().child("아이유.png");
        pathRef = storageRef.child("Naver/아이유.png");
        final StorageReference ref = storage.getInstance().getReference("Naver/아이유.png");

        final ImageView imageView = (ImageView)findViewById(R.id.imageView);




    storageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                                          @Override
                                                          public void onComplete(@NonNull Task<Uri> task) {
                                                              if (task.isSuccessful()) {
                                                                  Toast.makeText(getApplicationContext(), "이미지 불러오기 성공!", Toast.LENGTH_SHORT).show();
                                                                  //Glide.with(SoloQuizActivity.this).load(ref).into(imageView);
                                                                  Glide.with(SoloQuizActivity.this).load(task.getResult()).override(1024, 980).into(imageView);
                                                              } else {
                                                                  Toast.makeText(getApplicationContext(), "이미지 불러오기 실패!", Toast.LENGTH_SHORT).show();
                                                              }
                                                          }
                                                      });
        /*public void onSuccess(Uri uri) {

        }*/





        TextView tvMode = (TextView)findViewById(R.id.tv_mode);



        Intent intent = getIntent();

        flag = intent.getExtras().getString("flag");
        tvMode.setText(flag);

        }

}
