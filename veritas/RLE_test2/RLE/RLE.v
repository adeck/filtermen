module kernel_RLE(
    input_S1,
    avail_S1,
    read_S1,
    output_S2,
    write_S2,
    afull_S2,
    rst,
    clk
);
    input wire [15:0] input_S1;
    input wire avail_S1;
    output wire read_S1;
    output wire [15:0] output_S2;
    output wire write_S2;
    input wire afull_S2;
    input wire rst;
    input wire clk;
    parameter pixelCount = 1600;
    reg [15:0] state_S3;
    reg [15:0] state_S4;
    reg signed [7:0] state_S5;
    reg [31:0] state_S6;
    reg [15:0] temp1;
    reg signed [7:0] temp3;
    reg signed [7:0] temp4;
    reg [15:0] temp6;
    reg signed [7:0] temp7;
    reg [15:0] temp8;
    reg [15:0] temp10;
    reg signed [7:0] temp11;
    reg signed [7:0] temp12;
    reg [31:0] state;
    reg [31:0] last_state;
    wire [15:0] sp_addIx0_result;
    sp_addI #(.WIDTH(16))
        sp_addIx0(1, state_S3, sp_addIx0_result);

    wire [15:0] sp_addIx2_result;
    sp_addI #(.WIDTH(16))
        sp_addIx2(1, state_S4, sp_addIx2_result);

    wire [15:0] sp_addIx1_result;
    sp_addI #(.WIDTH(16))
        sp_addIx1(1, state_S4, sp_addIx1_result);

    wire guard_23 = !afull_S2;
    wire guard_8 = 1;
    wire guard_11 = 1;
    wire guard_2 = 1;
    wire guard_20 = 1;
    wire guard_14 = 1;
    wire guard_4 = 1;
    wire guard_22 = 1;
    wire guard_7 = !afull_S2;
    wire guard_16 = !afull_S2;
    wire guard_1 = avail_S1;
    wire guard_19 = !afull_S2;
    wire guard_18 = !afull_S2;
    wire guard_21 = 1;
    wire guard_24 = 1;
    wire guard_6 = 1;
    assign read_S1 = ((state == 1) & guard_1);
    assign write_S2 = ((state == 19) & guard_19) | ((state == 23) & guard_23) | ((state == 18) & guard_18) | ((state == 16) & guard_16) | ((state == 7) & guard_7);
    assign output_S2 =  state == 19 ? temp1 : ( state == 23 ? state_S4 : ( state == 7 ? 0 : ( state == 18 ? state_S4 : (temp8))));
    reg dummy;
    always @(*) begin
        dummy <= clk;
        temp8 <= state_S6[15:0];
        temp6 <= sp_addIx2_result;
        temp7 <= 0 == state_S5;
        temp3 <= 0 == state_S6;
        temp11 <= state_S3 == pixelCount;
        temp4 <= 0 == state_S5;
    end


    always @(posedge clk) begin
        if (rst) begin
            temp12 <= 0;
            temp1 <= 0;
            temp10 <= 0;
            // S5 <- 0
            state_S5 <= 0;
            // S4 <- 0
            state_S4 <= 0;
            // S3 <- 0
            state_S3 <= 0;
            // start 1
            state <= 1;
            last_state <= 0;
        end else begin
            last_state <= state;
            if (state == 1) begin
                if (guard_1) begin
                    // temp10 <- 1 + S3
                    temp10 <= sp_addIx0_result;
                    // temp1 <- input0
                    temp1 <= input_S1;
                    // goto 2
                    state <= 2;
                end
            end
            if (state == 2) begin
                if (guard_2) begin
                    // S3 <- temp10
                    state_S3 <= temp10;
                    // S6 <- convert temp1
                    state_S6 <= {{16{1'b0}},temp1};
                    // goto 3
                    state <= 4;
                end
            end
            // temp11 <- S3 == pixelCount
            // temp3 <- 0 == S6
            // goto 4
            if (state == 4) begin
                if (guard_4) begin
                    // if temp3 then 5 else 13
                    state <= temp3 ? 6 : 14;
                end
            end
            // temp4 <- 0 == S5
            // goto 6
            if (state == 6) begin
                if (guard_6) begin
                    // if temp4 then 7 else 10
                    state <= temp4 ? 7 : 11;
                end
            end
            if (state == 7) begin
                if (guard_7) begin
                    // output0 <- 0
                    // S5 <- 1
                    state_S5 <= 1;
                    // temp1 <- 1 + S4
                    temp1 <= sp_addIx1_result;
                    // goto 8
                    state <= 8;
                end
            end
            if (state == 8) begin
                if (guard_8) begin
                    // S4 <- temp1
                    state_S4 <= temp1;
                    // goto 9
                    state <= 20;
                end
            end
            // goto 12
            // temp6 <- 1 + S4
            // goto 11
            if (state == 11) begin
                if (guard_11) begin
                    // S4 <- temp6
                    state_S4 <= temp6;
                    // goto 12
                    state <= 20;
                end
            end
            // goto 20
            // temp7 <- 0 == S5
            // goto 14
            if (state == 14) begin
                if (guard_14) begin
                    // if temp7 then 15 else 18
                    state <= temp7 ? 16 : 18;
                end
            end
            // temp8 <- convert S6
            // goto 16
            if (state == 16) begin
                if (guard_16) begin
                    // output0 <- temp8
                    // goto 17
                    state <= 20;
                end
            end
            // goto 20
            if (state == 18) begin
                if (guard_18) begin
                    // output0 <- S4
                    // S5 <- 0
                    state_S5 <= 0;
                    // temp1 <- convert S6
                    temp1 <= state_S6[15:0];
                    // goto 19
                    state <= 19;
                end
            end
            if (state == 19) begin
                if (guard_19) begin
                    // output0 <- temp1
                    // S4 <- 0
                    state_S4 <= 0;
                    // goto 20
                    state <= 20;
                end
            end
            if (state == 20) begin
                if (guard_20) begin
                    // if temp11 then 21 else 28
                    state <= temp11 ? 21 : 1;
                end
            end
            if (state == 21) begin
                if (guard_21) begin
                    // S3 <- 0
                    state_S3 <= 0;
                    // temp12 <- 1 == S5
                    temp12 <= 1 == state_S5;
                    // goto 22
                    state <= 22;
                end
            end
            if (state == 22) begin
                if (guard_22) begin
                    // S5 <- 0
                    state_S5 <= 0;
                    // if temp12 then 23 else 26
                    state <= temp12 ? 23 : 1;
                end
            end
            if (state == 23) begin
                if (guard_23) begin
                    // output0 <- S4
                    // goto 24
                    state <= 24;
                end
            end
            if (state == 24) begin
                if (guard_24) begin
                    // S4 <- 0
                    state_S4 <= 0;
                    // goto 25
                    state <= 1;
                end
            end
            // goto 27
            // goto 27
            // goto 29
            // goto 29
            // goto 1
        end
    end
endmodule
