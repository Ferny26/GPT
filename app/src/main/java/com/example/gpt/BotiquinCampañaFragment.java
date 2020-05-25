package com.example.gpt;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class BotiquinCampañaFragment extends Fragment {
    private ImageView mMainImageView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        getActivity().setTitle(getString(R.string.inventario_botiquin));
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.universal_list_activity, null);
        mMainImageView = view.findViewById(R.id.main_image_view);
        mMainImageView.setImageResource(R.drawable.medicina_color);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.botiquin_campanias, menu);
    }
}
