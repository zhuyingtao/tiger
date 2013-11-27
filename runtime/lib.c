#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int System_out_println(int i) {
	printf("%d\n", i);
	return 0;
}

//int* Array_new(int sz) {
//	int* id = (int*) malloc(sizeof(int) * (sz + 1));
//	id[sz] = 0; //to mark the end of the array
//	return id;
//}

int Length(int* array) {
	int i = *(array - 8);
//	int count = 0;
//	while (array[i] != 0) {
//		count++;
//		i++;
//	}
	return i;
}
