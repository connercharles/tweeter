package edu.byu.cs.tweeter.client.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

import edu.byu.cs.tweeter.R;
import edu.byu.cs.tweeter.model.service.request.RegisterRequest;
import edu.byu.cs.tweeter.model.service.response.RegisterResponse;
import edu.byu.cs.tweeter.client.presenter.RegisterPresenter;
import edu.byu.cs.tweeter.client.view.asyncTasks.RegisterTask;
import edu.byu.cs.tweeter.client.view.main.MainActivity;

public class RegisterFragment extends Fragment implements RegisterPresenter.View, RegisterTask.Observer {
    private static final String LOG_TAG = "RegisterFragment";
    private static final String RETAKE_PIC = "Retake Profile Picture";
    private static final int PIC_ID = 42;

    EditText firstNameTxt;
    EditText lastNameTxt;
    EditText usernameTxt;
    EditText passwordTxt;
    Button registerBtn;
    TextView takePicBtn;
    private Toast registerInToast;

    String firstName;
    String lastName;
    String username;
    String password;
    byte [] imageBytes;
    String imageEncoded;

    private RegisterPresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_register, container, false);
        presenter = new RegisterPresenter(this);

        registerBtn = root.findViewById(R.id.button_register);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (errorCheckInput()){
                    getInput();

                    registerInToast = Toast.makeText(getContext(), "Registered, Logging In", Toast.LENGTH_LONG);
                    registerInToast.show();

                    RegisterRequest registerRequest = new RegisterRequest(firstName, lastName, username, password, imageEncoded);
                    RegisterTask registerTask = new RegisterTask(presenter, RegisterFragment.this);
                    registerTask.execute(registerRequest);

                } else {
                    Toast.makeText(getContext(), "Please fill out everything and take picture", Toast.LENGTH_LONG).show();
                }
            }
        });

        takePicBtn = root.findViewById(R.id.takePicText);
        takePicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(camera_intent, PIC_ID);
                takePicBtn.setText(RETAKE_PIC);
            }
        });


        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == PIC_ID) {

            Bitmap photo = (Bitmap)data.getExtras().get("data");
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
            imageBytes = stream.toByteArray();
            imageEncoded = Base64.getEncoder().encodeToString(imageBytes);

//            photo.recycle();
//            imageBytes = (byte[])data.getExtras().get("data");
        }
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        firstNameTxt = (EditText) view.findViewById(R.id.inputRegFirstName);
        lastNameTxt = (EditText) view.findViewById(R.id.inputRegLastName);
        usernameTxt = (EditText) view.findViewById(R.id.inputRegUserName);
        passwordTxt = (EditText) view.findViewById(R.id.inputRegPassword);
    }

    @Override
    public void registerSuccessful(RegisterResponse registerResponse) {
        Intent intent = new Intent(getActivity(), MainActivity.class);

        intent.putExtra(MainActivity.CURRENT_USER_KEY, registerResponse.getUser());
        intent.putExtra(MainActivity.AUTH_TOKEN_KEY, registerResponse.getAuthToken());

        registerInToast.cancel();
        startActivity(intent);
    }

    @Override
    public void registerUnsuccessful(RegisterResponse registerResponse) {
        Toast.makeText(getActivity(), "Failed to register. " + registerResponse.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void handleException(Exception ex) {
        Log.e(LOG_TAG, ex.getMessage(), ex);
        Toast.makeText(getActivity(), "Failed to register because of exception: " + ex.getMessage(), Toast.LENGTH_LONG).show();
    }

    private boolean errorCheckInput(){
        return !firstNameTxt.getText().toString().isEmpty()
                && !lastNameTxt.getText().toString().isEmpty()
                && !usernameTxt.getText().toString().isEmpty()
                && !passwordTxt.getText().toString().isEmpty()
                && imageBytes != null
                && imageBytes.length != 0;
    }

    private void getInput(){
        firstName = firstNameTxt.getText().toString();
        lastName = lastNameTxt.getText().toString();
        username = usernameTxt.getText().toString();
        password = passwordTxt.getText().toString();
    }
}
