 package com.gmail.peeman34.eglisaofficial;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Criteria;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageActivity;
import com.theartofdev.edmodo.cropper.CropImageView;

/**
 * Created by pee on 3/28/2017.
 */

public class ACCOUNTSETUP extends AppCompatActivity {

    private ImageButton setupImageButton;
    private EditText mNameField;
    private Button msubmitButton;

    private static final int GALLERY_REQUEST = 1;
    private  Uri mimageUri = null;
    private DatabaseReference mDatabaseUsers;
    private StorageReference mStorageImage;
    private FirebaseAuth mAuth;

    private ProgressDialog mProgress;


    public ACCOUNTSETUP() {
        super();
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.accountsetup);

        setupImageButton = (ImageButton) findViewById(R.id.setupimageby);
        mNameField = (EditText) findViewById(R.id.setupname);

        msubmitButton = (Button)findViewById(R.id.setupbutton);

        mProgress = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();

        mStorageImage = FirebaseStorage.getInstance().getReference().child("Profile_images");

        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");

        mDatabaseUsers.keepSynced(true);


        setupImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);

            }
        });



        msubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startSetupAccount();
            }
        });


        //      setupaccouont.start();


    }


/*
       public  void setuptheaccount(){
           handler.post(new Runnable() {
               @Override
               public void run() {



               }
           });
       }

*/

   /* Thread setupaccouont = new Thread() {

        @Override
        public void run() {

            try {
                sleep(1000);


                Looper.prepare();
                handler = new Handler();
                Looper.loop();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }



    };

*/


    private void startSetupAccount() {

     final    String name = mNameField.getText().toString().trim();
        final  String user_id = mAuth.getCurrentUser().getUid();


        if (!TextUtils.isEmpty(name) && mimageUri != null) {

            mProgress.setMessage("Finishing setup... ");
            mProgress.show();

            StorageReference filepath = mStorageImage.child(mimageUri.getLastPathSegment());
            filepath.putFile(mimageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    String downloadUri = taskSnapshot.getDownloadUrl().toString();

                    mDatabaseUsers.child(user_id).child("name").setValue(name);
                    mDatabaseUsers.child(user_id).child("image").setValue(downloadUri);




                }
            });

        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {

            Uri imageUri = data.getData();

          CropImage.activity(imageUri).setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(1,1).start(this);


        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
                 if (resultCode == RESULT_OK){

                     mimageUri  = result.getUri();

                     setupImageButton.setImageURI(mimageUri);

                 }else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){

                     Exception error = result.getError();
                 }

        }








   /*        if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
              if (resultCode == RESULT_OK){
                  Uri mimageUri = result.getUri();
                  setupImageButton.setImageURI(mimageUri);

              } else  if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                  Exception error = result.getError();
                  
              }

        }


    }
*/
    }
}