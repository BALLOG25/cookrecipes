package com.example.balaji.cookrecipes;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.HashMap;
import java.util.Scanner;

public class MainActivity extends Activity {

    //control value to signal user's granting of READ_EXTERNAL request
    final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL = 111;


    //Declaring the variables

    TextView txtMsg;
    ListView l1;
    String[] items;
    TextView itemName;
    ImageView icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        applyMarshmallowPermissionModel();

        //plumbing: bind GUI controls to Java classes
        String strPath2SDCard = "n.a.";

        txtMsg = (TextView) findViewById(R.id.txtMsg);
        itemName = (TextView) findViewById(R.id.itemName);
        icon = (ImageView) findViewById(R.id.icon);


        try {


            //photo captions are held in a (single-line) comma-separated text file
            File nameFile = new File(strPath2SDCard + "/Pictures/foodnames.txt");

            // VERSION2. Reaching standard Android file organization entry-points

            String strPath2Pictures = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();
            nameFile = new File(strPath2Pictures, "foodnames.txt");

            Scanner scanner = new Scanner(nameFile);
            String line = "";
            while (scanner.hasNext()) {
                line += scanner.nextLine();
                Log.e("SPY", "This is message--> " + line );
            }
            items = line.split(",");

            String pathThumbnailsFolder = new File(strPath2Pictures, "/thumbnails").getAbsolutePath();
            File sdPictureFiles = new File(pathThumbnailsFolder);
            File[] thumbnailArray = sdPictureFiles.listFiles();

            //txtMsg.append("\nNum files: " + thumbnailArray.length);

            String pathLargeFolder = new File(strPath2Pictures, "/Large").getAbsolutePath();
            File sdPictureLargeFiles = new File(pathLargeFolder);
            File[] LargeArray = sdPictureLargeFiles.listFiles();

            String pathInstructionFolder = new File(strPath2Pictures, "/instruction").getAbsolutePath();
            File sdPictureInstructionFiles = new File(pathInstructionFolder);
            File[] InstructionArray = sdPictureInstructionFiles.listFiles();
            // the arguments of the custom adapter are:

            // activity-context, layout-to-be-inflated, top labels, icons, description

            CustomIconLabelAdapter adapter = new CustomIconLabelAdapter(this, MainActivity.this,
                    R.layout.custom_list, items,thumbnailArray, InstructionArray,LargeArray);

            // bind intrinsic ListView to custom adapter
            l1 = (ListView) findViewById(R.id.list_item);
            l1.setAdapter(adapter);

        }catch (Exception e){
            txtMsg.append("\nError: \nPATH=" + strPath2SDCard + "\n" + e.getMessage());
        }
    }//Oncreate

    // This method enforces the new permission policy introduced in API23
    private void applyMarshmallowPermissionModel() {

        //if we already have permission to use the external SD card then do nothing!
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED)
            return;

        // CAUTION: We DO NOT have the necessary permissions
        // Should an explanation be given to the user regarding usage of external SD card?
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            // Show an ASYNCHRONOUS explanation to the user, this thread will continue
            // cancel app if user doesn't allow reading from SD card
            Toast.makeText(this, "Explanation...", Toast.LENGTH_LONG).show();

        }

        // request the permission to use SD card.
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                MY_PERMISSIONS_REQUEST_READ_EXTERNAL);


    }//applyNewPermissionModel

    // /////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[],
                                           int[] grantResults) {

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted - call onCreate again to setup any pending widgets
                    onCreate(null);
                } else {

                    // permission denied
                }

                return;
            }

        }
    }//onRequestPermissionResult
}//Activity
