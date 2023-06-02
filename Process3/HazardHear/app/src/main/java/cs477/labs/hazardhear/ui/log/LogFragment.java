package cs477.labs.hazardhear.ui.log;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cs477.labs.hazardhear.R;
import cs477.labs.hazardhear.databinding.FragmentLogBinding;
import cs477.labs.hazardhear.ui.home.HomeViewModel;
import cs477.labs.protobufftest.SoundObj;

public class LogFragment extends Fragment {

    private FragmentLogBinding binding;
    private static ArrayAdapter<String> adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        LogViewModel logViewModel =
                new ViewModelProvider(this).get(LogViewModel.class);

        binding = FragmentLogBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        if(HomeViewModel.history !=null ){
            adapter = new ArrayAdapter<String>(this.getContext(), R.layout.my_list_item1, HomeViewModel.history);
        }
        else {
            adapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_list_item_1);
        }
        //Log.e("LOGGER","Adapter created: "+adapter.getCount());

        final ListView list = binding.listLog;

        list.setAdapter(adapter);
        return root;
    }

    public static void update(){
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                if(adapter!=null)
                    adapter.notifyDataSetChanged();
            }
        });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}