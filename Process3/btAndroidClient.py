import bluetooth

addr = "54:21:9D:4A:05:A0"
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

dv=devices[0]
#devices= sock.find_devices(
#client_sock,client_addr = sock.accept()
sock.connect((dv['host'],dv['port']))
print("connected")
while True:
	text = input()
	if text == "quit":
		break;
	sock.send(text.encode())
sock.close()
