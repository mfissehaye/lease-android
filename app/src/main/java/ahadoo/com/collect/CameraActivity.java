package ahadoo.com.collect;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.wonderkiln.camerakit.CameraKit;
import com.wonderkiln.camerakit.CameraKitError;
import com.wonderkiln.camerakit.CameraKitEvent;
import com.wonderkiln.camerakit.CameraKitEventListener;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraKitVideo;
import com.wonderkiln.camerakit.CameraView;

import ahadoo.com.collect.fragment.ImageFragment;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CameraActivity extends AppCompatActivity {

    @BindView(R.id.camera)
    CameraView cameraView;

    @BindView(R.id.take_new_picture)
    ImageView takePictureButton;

    @BindView(R.id.picture_options_container)
    LinearLayout pictureOptionsContainer;

    @BindView(R.id.approve_picture)
    ImageView approvePicture;

    @BindView(R.id.retake_picture)
    ImageView retakePicture;

    byte[] picture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_camera);

        ButterKnife.bind(this);

        cameraView.setMethod(CameraKit.Constants.METHOD_STANDARD);

        cameraView.addCameraKitListener(new CameraKitEventListener() {

            boolean canTakePicture = false;

            @Override
            public void onEvent(CameraKitEvent cameraKitEvent) {
                switch(cameraKitEvent.getType()) {

                    case CameraKitEvent.TYPE_CAMERA_OPEN:
                        canTakePicture = true;
                        break;

                    case CameraKitEvent.TYPE_CAMERA_CLOSE:
                        canTakePicture = false;
                        break;
                }
            }

            @Override
            public void onError(CameraKitError cameraKitError) {

            }

            @Override
            public void onImage(CameraKitImage cameraKitImage) {

                picture = cameraKitImage.getJpeg();
            }

            @Override
            public void onVideo(CameraKitVideo cameraKitVideo) {

            }
        });

        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cameraView.captureImage();

                takePictureButton.setVisibility(View.INVISIBLE);

                pictureOptionsContainer.setVisibility(View.VISIBLE);

                cameraView.stop();
            }
        });

        retakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cameraView.start();

                takePictureButton.setVisibility(View.VISIBLE);

                pictureOptionsContainer.setVisibility(View.INVISIBLE);
            }
        });

        approvePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle resultData = new Bundle();

                takePictureButton.setVisibility(View.VISIBLE);

                resultData.putByteArray(ImageFragment.PICTURE_DATA_RESULT_CODE, picture);

                /*setResult(ImageFragment.REQUEST_CODE_TAKE_PICTURE, new Intent(resultData));*/

                finish();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        cameraView.start();
    }

    @Override
    public void onPause() {
        cameraView.stop();
        super.onPause();
    }
}
