module wrap0(
    input wire clk,
    input wire rst
    ,input wire [15:0] din1
    ,input wire write1
    ,output wire full1
    ,output wire [15:0] dout2
    ,input wire read2
    ,output wire avail2
);

    wire [15:0] data1;
    wire do_write1 = !full1 && write1;
    assign data1[7:0] = din1[15:8];
    assign data1[15:8] = din1[7:0];

    wire [15:0] data2;
    wire do_read2 = avail2 && read2;
    assign dout2[7:0] = data2[15:8];
    assign dout2[15:8] = data2[7:0];

    fpga0 sp(.clk(clk), .rst(rst)
        , .I1input(data1), .I1write(do_write1), .I1afull(full1)
        , .O2output(data2), .O2avail(avail2), .O2read(do_read2)
    );

endmodule

