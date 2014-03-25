#include "std.x"
#include "ipc.x"
block NumSequence {
    output UNSIGNED16 S2;
};
platform C {
    impl NumSequence(base="NumSequence");
};
block RLE {
    config UNSIGNED16 pixelCount = 1600
    input UNSIGNED16 S1;
    output UNSIGNED16 S2;
};
platform HDL {
    impl RLE(base="RLE");
};
block Print {
    input UNSIGNED16 S1;
};
platform C {
    impl Print(base="Print");
};

block top {

    NumSequence instance1;
    RLE instance2;
    Print instance3;


edge1: instance1.S2 -> instance2.S1;
edge2: instance2.S2 -> instance3.S1;

};
use top app;
map device1[1] = { app.edge1 };
map device2[1] = { app.edge2 };



