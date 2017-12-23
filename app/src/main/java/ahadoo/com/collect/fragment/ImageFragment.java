package ahadoo.com.collect.fragment;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import ahadoo.com.collect.QuestionActivity;
import ahadoo.com.collect.R;
import ahadoo.com.collect.model.SurveyQuestion;
import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;

public class ImageFragment extends Fragment {

    private static final int PICTURE_PERMISSIONS = 1;

    public static final int REQUEST_CODE_TAKE_PICTURE = 2;

    public static final String PICTURE_DATA_RESULT_CODE = "take_picture_request_code";

    private static final int REQUEST_CODE_PICK_IMAGE = 3;

    private static final int PERMISSIONS_REQUEST_CODE = 4;

    private SurveyQuestion question;

    @BindView(R.id.preview)
    ImageView preview;

    @BindView(R.id.question)
    TextView titleTextView;

    @BindView(R.id.bullet)
    TextView bulletTextView;

    @BindView(R.id.choose_from_gallery)
    Button chooseFromGalleryButton;

    @BindView(R.id.take_new_picture)
    Button takeNewPictureButton;

    private File previewFile;

    public static ImageFragment getInstance(SurveyQuestion question) {

        ImageFragment fragment = new ImageFragment();

        fragment.question = question;

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if(savedInstanceState != null) {

            question.response = savedInstanceState.getString(QuestionActivity.SAVED_RESPONSE_IDENTIFIER);
        }

        View view = inflater.inflate(R.layout.single_question_layout_image, container, false);

        ButterKnife.bind(this, view);

        titleTextView.setText(question.text.toString());

        bulletTextView.setText(String.format(getString(R.string.bullet), question.index));

        chooseFromGalleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                chooseImageFromGallery();
            }
        });

        if(checkPermissions()) {

            showPreviewFromURI();
        }


        final String dirName = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/survey-pictures/";

        final File dir = new File(dirName);

        String fileName = dir + "survey-response-" + question.surveyUUID + "-" + question.index + ".jpg";

        previewFile = new File(fileName);

        if(!previewFile.exists()) {

            question.response = "";

            try {

                previewFile.createNewFile();

            } catch (IOException e) {}
        }

        dir.mkdirs();

        final Uri outputFileURI = FileProvider.getUriForFile(
                getContext(),
                getContext().getApplicationContext().getPackageName() + ".ahadoo.com.collect.provider",
                previewFile
        );

        takeNewPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Uri outputFileURI = Uri.fromFile(file);

                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileURI);

                startActivityForResult(cameraIntent, REQUEST_CODE_TAKE_PICTURE);
            }
        });

        return view;
    }

    private boolean checkPermissions() {

        List<String> permissions = new ArrayList<>();

        boolean permissionRequested = false;

        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        permissions.add(Manifest.permission.CAMERA);

        for(String permission : permissions) {

            if(ContextCompat.checkSelfPermission(getContext(), permission) != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(permissions.toArray(new String[permission.length()]), PERMISSIONS_REQUEST_CODE);

                permissionRequested = true;
            }
        }

        return ! permissionRequested;
    }

    private void chooseImageFromGallery() {

        Intent intent = new Intent();

        intent.setType("image/*");

        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_CODE_PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            question.response = previewFile.getAbsolutePath();

            switch (requestCode) {

                case REQUEST_CODE_TAKE_PICTURE:

                    showPreviewFromURI();

                    break;

                case REQUEST_CODE_PICK_IMAGE:

                    Uri selectedImage = data.getData();

                    InputStream is = null;

                    if (ContentResolver.SCHEME_CONTENT.equals(selectedImage.getScheme())) {

                        try {

                            is = getContext().getContentResolver().openInputStream(selectedImage);

                        } catch (FileNotFoundException e) {

                            e.printStackTrace();
                        }

                    } else {

                        if (ContentResolver.SCHEME_FILE.equals(selectedImage.getScheme())) {

                            try {

                                is = new FileInputStream(selectedImage.getPath());

                            } catch (FileNotFoundException e) {

                                e.printStackTrace();
                            }
                        }
                    }

                    if(is != null) {

                        writeStreamToFile(is);
                    }

                    showPreviewFromURI();

                    break;
            }
        }
    }

    private void writeStreamToFile(InputStream is) {

        OutputStream os = null;

        try {

            os = new FileOutputStream(previewFile.getAbsoluteFile());

            byte[] buffer = new byte[1024];

            int bytesRead;

            while((bytesRead = is.read(buffer)) != -1) {

                os.write(buffer, 0,bytesRead);
            }

            os.close();

        } catch (FileNotFoundException e) {

            e.printStackTrace();

        } catch(IOException e) {

            e.printStackTrace();

        } finally {

            if(os != null) {

                try {

                    os.flush();

                    os.close();

                } catch (IOException e) {

                    e.printStackTrace();
                }
            }
        }
    }

    private void showPreviewFromURI() {

        Log.d("CameraDemo", "Picture saved");

        if(question.response != null) {

            Bitmap bitmap = BitmapFactory.decodeFile(question.response);

            preview.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(QuestionActivity.SAVED_RESPONSE_IDENTIFIER, question.response);
    }
}