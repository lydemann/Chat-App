package com.strangerchat.strangerchat;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


public class FriendFragment extends DialogFragment {

    private EditText mEditText;

    public FriendFragment() {
        // Empty constructor required for DialogFragment
    }

    public static FriendFragment newInstance(String title) {
        FriendFragment frag = new FriendFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        //frag.setStyle(DialogFragment.STYLE_NO_FRAME, 0);
        frag.setStyle(DialogFragment.STYLE_NO_TITLE,0);

        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend, container);




        String title = getArguments().getString("title", "Enter Name");
        //getDialog().setTitle(title);
        // Show soft keyboard automatically
//        mEditText.requestFocus();
//        getDialog().getWindow().setSoftInputMode(
//                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        return view;
    }
}