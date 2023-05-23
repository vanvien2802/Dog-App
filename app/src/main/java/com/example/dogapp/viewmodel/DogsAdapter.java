package com.example.dogapp.viewmodel;

import android.os.Binder;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dogapp.R;
import com.example.dogapp.databinding.DogsItemBinding;
import com.example.dogapp.model.DogBreed;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DogsAdapter extends RecyclerView.Adapter<DogsAdapter.ViewHolder> implements Filterable {

    private ArrayList<DogBreed> dogBreeds;
    private ArrayList<DogBreed> dogBreedsCoppy;

    public DogsAdapter(ArrayList<DogBreed> dogBreeds) {
        this.dogBreeds = dogBreeds;
        this.dogBreedsCoppy = dogBreeds;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DogsItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.dogs_item,
                parent,
                false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DogsAdapter.ViewHolder holder, int position) {
        holder.binding.setDog(dogBreeds.get(position));
        Picasso.get().load(dogBreeds.get(position).getUrl()).into(holder.binding.ivAvatar);
    }

    @Override
    public int getItemCount() {
        return dogBreeds.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String input = charSequence.toString().toLowerCase();
                List<DogBreed> filteredDog = new ArrayList<DogBreed>();
                if(input.isEmpty()){
                    filteredDog.addAll(dogBreedsCoppy);
                }
                else{
                    for (DogBreed dog : dogBreedsCoppy){
                        if(dog.getName().toLowerCase().contains(input)){
                            filteredDog.add(dog);
                        }
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredDog;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                dogBreeds = new ArrayList<>();
                dogBreeds.addAll((List)filterResults.values);
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public DogsItemBinding binding;

        public ViewHolder (DogsItemBinding itemBinding){
            super(itemBinding.getRoot());
            this.binding = itemBinding;

            binding.ivAvatar.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    DogBreed dog = dogBreeds.get(getAdapterPosition());
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("dogBreed", dog);

                    Navigation.findNavController(view).navigate(R.id.detailsFragment, bundle);
                }
            });


            itemView.setOnTouchListener(new OnSwipeTouchListener(){
                @Override
                public void onSwipeLeft() {
                    if(binding.layout1.getVisibility() == View.GONE){
                        binding.layout1.setVisibility(View.VISIBLE);
                        binding.layout2.setVisibility(View.GONE);
                    }
                    else{
                        binding.layout1.setVisibility(View.GONE);
                        binding.layout2.setVisibility(View.VISIBLE);
                    }
                    super.onSwipeLeft();
                }
            });
        }
    }
    
    public class OnSwipeTouchListener implements View.OnTouchListener{

        private final GestureDetector gestureDetector = new GestureDetector(new GestureListener());

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return gestureDetector.onTouchEvent(event);
        }


        private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

            private static final int SWIPE_THRESHOLD = 50;
            private static final int SWIPE_VELOCITY_THRESHOLD = 50;

            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                onClick();
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                OnSwipeTouchListener.this.onLongPress();
            }


            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                boolean result = false;
                try {
                    float diffY = e2.getY() - e1.getY();
                    float diffX = e2.getX() - e1.getX();
                    if (Math.abs(diffX) > Math.abs(diffY)) {
                        if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                            if (diffX > 0) {
                                onSwipeRight();
                            } else {
                                onSwipeLeft();
                            }
                        }
                        result = true;
                    } else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffY > 0) {
                            onSwipeBottom();
                        } else {
                            onSwipeTop();
                        }
                    }
                    result = true;

                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                return result;
            }
        }

        private void onLongPress() {
        }

        public void onSwipeRight() {
        }

        public void onSwipeLeft() {
        }

        public void onSwipeTop() {
        }

        public void onSwipeBottom() {
        }

        public void onClick() {
        }

    }
}
