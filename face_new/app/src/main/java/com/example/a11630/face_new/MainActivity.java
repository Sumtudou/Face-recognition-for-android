package com.example.a11630.face_new;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    Button btn_change,btn_opt,btn_in,btn_delete,btn_out;
    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    @SuppressLint("NewApi")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       btn_change=findViewById(R.id.change);
        btn_change.setOnClickListener(this);

        btn_in=findViewById(R.id.in);
        btn_in.setOnClickListener(this);

        btn_opt=findViewById(R.id.opt);
        btn_opt.setOnClickListener(this);

       btn_delete=findViewById(R.id.delete);
        btn_delete.setOnClickListener(this);

        btn_out=findViewById(R.id.out);
        btn_out.setOnClickListener(this);

        MyHelper hhh=new MyHelper(MainActivity.this);
        readRequest();
    }


    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.change){
            Intent in=new Intent(this,change.class);
            startActivity(in);
        }
        else if(v.getId()==R.id.in){
            Intent in=new Intent(this,in.class);
            startActivity(in);
        }else  if(v.getId()==R.id.opt){
            Intent in=new Intent(this,opt.class);
            startActivity(in);
        }else if(v.getId()==R.id.delete){
            Intent in=new Intent(this,delete.class);
            startActivity(in);
        }else{
            System system = null;
            system.exit(0);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    void readRequest() {             //获取相机拍摄读写权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA}, 1);
            }
        }
    }
}
