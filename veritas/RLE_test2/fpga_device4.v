module fpga0(
    input wire clk,
    input wire rst
    ,
    input wire [15:0] I1input,
    input wire I1write,
    output wire I1afull
    ,
    output wire [15:0] O2output,
    output wire O2avail,
    input wire O2read
);

    wire [15:0] edge1_input;
    wire [15:0] edge1_dout;
    wire edge1_avail;
    wire edge1_read;
    wire edge1_empty;
    wire [15:0] edge2_output;
    wire [15:0] edge2_din;
    wire edge2_write;
    wire edge2_full;
    wire edge2_empty;

    kernel_RLE
        instance2(
            .clk(clk), .rst(rst)
            ,
            .input_S1(edge1_input),
            .avail_S1(edge1_avail),
            .read_S1(edge1_read)
            ,
            .output_S2(edge2_output),
            .write_S2(edge2_write),
            .afull_S2(edge2_full)
        );
    assign edge1_avail = !edge1_empty;

    sp_register #(.WIDTH(16), .ADDR_WIDTH(0))
        fifo_edge1(
            .clk(clk), .rst(rst),
            .din(I1input),
            .dout(edge1_dout),
            .re(edge1_read),
            .we(I1write),
            .empty(edge1_empty),
            .full(I1afull)
        );
    assign edge1_input = edge1_dout;

    sp_register #(.WIDTH(16), .ADDR_WIDTH(0))
        fifo_edge2(
            .clk(clk), .rst(rst),
            .din(edge2_din),
            .dout(O2output),
            .re(O2read),
            .we(edge2_write),
            .empty(edge2_empty),
            .full(edge2_full)
        );
    assign edge2_din = edge2_output;
    assign O2avail = !edge2_empty;

endmodule

