#ifndef sp_Print_H_
#define sp_Print_H_
#include "ScalaPipe.h"
#include <stdio.h>
/* UNSIGNED16 */
struct sp_Print_data
{
}
;
void sp_Print_init(struct sp_Print_data*);
void sp_Print_destroy(struct sp_Print_data*);
void sp_Print_run(struct sp_Print_data*);
#endif
