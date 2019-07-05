package com.example.a11630.face_new;


import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class delete extends AppCompatActivity implements View.OnClickListener {

    Button btn_confirm;
    EditText et_user;
    String S_user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete);
        btn_confirm=(Button)findViewById(R.id.confirm);
        btn_confirm.setOnClickListener(this);
        et_user=(EditText)findViewById(R.id.user);
    }

    @Override
    public void onClick(View v) {
       S_user=et_user.getText().toString().trim();
       runthreaad();
    }

    void runthreaad()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url ="https://aip.baidubce.com/rest/2.0/face/v3/faceset/user/delete";
                try {
                    Map<String, Object> map = new HashMap<>();
                    map.put("group_id", "face");
                    map.put("user_id", S_user);
                    String param = GsonUtils.toJson(map);
                    // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
                    String accessToken = "24.0ee082c20fc94f47c01a9e73a023af58.2592000.1553952629.282335-15236904";

                    String result = HttpUtil.post(url, accessToken, "application/json", param);
                    System.out.println(result);

                  /*  Looper.prepare();
                    Toast.makeText(delete.this,result,Toast.LENGTH_LONG).show();
                    Looper.loop();*/
                    Gson gson=new Gson();
                    add_result_bean  Result_bean=gson.fromJson(result,add_result_bean.class);

                    System.out.println("哈哈哈哈哈哈哈哈"+ Result_bean.getError_code());
                    int Error_code=Result_bean.getError_code();
                    if(Error_code==0){

                       String message="id=\""+  S_user   +"\"";
                        SQLiteDatabase db;
                        MyHelper ggg= new MyHelper(delete.this);
                        db=ggg.getWritableDatabase();
                        ggg.Delete(db,"name_id",message);


                        BackTool.SendHttpDelete_orQueryAllface(S_user,1);


                        Looper.prepare();
                        Toast.makeText(delete.this,"删除成功" , Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }else{
                        String message="id=\""+  S_user   +"\"";
                        System.out.println("删除:"+message);

                        SQLiteDatabase db;
                        MyHelper ggg= new MyHelper(delete.this);
                        db=ggg.getWritableDatabase();
                        ggg.Delete(db,"name_id",message);

                        BackTool.SendHttpDelete_orQueryAllface(S_user,1);

                        String error_message="删除失败："+Result_bean.getError_msg();
                        System.out.println("xixixixixixi"+ error_message);

                        Looper.prepare();
                        Toast.makeText(delete.this,error_message , Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}