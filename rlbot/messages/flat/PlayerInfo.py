# automatically generated by the FlatBuffers compiler, do not modify

# namespace: flat

import flatbuffers

class PlayerInfo(object):
    __slots__ = ['_tab']

    @classmethod
    def GetRootAsPlayerInfo(cls, buf, offset):
        n = flatbuffers.encode.Get(flatbuffers.packer.uoffset, buf, offset)
        x = PlayerInfo()
        x.Init(buf, n + offset)
        return x

    # PlayerInfo
    def Init(self, buf, pos):
        self._tab = flatbuffers.table.Table(buf, pos)

    # PlayerInfo
    def Physics(self):
        o = flatbuffers.number_types.UOffsetTFlags.py_type(self._tab.Offset(4))
        if o != 0:
            x = self._tab.Indirect(o + self._tab.Pos)
            from .Physics import Physics
            obj = Physics()
            obj.Init(self._tab.Bytes, x)
            return obj
        return None

    # PlayerInfo
    def ScoreInfo(self):
        o = flatbuffers.number_types.UOffsetTFlags.py_type(self._tab.Offset(6))
        if o != 0:
            x = self._tab.Indirect(o + self._tab.Pos)
            from .ScoreInfo import ScoreInfo
            obj = ScoreInfo()
            obj.Init(self._tab.Bytes, x)
            return obj
        return None

    # PlayerInfo
    def IsDemolished(self):
        o = flatbuffers.number_types.UOffsetTFlags.py_type(self._tab.Offset(8))
        if o != 0:
            return self._tab.Get(flatbuffers.number_types.BoolFlags, o + self._tab.Pos)
        return 0

# /// True if your wheels are on the ground, the wall, or the ceiling. False if you're midair or turtling.
    # PlayerInfo
    def HasWheelContact(self):
        o = flatbuffers.number_types.UOffsetTFlags.py_type(self._tab.Offset(10))
        if o != 0:
            return self._tab.Get(flatbuffers.number_types.BoolFlags, o + self._tab.Pos)
        return 0

    # PlayerInfo
    def IsSupersonic(self):
        o = flatbuffers.number_types.UOffsetTFlags.py_type(self._tab.Offset(12))
        if o != 0:
            return self._tab.Get(flatbuffers.number_types.BoolFlags, o + self._tab.Pos)
        return 0

    # PlayerInfo
    def IsBot(self):
        o = flatbuffers.number_types.UOffsetTFlags.py_type(self._tab.Offset(14))
        if o != 0:
            return self._tab.Get(flatbuffers.number_types.BoolFlags, o + self._tab.Pos)
        return 0

# /// True if the player has jumped. Falling off the ceiling / driving off the goal post does not count.
    # PlayerInfo
    def Jumped(self):
        o = flatbuffers.number_types.UOffsetTFlags.py_type(self._tab.Offset(16))
        if o != 0:
            return self._tab.Get(flatbuffers.number_types.BoolFlags, o + self._tab.Pos)
        return 0

# ///  True if player has double jumped. False does not mean you have a jump remaining, because the
# ///  aerial timer can run out, and that doesn't affect this flag.
    # PlayerInfo
    def DoubleJumped(self):
        o = flatbuffers.number_types.UOffsetTFlags.py_type(self._tab.Offset(18))
        if o != 0:
            return self._tab.Get(flatbuffers.number_types.BoolFlags, o + self._tab.Pos)
        return 0

    # PlayerInfo
    def Name(self):
        o = flatbuffers.number_types.UOffsetTFlags.py_type(self._tab.Offset(20))
        if o != 0:
            return self._tab.String(o + self._tab.Pos)
        return bytes()

    # PlayerInfo
    def Team(self):
        o = flatbuffers.number_types.UOffsetTFlags.py_type(self._tab.Offset(22))
        if o != 0:
            return self._tab.Get(flatbuffers.number_types.Int32Flags, o + self._tab.Pos)
        return 0

    # PlayerInfo
    def Boost(self):
        o = flatbuffers.number_types.UOffsetTFlags.py_type(self._tab.Offset(24))
        if o != 0:
            return self._tab.Get(flatbuffers.number_types.Int32Flags, o + self._tab.Pos)
        return 0

def PlayerInfoStart(builder): builder.StartObject(11)
def PlayerInfoAddPhysics(builder, physics): builder.PrependUOffsetTRelativeSlot(0, flatbuffers.number_types.UOffsetTFlags.py_type(physics), 0)
def PlayerInfoAddScoreInfo(builder, scoreInfo): builder.PrependUOffsetTRelativeSlot(1, flatbuffers.number_types.UOffsetTFlags.py_type(scoreInfo), 0)
def PlayerInfoAddIsDemolished(builder, isDemolished): builder.PrependBoolSlot(2, isDemolished, 0)
def PlayerInfoAddHasWheelContact(builder, hasWheelContact): builder.PrependBoolSlot(3, hasWheelContact, 0)
def PlayerInfoAddIsSupersonic(builder, isSupersonic): builder.PrependBoolSlot(4, isSupersonic, 0)
def PlayerInfoAddIsBot(builder, isBot): builder.PrependBoolSlot(5, isBot, 0)
def PlayerInfoAddJumped(builder, jumped): builder.PrependBoolSlot(6, jumped, 0)
def PlayerInfoAddDoubleJumped(builder, doubleJumped): builder.PrependBoolSlot(7, doubleJumped, 0)
def PlayerInfoAddName(builder, name): builder.PrependUOffsetTRelativeSlot(8, flatbuffers.number_types.UOffsetTFlags.py_type(name), 0)
def PlayerInfoAddTeam(builder, team): builder.PrependInt32Slot(9, team, 0)
def PlayerInfoAddBoost(builder, boost): builder.PrependInt32Slot(10, boost, 0)
def PlayerInfoEnd(builder): return builder.EndObject()
