module sim_device4;

    reg clk;
    reg rst;
    integer rc;

    integer streamedge1;
    integer streamedge2;

    reg [31:0] count1;
    reg [15:0] din1;
    wire write1;
    wire full1;
    reg got_data1;
    reg sent_data1;
    wire [15:0] dout2;
    wire read2;
    wire avail2;

    wrap0 dut(.clk(clk), .rst(rst)
        , .din1(din1)
        , .write1(write1)
        , .full1(full1)
        , .dout2(dout2)
        , .read2(read2)
        , .avail2(avail2)
    );

    initial begin

        streamedge1 = $fopen("streamedge1", "rb");
        if(streamedge1 == 0) begin
            $display("could not open streamedge1");
            $finish;
        end
        streamedge2 = $fopen("streamedge2", "wb");
        if(streamedge2 == 0) begin
            $display("could not open streamedge2");
            $finish;
        end

        clk <= 0;
        rst <= 1;
        #10 clk <= 1; #10 clk <= 0;
        rst <= 0;

        forever begin
            #10 clk <= !clk;
        end
    end

    always @(posedge clk) begin
        got_data1 <= 0;
        sent_data1 <= got_data1;
        if (rst) begin
            count1 <= 0;
        end else if (!full1 && count1 > 0) begin
            din1[15:8] <= $fgetc(streamedge1);
            din1[7:0] <= $fgetc(streamedge1);
            got_data1 <= 1;
            count1 <= count1 - 1;
        end else if (!full1 && count1 == 0) begin
            count1[7:0] <= $fgetc(streamedge1);
            count1[15:8] <= $fgetc(streamedge1);
            count1[23:16] <= $fgetc(streamedge1);
            count1[31:24] <= $fgetc(streamedge1);
        end
    end
    assign write1 = got_data1 & !sent_data1;

    always @(posedge clk) begin
        if(!rst & avail2) begin
            rc <= $fputc(dout2[15:8], streamedge2);
            rc <= $fputc(dout2[7:0], streamedge2);
            $fflush(streamedge2);
        end
    end
    assign read2 = !rst & avail2;

endmodule

