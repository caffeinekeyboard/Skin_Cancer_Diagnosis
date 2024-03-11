package com.example.lesionlens3;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//import com.example.pblproject1.ml.Mobilenetv2;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private Button button;
    private Button button3;
    private Button button2;
    private TextView textView2;
    //    private EditText editText;
    private Classifier mClassifier;
    private Bitmap mBitmap;
    private final int mInputSize = 224;
    private final String mModelPath = "InceptionResNetV2_Lite_Optimized.tflite";
    private final String mLabelPath = "labels_dictionary.txt";
    //    private final String mSamplePath = "skin-icon.jpg";
    private final int GALLERY_REQ_CODE=1000;
    private final int mCameraRequestCode=100;
    ImageView imageView;
    //    Icon icon;
//    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        button = findViewById(R.id.button);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        imageView=findViewById(R.id.imageView);
        textView2=findViewById(R.id.textview2);
        mClassifier = new Classifier(getAssets(), mModelPath, mLabelPath, mInputSize);

        try {
//            mBitmap = BitmapFactory.decodeStream(getAssets().open(mSamplePath));
            mBitmap = Bitmap.createScaledBitmap(mBitmap, mInputSize, mInputSize, true);
            imageView.setImageBitmap(mBitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }

//        textView=findViewById(R.id.textView);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iGallery=new Intent();
                iGallery.setAction(Intent.ACTION_PICK);
                iGallery.setType("image/*");
                startActivityForResult(iGallery,GALLERY_REQ_CODE);

            }
        });
        button2.setOnClickListener(v -> {
            Intent callCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(callCameraIntent, mCameraRequestCode);
        });

        //Here the model loading code is commented you can load the model
        // in the assets folder in .tflite format
        // and uncomment it to get the desired result

//        button3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mClassifier != null && mBitmap != null) {
//                    try {
//                        Classifier.Recognition result = mClassifier.recognizeImage(mBitmap).stream().findFirst().orElse(null);
//                        if (result != null) {
//                            textView2.setText(result.title + " Confidence:" + result.confidence);
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        Log.e("MainActivity", "Error in onClick", e);
//                    }
//                } else {
//                    Log.e("MainActivity", "mClassifier or mBitmap is null");
//                }
//            }
//        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == mCameraRequestCode) {
            if (resultCode == RESULT_OK && data != null) {
                mBitmap = (Bitmap) data.getExtras().get("data");
                mBitmap = scaleImage(mBitmap);
                Toast toast = Toast.makeText(this, "Image crop to: w= " + mBitmap.getWidth() + " h= " + mBitmap.getHeight(), Toast.LENGTH_LONG);
                toast.setGravity(Gravity.BOTTOM, 0, 20);
                toast.show();
                imageView.setImageBitmap(mBitmap);
                textView2.setText("Your photo image set now click on detect.");
            } else {
                Toast.makeText(this, "Camera cancel..", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == 1000) {
            if (data != null) {
                try {
                    mBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                System.out.println("Success!!!");
                mBitmap = scaleImage(mBitmap);

                imageView.setImageBitmap(mBitmap);
            }
        } else {
            Toast.makeText(this, "Unrecognized request code", Toast.LENGTH_LONG).show();
        }
    }
    private void saveBitmapToFile(Bitmap bitmap, String fileName) {
        File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), fileName);
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
            Toast.makeText(MainActivity.this, "Resized image saved to: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "Error saving resized image", Toast.LENGTH_LONG).show();
        }
    }

    public Bitmap scaleImage(Bitmap bitmap) {
        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        float scaleWidth = (float) mInputSize / originalWidth;
        float scaleHeight = (float) mInputSize / originalHeight;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(bitmap, 0, 0, originalWidth, originalHeight, matrix, true);
    }
}