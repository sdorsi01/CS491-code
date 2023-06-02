import bluetooth
import time
import Predictions_pb2
from google.protobuf.internal.encoder import _VarintBytes

addr = "40:45:DA:B3:D1:18"
port = bluetooth.PORT_ANY
sock = bluetooth.BluetoothSocket(bluetooth.RFCOMM)
#sock.bind(("",port))
#sock.listen(1)
uuid = "94f39d29-7d6d-437d-973b-fba39e49d4ee" #"0000110a-0000-1000-8000-00805f9b34fb"
#sock.connect((addr,port))
#bluetooth.advertise_service(sock,"test",service_id=uuid,
#	service_classes=[uuid,bluetooth.SERIAL_PORT_CLASS],
#	profiles = [bluetooth.SERIAL_PORT_PROFILE])
devices = []
while len(devices) < 1:
	devices = bluetooth.find_service(name="Test",uuid=uuid,address=addr)
print("DEVICES")
for item in devices:
	print(item['host'])

soundToSend = Predictions_pb2.SoundObj()
soundToSend.name = "Siren"
soundToSend.location = 2
soundToSend.time = "3:33:01"
soundToSend.amplitude = "45.6"
packed = soundToSend.SerializeToString()

soundToSend2 = Predictions_pb2.SoundObj()
soundToSend2.name = "Siren"
soundToSend2.location = 0
soundToSend2.time = "3:33:01"
soundToSend2.amplitude = "45.6"
packed2 = soundToSend2.SerializeToString()

otherSound = Predictions_pb2.SoundObj()
otherSound.name = "Dog"
otherSound.location = 3
otherSound.time = "4:45:01"
otherSound.amplitude = "345.6"
other = otherSound.SerializeToString()

endPkt = Predictions_pb2.SoundObj()
endPkt.name = "end"
endPkt.location = 1
endPkt.time="t1"
endPkt.amplitude = "t2"
end = endPkt.SerializeToString()

dv=devices[0]
#devices= sock.find_devices(
#client_sock,client_addr = sock.accept()
sock.connect((dv['host'],dv['port']))
print("connected")


#packet = struct.pack(int,int  #right now getting freq(int) and ampl(float)
#Size,ID,Loc,Checksum => unpack into ID, Loc, Timestamp[?]
blips = [other,packed,other,packed2,end]
i=0
while i<len(blips):
	text = blips[i] #input()
	if i%2==1:
		size = soundToSend.ByteSize()
	else:
		size = otherSound.ByteSize()
	if i == 4:
		size = endPkt.ByteSize()

	payload=_VarintBytes(size)+text#text.encode())
	print("Sending", payload)
	sock.send(payload)
	time.sleep(2)
	i=i+1
sock.close()
