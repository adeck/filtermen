TARGETS=proc_localhost
C_BLOCKS=Print NumSequence
FPGA_BLOCKS=RLE
TTOBJ=
EXTRA_CFLAGS=
EXTRA_CXXFLAGS=
EXTRA_LDFLAGS=


# Determine the full path to the project.
ifndef THIS
    export THIS:=$(shell pwd)
endif

BLOCKS := $(sort $(FPGA_BLOCKS) $(C_BLOCKS))
BLOCK_CINC := $(foreach x,$(C_BLOCKS),-I$(THIS)/$x)
BLOCK_XINC := $(foreach x,$(BLOCKS),-I$(THIS)/$x)

# Determine our architecture.
ARCH := $(shell uname -s)-$(shell uname -m)

INCS = -I$(THIS)
export CFLAGS = -Wall -O2 $(INCS) $(EXTRA_CFLAGS) $(USER_CFLAGS)
export CXXFLAGS = $(CFLAGS) $(EXTRA_CFLAGS) $(USER_CXXFLAGS) 
export CC=gcc
export CXX=g++
export LDFLAGS=-lpthread $(EXTRA_LDFLAGS) $(USER_LDFLAGS)
export C_FILE_LIST=$(THIS)/.c_file_list
export CXX_FILE_LIST=$(THIS)/.cxx_file_list
export VHDL_FILE_LIST=$(THIS)/.vhdl_file_list
export V_FILE_LIST=$(THIS)/.v_file_list

CXXFILES=$(strip $(foreach f,$(shell cat $(CXX_FILE_LIST) 2>/dev/null),$f))
CFILES=$(strip $(foreach f,$(shell cat $(C_FILE_LIST) 2>/dev/null),$f))
ALL_C_FILES=$(CXXFILES) $(CFILES)
OBJECTS = $(foreach f,$(ALL_C_FILES),$(addsuffix .o,$(basename $f))) $(TTOBJ)

# Compile by default
all: compile

flow: compile
	$(MAKE) syn
	$(MAKE) build

help:
	@echo
	@echo "Makefile targets:"
	@echo "    all              Same as compile (default target)"
	@echo "    compile          Check out blocks and compile"
	@echo "    update           Update blocks to the latest revision"
	@echo "    syn              Synthesize HDL"
	@echo "    build            Generate a bitfile"
	@echo "    flow             Same as \"make syn build\""
	@echo "    sim              Simluate HDL"
	@echo "    install          Install the bitfile"
	@echo "    clean            Remove generated files"
	@echo "    distclean        Remove generated files and block checkouts"
	@echo "    help             Display this message"
	@echo

# Check out and build the blocks.
.PHONY: blocks
blocks:
	$(foreach b,$(BLOCKS), $(MAKE) $(b);)
	rm -f $(V_FILE_LIST) $(VHDL_FILE_LIST) $(C_FILE_LIST) $(CXX_FILE_LIST)
	$(foreach b,$(BLOCKS),\
	    (cd $b ; $(MAKE) get_files "BLKDIR=$(THIS)/$b");)

.PHONY: clean_blocks
clean_blocks:
	$(foreach b,$(C_BLOCKS), \
		(if [ -e $(b) ] ; then \
			cd $(b) && $(MAKE) -i clean ; \
		fi);)

# Rule for compiling everything.
compile: blocks
	$(MAKE) $(TARGETS)

# Rule for compiling C++ code.
%.o: %.cpp
	$(CXX) -c $(CXXFLAGS) $(BLOCK_CINC) -o $@ $<

# Rule for compiling C code.
%.o: %.c
	$(CC) -c $(CFLAGS) $(BLOCK_CINC) -o $@ $<

# Rule for creating the proc_* executables.
proc_%: proc_%.o $(OBJECTS)
	$(CXX) $(CXXFLAGS) -o $@ $@.o $(OBJECTS) $(LDFLAGS)


SIM_device4_KERNELS =sim_device4.v scalapipe.v platform.v fpga_device4.v wrap_device4.v RLE/RLE.v
TARGETS += hdl_device4

build_device4: hdl_device4

hdl_device4: $(SIM_device4_KERNELS)
	iverilog -o hdl_device4 $(SIM_device4_KERNELS)

sim: $(TARGETS) compile
	echo finish | ./proc_localhost



# Rule for cleaning up.
clean: clean_blocks
	rm -f $(TARGETS) proc_*.o $(VHDL_FILE_LIST) $(V_FILE_LIST) $(C_FILE_LIST) $(CXX_FILE_LIST) dump.vcd

# Rule for cleaning up everything.
distclean: clean
	$(foreach b,$(C_BLOCKS), rm -rf $b;)
	$(foreach b,$(FPGA_BLOCKS), rm -rf $b;)


