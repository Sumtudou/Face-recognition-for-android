package com.example.a11630.face_new;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class opt extends AppCompatActivity implements View.OnClickListener {

    private String ImagePath = null;
    private Uri imageUri,imageUri_display;
    private int Photo_ALBUM = 1, CAMERA = 2;
    private Bitmap bp = null;

    String result;

    Button btn_pai, btn_xuan;
    ImageView iv_picture;
    TextView tv_sum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.opt);
        btn_pai = (Button) findViewById(R.id.take_a_picture);
        btn_pai.setOnClickListener(this);
        iv_picture = (ImageView) findViewById(R.id.picture);
        btn_xuan = (Button) findViewById(R.id.xuan);
        btn_xuan.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("NewApi")
    @Override
    public void onClick(View v) {          //点击拍照或者从相册选取，返回值为带地址的intent
        if (v.getId() == R.id.take_a_picture) {   ///拍照
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            builder.detectFileUriExposure();            //7.0拍照必加
            File outputImage = new File(Environment.getExternalStorageDirectory() + File.separator + "face.jpg");     //临时照片存储地
            try {                                                                                   //文件分割符
                if (outputImage.exists()) {   //如果临时地址有照片，先清除
                    outputImage.delete();
                }
                outputImage.createNewFile();    ///创建零食地址
            } catch (IOException e) {
                e.printStackTrace();
            }
            imageUri = Uri.fromFile(outputImage);              //获取Uri

         //   imageUri_display= FileProvider.getUriForFile(opt.this,"com.example.a11630.face_new.fileprovider",outputImage);

            ImagePath = outputImage.getAbsolutePath();
            Log.i("拍照图片路径", ImagePath);         //，是传递你要保存的图片的路径，打开相机后，点击拍照按钮，系统就会根据你提供的地址进行保存图片
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);    //跳转相机
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);                          //相片输出路径
            startActivityForResult(intent, CAMERA);                        //返回照片路径

        } else {
            Intent in = new Intent(Intent.ACTION_PICK);      //选择数据
            in.setType("image/*");                     //选择的数据为图片
            startActivityForResult(in, Photo_ALBUM);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 相册选择图片
        if (requestCode == Photo_ALBUM) {
            if (data != null) {       //开启了相册，但是没有选照片
                Uri uri = data.getData();
                //从uri获取内容的cursor
                Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                cursor.moveToNext();
                ImagePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA));   //获得图片的绝对路径
                cursor.close();
                Log.i("图片路径", ImagePath);
                bp = getimage(ImagePath);
              //  iv_picture.setImageBitmap(bp);
                runthreaad();      //开启线程，传入图片
            }
        } else if (requestCode == CAMERA) {

            bp = getimage(ImagePath);
         //  iv_picture.setImageBitmap(bp);
            runthreaad();  //开启线程，传入图片
        }
    }
  //  Bitmap bitmap_1=null;
    private Bitmap getimage(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        //此时返回bm为空
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例


        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
      //  Bitmap bitmap_1=bitmap.copy(Bitmap.Config.ARGB_8888, true);

           return compressImage(bitmap);

    }

    private Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 300) { //循环判断如果压缩后图片是否大于300kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    public byte[] getBytesByBitmap(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(bitmap.getByteCount());
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        return outputStream.toByteArray();
    }


    void runthreaad() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "https://aip.baidubce.com/rest/2.0/face/v3/search";
                try {
                    byte[] bytes1 = getBytesByBitmap(bp);
                    //    byte[] bytes1 = FileUtil.readFileByBytes(ImagePath);
                    String image1 = Base64Util.encode(bytes1);

                    Map<String, Object> map = new HashMap<>();
                    map.put("image", image1);
                    map.put("liveness_control", "NORMAL");
                    map.put("group_id_list", "face");
                    map.put("image_type", "BASE64");
                    map.put("quality_control", "LOW");

                    String param = GsonUtils.toJson(map);

                    // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
                 //   String accessToken = "24.470560ecfc8ded10d622b3dd4e258f34.2592000.1563086633.282335-15236904";

                    String clientId = "HuTNjUbYeHuOBibawwwDHnwN";
                    // 官网获取的 Secret Key 更新为你注册的
                    String clientSecret = "c56Xi5qXmzmNcWxiBH6WdObZsbAGoPVp";
                    String accessToken = getAuth(clientId, clientSecret);

                    result = HttpUtil.post(url, accessToken, "application/json", param);
                    System.out.println("hehehe:" + result);


                    Gson gson = new Gson();                      //新建GSON
                    Search_result_bean Result_bean = gson.fromJson(result, Search_result_bean.class); //GSON与我的工具类绑定
                  //  System.out.println("哈哈哈哈哈哈哈哈" + Result_bean.getError_code());
                    int Error_code = Result_bean.getError_code();
                    if (Error_code == 0) {                     //返回值为零，就是打卡识别成功

                        double score = Result_bean.getResult().getUser_list().get(0).getScore();   //一层层进入，获取到score

                        String user = Result_bean.getResult().getUser_list().get(0).getUser_id();   //获取用户名

                     //   System.out.println("分数：" + score);
                        if (score >= 78.0) {                                  //分数大于78.0分，判断为同一个人，提示打卡成功

                            SQLiteDatabase db;
                            MyHelper ggg = new MyHelper(opt.this);
                            db = ggg.getWritableDatabase();
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                            //  System.out.println(df.format(new Date()));// new Date()为获取当前系统时间
                            ggg.Insert_two(db, "time_id", df.format(new Date()), user);

                            BackTool.SendHttpInsert(user,df.format(new Date()),2);

                            Looper.prepare();
                            Toast.makeText(opt.this, "打卡成功！", Toast.LENGTH_LONG).show();
                            Looper.loop();
                        } else {
                            Looper.prepare();
                            Toast.makeText(opt.this, "打卡失败！照片不在人脸库", Toast.LENGTH_LONG).show();
                            Looper.loop();
                        }
                    } else {
                        String error_message = "打卡失败：" + Result_bean.getError_msg();
                     //   System.out.println("xixixixixixi" + error_message);

                        Looper.prepare();
                        Toast.makeText(opt.this, error_message, Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }

                } catch (Exception e) {
                    Log.i("错误", "hahaha");
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public static String getAuth(String ak, String sk) {
        // 获取token地址
        String authHost = "https://aip.baidubce.com/oauth/2.0/token?";
        String getAccessTokenUrl = authHost
                // 1. grant_type为固定参数
                + "grant_type=client_credentials"
                // 2. 官网获取的 API Key
                + "&client_id=" + ak
                // 3. 官网获取的 Secret Key
                + "&client_secret=" + sk;
        try {
            URL realUrl = new URL(getAccessTokenUrl);
            // 打开和URL之间的连接
            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.err.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String result = "";
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            /**
             * 返回结果示例
             */
            System.err.println("result:" + result);
            JSONObject jsonObject = new JSONObject(result);
            String access_token = jsonObject.getString("access_token");
            return access_token;
        } catch (Exception e) {
            System.err.printf("获取token失败！");
            e.printStackTrace(System.err);
        }
        return null;
    }
}
