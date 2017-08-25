package com.fleming.androidintent;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Fleming on 2017/8/21.
 * Contact me: jialongchen5@gmail.com
 * Github: https://github.com/flemingme
 */

public class PhotoHelper {

    private Context mContext;
    private File mPhoto;

    public PhotoHelper(Context context) {
        mContext = context;
    }

    public File createImageFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        try {
            mPhoto = File.createTempFile(imageFileName, ".jpg", storageDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mPhoto;
    }

    public File getPhoto() {
        return mPhoto;
    }

}
