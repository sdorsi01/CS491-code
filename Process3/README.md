# Module responsible for displaying output to a user.

## For the python client file, configure the raspberrypi with:
```
sudo apt-get update
sudo apt install bluetooth bluez libbluetooth-dev
sudo python3 -m pip install pybluez
```
Then run the client with the command
```
sudo python btAndroidClient.py
```

## For the Android app:
-> Main Activity code for UI is stored at
```
Vehicle_UI/app/src/main/java/cs477/labs/vehicle_ui/MainActivity.java
```
-> App to download for android device as of 11/17/2022 is at
```
app-protobuf.apk
```

**NOTE**: Current version has a fixed mac address for the device being connected to, so for testing purposes this address will need to be manually edited until the app is updated to account for multiple devices.

---

## For the new data implementation with Protocol Buffers:
I primarily used the tutorials for each respective language at: https://protobuf.dev/getting-started/javatutorial/

Help getting it to work on Android: https://medium.com/mobile-app-development-publication/android-dev-must-know-data-format-protocol-buffers-eb18fd9146a6

## For compiling and generating new protocol buffer objects:

### Packages to Install (with "sudo apt-get install ..." or "sudo pip install" for linux devices):
```
sudo apt-get install protoc-exe
sudo pip install protobuf 
sudo pip install google
```
*(check for successful installaiton with "protoc --version")*

### After making the .proto file and downloading the packages above, use this command to complile it into new objects:
**java**
```
protoc -I=$SRC_DIR --java_out=$DST_DIR $SRC_DIR/filename.proto
```
**python**
```
protoc -I=$SRC_DIR --python_out=pyi_out:$DST_DIR $SRC_DIR/filename.proto
protoc -I=$SRC_DIR --python_out=$DST_DIR $SRC_DIR/filename.proto
```
Where $SRC_DIR is where the proto file is and $DST_DIR is where the objects will be generated.


