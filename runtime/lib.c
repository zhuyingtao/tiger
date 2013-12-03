#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int System_out_println(int i) {
	printf("%d\n", i);
	return 0;
}

int Length(int* array) {
	//minus the array pointer with 2 sizeof(int)
	//the new address contains the length of the array;
	int i = *(array - 2);
	//printf("the size of the array %d\n", i);
	return i;
}
