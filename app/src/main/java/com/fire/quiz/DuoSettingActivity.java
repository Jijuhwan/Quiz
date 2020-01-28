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

    //갤러리 다중 선택
    int PICK_FROM_ALBUM = 1;
    File tempFile;
    String imageEncoded;
    List<String> imagesEncodedList;

    //뷰페이저
    private ViewPager viewPager;
    private PictureAdapter pictureAdapter;

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_duo_setting);

        //imgStroage = (ImageView)findViewById(R.id.iv_Stroage);
        Button btnPicture = (Button)findViewById(R.id.btn_picture);
        Button btnStart = (Button)findViewById(R.id.btn_start);

        viewPager = (ViewPager)findViewById(R.id.view);
        pictureAdapter = new PictureAdapter(this);
        viewPager.setAdapter(pictureAdapter);

        //사진 선택
        btnPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //권한 요청
                //permissionCheck();
                //갤러리에서 이미지 가져오기
                getImage();
                /*Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMAGE_MULTIPLE);*/
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

    private void getImage(){
        /*Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_FROM_ALBUM);*/
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,PICK_FROM_ALBUM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try{
            if(requestCode == PICK_FROM_ALBUM && resultCode == RESULT_OK && null != data){
                Uri uri = data.getData();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                int nh = (int)(bitmap.getHeight()*(1024.0/bitmap.getWidth()));
                Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 1024, nh, true);
                imgStroage.setImageBitmap(scaled);
            }
            else{
                Toast.makeText(this,"실패!",Toast.LENGTH_SHORT).show();
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        /*if (data == null) {
            Toast.makeText(this, "null 입니다.", Toast.LENGTH_SHORT).show();
        }else{
            ClipData clipData = data.getClipData();
            Log.i("ClipData 값 : ", String.valueOf(clipData.getItemCount()));

            if(clipData.getItemCount() > 10){
                Toast.makeText(getApplicationContext(),"사진은 10장까지 선택 가능합니다.",Toast.LENGTH_SHORT).show();
            }else if(clipData.getItemCount() == 1){
                Path imagePath = getPath(clipData.getItemAt(0).getUri());
            }
        }
        if(requestCode == PICK_FROM_ALBUM){
            Uri photoUri = data.getData();
            Log.d("포토 URL : ", String.valueOf(photoUri));
            Cursor cursor = null;

            try {
                String[] proj = { MediaStore.Images.Media.DATA};
                assert photoUri != null;
                cursor = getContentResolver().query(photoUri,proj,null,null,null);

                assert cursor != null;
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

                cursor.moveToFirst();
                tempFile = new File(cursor.getString(column_index));

            }finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
            try {
                InputStream in = getContentResolver().openInputStream(data.getData());
                Bitmap img = BitmapFactory.decodeStream(in);
                in.close();
                imgStroage.setImageBitmap(img);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
*/
    }
}
