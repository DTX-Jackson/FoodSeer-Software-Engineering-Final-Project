package com.blastbeatsandcode.seefood.view;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.blastbeatsandcode.seefood.R;
import com.blastbeatsandcode.seefood.controller.SFController;
import com.blastbeatsandcode.seefood.model.SFImage;
import com.blastbeatsandcode.seefood.utils.FileUtils;
import com.blastbeatsandcode.seefood.utils.Messages;
import com.blastbeatsandcode.seefood.utils.SFConstants;
import com.darsh.multipleimageselect.activities.AlbumSelectActivity;
import com.darsh.multipleimageselect.helpers.Constants;
import com.sun.jna.platform.win32.OaIdl;

import java.io.File;
import java.util.ArrayList;

// Image Pick library used for camera: https://github.com/jrvansuita/PickImage

public class MainActivity extends AppCompatActivity implements SFView {

    // view elements in order of position top to bottom
    private static ImageButton buttonHelp;
    private static ImageButton buttonCamera;
    private static ImageButton buttonUpload;
    private static ImageView imageMainResult;
    private static TextView textNoneUploadedYet;
    private static SeekBar seekbarMainResult;
    private static TextView textMainResult;
    private static TableLayout tableGallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Register with the view
        SFController.getInstance().registerView(this);

        // assign all view elements

        buttonHelp = (ImageButton)findViewById(R.id.buttonHelp);
        buttonCamera = (ImageButton)findViewById(R.id.buttonCamera);
        buttonUpload = (ImageButton)findViewById(R.id.buttonUpload);
        imageMainResult = (ImageView)findViewById(R.id.imageMainResult);
        textNoneUploadedYet = (TextView)findViewById(R.id.textNoneUploadedYet);
        seekbarMainResult = (SeekBar)findViewById(R.id.seekbarMainResult);
        seekbarMainResult.setEnabled(false); // make the seekbar frozen
        textMainResult = (TextView)findViewById(R.id.textMainResult);
        tableGallery = (TableLayout)findViewById(R.id.tableGallery);

        // start all listeners

        helpListener();
        cameraListener();
        uploadListener();

        // initialize
        initialize();
    }


    /**
     * TESTING THE IMAGE VIEW
     *
     */
//    public void showImage() {
//        Dialog builder = new Dialog(this);
//        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        builder.getWindow().setBackgroundDrawable(
//                new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialogInterface) {
//                //nothing;
//            }
//        });
//
//        ImageView imageView = new ImageView(this);
//        Bitmap b = SFController.getInstance().getLastImage().getImageBitmap();
//        System.out.println(b);
//        //imageView.setImageBitmap(SFController.getInstance().getLastImage().getImageBitmap());
//        builder.addContentView(imageView, new RelativeLayout.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.MATCH_PARENT));
//        builder.show();
//    }

    public void initialize(){ // a lot of this should probably be done by controller
        //TODO
        // set imageMainResult to first most recent image from db (or return now if theres none)
            // imageMainResult.setImageResource(R.drawable.my_image); // have first image passed here
        // hide textNoneUploadedYet
        // based on main image, set seekbarMainResult and textMainResult
            // appropriateView(get this somehow,seekbarMainResult,textMainResult );
        // populate the gallery




        // TODO: FIX THIS
        Image[] gallery = new Image[10];
        //TODO have this ^ come from somewhere else and be filled with
        // the latest x (10) images excluding the most recent one
        // also these might want to be an seeFoodImage objects which have data about foodness rather than just
        // images so populating the gallery is easier :)
        populateGallery(gallery);
        appropriateView(5,seekbarMainResult,textMainResult ); //TODO remove later

        // Run through, grab our images down
//        ArrayList<SFImage> t = SFController.getInstance().getBatchImages();
//        for (SFImage i : t) {
//            SFController.getInstance().createImage(i.getImagePath(), i.getFileType());
//        }

        // Get the last uploaded image from the server and set it to the last uploaded image
        //imageMainResult.setImageBitmap(SFController.getInstance().getLastImage().getImageBitmap());
    }

    // TODO: Update this with real images
    private void populateGallery(Image[] gallery) {
        for (Image i: gallery){ //for each image in gallery array
            TableRow row = (TableRow)LayoutInflater.from(MainActivity.this).inflate(R.layout.attrib_row, null);
            ((ImageView)row.findViewById(R.id.galleryImage)).setImageResource(R.drawable.defaultimage);
            ((TextView)row.findViewById(R.id.galleryText)).setText("test");
            ((SeekBar)row.findViewById(R.id.gallerySeekbar)).setEnabled(false);
            tableGallery.addView(row);
            // TODO populate more based on object's attributes
        }

    }

    // for now, foodness is a double out of 10, 0 being not food and 10 being food
    // this function sets the color of text, the content of text, and the seekbar percent
    private void appropriateView(double foodness, SeekBar s, TextView t ){
        // useing just 3 colors for now
        if (foodness < 3.5){
            t.setTextColor(Color.RED);
            t.setText("Not Food");
            s.setProgress((int)foodness*10);
        } else if (foodness>= 3.5 && foodness < 7.5){
            t.setTextColor(Color.YELLOW);
            t.setText("Hard to Say");
            s.setProgress((int)foodness*10);
        } else {
            t.setTextColor(Color.GREEN);
            t.setText("Food!");
            s.setProgress((int)foodness*10);
        }
    }

    public void helpListener(){
        buttonHelp.setOnClickListener(
            new View.OnClickListener() {
                @Override public void onClick(View v) {
                    // Call the Display Help method
                    displayHelp();
                }
            }
        );
    }

    public void cameraListener(){
        buttonCamera.setOnClickListener(
                new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        // Call the Take Picture Method
                        takePicture();
                    }
                }
        );
    }

    public void uploadListener(){
        buttonUpload.setOnClickListener(
                new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        // Call the Upload Image method
                        uploadImage();
                    }
                }
        );
    }


    @Override
    public void uploadImage() {
        Intent intent = new Intent(this, AlbumSelectActivity.class);
        //set limit on number of images that can be selected, default is 10
        //intent.putExtra(Constants.INTENT_EXTRA_LIMIT, numberOfImagesToSelect);
        startActivityForResult(intent, Constants.REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Handle what to do after images have been selected from the gallery
        if (requestCode == Constants.REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            //The array list has the image paths of the selected images
            ArrayList images = data.getParcelableArrayListExtra(Constants.INTENT_EXTRA_IMAGES);
            for (Object image : images) {
                // Send each image to the AI
                String path = ((com.darsh.multipleimageselect.models.Image) image).path;
                File imageFile = new File(path);
                //Messages.makeToast(getApplicationContext(), "IMAGE FILE PATH: " + path);
                SFController.getInstance().addImageToUpload(imageFile);

                String r = SFController.getInstance().sendImageToAI(path, "alex_test");
                Messages.makeToast(getApplicationContext(), r);
            }

            Messages.makeToast(getApplicationContext(), "Number of images in the list: " + SFController.getInstance().getImagesToUpload().size());
        } else if (requestCode == SFConstants.TAKE_PICTURE_REQUEST_CODE && resultCode == RESULT_OK && data!= null) {
            Bitmap image = (Bitmap) data.getExtras().get("data");
            // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
            Uri tempUri = FileUtils.getImageUri(getApplicationContext(), image);

            // CALL THIS METHOD TO GET THE ACTUAL PATH
            File imageFile = new File(FileUtils.getRealPathFromURI(getContentResolver(), tempUri));
            SFController.getInstance().addImageToUpload(imageFile);
            Messages.makeToast(getApplicationContext(), "Number of images in list: " + SFController.getInstance().getImagesToUpload().size());

            // Send the image to the AI with the absolute path
            String absPath = imageFile.getAbsolutePath();
            System.out.println("ABSOLUTE PATH: " + absPath);
            String r = SFController.getInstance().sendImageToAI(absPath, "alex_test");
            Messages.makeToast(getApplicationContext(), r);
        }
    }

    @Override
    public void viewGallery() {
        // TODO: Implement this!
    }

    @Override
    public void displayHelp() {
        // TODO: Implement this!
    }

    @Override
    public void takePicture() {
        // Start the activity for taking a picture
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, SFConstants.TAKE_PICTURE_REQUEST_CODE);
    }


    @Override
    public void update(ArrayList<SFImage> currentImageSet) {
        // Set the main image to the image at the end of the list
        imageMainResult.setImageBitmap(currentImageSet.get(currentImageSet.size() - 1)
                .getImageBitmap());

        // Populate the rest of the images
        for (int currentPos = currentImageSet.size() - 2; currentPos > 0; currentPos--) {
            // TODO: Make this populate the rest of the image gallery
            //       This should already account for the first image in the set being put in main
            //       result.
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // WE NEED TO DELETE OUR TEMP PHOTO CACHE UPON EXIT //

        // Get the files in the directory
        File rootPath = Environment.getExternalStorageDirectory();
        File folder = new File(rootPath + "/DCIM/seefoodtemp/");
        File[] listOfFiles = folder.listFiles();

        // Delete all the images in the folder
        for (File f : listOfFiles) {
            f.delete();
        }

        // Delete the folder (so it never even happened)
        folder.delete();
    }
}
