package com.fleming.androidintent;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;

import com.blankj.utilcode.util.ScreenUtils;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Fleming on 2017/8/21.
 * Contact me: jialongchen5@gmail.com
 * Github: https://github.com/flemingme
 */

public class PreviewActivity extends AppCompatActivity {

    private static String EXTRA_PHOTO = "photo";
    @BindView(R.id.iv_result)
    ImageView ivResult;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        File currentPhoto = (File) getIntent().getSerializableExtra(EXTRA_PHOTO);
        int requestWidth = (int) (ScreenUtils.getScreenWidth() * 0.75f);
        int requestHeight = (int) (ScreenUtils.getScreenHeight() * 0.75f);
        Bitmap bitmap = BitmapUtils.decodeBitmapFromFile(currentPhoto, requestWidth, requestHeight);
        Log.i("BitmapUtils", "result height: " + bitmap.getHeight());
        Log.i("BitmapUtils", "result width: " + bitmap.getWidth());
        if (bitmap != null) {
            int degree = BitmapUtils.getBitmapDegree(currentPhoto.getAbsolutePath());
            if (degree != 0) {
                bitmap = BitmapUtils.rotateBitmapByDegree(bitmap, degree);
            }
            ivResult.setImageBitmap(bitmap);
        }
        BitmapUtils.displayToGallery(this, currentPhoto);
    }

    public static void preview(Context context, File file) {
        Intent intent = new Intent(context, PreviewActivity.class);
        intent.putExtra(EXTRA_PHOTO, file);
        context.startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
