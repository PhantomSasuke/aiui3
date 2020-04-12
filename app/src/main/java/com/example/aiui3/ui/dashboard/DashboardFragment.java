package com.example.aiui3.ui.dashboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.aiui3.MainActivity;
import com.example.aiui3.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;

public class DashboardFragment extends Fragment {

    public static Bitmap bitmap;
    public static Bitmap linesBitMap;
    public static Bitmap onlyLinesBitMap;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        BottomNavigationView navView = getActivity().findViewById(R.id.nav_view);
        navView.getMenu().getItem(0).setEnabled(true);
        navView.getMenu().getItem(1).setEnabled(false);
        navView.getMenu().getItem(2).setEnabled(true);

        FrameLayout f = getActivity().findViewById(R.id.noimg);
        View[] ff = {getActivity().findViewById(R.id.img), getActivity().findViewById(R.id.linesToggle),
                getActivity().findViewById( R.id.linesButton),getActivity().findViewById(R.id.cvimg),getActivity().findViewById(R.id.cvbut1),getActivity().findViewById(R.id.cvbut2)};
        LinearLayout ll = getActivity().findViewById(R.id.imgL);
        CardView cv = getActivity().findViewById(R.id.cardid);
        ProgressBar pb = getActivity().findViewById(R.id.indeterminateBar);
        System.out.println(bitmap);
        if (bitmap == null){
            cv.setVisibility(View.VISIBLE);
            f.setVisibility(View.VISIBLE);
            for(View v: ff)
                v.setVisibility(View.INVISIBLE);
            pb.setVisibility(View.INVISIBLE);
            ll.setVisibility(View.INVISIBLE);
        }
        else{
            cv.setVisibility(View.INVISIBLE);
            f.setVisibility(View.INVISIBLE);
            ImageView imageView = (ImageView) getActivity().findViewById(R.id.img);
            ll.setVisibility(View.VISIBLE);
            imageView.setImageBitmap(bitmap);
            pb.setVisibility(View.INVISIBLE);
            for(View v: ff)
                v.setVisibility(View.VISIBLE);
        }

        ToggleButton toggle = getActivity().findViewById(R.id.linesToggle);
        ToggleButton toggle2 = getActivity().findViewById(R.id.linesButton);

        toggle2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ImageView imageView = (ImageView) getActivity().findViewById(R.id.img);
                ProgressBar pb = getActivity().findViewById(R.id.indeterminateBar);
                CardView cv = getActivity().findViewById(R.id.cvbut2);

                if(isChecked){
                    cv.setVisibility(View.INVISIBLE);
                    toggle.setVisibility(View.INVISIBLE);
                    imageView.setImageBitmap(onlyLinesBitMap);
                    if (onlyLinesBitMap == null){
                        pb.setVisibility(View.VISIBLE);
                    }
                    else{
                        pb.setVisibility(View.INVISIBLE);
                    }

                }
                else{
                    cv.setVisibility(View.VISIBLE);
                    toggle.setVisibility(View.VISIBLE);
                    toggle.setChecked(false);
                    imageView.setImageBitmap(bitmap);
                    pb.setVisibility(View.INVISIBLE);
                }
            }
        });

        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ImageView imageView = (ImageView) getActivity().findViewById(R.id.img);
                ProgressBar pb = getActivity().findViewById(R.id.indeterminateBar);
                if(isChecked){
                    imageView.setImageBitmap(linesBitMap);
                    if (linesBitMap == null){
                        pb.setVisibility(View.VISIBLE);
                    }
                    else{
                        pb.setVisibility(View.INVISIBLE);
                    }

                }
                else{
                    imageView.setImageBitmap(bitmap);
                    pb.setVisibility(View.INVISIBLE);
                }
            }
        });
    }


}