package com.fire.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class SoloQuizActivity extends AppCompatActivity {
    String flag;
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solo_quiz);

        //파이어베이스 참조 생성
        mStorageRef = FirebaseStorage.getInstance().getReference();
        StorageReference naverRef = mStorageRef.child("Naver");

        TextView tvMode = (TextView)findViewById(R.id.tv_mode);



        Intent intent = getIntent();

        flag = intent.getExtras().getString("flag");
        tvMode.setText(flag);

        }

}
