package com.example.composeweb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.composeweb.data.citySQL;
import com.zaaach.citypicker.CityPicker;
import com.zaaach.citypicker.adapter.OnPickListener;
import com.zaaach.citypicker.model.City;

public class CityPickerActivity extends AppCompatActivity {
    private citySQL dbHelper;
    private ScrollView scrollView;
    private LinearLayout container;

private ImageView addImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("OUTaaa","aaa");
        setContentView(R.layout.activity_city_picker);
        super.onCreate(savedInstanceState);
        scrollView = findViewById(R.id.scrollView);
        container = findViewById(R.id.container);
        addImageView = findViewById(R.id.add);
        //数据库处理
        dbHelper = new citySQL(this, "City.db", null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        insertIfTableEmpty(db,"city");
        Cursor cursor = db.query(true, "city", null, null, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
                addCardView(name);
            } while (cursor.moveToNext());
        } else {
            Log.d("OUTaaa", "No data found");
        }
        addImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("OUTaaa","Clicked");
               CityPicker.from(CityPickerActivity.this)
                       .setOnPickListener(new OnPickListener() {
                           @Override
                           public void onPick(int position, City data) {
                               ContentValues values = new ContentValues();
                               values.put("name", data.getName());
                               addCardView(data.getName());
                               db.insert("city", null, values);
                           }

                           @Override
                           public void onCancel() {
                               Toast.makeText(getApplicationContext(), "取消选择", Toast.LENGTH_SHORT).show();
                           }
                           @Override
                           public void onLocate() {

                           }
                           })
                           .show();
            }
        });
    }
    public boolean isTableEmpty(SQLiteDatabase db, String tableName) {
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + tableName, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count == 0;
    }
    public void insertIfTableEmpty(SQLiteDatabase db, String tableName) {
        if (isTableEmpty(db, tableName)) {
            ContentValues values = new ContentValues();
            values.put("name", "北京");
           db.insert(tableName, null, values);
        }
    }

    public void addCardView(String name1) {
        CardView cardView = new CardView(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 300);
        layoutParams.setMargins(160, 16, 160, 16);
        cardView.setLayoutParams(layoutParams);

        TextView textView = new TextView(this);
        LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        textView.setLayoutParams(layoutParams1);
        textView.setText(name1);
        textView.setTextSize(30);
        textView.setPadding(16, 16, 16, 0);
        textView.setTextColor(ContextCompat.getColor(this, com.zaaach.citypicker.R.color.cp_color_gray_deep));
        layoutParams1.gravity = Gravity.CENTER;
        textView.setGravity(Gravity.CENTER);
        cardView.addView(textView);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cardView.setCardElevation(8);
            cardView.setUseCompatPadding(true);
        }

        container.addView(cardView);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CityPickerActivity.this, MainActivity.class);
                intent.putExtra("cityName", name1);
                startActivity(intent);
            }
        });
        cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
            SQLiteDatabase db=dbHelper.getWritableDatabase();
            String[] whereArgs={"name1"};
            db.delete("city","name=?", whereArgs);
                return true;
            }
        });
    }

}