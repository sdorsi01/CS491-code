package cs477.labs.hazardhear;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import static cs477.labs.hazardhear.ui.home.HomeViewModel.doThings;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.companion.CompanionDeviceManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.io.IOException;
import java.sql.Time;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import cs477.labs.hazardhear.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private static final int REQUEST_ENABLE_BT = 1;
    private static final int SELECT_DEVICE_REQUEST_CODE = 2;
    private BluetoothAdapter bluetoothAdapter;
    private UUID myUUID;
    private Handler handler;
    private ImageView displayPic;
    private Resources resources;
    //public static Resources res;

    //@RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //res = getResources();
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_video, R.id.navigation_log)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);


       if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH)) {
            Toast.makeText(this, "Not Supported", Toast.LENGTH_SHORT).show();
            finish();
        }
        BluetoothManager bluetoothManager = getSystemService(BluetoothManager.class);
        bluetoothAdapter = bluetoothManager.getAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth not supported", Toast.LENGTH_SHORT).show();// Device doesn't support Bluetooth
        }
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);

            Toast.makeText(this, "Bluetooth not enabled", Toast.LENGTH_SHORT).show();

            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.BLUETOOTH_CONNECT}, SELECT_DEVICE_REQUEST_CODE);
            }
            startActivity(enableBtIntent);
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            finish();

        }
        requestPermissions(new String[]{Manifest.permission.BLUETOOTH_CONNECT}, SELECT_DEVICE_REQUEST_CODE);
        handler = new Handler();
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        ArrayList<String> pairedStr = new ArrayList<>();
        if (pairedDevices.size() > 0) {
            // There are paired devices. Get the name and address of each paired device.
            for (BluetoothDevice device : pairedDevices) {
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress();
                if(deviceName.equals("raspberry"))//right now always accepts as long as name has been paired TODO
                {
                    //Toast.makeText(this, "Found paired device: "+deviceName, Toast.LENGTH_SHORT).show();


                    (new AcceptThread()).start();
                    //Toast.makeText(this,"Continuing",Toast.LENGTH_SHORT).show();

                }
            }
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode != SELECT_DEVICE_REQUEST_CODE)
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        /*else {
            String results = ":";
            for (int i = 0; i < grantResults.length; i++) {
                results += grantResults[i] + " ";
            }
            Toast.makeText(this, "RESULTS" + results, Toast.LENGTH_SHORT).show();
            if (grantResults[0] == RESULT_OK) {
                Toast.makeText(this, "GRANT RESULT OK", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(this, "GRANT RESULT not OK:" + grantResults[0], Toast.LENGTH_SHORT).show();
        }*/
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == SELECT_DEVICE_REQUEST_CODE && data != null) {
            BluetoothDevice deviceToPair =
                    data.getParcelableExtra(CompanionDeviceManager.EXTRA_DEVICE);
            if (deviceToPair != null) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.BLUETOOTH_CONNECT}, SELECT_DEVICE_REQUEST_CODE);
                }
                deviceToPair.createBond();
                // Continue to interact with the paired device.
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private class AcceptThread extends Thread {
        private final BluetoothServerSocket mmServerSocket;
        private final String NAME = "Test";
        private final UUID MY_UUID = UUID.fromString("94f39d29-7d6d-437d-973b-fba39e49d4ee");

        @SuppressLint("MissingPermission")
        public AcceptThread() {

            // Use a temporary object that is later assigned to mmServerSocket
            // because mmServerSocket is final.
            BluetoothServerSocket tmp = null;
            //Toast.makeText(getApplicationContext(), "Listening for device \"connect()\"", Toast.LENGTH_SHORT).show();
            try {
                // MY_UUID is the app's UUID string, also used by the client code.
                tmp = bluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME, MY_UUID);
            } catch (IOException e) {
                Log.e(TAG, "Socket's listen() method failed", e);
            }
            mmServerSocket = tmp;
        }

        public void run() {
            BluetoothSocket socket = null;
            // Keep listening until exception occurs or a socket is returned.
            while(socket==null||!socket.isConnected()){
                try {
                    Log.e("Socket", "Attempt to make socket");

                    //Toast.makeText(getApplicationContext(),"Attempt connect",Toast.LENGTH_LONG).show();
                    socket = mmServerSocket.accept();//device.createRfcommSocketToServiceRecord(UUID.fromString("0000110a-0000-1000-8000-00805f9b34fb"));
                    mmServerSocket.close();

                    if(socket!=null)
                    {
                        //Toast.makeText(getApplicationContext(), "Success! Connected", Toast.LENGTH_SHORT).show();
                        doThings(socket);//Method in HomeViewModel
                        socket.close();
                        Looper.prepare();
                        Toast.makeText(getApplicationContext(), "Disconnected",Toast.LENGTH_LONG).show();
                        break;
                    }

                } catch (IOException e) {
                    Log.e("Socket", "Attempt failed");
                    //Toast.makeText(getApplicationContext(),"Socket creation failed",Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }

        // Closes the connect socket and causes the thread to finish.
        public void cancel() {
            try {
                mmServerSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close the connect socket", e);
            }
        }
    }

}