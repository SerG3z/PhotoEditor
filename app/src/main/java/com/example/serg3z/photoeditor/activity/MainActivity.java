package com.example.serg3z.photoeditor.activity;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
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
import com.example.serg3z.photoeditor.fragment.DialogItemResult;
import com.example.serg3z.photoeditor.fragment.DialogSelectPhoto;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;


public class MainActivity extends AppCompatActivity implements DialogSelectPhoto.DialogFragmentListener, DialogItemResult.DialogItemResultListener {

    private Button buttonRotation;
    private Button buttonInvertColors;
    private Button buttonMirrorImage;

    private ImageView imageView;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private FragmentManager fragmentManager;

    private Context context;

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
            BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable().getConstantState().newDrawable();
            Bitmap bitmap = drawable.getBitmap();
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
            Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
            Drawable drawableResult = new BitmapDrawable(rotatedBitmap);
            fillData(drawableResult);
        }
    };

    View.OnClickListener onClickButtonInvertColors = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Drawable drawable = imageView.getDrawable().getConstantState().newDrawable();
            float[] NEGATIVE = {
                    -1.0f, 0, 0, 0, 255,    // red
                    0, -1.0f, 0, 0, 255,    // green
                    0, 0, -1.0f, 0, 255,    // blue
                    0, 0, 0, 1.0f, 0        // alpha
            };
            drawable.setColorFilter(new ColorMatrixColorFilter(NEGATIVE));
            fillData(drawable);
        }
    };

    View.OnClickListener onClickButtonMirrorImage = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
            Bitmap bitmap = drawable.getBitmap();
            Drawable drawableRes = new BitmapDrawable(flipImage(bitmap, 2));
            fillData(drawableRes);
        }
    };

    public Bitmap flipImage(Bitmap src, int type) {
        Matrix matrix = new Matrix();
        if (type == 1) {
            matrix.preScale(1.0f, -1.0f);
        } else if (type == 2) {
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
            Intent takePicture = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(takePicture, 1);
        }
        if (id == 2) {
            new LoadImage().execute("https://www.learn2crack.com/wp-content/uploads/2016/05/cover-tips-1024x483.png");
        }
    }

    @Override
    public void onDialogItemResultClicked(int id, int position) {
        if (id == 0) {
            ((CustomAdapter) adapter).removeData(position);
        }
        if (id == 1) {
            Drawable drawable = ((CustomAdapter) adapter).getDataPosition(position).getConstantState().newDrawable();
            imageView.setImageDrawable(drawable);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        ((CustomAdapter) adapter).setOnItemClickListener(
                new CustomAdapter.ListClickListener() {
                    @Override
                    public void onItemClick(int position, View v) {
                        DialogFragment dialogItemResult = DialogItemResult.newIntent(position);
                        dialogItemResult.show(fragmentManager, "item");
                    }
                });
    }

    private class LoadImage extends AsyncTask<String, String, Bitmap> {
        private Bitmap bitmap;
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Loading Image ....");
            pDialog.show();

        }
        protected Bitmap doInBackground(String... args) {
            try {
                bitmap = BitmapFactory.decodeStream((InputStream)new URL(args[0]).getContent());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap image) {
            if(image != null){
                imageView.setImageBitmap(image);
                pDialog.dismiss();
            }else{
                pDialog.dismiss();
                Toast.makeText(MainActivity.this, "Image Does Not exist or Network Error", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
