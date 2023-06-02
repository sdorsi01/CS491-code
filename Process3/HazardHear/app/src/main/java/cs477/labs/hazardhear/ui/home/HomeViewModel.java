package cs477.labs.hazardhear.ui.home;

import android.bluetooth.BluetoothSocket;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Objects;

import cs477.labs.hazardhear.R;
import cs477.labs.hazardhear.ui.log.LogFragment;
import cs477.labs.protobufftest.SoundObj;

public class HomeViewModel extends ViewModel {

    private static MutableLiveData<String> mText;
    private static MutableLiveData<Integer> mColor;
    private static MutableLiveData<Integer> mBGColor;
    private static MutableLiveData<Integer>[] mVisible;
    private static MutableLiveData<Integer> mLoc;
    private static Handler handler;
   public static ArrayList<String> history;

    public HomeViewModel() {
        handler = new Handler();
        mText = new MutableLiveData<>();
        mColor = new MutableLiveData<>();
        mVisible = new MutableLiveData[4];
        mVisible[0]=new MutableLiveData<Integer>();
        mVisible[1]=new MutableLiveData<Integer>();
        mVisible[2]=new MutableLiveData<Integer>();
        mVisible[3]=new MutableLiveData<Integer>();
        mLoc = new MutableLiveData<>();
        mBGColor = new MutableLiveData<>();
        mText.setValue("Awaiting Connection...");
        history = new ArrayList<>();
    }


    public LiveData<String> getText() {//only calls these once at the beginning
        return mText;
    }

    public LiveData<Integer> getColor(){//only calls these once at the beginning
                return mColor;
    }

    public LiveData<Integer> getBGColor(){//only calls these once at the beginning
        return mBGColor;
    }

    public LiveData<Integer>getVisibility(int i){
        return mVisible[i];
    }

    public static void doThings(BluetoothSocket socket){
        String input="";
        int SIZE=1024;
        byte[]buffer=new byte[SIZE];
        while(!input.equals("end") && socket.isConnected())
        {
            try {
                //Log.e("Output", String.valueOf(socket.getOutputStream());
                //int byteCt = socket.getInputStream().read(buffer);**using socket buffer cleared it?

                //input= new String(buffer,0,byteCt);

                SoundObj inSound=null;
                int inLoc= -1;
                try {
                    inSound = SoundObj.parseDelimitedFrom(socket.getInputStream());
                    //Log.e("Input", inSound.toString());
                }catch (Exception e){
                    Log.e("TEST","Error on logging parsed soundObj: "+e);
                }
                if(inSound!=null)
                {
                    input = inSound.getName();
                    inLoc = inSound.getLocation();
                }
                else
                {
                    input="";
                }
                Log.e("INPUT",input);
                String finalInput = input;
                int finalInLoc = inLoc;
                SoundObj finalInSound = inSound;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                            mText.setValue(finalInput);
                            mLoc.setValue(finalInLoc);
                            if (mText.getValue().equals("Siren")||mText.getValue().equals("Hazard")) {
                                //Log.e("COLOR","changed to red");
                                mColor.setValue(0xFFF44336);//red
                                //mTextColor.setValue(0xFFFFFFFF);//White
                                mBGColor.setValue(0xFFFFFFFF);//white//(0xFF731111);//dark red

                                mVisible[finalInLoc].setValue(View.VISIBLE);
                            }
                            else {
                                mColor.setValue(0xFF2196F3);//light blue
                                mBGColor.setValue(0);//Black//(0xFFFFFFFF);//white
                                for(int i=0;i<4;i++)
                                    mVisible[i].setValue(View.INVISIBLE);

                                if(mText.getValue().equals("end")) {
                                    mText.setValue("Disconnected");
                                }
                            }
                        if(!finalInput.equals("")) {
                            String buildItem="Name: "+finalInSound.getName()+" | Amplitude: "+finalInSound.getAmplitude()+" | Time: "+finalInSound.getTime()+" | Location: "+finalInSound.getLocation();
                            history.add(0, buildItem);
                            LogFragment.update();
                        }
                        //Log.e("LIST", history.toString());
                    }
                });
            }catch (Exception e){}
        }

    }
}