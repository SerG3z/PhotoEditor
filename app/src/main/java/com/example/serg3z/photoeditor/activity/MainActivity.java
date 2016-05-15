package com.example.serg3z.photoeditor.activity;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.serg3z.photoeditor.R;
import com.example.serg3z.photoeditor.adapter.CustomAdapter;
import com.example.serg3z.photoeditor.fragment.DialogSelectPhoto;


public class MainActivity extends AppCompatActivity implements DialogSelectPhoto.DialogFragmentListener {

    Button buttonRotation;
    Button buttonInvertColors;
    Button buttonMirrorImage;

    ImageView imageView;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    FragmentManager fragmentManager;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();

        buttonRotation = (Button) findViewById(R.id.buttonRotation);
        buttonInvertColors = (Button) findViewById(R.id.buttonInvertColor);
        buttonMirrorImage = (Button) findViewById(R.id.buttonMirrorImage);
        imageView = (ImageView) findViewById(R.id.image);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        buttonRotation.setOnClickListener(onClickButtonRotation);
        buttonInvertColors.setOnClickListener(onClickButtonInvertColors);
        buttonMirrorImage.setOnClickListener(onClickButtonMirrorImage);
        imageView.setOnClickListener(onClickImageView);

        fragmentManager = getFragmentManager();

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new CustomAdapter();
        recyclerView.setAdapter(adapter);
    }

    private void fillData(Drawable drawable) {
        ((CustomAdapter) adapter).addData(drawable);
    }

    View.OnClickListener onClickButtonRotation = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
            Bitmap bitmap = drawable.getBitmap();
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
            Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap , 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
            Drawable drawableResult = new BitmapDrawable(rotatedBitmap);
            fillData(drawableResult);
            Toast.makeText(getApplicationContext(), "rotation", Toast.LENGTH_SHORT).show();
        }
    };

    View.OnClickListener onClickButtonInvertColors = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Drawable drawable = imageView.getDrawable().getConstantState().newDrawable();
            float[] NEGATIVE = {
                    -1.0f, 0,     0,     0,    255, // red
                    0,     -1.0f, 0,     0,    255, // green
                    0,     0,     -1.0f, 0,    255, // blue
                    0,     0,     0,     1.0f, 0  // alpha
            };
            drawable.setColorFilter(new ColorMatrixColorFilter(NEGATIVE));
            fillData(drawable);
            Toast.makeText(getApplicationContext(), "invert", Toast.LENGTH_SHORT).show();
        }
    };

    View.OnClickListener onClickButtonMirrorImage = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
            Bitmap bitmap = drawable.getBitmap();
            Drawable drawableRes = new BitmapDrawable(flipImage(bitmap, 2));
            fillData(drawableRes);
            Toast.makeText(getApplicationContext(), "mirror", Toast.LENGTH_SHORT).show();
        }
    };

    public Bitmap flipImage(Bitmap src, int type) {
        Matrix matrix = new Matrix();
        if(type == 1) {
            matrix.preScale(1.0f, -1.0f);
        }
        else if(type == 2) {
            matrix.preScale(-1.0f, 1.0f);
        } else {
            return null;
        }
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
    }


    View.OnClickListener onClickImageView = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DialogFragment dialogFragment = new DialogSelectPhoto();
            dialogFragment.show(fragmentManager, "dialog");
        }
    };

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageReturnedIntent.getData();
                    imageView.setImageURI(selectedImage);
                }

                break;
            case 1:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageReturnedIntent.getData();
                    imageView.setImageURI(selectedImage);
                }
                break;
        }
    }

    @Override
    public void onDialogFragmentClicked(int id) {
        if (id == 0) {
            Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(takePicture, 0);
        }
        if (id == 1) {
            Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(pickPhoto, 1);
        }
    }
}
