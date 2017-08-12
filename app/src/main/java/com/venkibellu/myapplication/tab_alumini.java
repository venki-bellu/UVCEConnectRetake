package com.venkibellu.myapplication;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class tab_alumini extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_alumini, container, false);

        TextView txt = (TextView) v.findViewById(R.id.t);
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/LibreBaskerville-Bold.ttf");
        txt.setTypeface(font);
        return v;
    }
}