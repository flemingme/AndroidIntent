package com.fleming.androidintent;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.PermissionUtils;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements IAction {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.bt_call)
    Button btCall;
    @BindView(R.id.bt_contact)
    Button btContact;
    @BindView(R.id.bt_msg)
    Button btMsg;
    @BindView(R.id.bt_camera)
    Button btCamera;
    @BindView(R.id.bt_photo)
    Button btPhoto;
    @BindView(R.id.bt_browser)
    Button btBrowser;
    @BindView(R.id.bt_setting)
    Button btSetting;
    @BindView(R.id.bt_file)
    Button btFile;
    @BindView(R.id.bt_dial)
    Button btDial;
    @BindView(R.id.bg_content)
    LinearLayout bgContent;
    private int CALL_REQUEST_CODE = 1;
    private int CAMERA_REQUEST_CODE = 2;
    private int WRITE_REQUEST_CODE = 3;
    private final static int TAKE_PHOTO = 0x1;
    private final static int SELECT_PHOTO = 0x2;
    private final static String TAG = "Main";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class MyRationalHandler extends PermissionUtils.RationaleHandler {

        @Override
        protected void showRationale() {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("提示")
                    .setMessage("申请权限")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            requestPermissionsAgain();
                        }
                    }).show();
        }
    }

    @OnClick({R.id.bt_call, R.id.bt_dial, R.id.bt_contact, R.id.bt_msg, R.id.bt_camera, R.id.bt_photo, R.id.bt_browser, R.id.bt_setting, R.id.bt_file})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_call:
                PermissionUtils.requestPermissions(this, CALL_REQUEST_CODE, new String[]{Manifest.permission.CALL_PHONE},
                        new PermissionUtils.OnPermissionListener() {
                            @Override
                            public void onPermissionGranted() {
                                lunchCall();
                            }

                            @Override
                            public void onPermissionDenied(String[] deniedPermissions) {

                            }
                        }, new MyRationalHandler());
                break;
            case R.id.bt_dial:
                lunchDial();
                break;
            case R.id.bt_contact:
                lunchContact();
                break;
            case R.id.bt_msg:
                lunchMessage();
                break;
            case R.id.bt_camera:
                PermissionUtils.requestPermissions(this, CAMERA_REQUEST_CODE, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        new PermissionUtils.OnPermissionListener() {
                            @Override
                            public void onPermissionGranted() {
                                lunchCamera();
                            }

                            @Override
                            public void onPermissionDenied(String[] deniedPermissions) {

                            }
                        }, new MyRationalHandler());
                break;
            case R.id.bt_photo:
                lunchPhoto();
                break;
            case R.id.bt_browser:
                lunchBrowser();
                break;
            case R.id.bt_setting:
                lunchSetting();
                break;
            case R.id.bt_file:
                lunchFileSystem();
                break;
        }
    }

    @Override
    public void lunchCall() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:10086"));
        startActivity(intent);
    }

    @Override
    public void lunchDial() {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:10086"));
        startActivity(intent);
    }

    @Override
    public void lunchContact() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(ContactsContract.Contacts.CONTENT_URI);
        startActivity(intent);
    }

    @Override
    public void lunchMessage() {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:10086"));
        intent.putExtra("sms_body", "The SMS Text!");
        startActivity(intent);
    }

    private PhotoHelper mHelper;

    @Override
    public void lunchCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            mHelper = new PhotoHelper(this);
            File photoFile = mHelper.createImageFile();
            Uri photoUri;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                photoUri = FileProvider.getUriForFile(this, this.getPackageName() + ".fileprovider", photoFile);
            } else {
                photoUri = Uri.fromFile(photoFile);
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(intent, TAKE_PHOTO);
        }
    }

    @Override
    public void lunchPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, SELECT_PHOTO);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    File photo = mHelper.getPhoto();
                    if (photo != null) {
                        PreviewActivity.preview(this, photo);
                    }
                }
                break;
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    if (uri != null) {
                        Log.d(TAG, "onActivityResult: " + uri.toString());
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                            bgContent.setBackground(new BitmapDrawable(getResources(), bitmap));
                        } catch (IOException e) {
                            LogUtils.e(e);
                        }
                    } else {
                        Bundle extras = data.getExtras();
                        Bitmap image = extras.getParcelable("data");
                        Log.d(TAG, "onActivityResult: " + image.toString());
                    }
                }
                break;
        }
    }

    @Override
    public void lunchBrowser() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://flemingme.github.io"));
        startActivity(intent);
    }

    @Override
    public void lunchSetting() {
        Intent intent = new Intent(Settings.ACTION_SETTINGS);
        startActivity(intent);
    }

    @Override
    public void lunchFileSystem() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivity(intent);
    }

}
