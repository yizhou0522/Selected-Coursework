
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

char *COMMA = ",";

/* COMPLETED:       
 * Retrieves from the first line of the input file,
 * the size of the board (number of rows and columns).
 * 
 * fp: file pointer for input file
 * size: pointer to size
 */
void get_dimensions(FILE *fp, int *size) {
    char *line = NULL;
    size_t len = 0;
    if (getline(&line, &len, fp) == -1) {
        printf("Error in reading the file\n");
        exit(1);
    }

    char *token = NULL;
    token = strtok(line, COMMA);
    *size = atoi(token);
}


/* TODO:
 * Returns 1 if and only if the board is in a valid state.
 * Otherwise returns 0.
 * 
 * board: heap allocated 2D board
 * size: number of rows and columns
 */
int n_in_a_row(int **board, int size) {

    int numOne = 0;//record the times 1 exist
    int numTwo = 0;//record the times 2 exist
    int record = 0;//record the times 1 or 2 exist in a row, col, or diagonal
    int winTimes1 = 0;//record the times 1 wins
    int winTimes2 = 0;//record the times 2 wins
    for (int i = 0; i < size; i++) {
        for (int k = 0; k < size; k++) {
            if (*(*(board + i) + k) == 1)
                numOne++;
            else if (*(*(board + i) + k) == 2)
                numTwo++;
        }
    }
    if ((numOne == numTwo + 1) || (numOne == numTwo)) {

    } else
        return 0;
//judge for rows for 1
    for (int i = 0; i < size; i++) {
        for (int k = 0; k < size; k++) {
            if (*(*(board + i) + k) == 1)
                record++;
        }
        if (record == size)
            winTimes1++;
        record = 0;
    }
//judge for rows for 2
    for (int i = 0; i < size; i++) {
        for (int k = 0; k < size; k++) {
            if (*(*(board + i) + k) == 2)
                record++;
        }
        if (record == size)
            winTimes2++;
        record = 0;
    }
//judge for cols for 1
    for (int k = 0; k < size; k++) {
        for (int i = 0; i < size; i++) {
            if (*(*(board + i) + k) == 1)
                record++;
        }
        if (record == size)
            winTimes1++;
        record = 0;
    }

    //judge for cols for 2
    for (int k = 0; k < size; k++) {
        for (int i = 0; i < size; i++) {
            if (*(*(board + i) + k) == 2)
                record++;
        }
        if (record == size)
            winTimes2++;
        record = 0;
    }
//judge main diagonal for 1
    for (int k = 0, i = 0; k < size&& i < size; k++, i++) {
        if (*(*(board + i) + k) == 1)
            record++;
    }
    if (record == size)
        winTimes1++;
    record = 0;
//judge main diagonal for 2
    for (int k = 0, i = 0; k < size&& i < size; k++, i++) {
        if (*(*(board + i) + k) == 2)
            record++;
    }
    if (record == size)
        winTimes2++;
    record = 0;
    //judge another diagonal for 1
    for (int k = size - 1, i = size - 1; k > 0&& i > 0; k--, i--) {
        if (*(*(board + i) + k) == 1)
            record++;
    }
    if (record == size)
        winTimes1++;
    record = 0;

    //judge another diagonal for 2
    for (int k = size - 1, i = size - 1; k > 0&& i > 0; k--, i--) {
        if (*(*(board + i) + k) == 2)
            record++;
    }
    if (record == size)
        winTimes2++;
    record = 0;
//compare these statistics and get the result
    if ((winTimes1 == 0 && winTimes2 == 0) || (winTimes1 == 1 && winTimes2 == 0) || (winTimes1 == 0 && winTimes2 == 1))
        return 1;
    else
        return 0;
}


/* PARTIALLY COMPLETED:
 * This program prints Valid if the input file contains
 * a game board with either 1 or no winners; and where
 * there is at most 1 more X than O.
 * 
 * argc: CLA count
 * argv: CLA value
 */
int main(int argc, char *argv[]) {

    //TODO: Check if number of command-line arguments is correct.
//printf("%d",argc);
    if (argc != 2) {
        printf("Error");
        exit(1);
    }

    //Open the file and check if it opened successfully.
    FILE *fp = fopen(*(argv + 1), "r");
    if (fp == NULL) {
        printf("Cannot open file for reading\n");
        exit(1);
    }


    //Declare local variables.
    int size = 0;
    int *pSize = &size;
    //TODO: Call get_dimensions to retrieve the board size.
    get_dimensions(fp, pSize);// no need to add asterisk
    size = *pSize;
    //TODO: Dynamically allocate a 2D array of dimensions retrieved above.
//    int **arr = (int **) malloc(size*sizeof(int *)+size * size * sizeof(int));//two asterisks means two d array
    int *arr[size];//how to use pointer to replace this?
    for (int i = 0; i < size; i++)
        *(arr+i) = (int *) malloc(size * sizeof(int));//*(arr+i)==arr[i]
    //Read the file line by line.
    //Tokenize each line wrt comma to store the values in your 2D array.
    char *line = NULL;
    size_t len = 0;
    char *token = NULL;
    for (int i = 0; i < size; i++) {
        if (getline(&line, &len, fp) == -1) {
            printf("Error while reading the file\n");
            exit(1);
        }
        token = strtok(line, COMMA);
        for (int j = 0; j < size; j++) {
            //TODO: Complete the line of code below
            //to initialize your 2D array.
            /* ADD ARRAY ACCESS CODE HERE */
            *(*(arr + i) + j) = atoi(token);
            token = strtok(NULL, COMMA);
        }
    }
    //TODO: Call the function n_in_a_row and print the appropriate
    //output depending on the function's return value.

    if (n_in_a_row(arr, size) == 0)
        printf("invalid");
    else
        printf("valid");

    //TODO: Free all dynamically allocated memory.
    for (int i = 0; i < size; i++) {
        int *currentIntPtr = arr[i];
        free(currentIntPtr);
    }

    //Close the file.
    if (fclose(fp) != 0) {
        printf("Error while closing the file\n");
        exit(1);
    }
    return 0;
}       

