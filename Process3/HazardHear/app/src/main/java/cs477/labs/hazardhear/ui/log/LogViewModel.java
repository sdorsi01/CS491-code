package cs477.labs.hazardhear.ui.log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LogViewModel extends ViewModel {

    private static MutableLiveData<String> mItem;

    public LogViewModel() {
        mItem = new MutableLiveData<>();
        mItem.setValue("");
    }

    /*public static void addItem(String item){
        mItem.setValue(item);
    }*/
    public LiveData<String> getText() {
        return mItem;
    }
    //public LiveData<> getAdapter()
}