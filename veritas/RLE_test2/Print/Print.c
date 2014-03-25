#include "Print.h"
void sp_Print_init(struct sp_Print_data *kernel)
{
}
void sp_Print_destroy(struct sp_Print_data *kernel)
{
}
SP_READ_FUNCTION(UNSIGNED16, sp_Print_data, 0);
void sp_Print_run(struct sp_Print_data *kernel)
{
    for(;;)
    {
        printf(" %u \012", sp_read_input0(kernel));
        exit(0);
    }
}
