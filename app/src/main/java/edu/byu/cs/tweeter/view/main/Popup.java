package edu.byu.cs.tweeter.view.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import edu.byu.cs.tweeter.R;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class Popup extends DialogFragment {
    private static final String USER_KEY = "UserKey";
    private static final String AUTH_TOKEN_KEY = "AuthTokenKey";

    EditText prompt;
    Button postBtn;
    Button exitBtn;

    public static Popup newInstance(User user, AuthToken authToken) {
        Popup frag = new Popup();

        Bundle args = new Bundle(2);
        args.putSerializable(USER_KEY, user);
        args.putSerializable(AUTH_TOKEN_KEY, authToken);

        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.popup, container);

        //noinspection ConstantConditions
        User user = (User) getArguments().getSerializable(USER_KEY);
        AuthToken authToken = (AuthToken) getArguments().getSerializable(AUTH_TOKEN_KEY);
        if(authToken == null) {
            throw new RuntimeException("AuthToken not passed to fragment");
        }
        prompt = (EditText) view.findViewById(R.id.text_prompt);
        postBtn = (Button) view.findViewById(R.id.button_post);
        exitBtn = (Button) view.findViewById(R.id.popup_exit);

        // listener for the bottom tab
        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        prompt = (EditText) view.findViewById(R.id.text_prompt);
        // Show soft keyboard automatically and request focus to field
        prompt.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }
}