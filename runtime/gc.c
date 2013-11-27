#include <stdio.h>
#include <stdlib.h>
#include <string.h>

// The Gimple Garbage Collector.

void Tiger_gc();

//===============================================================//
// The Java Heap data structure.

/*
 ----------------------------------------------------
 |                        |                         |
 ----------------------------------------------------
 ^\                      /^
 | \<~~~~~~~ size ~~~~~>/ |
 from                       to
 */
struct JavaHeap {
	int size;         // in bytes, note that this if for semi-heap size
	char *from;       // the "from" space pointer
	char *fromFree;   // the next "free" space in the from space
	char *to;         // the "to" space pointer
	char *toStart;    // "start" address in the "to" space
	char *toNext;     // "next" free space pointer in the to space
};

// The Java heap, which is initialized by the following
// "heap_init" function.
struct JavaHeap heap;

// Lab 4, exercise 10:
// Given the heap size (in bytes), allocate a Java heap
// in the C heap, initialize the relevant fields.
void Tiger_heap_init(int heapSize) {
	// You should write 7 statement here:
	// #1: allocate a chunk of memory of size "heapSize" using "malloc"
	char *object = (char*) malloc(heapSize);

	// #2: initialize the "size" field, note that "size" field
	// is for semi-heap, but "heapSize" is for the whole heap.
	heap.size = heapSize / 2;

	// #3: initialize the "from" field (with what value?)
	heap.from = object;

	// #4: initialize the "fromFree" field (with what value?)
	heap.fromFree = object;

	// #5: initialize the "to" field (with what value?)
	heap.to = object + heap.size;

	// #6: initizlize the "toStart" field with NULL;
	heap.toStart = NULL;

	// #7: initialize the "toNext" field with NULL;
	heap.toNext = NULL;

	return;
}

// The "prev" pointer, pointing to the top frame on the GC stack.
// (see part A of Lab 4)
void *prev = 0;

//===============================================================//
// Object Model And allocation

// Lab 4: exercise 11:
// "new" a new object, do necessary initializations, and
// return the pointer (reference).
/*    ----------------
 	  | vptr      ---|----> (points to the virtual method table)
  	  |--------------|
 	  | isObjOrArray | (0: for normal objects)
 	  |--------------|
 	  | length       | (this field should be empty for normal objects)
 	  |--------------|
 	  | forwarding   |
 	  |--------------|\
p---->| v_0          | \
      |--------------|  s
 	  | ...          |  i
 	  |--------------|  z
 	  | v_{size-1}   | /e
 	  ----------------/
 */
// Try to allocate an object in the "from" space of the Java
// heap. Read Tiger book chapter 13.3 for details on the
// allocation.
// There are two cases to consider:
//   1. If the "from" space has enough space to hold this object, then
//      allocation succeeds, return the apropriate address (look at
//      the above figure, be careful);
//   2. if there is no enough space left in the "from" space, then
//      you should call the function "Tiger_gc()" to collect garbages.
//      and after the collection, there are still two sub-cases:
//        a: if there is enough space, you can do allocations just as case 1;
//        b: if there is still no enough space, you can just issue
//           an error message ("OutOfMemory") and exit.
//           (However, a production compiler will try to expand
//           the Java heap.)

void *Tiger_new(void *vtable, int size) {
	// Your code here:
	//#1 : check whether the rest size of the Java heap is enough;
	int restSize = heap.to - heap.fromFree;
	int neededSize = size + 16;  //the object size and the four machine words;

	if (restSize < neededSize) {                  //case 2: no enough space;
		Tiger_gc();
		//restSize=heap.size-abs(heap.fromFree-heap.from);
		restSize = heap.to - heap.fromFree;
		if (restSize < neededSize) {          //case 2.b: still no enough space;
			//print out the error message
			exit(1);
		}
	}
	//case 1 && case 2.a: has enough space,then continue;
	//#2 : Initialize the new object;
	int *vptr;                    // virtual method table pointer
	vptr = (int*) heap.fromFree;
	*vptr = (int) vtable;
	int isObjOrArray = 0; // is this a normal object or an (integer) array object?
	*(vptr + 4) = isObjOrArray;
	unsigned int length = 0;            // array length
	*(vptr + 8) = length;
	void *forwarding;      // forwarding pointer, will be used by your Gimple GC
	*(vptr + 12) = (int) forwarding;

	//#3 : change the fromFree pointer;
	heap.fromFree = heap.fromFree + neededSize;  //now the fromFree change

	//#4 : set up the object pointer and return;
	int *p = vptr + 16;               //the object pointer
	p = vptr;                       //the first field is the vptr pointer
	return p;
}

// "new" an array of size "length", do necessary
// initializations. And each array comes with an
// extra "header" storing the array length and other information.
/*    ----------------
 	  | vptr         | (this field should be empty for an array)
 	  |--------------|
 	  | isObjOrArray | (1: for array)
 	  |--------------|
 	  | length       |
 	  |--------------|
 	  | forwarding   |
 	  |--------------|\
p---->| e_0          | \
      |--------------|  s
 	  | ...          |  i
 	  |--------------|  z
 	  | e_{length-1} | /e
 	  ----------------/
 */
// Try to allocate an array object in the "from" space of the Java
// heap. Read Tiger book chapter 13.3 for details on the
// allocation.
// There are two cases to consider:
//   1. If the "from" space has enough space to hold this array object, then
//      allocation succeeds, return the apropriate address (look at
//      the above figure, be careful);
//   2. if there is no enough space left in the "from" space, then
//      you should call the function "Tiger_gc()" to collect garbages.
//      and after the collection, there are still two sub-cases:
//        a: if there is enough space, you can do allocations just as case 1;
//        b: if there is still no enough space, you can just issue
//           an error message ("OutOfMemory") and exit.
//           (However, a production compiler will try to expand
//           the Java heap.)
void *Tiger_new_array(int length) {
	// Your code here:
	//#1 : check whether the rest size of the Java heap is enough;
	int restSize = heap.to - heap.fromFree;
	int neededSize = length * sizeof(int) + 16; //the array size and the four machine words;
	if (restSize < neededSize) {                  //case 2: no enough space;
		Tiger_gc();
		restSize = heap.to - heap.fromFree;
		if (restSize < neededSize) {          //case 2.b: still no enough space;
			//print out the error message
			exit(1);
		}
	}
	//case 1 && case 2.a: has enough space,then continue;
	//#2 : Initialize the new object;
	int *vptr;                    // virtual method table pointer
	vptr = (int*) heap.fromFree;
	*vptr = 0;
	int isObjOrArray = 1; // is this a normal object or an (integer) array object?
	*(vptr + 4) = isObjOrArray;
	unsigned int alength = length;  // array length
	*(vptr + 8) = alength;
	void *forwarding;      // forwarding pointer, will be used by your Gimple GC
	*(vptr + 12) = (int) forwarding;

	//#3 : change the fromFree pointer;
	heap.fromFree = heap.fromFree + neededSize;  //now the fromFree change

	//#4 : set up the object pointer and return;
	int *p = vptr + 16;               //the object pointer
	return p;

}

//===============================================================//
// The Gimple Garbage Collector

// Lab 4, exercise 12:
// A copying collector based-on Cheney's algorithm.
void Tiger_gc() {
	// Your code here:

}

