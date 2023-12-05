package com.example.techarchive;

import java.util.ArrayList;
import java.util.List;
import com.itextpdf.kernel.geom.PageSize;

import com.itextpdf.io.IOException;
import com.itextpdf.io.codec.Base64;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.element.Paragraph;

import android.app.DownloadManager;
import android.content.ClipData;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.UnitValue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class UploadActivity extends AppCompatActivity {

    ImageView uploadImage;
    Button saveButton, addImageButton;
    EditText uploadName, uploadGuest, uploadInfo;
    String imageURL;
    Uri uri;

    List<Uri> imageUris = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        uploadImage = findViewById(R.id.uploadImage);
        uploadName = findViewById(R.id.eventName);
        uploadGuest = findViewById(R.id.guestName);
        uploadInfo = findViewById(R.id.eventInfo);
        saveButton = findViewById(R.id.saveButton);
        addImageButton = findViewById(R.id.addImageButton);

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK){
                            Intent data = result.getData();
                            if (data != null) {
                                if (data.getData() != null) {
                                    // Single image selection
                                    uri = data.getData();
                                    uploadImage.setImageURI(uri);
                                } else if (data.getClipData() != null) {
                                    // Multiple image selection
                                    ClipData clipData = data.getClipData();
                                    imageUris.clear(); // Clear previous selections
                                    for (int i = 0; i < clipData.getItemCount(); i++) {
                                        Uri multipleImageUri = clipData.getItemAt(i).getUri();
                                        imageUris.add(multipleImageUri);
                                    }
                                    // Display the first selected image in the ImageView (customize as needed)
                                    if (!imageUris.isEmpty()) {
                                        uploadImage.setImageURI(imageUris.get(0));
                                    }
                                }
                            }
                        } else {
                            Toast.makeText(UploadActivity.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );


        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });

        Button addImageButton = findViewById(R.id.addImageButton);
        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent multiplePhotoPicker = new Intent(Intent.ACTION_PICK);
                multiplePhotoPicker.setType("image/*");
                multiplePhotoPicker.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                activityResultLauncher.launch(multiplePhotoPicker);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
                generatePdf();
            }
        });
    }

    public void saveData(){

        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Android Image")
                .child(uri.getLastPathSegment());

        AlertDialog.Builder builder = new AlertDialog.Builder(UploadActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_lauout);
        AlertDialog dialog = builder.create();
        dialog.show();

        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete());
                Uri urlImage = uriTask.getResult();
                imageURL = urlImage.toString();
                uploadData();
                dialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
            }
        });
    }
    public void uploadData(){
        String name = uploadName.getText().toString();
        String guest = uploadGuest.getText().toString();
        String info = uploadInfo.getText().toString();

        DataClass dataClass = new DataClass(name, guest, info, imageURL);

        FirebaseDatabase.getInstance().getReference("TechArchive Project").child(name)
                .setValue(dataClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(UploadActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UploadActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public void generatePdf() {
        try {
            // Create a PDF document
            File pdfFile = new File(getExternalFilesDir(null), "report.pdf");
            PdfDocument pdfDocument = new PdfDocument(new PdfWriter(pdfFile));
            PageSize pageSize = PageSize.A4; // or another suitable size
            Document document = new Document(pdfDocument, pageSize);

            // Define a style for bold text
            Style boldStyle = new Style().setBold();

            // Add data to the PDF
            Paragraph eventNameParagraph = new Paragraph()
                    .add(new Text("Event Name: ").addStyle(boldStyle))
                    .add(uploadName.getText().toString());
            document.add(eventNameParagraph);

            Paragraph guestNameParagraph = new Paragraph()
                    .add(new Text("Guest Name: ").addStyle(boldStyle))
                    .add(uploadGuest.getText().toString());
            document.add(guestNameParagraph);

            // Add "Event Info" to the document
            Paragraph eventInfoParagraph = new Paragraph()
                    .add(new Text("Event Info: ").addStyle(boldStyle))
                    .add(uploadInfo.getText().toString());
            document.add(eventInfoParagraph);

            // Add some space
            document.add(new Paragraph("\n"));

            if (uri != null) {
                // Convert Uri to Bitmap
                Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));

                // Convert Bitmap to iText Image
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                Image selectedImage = new Image(ImageDataFactory.create(stream.toByteArray()));

                // Set width and height of the image
                selectedImage.scaleToFit(500f, 500f); // Adjust width and height as needed

                document.add(selectedImage);
            }

            document.add(new Paragraph("\n"));

            // Add multiple images below "Event Info" horizontally using a Table
            if (!imageUris.isEmpty()) {
                // Set the maximum width for each image
                float maxWidth = pageSize.getWidth() - document.getLeftMargin() - document.getRightMargin();

                // Create a Table
                Table imageTable = new Table(new float[imageUris.size()]);

                // Set the gap between images
                float imageGap = 10f;

                float totalImageWidth = 0f;

                for (Uri imageUri : imageUris) {
                    // Convert Uri to Bitmap
                    Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));

                    // Convert Bitmap to iText Image
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    Image image = new Image(ImageDataFactory.create(stream.toByteArray()));

                    // Set width and height of the image
                    image.scaleToFit(200f, 200f); // Adjust width and height as needed

                    // Calculate the total width of images
                    totalImageWidth += image.getImageScaledWidth() + imageGap;

                    // Create a cell with the image and add it to the table
                    Cell cell = new Cell().add(image);
                    cell.setMarginTop(imageGap / 2);
                    cell.setMarginBottom(imageGap / 2);
                    imageTable.addCell(cell);
                }

                // If the total width of the images exceeds the available space, create a new line
                if (totalImageWidth > maxWidth) {
                    document.add(imageTable);
                    document.add(new Paragraph()); // Add an empty line as a gap
                } else {
                    // Add the table to the document
                    document.add(imageTable);
                }
            }

            // Close the document
            document.close();

            // Open the PDF file using an Intent
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri fileUri = FileProvider.getUriForFile(
                    this,
                    getApplicationContext().getPackageName() + ".fileprovider",
                    pdfFile
            );
            intent.setDataAndType(fileUri, "application/pdf");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(intent);

        } catch (Exception e) {
            e.printStackTrace();  // Log the exception stack trace
            Toast.makeText(UploadActivity.this, "Error generating PDF: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}
