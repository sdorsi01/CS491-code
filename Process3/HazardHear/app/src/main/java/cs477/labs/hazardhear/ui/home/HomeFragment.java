package cs477.labs.hazardhear.ui.home;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import cs477.labs.hazardhear.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {
    Handler handler;
    private FragmentHomeBinding binding;
    TextView textView;
    ImageView[]alarm_pics;
    ConstraintLayout screen_layout;
    Context context;
    //static public long HOME_ID;
    HomeViewModel homeViewModel;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        context=container.getContext();
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        handler = new Handler();
        textView = binding.textHome;
        screen_layout = binding.layout;
        alarm_pics = new ImageView[4];
        alarm_pics[0]=binding.alrmImgTopLeft;
        alarm_pics[1]=binding.alrmImgTopRight;
        alarm_pics[2]=binding.alrmImgBotLeft;
        alarm_pics[3]=binding.alrmImgBotRight;
        for (ImageView pic:alarm_pics) {
            pic.setVisibility(View.INVISIBLE);
        }

        manageDisplay();
        return root;
    }
    private void manageDisplay(){
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        homeViewModel.getColor().observe(getViewLifecycleOwner(), textView::setBackgroundColor);
        //homeViewModel.getBGColor().observe(getViewLifecycleOwner(),screen_layout ::setBackgroundColor);
        for (int i=0;i<alarm_pics.length;i++) {
            homeViewModel.getVisibility(i).observe(getViewLifecycleOwner(),alarm_pics[i]::setVisibility);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}