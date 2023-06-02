from google.protobuf import descriptor as _descriptor
from google.protobuf import message as _message
from typing import ClassVar as _ClassVar, Optional as _Optional

DESCRIPTOR: _descriptor.FileDescriptor

class SoundObj(_message.Message):
    __slots__ = ["name", "location", "time", "amplitude"]
    NAME_FIELD_NUMBER: _ClassVar[int]
    LOCATION_FIELD_NUMBER: _ClassVar[int]
    TIME_FIELD_NUMBER: _ClassVar[int]
    AMPLITUDE_FIELD_NUMBER: _ClassVar[int]
    name: str
    location: int
    time: str
    amplitude: str
    def __init__(self, name: _Optional[str] = ..., location: _Optional[int] = ..., time: _Optional[str] = ..., amplitude: _Optional[str] = ...) -> None: ...
