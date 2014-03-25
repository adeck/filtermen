#ifndef sp_NumSequence_H_
#define sp_NumSequence_H_
#include "ScalaPipe.h"
/* UNSIGNED16 */
struct sp_NumSequence_data
{
    UNSIGNED16 S1;
    UNSIGNED16 S3;
}
;
void sp_NumSequence_init(struct sp_NumSequence_data*);
void sp_NumSequence_destroy(struct sp_NumSequence_data*);
void sp_NumSequence_run(struct sp_NumSequence_data*);
#endif
