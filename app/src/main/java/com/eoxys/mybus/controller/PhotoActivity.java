package com.eoxys.mybus.controller;

import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eoxys.mybus.R;
import com.eoxys.mybus.adapter.stop_image_adapter;
import com.eoxys.mybus.model.stop_image_model;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class PhotoActivity extends AppCompatActivity {

    TextView textheader, name,no_data;
    ImageButton imageButton;
    protected static final int REQUEST_CAMERA = 100;
    private static final int PICTURE_RESULT = 1888;
    String stopname;
    String picname;
    Uri imageUri;
    Bitmap thumbnail;
    File myDir;
    String fname;
    String uploadFileName = null;
    File file;
    String imagefile;
    String Invo_path;
    final  String root = Environment.getExternalStorageDirectory().toString();

    private RecyclerView recyclerView;
    public List<stop_image_model> my_image_list = new ArrayList<>();
    private RecyclerView.Adapter adapter;
    File myFileDirectory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        my_image_list.clear();

        name = findViewById(R.id.stopname);
        no_data = (TextView) findViewById(R.id.no_data);
        textheader = findViewById(R.id.textheader);
        imageButton = findViewById(R.id.imageButton);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_bus_stop);

       recyclerView.setLayoutManager(new LinearLayoutManager(this));



        getIncomingIntent();
        read_data_from_file();

    }
public void read_data_from_file()
{
    myFileDirectory = null;
    try {
        myFileDirectory = new File(root + "/DCIM/MyBus/"+stopname);
        if (myFileDirectory.exists())
        {
           File[] file_data =myFileDirectory.listFiles();
           for(File file : file_data)
           {

               String file_name = file.getName();
               String file_path = file.getAbsolutePath();

               stop_image_model model = new stop_image_model();
               model.setIamge_name(file_name);
               model.setImage_location(file_path);
               my_image_list.add(model);

           }
            adapter = new stop_image_adapter(my_image_list,getApplicationContext());
            recyclerView.setAdapter(adapter);
        }
        else
        {
            no_data.setVisibility(View.VISIBLE);
        }

    }
    catch (Exception e) {
        e.printStackTrace();
    }
}
    private void getIncomingIntent(){

        if(getIntent().hasExtra("stop_name")){

            stopname = getIntent().getStringExtra("stop_name");

            textheader.setText(stopname);

            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    showimagedialog();

                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICTURE_RESULT)
            if (resultCode == Activity.RESULT_OK)
            {
                try
                {
                    thumbnail = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                    SaveImage(thumbnail);
                    my_image_list.clear();
                    read_data_from_file();

                } catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
    }

    public void Takepic(){
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, PICTURE_RESULT);
    }

    public void SaveImage(Bitmap showedImgae) {


        myDir = new File(root + "/DCIM/MyBus/"+stopname);
        if(!myDir.exists()){
        myDir.mkdirs();}
        fname = picname + ".jpeg";
        uploadFileName = fname;
        file = new File(myDir, fname);
        imagefile = file.toString();
        Invo_path = file.getPath();
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            showedImgae.compress(Bitmap.CompressFormat.JPEG, 90, out);
            Toast.makeText(this, "Image Saved", Toast.LENGTH_SHORT).show();
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(file);
        mediaScanIntent.setData(contentUri);
        getApplicationContext().sendBroadcast(mediaScanIntent);
    }

    private void showimagedialog(){

        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.dialogimage_name, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // get user input and set it to result
                                // edit text
                                picname = userInput.getText().toString();
                                Takepic();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }
}
