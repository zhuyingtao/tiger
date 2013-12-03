#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
// The Gimple Garbage Collector.
FILE *out;

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
	void *from;       // the "from" space pointer
	void *fromFree;   // the next "free" space in the from space
	void *to;         // the "to" space pointer
	void *toStart;    // "start" address in the "to" space
	void *toNext;     // "next" free space pointer in the to space
};

// The Java heap, which is initialized by the following
// "heap_init" function.
struct JavaHeap heap;

// Lab 4, exercise 10:
// Given the heap size (in bytes), allocate a Java heap
// in the C heap, initialize the relevant fields.
void Tiger_heap_init(int heapSize) {

	out = fopen("log.txt", "w");

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
	// #6: initialize the "toStart" field with NULL;
	heap.toStart = heap.to;
	// #7: initialize the "toNext" field with NULL;
	heap.toNext = heap.to;

	fprintf(out, "The Java Heap init info :\n"
			"----the heapSize : %d\n"
			"----the 'from' pointer : 0x%p\n"
			"----the 'fromFree' pointer : 0x%p\n"
			"----the 'to' pointer : 0x%p\n\n\n", heapSize, heap.from,
			heap.fromFree, heap.to);
	fflush(out);
	return;
}

// The "prev" pointer, pointing to the top frame on the GC stack.
// (see part A of Lab 4)
extern void *prev;

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
struct ObjOrArray_header {
	void *vptr;		// virtual method table pointer
	int isObjOrArray;	//0 for a normal object && 1 for an (integer) array
	int length;		// array length
	void *forwarding;	// forwarding pointer, will be used by your Gimple GC
};

void *Tiger_new(void *vtable, int size) {
	// Your code here:
	//#1 : check whether the rest size of the Java heap is enough;
	int restSize = heap.to - heap.fromFree;
	int neededSize = size;
	//"size" has contained the object fields and the four machine words;

	if (restSize < neededSize) {                  //case 2: no enough space;
		Tiger_gc();
		restSize = heap.to - heap.fromFree;
		if (restSize < neededSize) {          //case 2.b: still no enough space;
			//print out the error message
			fprintf(stderr, "Error : still have not enough memory after GC,"
					"you should enlarge the Heap size!");
			exit(1);
		}
	}

	//case 1 && case 2.a: has enough space,then continue;
	//#2 : Initialize the new object;
	struct ObjOrArray_header *header;
	header = (struct ObjOrArray_header *) heap.fromFree;
	memset(header, 0, size);
	header->vptr = vtable;
	header->isObjOrArray = 0;

	//#3 : change the fromFree pointer;
	heap.fromFree = heap.fromFree + neededSize;  //now the fromFree change

	fprintf(out, "The new object info :\n"
			"----the rest size of Heap : %d\n"
			"----the needed size of this object : %d\n"
			"----the 'vptr' pointer : 0x%p\n"
			"----the 'fromFree' pointer : 0x%p\n\n\n", restSize, neededSize,
			header->vptr, heap.fromFree);

	//#4 : set up the object pointer and return;
	return header;
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
	int neededSize = length * sizeof(int) + sizeof(struct ObjOrArray_header);
	if (restSize < neededSize) {                  //case 2: no enough space;
		Tiger_gc();
		restSize = heap.to - heap.fromFree;
		if (restSize < neededSize) {          //case 2.b: still no enough space;
			//print out the error message
			fprintf(stderr, "Error : still have not enough memory after GC,"
					"you should enlarge the Heap size!");
			exit(1);
		}
	}

	//case 1 && case 2.a: has enough space,then continue;
	//#2 : Initialize the new object;
	struct ObjOrArray_header *header;
	header = (struct ObjOrArray_header *) heap.fromFree;
	memset(header, 0, neededSize);
	header->isObjOrArray = 1;
	header->length = length;

	//#3 : change the fromFree pointer;
	heap.fromFree = heap.fromFree + neededSize;  //now the fromFree change

	//#4 : set up the object pointer and return;
	int *p = (int*) (header + 1);  //plus the pointer with sizeof(header)

	fprintf(out, "The new array info :\n"
			"----the rest size of Heap : %d\n"
			"----the needed size of this object : %d\n"
			"----the length of the array : %d\n"
			"----the 'p' pointer : 0x%p\n"
			"----the 'fromFree' pointer : 0x%p\n\n\n", restSize, neededSize,
			header->length, p, heap.fromFree);
	return p;
}

//===============================================================//
// The Gimple Garbage Collector

// Lab 4, exercise 12:
// A copying collector based-on Cheney's algorithm.
struct node {
	void **p;
	struct node *next;
};

struct vtable_header {
	const char *class_gc_map;
};

struct gc_frame_header {
	void *prev;
	char *arguments_gc_map;
	void *arguments_base_address;
	int locals_gc_number;
};

//the usage of the double *:
//if you want to change the pointer you deliver,you should use the double '*' ;
void add(struct node **head, struct node **tail, void **p) {
	if (!(*((struct ObjOrArray_header **) p)))
		return;

	fprintf(out, "    Info : add 0x%p to list ;\n", p);
	fflush(out);

	struct node *new = (struct node *) malloc(sizeof(struct node));
	new->p = p;
	new->next = NULL;
	if (!*head) {		//if is the first node
		*head = *tail = new;
	} else {
		(*tail)->next = new;
		*tail = new;
	}
}

void **pop(struct node **head, struct node **tail) {
	if (!*head) {
		fprintf(stderr, "Error : Nothing pop from the list !\n");
		exit(1);
	}
	void **result = (*head)->p;
	if (*head == *tail) {
		free(*head);
		*head = *tail = NULL;
	} else {
		struct node *p = *head;
		*head = p->next;
		free(p);
	}
	return result;
}

void swap(void **p1, void **p2) {
	void *tmp;
	memcpy(&tmp, p1, sizeof(void *));
	memcpy(p1, p2, sizeof(void *));
	memcpy(p2, &tmp, sizeof(void *));
}

void forward(struct node **head, struct node **tail, void **p) {

	struct ObjOrArray_header **root = (struct ObjOrArray_header **) p;
	struct ObjOrArray_header *to_be_process = *root;

	if (to_be_process == NULL) {
		fprintf(out, "Error : the pointer to be process is NULL ! \n");
		return;
	}

	//case 1 : the object has already been processed.
	if (to_be_process->forwarding >= heap.to
			&& to_be_process->forwarding < heap.toNext) {
		fprintf(out, "    Info : the 0x%p has already been processed !\n",
				to_be_process);
		*root = to_be_process->forwarding;
		return;
	}

	//case 2: the object has not been processed.
	int size = sizeof(struct ObjOrArray_header);
	struct vtable_header *vtable = (struct vtable_header *) to_be_process->vptr;
	//calculate the needed size of the object or array;
	switch (to_be_process->isObjOrArray) {
	case 0:
		//if is object : header + all the fields(only has the type 'int' and '*') ;
		size += strlen(vtable->class_gc_map) * sizeof(void *);
		break;
	case 1:
		//if is array : header + length * int;
		size += to_be_process->length * sizeof(int);
		break;
	default:
		fprintf(stderr, "Error : unknown type for Tiger when GC !\n");
		exit(1);
	}
	memcpy(heap.toNext, to_be_process, size);
	fprintf(out, "----copy 0x%p to 'to' space;\n", to_be_process);
	*root = to_be_process->forwarding = heap.toNext;
	heap.toNext += size;

	//After copy ,traverse its fields;
	if (to_be_process->isObjOrArray == 0) {
		void **next = (void *) to_be_process + sizeof(struct ObjOrArray_header);
		const char *c = vtable->class_gc_map;
		int index;
		for (index = 0; *c != '\0'; c++, index++) {
			if (*c == '1') {
				fprintf(out, "    Info: find a reference field 0x%p"
						" for 0x%p ;\n",
						((void *) next + index * sizeof(void *)),
						to_be_process);
				add(head, tail, (void *) next + index * sizeof(void *));
			}
		}
	}
}

void traverse(struct node **head, struct node **tail) {
	fprintf(out, "\n<-----Traverse the list start------>\n");
	while (*head != NULL || *tail != NULL) {
		void **p = pop(head, tail);
		forward(head, tail, p);
	}
	fprintf(out, "<-----Traverse the list end------>\n\n");
}

void Tiger_gc() {
	// Your code here:
	// scan the GCStack frames to find the unreachable object in the "from" space
	// The "prev" pointer, pointing to the top frame on the GC stack.
	// use BFS strategy to traverse all the struct.

	/*The C calling convention and call stack layout can be well explained by the
	 * function f from the above exercise. The following figure illustrates the
	 * stack layout of f on the x86 ISA:

	 ----------------------------------------------------------> low address
	 | arg3 | arg2 | arg1 | this | ret | ebp |  locals?
	 -----------------------------------------------------------
	 ^
	 ebp
	 */
	fprintf(out, "===============Collect Garbage Start==============\n");

	static int round = 0;
	clock_t start, finish;
	int size_before = heap.fromFree - heap.from;
	start = clock();   //gc begin;

	struct node *head = NULL;
	struct node *tail = NULL;

	if (!prev) {
		fprintf(stderr, "Error : no stack now but the gc is called !\n");
		exit(1);
	}

	struct gc_frame_header *stack_top = prev; //the prev always pointer to the stack top;
	while (stack_top) {
		//#1 : append the reference arguments;
		if (stack_top->arguments_gc_map != NULL) {
			char *p = stack_top->arguments_gc_map;
			int index = 0;
			fprintf(out, "Info : on the top stack 0x%p , "
					"arguments_gc_map is '%s' \n", stack_top,
					stack_top->arguments_gc_map);

			while (*p != '\0') {
				if (*p == '1') {
					fprintf(out, "----argument %d address 0x%x \n", index,
							(stack_top->arguments_base_address + index));
					add(&head, &tail,
							(((void *) stack_top->arguments_base_address)
									+ (index) * sizeof(void *)));
				}
				index++;
				p++;
			}
		}
		//#2 : append the reference locals;
		if (stack_top->locals_gc_number != 0) {
			fprintf(out, "Info : on the top stack 0x%p , "
					"locals_gc_number is %d \n", stack_top,
					stack_top->locals_gc_number);
			void **base = (void *) stack_top + sizeof(struct gc_frame_header);
			int i;
			for (i = 0; i < stack_top->locals_gc_number; i++) {
				fprintf(out, "----local %d address 0x%x \n", i,
						(void *) base + i * sizeof(void *));
				add(&head, &tail, (void *) base + i * sizeof(void *));
			}
		}
		//#3 : traverse the map with BFS;
		traverse(&head, &tail);
		stack_top = (struct gc_frame_header *) stack_top->prev;
	}

	swap(&heap.to, &heap.from);
	swap(&heap.toNext, &heap.fromFree);
	heap.toStart = heap.toNext = heap.to;

	finish = clock();  //gc end;
	int size_after = heap.fromFree - heap.from;
	double time = (double) (finish - start) / CLOCKS_PER_SEC; //calculate the gc time;
	int collect_bytes = size_before - size_after;
	fprintf(out, "At last , used %f second collected %d bytes.\n", time,
			collect_bytes);

	fprintf(out, "===============Collect Garbage End==============\n");
	fflush(out);
}

