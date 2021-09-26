package com.example.pickpacklogin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.DuplicateFormatFlagsException;
import java.util.HashMap;
import java.util.Objects;

public class AddPostActivity extends AppCompatActivity {

    TextView cancel,post;
    ImageView selected_image;
    Button pickImage;
    EditText description;
    ProgressDialog progressDialog;

    Uri imageUri;
    String url;
    public static final int PICK_IMAGE=100;

    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference reference;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);


        init();


        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();

        storageReference= FirebaseStorage.getInstance().getReference().child("Post images/");

        pickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent("image/*");
                startActivityForResult(intent,PICK_IMAGE);
            }
        });

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageUri == null)
                {
                    Toast.makeText(AddPostActivity.this, "No image selected", Toast.LENGTH_SHORT).show();
                }else
                {
                    uploadPost();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==PICK_IMAGE && resultCode == RESULT_OK && data.getData() !=null)
        {
            imageUri=data.getData();
            selected_image.setImageURI(imageUri);
        }else
        {
            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
            selected_image.setImageResource(R.drawable.ic_add);
        }
    }

    private void init()
    {
        cancel = findViewById(R.id.cancel_post);
        post = findViewById(R.id.upload_post);
        selected_image = findViewById(R.id.selected_image);
        pickImage = findViewById(R.id.pick_image);
        description = findViewById(R.id.description);
        progressDialog = new ProgressDialog(this);
    }

    private void uploadPost()
    {

        progressDialog.setTitle("New Post");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if (imageUri !=null)
        {
            StorageReference sRef = storageReference.child(System.currentTimeMillis()+"."+getExtensionFile(imageUri));
            sRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    sRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            url = uri.toString();

                            reference = FirebaseDatabase.getInstance().getReference().child("Posts");

                            String postid=reference.push().getKey();
                            HashMap<String, Object> map =new HashMap<>();
                            map.put("postid",postid);
                            map.put("postImage",url);
                            map.put("description",description.getText().toString());
                            map.put("publisher",user.getUid());

                            progressDialog.dismiss();
                            reference.child(postid).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful())
                                    {
                                        Toast.makeText(AddPostActivity.this, "Post uploaded!", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(AddPostActivity.this,HomeActivity.class));
                                        finish();
                                    }else
                                    {
                                        Toast.makeText(AddPostActivity.this, "failed"+task.getException(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });




                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {



                            /*Toast.makeText(AddPostActivity.this, "failed"+e.getMessage(), Toast.LENGTH_SHORT).show();*/
                            progressDialog.dismiss();
                        }
                    });
                }
            });
        }










    }

    public String getExtensionFile(Uri uri)
    {
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap map = MimeTypeMap.getSingleton();
        return map.getExtensionFromMimeType(contentResolver.getType(uri));
    }
}