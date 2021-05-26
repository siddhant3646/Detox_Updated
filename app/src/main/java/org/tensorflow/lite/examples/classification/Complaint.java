package org.tensorflow.lite.examples.classification;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.tensorflow.lite.examples.classification.tflite.Classifier;

public class Complaint extends AppCompatActivity {
    Button btnbrowse, btnupload;
    TextView BTH;
    EditText txtdata ;
    EditText txtdata2 ;
    EditText txtdata3 ;
    EditText txtdata4;
    ImageView imgview;
    Uri FilePathUri;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    int Image_Request_Code = 7;
    ProgressDialog progressDialog ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint);
        storageReference = FirebaseStorage.getInstance().getReference("Images");
        databaseReference = FirebaseDatabase.getInstance().getReference("Images");
        btnbrowse = (Button)findViewById(R.id.btnbrowse);
        BTH=findViewById(R.id.backtohome);
        btnupload= (Button)findViewById(R.id.btnupload);
        txtdata = (EditText)findViewById(R.id.txtdata);
        txtdata2 = (EditText)findViewById(R.id.txtdata2);
        txtdata3 = (EditText)findViewById(R.id.txtdata3);
        txtdata4 = (EditText)findViewById(R.id.txtdata4);
        imgview = (ImageView)findViewById(R.id.image_view);
        BTH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i1 = new Intent(Complaint.this, home.class);
                startActivity(i1);
            }
        });
        progressDialog = new ProgressDialog(Complaint.this);


        btnbrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), Image_Request_Code);

            }
        });
        btnupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String TempImageName11 = txtdata.getText().toString().trim();
                String TempImageName21 = txtdata2.getText().toString().trim();
                String TempImageName31 = txtdata3.getText().toString().trim();
                String TempImageName41 = txtdata4.getText().toString().trim();

                if (TextUtils.isEmpty(TempImageName11)) {
                    txtdata.setError("Please enter name");
                    return;
                }
                if (TextUtils.isEmpty(TempImageName21)) {
                    txtdata2.setError("Please enter name");
                    return;
                }
                if (TextUtils.isEmpty(TempImageName31)) {
                    txtdata3.setError("Please enter name");
                    return;
                }
                if (TextUtils.isEmpty(TempImageName41)) {
                    txtdata4.setError("Please enter name");
                    return;
                }
                else {
                    UploadImage();
                }
            }
        });
        }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Image_Request_Code && resultCode == RESULT_OK && data != null && data.getData() != null) {

            FilePathUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), FilePathUri);
                imgview.setImageBitmap(bitmap);
            }
            catch (IOException e) {

                e.printStackTrace();
            }
        }
    }


    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;

    }


    public void UploadImage() {

        if (FilePathUri != null) {

            progressDialog.setTitle("Image is Uploading...");
            progressDialog.show();
            StorageReference storageReference2 = storageReference.child(System.currentTimeMillis() + "." + GetFileExtension(FilePathUri));
            storageReference2.putFile(FilePathUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            String TempImageName = txtdata.getText().toString().trim();
                            String TempImageName2 = txtdata2.getText().toString().trim();
                            String TempImageName3 = txtdata3.getText().toString().trim();
                            String TempImageName4 = txtdata4.getText().toString().trim();
                                progressDialog.dismiss();
                                Toast.makeText(Complaint.this, "Complaint Registered Succesfully", Toast.LENGTH_LONG).show();
                                @SuppressWarnings("VisibleForTests")
                                uploadinfo imageUploadInfo = new uploadinfo(TempImageName, TempImageName2, TempImageName3, TempImageName4, taskSnapshot.getUploadSessionUri().toString());
                                String ImageUploadId = databaseReference.push().getKey();
                                databaseReference.child(ImageUploadId).setValue(imageUploadInfo);

                        }
                    });
        }
        else {

            Toast.makeText(Complaint.this, "Please Select Image or Add Image Name", Toast.LENGTH_LONG).show();

        }
    }
    public class uploadinfo {

        public String imageName;
        public String imageEmail;
        public String imageAddress;
        public String ImageDetails;
        public String imageURL;
        public uploadinfo(){}


        public uploadinfo(String name,String email ,String  address,String details, String url) {
            this.imageName = name;
            this.imageAddress=address;
            this.imageEmail=email;
            this.ImageDetails=details;
            this.imageURL = url;
        }

        public String getImageName() {
            return imageName;
        }
        public String getImageURL() {
            return imageURL;
        }

    }
}
