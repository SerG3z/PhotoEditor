package com.example.serg3z.photoeditor.activity;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.serg3z.photoeditor.R;
import com.example.serg3z.photoeditor.fragment.DialogSelectPhoto;

public class MainActivity extends AppCompatActivity implements DialogSelectPhoto.DialogFragmentListener {

    Button buttonRotation;
    Button buttonInvertColors;
    Button buttonMirrorImage;

    ImageView imageView;
    ListView listView;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonRotation = (Button) findViewById(R.id.buttonRotation);
        buttonInvertColors = (Button) findViewById(R.id.buttonInvertColor);
        buttonMirrorImage = (Button) findViewById(R.id.buttonMirrorImage);
        imageView = (ImageView) findViewById(R.id.image);
        listView = (ListView) findViewById(R.id.listview);

        buttonRotation.setOnClickListener(onClickButtonRotation);
        buttonInvertColors.setOnClickListener(onClickButtonInvertColors);
        buttonMirrorImage.setOnClickListener(onClickButtonMirrorImage);
        imageView.setOnClickListener(onClickImageView);

        fragmentManager = getFragmentManager();
    }

    View.OnClickListener onClickButtonRotation = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(getApplicationContext(), "rotation", Toast.LENGTH_SHORT).show();
        }
    };

    View.OnClickListener onClickButtonInvertColors = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(getApplicationContext(), "invert", Toast.LENGTH_SHORT).show();
        }
    };

    View.OnClickListener onClickButtonMirrorImage = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(getApplicationContext(), "mirror", Toast.LENGTH_SHORT).show();
        }
    };

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
