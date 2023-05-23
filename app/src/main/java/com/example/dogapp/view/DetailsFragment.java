package com.example.dogapp.view;

import static com.example.dogapp.R.id.iv_avatar;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.dogapp.R;
import com.example.dogapp.databinding.FragmentDetailsBinding;
import com.example.dogapp.model.DogBreed;
import com.example.dogapp.viewmodel.DogsAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DetailsFragment extends Fragment {

    private DogBreed dogBreed;
    private FragmentDetailsBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            dogBreed = (DogBreed) getArguments().getSerializable("dogBreed");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(getLayoutInflater(),
                R.layout.fragment_details, null,false);
        View viewRoot = binding.getRoot();
        binding.setDog(dogBreed);
        Picasso.get().load(dogBreed.getUrl()).into((ImageView) viewRoot.findViewById(R.id.imageView2));
        return viewRoot;
    }
}