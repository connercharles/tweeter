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

public class Popup extends DialogFragment {
    EditText prompt;
    Button postBtn;
    Button exitBtn;

    public static Popup newInstance() {
        Popup frag = new Popup();
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.popup, container);

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