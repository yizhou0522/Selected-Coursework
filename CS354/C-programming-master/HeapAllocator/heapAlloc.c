
#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <sys/mman.h>
#include <stdio.h>
#include <string.h>
#include "heapAlloc.h"
#include<math.h>

/*
 * This structure serves as the header for each allocated and free block.
 * It also serves as the footer for each free block but only containing size.
 */
typedef struct blockHeader {
    int size_status;
    /*
    * Size of the block is always a multiple of 8.
    * Size is stored in all block headers and free block footers.
    *
    * Status is stored only in headers using the two least significant bits.
    *   Bit0 => least significant bit, last bit
    *   Bit0 == 0 => free block
    *   Bit0 == 1 => allocated block
    *
    *   Bit1 => second last bit 
    *   Bit1 == 0 => previous block is free
    *   Bit1 == 1 => previous block is allocated
    * 
    * End Mark: 
    *  The end of the available memory is indicated using a size_status of 1.
    * 
    * Examples:
    * 
    * 1. Allocated block of size 24 bytes:
    *    Header:
    *      If the previous block is allocated, size_status should be 27
    *      If the previous block is free, size_status should be 25
    * 
    * 2. Free block of size 24 bytes:
    *    Header:
    *      If the previous block is allocated, size_status should be 26
    *      If the previous block is free, size_status should be 24
    *    Footer:
    *      size_status should be 24
    */
} blockHeader;

/* Global variable - DO NOT CHANGE. It should always point to the first block,
 * i.e., the block at the lowest address.
 */

blockHeader *heapStart = NULL;
blockHeader *heapPrevious = NULL;//global variable that points to the most recently allocated block
blockHeader *heapFar = NULL;//global variable that points to the second last most recently allocated block
int total_mem_size = 0;//global variable that records the total heap space

/*
 * Function for allocating 'size' bytes of heap memory.
 * Argument size: requested size for the payload
 * Returns address of allocated block on success.
 * Returns NULL on failure.
 * This function should:
 * - Check size - Return NULL if not positive or if larger than heap space.
 * - Determine block size rounding up to a multiple of 8 and possibly adding padding as a result.
 * - Use NEXT-FIT PLACEMENT POLICY to chose a free block
 * - Use SPLITTING to divide the chosen free block into two if it is too large.
 * - Update header(s) and footer as needed.
 * Tips: Be careful with pointer arithmetic and scale factors.
 */
void *allocHeap(int size) {

    //initialize the heapPrevious Pointer
    if (heapPrevious == NULL) {
        heapPrevious = heapStart;
    }

    //initialize the total heapspace
    if (total_mem_size == 0) {
        total_mem_size = heapStart->size_status - 2;
    }

    if (size <= 0 || size > total_mem_size) {
        return NULL;
    }

    int const header = sizeof(blockHeader);
    size = header + size;
    //if size is not a multiple of 8, round up to it
    if (size % 8 != 0) {
        size = size + (8 - size % 8);
    }
    blockHeader *curr = heapPrevious;

    int currPbit = 0;
    int currAbit = 0;
    int currSize = 0;

    while (1) {
        //get the current pointer p-bit and a-bit
        currAbit = (curr->size_status) & 1;
        currPbit = (curr->size_status) & 2;
        currSize = curr->size_status - currAbit - currPbit;
        //if it is a free block and has a size larger than required, then break the loop
        if (currAbit == 0 && currSize >= size) {
            break;
        }
        //continuing traversing the heap if target block not found
        curr = curr + (currSize / sizeof(blockHeader));

        //current pointer reach the endmark of the heapspace
        int x = curr - heapStart;
        if (4 * x >= (total_mem_size - 8)) {
            curr = heapStart;
        }
        //current pointer reach the most recently allocated block
        if (curr == heapPrevious) {
            return NULL;
        }
    }

    //split the free block
    curr->size_status = size + 3;

    //if larger, then split the large free block
    if (currSize != size) {
        //get the new free block's header
        blockHeader *nfb = curr + size / sizeof(blockHeader);

        //set the new free block's size_status
        nfb->size_status = currSize - size + 2 + 0;

        //set the footer
        blockHeader *nfbFooter = nfb + (currSize - size) / sizeof(blockHeader) - 1;
        nfbFooter->size_status = currSize - size;

        // curr->size_status=curr->size_status-(nfb->size_status-2);
    } else {
        //set the next allocated block's SLB to 1
        blockHeader *next = curr + size / sizeof(blockHeader);
        next->size_status += 2;
    }
    blockHeader *blk = curr + 1;
    //records the previous heap pointers
    heapFar = heapPrevious;
    heapPrevious = curr;
    return blk;
}


/* 
 * Function for freeing up a previously allocated block.
 * Argument ptr: address of the block to be freed up.
 * Returns 0 on success.
 * Returns -1 on failure.
 * This function should:
 * - Return -1 if ptr is NULL.
 * - Return -1 if ptr is not a multiple of 8.
 * - Return -1 if ptr is outside of the heap space.
 * - Return -1 if ptr block is already freed.
 * - USE IMMEDIATE COALESCING if one or both of the adjacent neighbors are free.
 * - Update header(s) and footer as needed.
 */
int freeHeap(void *ptr) {
    if (ptr == NULL)
        return -1;

    blockHeader *hdrPtr = ptr - sizeof(blockHeader);
    int aBit = hdrPtr->size_status & 1;
    int pBit = hdrPtr->size_status & 2;
    int size = hdrPtr->size_status - aBit - pBit;

    if (hdrPtr - heapStart >= total_mem_size
        || hdrPtr < heapStart
        || aBit == 0
        || size % 8 != 0) {
        return -1;
    }
    //info of previous and next block
    int isPrevFree = 0;
    int isNextFree = 0;
    int prevSize = 0;
    blockHeader *prev = NULL;
    blockHeader *next = hdrPtr + size / sizeof(blockHeader);
    int nextSize = 0;
    blockHeader *finalFooter = NULL;
    blockHeader *currFooter = hdrPtr + size / sizeof(blockHeader) - 1;
    blockHeader *prevFooter = hdrPtr - 1;

    //get the previous header
    if (pBit == 0) {
        prevSize = (hdrPtr - 1)->size_status;
        prev = hdrPtr - prevSize / sizeof(blockHeader);
        //set isPrevFree if previous block is free
        isPrevFree = 1;
    }
    //get the next header
    int nextAbit = next->size_status & 1;
    int nextPbit = next->size_status & 2;

    if (nextAbit == 1) {
        //if the next is not free
        next->size_status -= 2;
    } else if (nextAbit == 0) {
        isNextFree = 1;
        nextSize = next->size_status - nextAbit - nextPbit;
    }

    if (isPrevFree & isNextFree) {
        //if next and previous is both freed
        //coalesce those 3
        prev->size_status = prevSize + size + nextSize + (prev->size_status % 8);
        currFooter->size_status = 0;
        prevFooter->size_status = 0;
        hdrPtr->size_status = 0;
        next->size_status = 0;
        finalFooter = next + nextSize / sizeof(blockHeader) - 1;
        finalFooter->size_status = prevSize + size + nextSize;
    } else if (isPrevFree) {
        //if only previous is free
        hdrPtr->size_status = 0;
        prevFooter->size_status = 0;
        prev->size_status = prevSize + size + (prev->size_status % 8);
        finalFooter = hdrPtr + size / sizeof(blockHeader) - 1;
        finalFooter->size_status = prevSize + size;
    } else if (isNextFree) {
        //if only next is free
        currFooter->size_status = 0;
        next->size_status = 0;
        hdrPtr->size_status = size + nextSize + 2;
        finalFooter = next + nextSize / sizeof(blockHeader) - 1;
        finalFooter->size_status = size + nextSize;
    } else {
        //if previous and next are both allocated
        hdrPtr->size_status = size + 2;
        finalFooter = hdrPtr + size / sizeof(blockHeader) - 1;
        finalFooter->size_status = size;
    }
    //change the heapPrevious pointer if necessary
    if (((heapPrevious->size_status) & 1) == 0) {
        heapPrevious = heapFar;
    }
    return 0;
}

/*
 * Function used to initialize the memory allocator.
 * Intended to be called ONLY once by a program.
 * Argument sizeOfRegion: the size of the heap space to be allocated.
 * Returns 0 on success.
 * Returns -1 on failure.
 */
int initHeap(int sizeOfRegion) {

    static int allocated_once = 0; //prevent multiple initHeap calls

    int pagesize;  // page size
    int padsize;   // size of padding when heap size not a multiple of page size
    int allocsize; // size of requested allocation including padding
    void *mmap_ptr; // pointer to memory mapped area
    int fd;

    blockHeader *endMark;

    if (0 != allocated_once) {
        fprintf(stderr,
                "Error:mem.c: InitHeap has allocated space during a previous call\n");
        return -1;
    }
    if (sizeOfRegion <= 0) {
        fprintf(stderr, "Error:mem.c: Requested block size is not positive\n");
        return -1;
    }

    // Get the pagesize
    pagesize = getpagesize();

    // Calculate padsize as the padding required to round up sizeOfRegion 
    // to a multiple of pagesize
    padsize = sizeOfRegion % pagesize;
    padsize = (pagesize - padsize) % pagesize;

    allocsize = sizeOfRegion + padsize;

    // Using mmap to allocate memory
    fd = open("/dev/zero", O_RDWR);
    if (-1 == fd) {
        fprintf(stderr, "Error:mem.c: Cannot open /dev/zero\n");
        return -1;
    }
    mmap_ptr = mmap(NULL, allocsize, PROT_READ | PROT_WRITE, MAP_PRIVATE, fd, 0);
    if (MAP_FAILED == mmap_ptr) {
        fprintf(stderr, "Error:mem.c: mmap cannot allocate space\n");
        allocated_once = 0;
        return -1;
    }

    allocated_once = 1;

    // for double word alignment and end mark
    allocsize -= 8;

    // Initially there is only one big free block in the heap.
    // Skip first 4 bytes for double word alignment requirement.
    heapStart = (blockHeader *) mmap_ptr + 1;

    // Set the end mark
    endMark = (blockHeader *) ((void *) heapStart + allocsize);
    endMark->size_status = 1;

    // Set size in header
    heapStart->size_status = allocsize;

    // Set p-bit as allocated in header
    // note a-bit left at 0 for free
    heapStart->size_status += 2;

    // Set the footer
    blockHeader *footer = (blockHeader *) ((char *) heapStart + allocsize - 4);
    footer->size_status = allocsize;

    return 0;
}

/* 
 * Function to be used for DEBUGGING to help you visualize your heap structure.
 * Prints out a list of all the blocks including this information:
 * No.      : serial number of the block 
 * Status   : free/used (allocated)
 * Prev     : status of previous block free/used (allocated)
 * t_Begin  : address of the first byte in the block (where the header starts) 
 * t_End    : address of the last byte in the block 
 * t_Size   : size of the block as stored in the block header
 */
void DumpMem() {

    int counter;
    char status[5];
    char p_status[5];
    char *t_begin = NULL;
    char *t_end = NULL;
    int t_size;

    blockHeader *current = heapStart;
    counter = 1;

    int used_size = 0;
    int free_size = 0;
    int is_used = -1;

    fprintf(stdout, "************************************Block list***\
                    ********************************\n");
    fprintf(stdout, "No.\tStatus\tPrev\tt_Begin\t\tt_End\t\tt_Size\n");
    fprintf(stdout, "-------------------------------------------------\
                    --------------------------------\n");

    while (current->size_status != 1) {
        t_begin = (char *) current;
        t_size = current->size_status;

        if (t_size & 1) {
            // LSB = 1 => used block
            strcpy(status, "used");
            is_used = 1;
            t_size = t_size - 1;
        } else {
            strcpy(status, "Free");
            is_used = 0;
        }

        if (t_size & 2) {
            strcpy(p_status, "used");
            t_size = t_size - 2;
        } else {
            strcpy(p_status, "Free");
        }

        if (is_used)
            used_size += t_size;
        else
            free_size += t_size;

        t_end = t_begin + t_size - 1;

        fprintf(stdout, "%d\t%s\t%s\t0x%08lx\t0x%08lx\t%d\n", counter, status,
                p_status, (unsigned long int) t_begin, (unsigned long int) t_end, t_size);

        current = (blockHeader *) ((char *) current + t_size);
        counter = counter + 1;
    }

    fprintf(stdout, "---------------------------------------------------\
                    ------------------------------\n");
    fprintf(stdout, "***************************************************\
                    ******************************\n");
    fprintf(stdout, "Total used size = %d\n", used_size);
    fprintf(stdout, "Total free size = %d\n", free_size);
    fprintf(stdout, "Total size = %d\n", used_size + free_size);
    fprintf(stdout, "***************************************************\
                    ******************************\n");
    fflush(stdout);

    return;
}  
