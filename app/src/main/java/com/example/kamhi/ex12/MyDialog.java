package com.example.kamhi.ex12;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * Created by Kamhi on 16/12/2016.
 */

public class MyDialog extends DialogFragment {

    private int requestCode;
    final public static int EXIT = 3;
    private ResultsListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        this.requestCode = getArguments().getInt("requestCode");
        if(requestCode == EXIT){
            return buildExitDialog().create();
        }
        else{
            return null;
        }

    }

    @Override
    public void onAttach(Activity activity) {
        try
        {
            if (getParentFragment()!=null)
                this.listener = (ResultsListener) getParentFragment();
            else
                listener=(ResultsListener) activity;
        }catch(ClassCastException e)
        {

            throw new ClassCastException("this fragment host must implements ResultsListner");
        }
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        this.listener = null;
        super.onDetach();
    }
    public static MyDialog newInstance(int requestCode) {
        Bundle args = new Bundle();
        MyDialog fragment = new MyDialog();
        args.putInt("requestCode",requestCode);
        fragment.setArguments(args);
        return fragment;
    }


    private AlertDialog.Builder buildExitDialog(){
        return new AlertDialog.Builder(getActivity())
                .setTitle("Closing the application")
                .setMessage("Are you sure")
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.OnfinishDialog(requestCode, "ok");
                    }
                })
                .setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                });
    }


    public interface ResultsListener {
        public void OnfinishDialog(int requestCode, Object result);
    }
}