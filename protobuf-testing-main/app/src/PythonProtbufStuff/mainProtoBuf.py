from google.protobuf.internal.encoder import _VarintBytes

import Predictions_pb2

soundToSend = Predictions_pb2.SoundObj()
soundToSend.name = "Dog"
soundToSend.location = 1
soundToSend.time = "4:45:01"
soundToSend.amplitude = "345.6"
print(soundToSend)
packed = soundToSend.SerializeToString()
print(packed,"\n") #send if python -> python

size = soundToSend.ByteSize()
payload = _VarintBytes(size) + packed
print(payload)#send this from java->java


soundRecv = Predictions_pb2.SoundObj()
# unpack
soundRecv.ParseFromString(packed)
print("\n"+str(soundRecv))