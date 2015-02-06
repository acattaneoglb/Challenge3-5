package co.mobilemakers.contactstudio2;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A placeholder fragment containing a simple view.
 */
public class CreateContactFragment extends Fragment {

    static final int REQUEST_IMAGE_CAPTURE = 10;

    public static final String EXTRA_CONTACT = "EXTRA_CONTACT";

    String mCurrentPhotoPath = "";

    EditText mEditFirstName;
    EditText mEditLastName;
    CheckBox mCheckNickname;
    EditText mEditNickname;
    TextView mTextPicture;
    ImageButton mImageButtonPicture;
    Button mButtonDone;

    public CreateContactFragment() {
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        //mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private class NameWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            mButtonDone.setEnabled(!mEditFirstName.getText().toString().isEmpty()
            || !mEditLastName.getText().toString().isEmpty());
        }
    }

    private class ImageButtonPictureListener implements View.OnClickListener {
        private void dispatchTakePictureIntent() {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                // Create the File where the photo should go
                File photoFile;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    // Error occurred while creating the File
                    return;
                }
                // Continue only if the File was successfully created
                if (photoFile != null) {
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(photoFile));
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        }

        @Override
        public void onClick(View v) {
            dispatchTakePictureIntent();
        }
    }

    private class DoneListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            ContactModel contact = new ContactModel();
            contact.setFirstName(mEditFirstName.getText().toString());
            contact.setLastName(mEditLastName.getText().toString());
            if (mCheckNickname.isChecked()) {
                contact.setNickname(mEditNickname.getText().toString());
            }
            else {
                contact.setNickname("");
            }
            contact.setPhotoURL(mCurrentPhotoPath);

            Activity activity = getActivity();
            Intent intent = new Intent();
            intent.putExtra(EXTRA_CONTACT, contact);
            activity.setResult(Activity.RESULT_OK, intent);
            getActivity().finish();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (resultCode == Activity.RESULT_OK) {
                ImageUtils.setPic(mImageButtonPicture, mCurrentPhotoPath);
            }
            else {
                mCurrentPhotoPath = "";
            }
        }
    }

    protected void controlsToVars(View view) {
        mEditFirstName = (EditText)view.findViewById(R.id.edit_first_name);
        mEditLastName = (EditText)view.findViewById(R.id.edit_last_name);
        mCheckNickname = (CheckBox)view.findViewById(R.id.check_nickname);
        mEditNickname = (EditText)view.findViewById(R.id.edit_nickname);
        mTextPicture = (TextView)view.findViewById(R.id.text_picture);
        mImageButtonPicture = (ImageButton)view.findViewById(R.id.image_button_picture);
        boolean hasCamera = getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
        if (!hasCamera) {
            mTextPicture.setText(R.string.text_picture_disabled);
            mImageButtonPicture.setEnabled(false);
        }

        mButtonDone = (Button)view.findViewById(R.id.button_done);
    }

    protected void setListeners() {
        NameWatcher watcher = new NameWatcher();

        mEditFirstName.addTextChangedListener(watcher);
        mEditLastName.addTextChangedListener(watcher);

        mCheckNickname.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mEditNickname.setEnabled(isChecked);
            }
        });

        mImageButtonPicture.setOnClickListener(new ImageButtonPictureListener());

        mButtonDone.setOnClickListener(new DoneListener());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_create_contact, container, false);

        controlsToVars(rootView);
        setListeners();

        return rootView;
    }

}
