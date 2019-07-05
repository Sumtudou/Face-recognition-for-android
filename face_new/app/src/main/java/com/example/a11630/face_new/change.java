package com.example.a11630.face_new;

import android.app.DatePickerDialog;
import android.arch.lifecycle.ViewModelStoreOwner;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class change extends AppCompatActivity implements View.OnClickListener {

    Button btn_search, btn_search_TI, btn_choosetime, btn_confirm;
    TextView search_sum;
    int flag;
    EditText IDS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        flag = 0;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change);
        btn_search = (Button) findViewById(R.id.search_NI);
        btn_search.setOnClickListener(this);
        btn_search_TI = (Button) findViewById(R.id.search_ka);
        btn_search_TI.setOnClickListener(this);
        search_sum = (TextView) findViewById(R.id.tv_sum);

        btn_choosetime = (Button) findViewById(R.id.time);
        btn_choosetime.setOnClickListener(this);

        btn_confirm = (Button) findViewById(R.id.FINALLY);
        btn_confirm.setOnClickListener(this);

        IDS = (EditText) findViewById(R.id.ids);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.search_ka) {
            StringBuffer sum = new StringBuffer();
            SQLiteDatabase db;
            MyHelper ggg = new MyHelper(change.this);
            db = ggg.getWritableDatabase();
            Cursor cursor = db.query("time_id", null,
                    null, null, null, null, null);
            if (cursor.getCount() != 0) {
                cursor.moveToFirst();
                String id = cursor.getString(0);
                String name_time = cursor.getString(1);

                System.out.println("查询结果：\n");
                System.out.println(id + ":" + name_time);
                sum.append("查询结果：" + "\n    ID:" + id + "        time:" + name_time + "\n");

                while (cursor.moveToNext()) {
                    String id1 = cursor.getString(0);
                    String name_time1 = cursor.getString(1);
                    System.out.println("查询结果：");
                    System.out.println(id1 + ":" + name_time1);
                    sum.append("    ID:" + id1 + "        time:" + name_time1 + "\n");
                }
            }
            cursor.close();
            db.close();
            search_sum.setText(sum.toString());

        } else if (v.getId() == R.id.search_NI) {   //face

            ThreadReturn td = new ThreadReturn();
            //1.执行 Callable 方式，需要 FutureTask 实现类的支持，用于接收运算结果。
            FutureTask<String> result = new FutureTask<>(td);
            new Thread(result).start();

            //2.接收线程运算后的结果
            try {
                String BACK = result.get();  //FutureTask 可用于 闭锁 类似于CountDownLatch的作用，在所有的线程没有执行完成之后这里是不会执行的
                String SSS = BackTool.unicodeToString(BACK);
                Log.d("BACKSTRING",SSS);
            } catch (Exception e) {
                e.printStackTrace();
            }

            StringBuffer sum = new StringBuffer();
            SQLiteDatabase db;
            MyHelper ggg = new MyHelper(change.this);
            db = ggg.getWritableDatabase();
            Cursor cursor = db.query("name_id", null,
                    null, null, null, null, null);
            if (cursor.getCount() != 0) {
                cursor.moveToFirst();
                String id = cursor.getString(0);
                String name_time = cursor.getString(1);
                System.out.println("查询结果：");
                System.out.println(id + ":" + name_time);
                sum.append("查询结果：" + "\n    ID:" + id + "        name:" + name_time + "\n");

                while (cursor.moveToNext()) {
                    String id1 = cursor.getString(0);
                    String name_time1 = cursor.getString(1);
                    System.out.println("查询结果：");
                    System.out.println(id1 + ":" + name_time1);
                    sum.append("    ID:" + id1 + "        name:" + name_time1 + "\n");
                }
            }
            cursor.close();
            db.close();
            search_sum.setText(sum.toString());
        } else if (v.getId() == R.id.time) {
            final Calendar c = Calendar.getInstance();
            DatePickerDialog dialog = new DatePickerDialog(change.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    c.set(year, monthOfYear, dayOfMonth);
                    btn_choosetime.setText(DateFormat.format("yyy-MM-dd", c));
                }
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
            dialog.show();
            flag = 1;

        } else if (v.getId() == R.id.FINALLY) {
            if (flag == 1) {    //选了日期
                if (IDS.getText().toString().trim().equals("")) {      //只选了日期
                    String TIMES = btn_choosetime.getText().toString().trim();   //日期在此.
                    System.out.println("截取时间在此：" + TIMES);

                    StringBuffer sum = new StringBuffer();
                    SQLiteDatabase db;
                    MyHelper ggg = new MyHelper(change.this);
                    db = ggg.getWritableDatabase();
                    Cursor cursor = db.query("time_id", null,
                            null, null, null, null, null);

                    sum.append("结果如下：");
                    if (cursor.getCount() != 0) {
                        cursor.moveToFirst();
                        String id = cursor.getString(0);
                        String name_time = cursor.getString(1);

                        if (TIMES.substring(0, TIMES.length()).equals
                                (name_time.substring(0, TIMES.length()))) {    //截取前面的部分比较。如2018-12-31
                            System.out.println("查询结果：\n");
                            System.out.println(id + ":" + name_time);
                            sum.append("\n    ID:" + id + "        time:" + name_time + "\n");
                        }

                        while (cursor.moveToNext()) {
                            String id1 = cursor.getString(0);
                            String name_time1 = cursor.getString(1);

                            if (TIMES.substring(0, TIMES.length()).equals
                                    (name_time1.substring(0, TIMES.length()))) {
                                System.out.println(id + ":" + name_time);
                                sum.append("    ID:" + id1 + "        time:" + name_time1 + "\n");
                            }
                        }
                    }
                    cursor.close();
                    db.close();
                    search_sum.setText(sum.toString());
                } else {    //选了日期，选了ID

                    String id = IDS.getText().toString().trim();
                    String TIMES = btn_choosetime.getText().toString().trim();   //日期在此.
                    SQLiteDatabase db;
                    MyHelper ggg = new MyHelper(change.this);
                    db = ggg.getWritableDatabase();

                    StringBuffer sum = new StringBuffer();

                    String sql = "select * from time_id where id=\"" + id + "\"";

                    System.out.println("差部分：" + sql);

                    Cursor cursor = db.rawQuery(sql, null);
                    while (cursor.moveToNext()) {
                        String ID = cursor.getString(0); //获取第一列的值,第一列的索引从0开始
                        String TIME = cursor.getString(1);//获取第二列的值

                        if(TIMES.equals(TIME.substring(0,TIMES.length()))){
                            sum.append("    ID:" + ID + "        time:" + TIME + "\n");
                        }
                    }
                    cursor.close();
                    db.close();
                    search_sum.setText(sum.toString());
                }
            } else {     //flag=0；  //没选日期
                if (IDS.getText().toString().trim().equals("")) {    //啥都没选
                    Toast.makeText(this, "别闹了", Toast.LENGTH_SHORT).show();
                } else {       //只选了ID
                    String id = IDS.getText().toString().trim();    //获取id

                    SQLiteDatabase db;
                    MyHelper ggg = new MyHelper(change.this);
                    db = ggg.getWritableDatabase();

                    StringBuffer sum = new StringBuffer();

                    String sql = "select * from time_id where id=\"" + id + "\"";
                   // System.out.println("差部分：" + sql);
                    Cursor cursor = db.rawQuery(sql, null);
                    while (cursor.moveToNext()) {
                        String ID = cursor.getString(0); //获取第一列的值,第一列的索引从0开始
                        String TIME = cursor.getString(1);//获取第二列的值
                        sum.append("    ID:" + ID + "        time:" + TIME + "\n");
                    }
                    cursor.close();
                    db.close();
                    search_sum.setText(sum.toString());
                }
            }
        }
    }
}
