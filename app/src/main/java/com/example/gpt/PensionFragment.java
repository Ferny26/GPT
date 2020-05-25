package com.example.gpt;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class PensionFragment extends Fragment {
    private ImageView mMainImageView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.universal_list_activity,container,false);
        mMainImageView= v.findViewById(R.id.main_image_view);
        mMainImageView.setImageResource(R.drawable.pension);
        return v;
    }
}
