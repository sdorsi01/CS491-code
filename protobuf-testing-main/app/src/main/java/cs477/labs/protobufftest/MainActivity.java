package cs477.labs.protobufftest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.protobuf.InvalidProtocolBufferException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Building obj on sender
        SoundObj test = SoundObj.newBuilder().setName("Dog").setLocation(1).setTime("3:35:00").setAmplitude("120").build();
        Log.e("TEST",test.toString());
        //Packing obj on sender
        byte[] testBytes = test.toByteArray();
        Log.e("TEST",testBytes+"");

        try {
            //Unpacking obj on receiver IF java -> java
            Log.e("TEST",SoundObj.parseFrom(testBytes).toString());

            //IF Python-> Java:
            //Log.e("TEST",SoundObj.parseDelimitedFrom(inputStream));

        } catch (InvalidProtocolBufferException e) {
            Log.e("TEST","Error on logging parsed soundObj");
            e.printStackTrace();
        }
    }
}