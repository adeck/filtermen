#include "NumSequence.h"
void sp_NumSequence_init(struct sp_NumSequence_data *kernel)
{
    kernel->S1 = 50;
    kernel->S3 = 0;
}
void sp_NumSequence_destroy(struct sp_NumSequence_data *kernel)
{
}
void sp_NumSequence_run(struct sp_NumSequence_data *kernel)
{
    UNSIGNED16 *S2;
    for(;;)
    {
        if(((kernel->S3) < (20)) || ((kernel->S3) > (30)))
        {
            S2 = (UNSIGNED16*)sp_allocate(kernel, 0);
            *S2 = 0;
            sp_send(kernel, 0);
        }
        else
        {
            S2 = (UNSIGNED16*)sp_allocate(kernel, 0);
            *S2 = kernel->S3;
            sp_send(kernel, 0);
        }
        if((kernel->S3) < (kernel->S1))
        {
            kernel->S3 = (kernel->S3) + (1);
        }
        else
        {
            return;
        }
    }
}
