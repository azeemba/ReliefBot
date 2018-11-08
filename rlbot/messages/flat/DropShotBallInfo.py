# automatically generated by the FlatBuffers compiler, do not modify

# namespace: flat

import flatbuffers

class DropShotBallInfo(object):
    __slots__ = ['_tab']

    @classmethod
    def GetRootAsDropShotBallInfo(cls, buf, offset):
        n = flatbuffers.encode.Get(flatbuffers.packer.uoffset, buf, offset)
        x = DropShotBallInfo()
        x.Init(buf, n + offset)
        return x

    # DropShotBallInfo
    def Init(self, buf, pos):
        self._tab = flatbuffers.table.Table(buf, pos)

    # DropShotBallInfo
    def AbsorbedForce(self):
        o = flatbuffers.number_types.UOffsetTFlags.py_type(self._tab.Offset(4))
        if o != 0:
            return self._tab.Get(flatbuffers.number_types.Float32Flags, o + self._tab.Pos)
        return 0.0

    # DropShotBallInfo
    def DamageIndex(self):
        o = flatbuffers.number_types.UOffsetTFlags.py_type(self._tab.Offset(6))
        if o != 0:
            return self._tab.Get(flatbuffers.number_types.Int32Flags, o + self._tab.Pos)
        return 0

    # DropShotBallInfo
    def ForceAccumRecent(self):
        o = flatbuffers.number_types.UOffsetTFlags.py_type(self._tab.Offset(8))
        if o != 0:
            return self._tab.Get(flatbuffers.number_types.Float32Flags, o + self._tab.Pos)
        return 0.0

def DropShotBallInfoStart(builder): builder.StartObject(3)
def DropShotBallInfoAddAbsorbedForce(builder, absorbedForce): builder.PrependFloat32Slot(0, absorbedForce, 0.0)
def DropShotBallInfoAddDamageIndex(builder, damageIndex): builder.PrependInt32Slot(1, damageIndex, 0)
def DropShotBallInfoAddForceAccumRecent(builder, forceAccumRecent): builder.PrependFloat32Slot(2, forceAccumRecent, 0.0)
def DropShotBallInfoEnd(builder): return builder.EndObject()
