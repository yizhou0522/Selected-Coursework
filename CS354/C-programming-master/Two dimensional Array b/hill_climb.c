

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define INT_MAX 2147483647
#define INT_MIN (-INT_MAX - 1)
char *COMMA = ",";
/*
 * USE THIS:
 * Structure representing the Matrix
 * numRows: number of rows
 * numCols: number of columns
 * matrix: 2D heap allocated array of integers
 */
typedef struct {
    int numRows;
    int numCols;
    int **matrix;
} Matrix;

/* 
 * Get the dimensions of the matrix that are on
 * the first line of the input file.
 *
 * infp: file pointer for input file
 * nRows: pointer to number of rows in the matrix
 * nCols: pointer to number of columns in the matrix
 */
void get_dimensions(FILE *infp, int *nRows, int *nCols) {
    char *line = NULL;
    size_t len = 0;
    if (getline(&line, &len, infp) == -1) {
        printf("Error in reading the file\n");
        exit(1);
    }
    //here line points to the entire input string with the help of getline func
    //len points to the size of input after getline func
    char *token = NULL;
    token = strtok(line, COMMA);
    *nRows = atoi(token);
    // Read the dimensions on the first line from infp
    while (token != NULL) {
        token = strtok(NULL, COMMA);
        if (token != NULL) { *nCols = atoi(token); }
    }

    
}


/* 
 * Continually find and print the largest neighbor
 * that is also larger than the current position
 * until nothing larger is found.
 *
 * outfp: file pointer for output file
 * mstruct: pointer to the Matrix structure
 */
void hill_climb(FILE *outfp, Matrix *mstruct) {
    // Write the intial starting number to outfp
    int x = *(*(mstruct->matrix + 0) + 0);//get the first value in matrix
    char str[100];//buffer
    sprintf(str, "%d", x);//convert integer to str value
    fputs(str, outfp);//write the str value to the output file
    fputs("\t", outfp);
    int max = INT_MIN;// the max number for serach
    int i = 0;//row number recorded
    int j = 0;//col number recorded
    int beforeMax = INT_MIN + 1;//compared max value
    // Find the largest neighbor that is also larger than the current position
    while (max != beforeMax) {
        beforeMax = max;
        //first eight cases are all boundary cases
        if (i == 0 && j == 0) {//matrix[0][0]
            for (int start = j; start < (j - 1) + 3; start++)
                if (max < *(*(mstruct->matrix + i) + start))
                    max = *(*(mstruct->matrix + i) + start);
            for (int start = j; start < (j - 1) + 3; start++)
                if (max < *(*(mstruct->matrix + i + 1) + start))
                    max = *(*(mstruct->matrix + i + 1) + start);
        } else if (i == 0 && j == mstruct->numCols - 1) {//matrix[0][cols-1]
            for (int start = j - 1; start < (j - 1) + 2; start++)
                if (max < *(*(mstruct->matrix + i) + start))
                    max = *(*(mstruct->matrix + i) + start);
            for (int start = j - 1; start < (j - 1) + 2; start++)
                if (max < *(*(mstruct->matrix + i + 1) + start))
                    max = *(*(mstruct->matrix + i + 1) + start);
        } else if (i == mstruct->numRows - 1 && j == 0) {//matrix[rows-1][0]
            for (int start = j; start < (j - 1) + 3; start++)
                if (max < *(*(mstruct->matrix + i - 1) + start))
                    max = *(*(mstruct->matrix + i - 1) + start);
            for (int start = j; start < (j - 1) + 3; start++)
                if (max < *(*(mstruct->matrix + i) + start))
                    max = *(*(mstruct->matrix + i) + start);
        } else if (i == mstruct->numRows - 1 && j == mstruct->numCols - 1) {//matrix[rows-1][cols-1]
            for (int start = j - 1; start < (j - 1) + 2; start++)
                if (max < *(*(mstruct->matrix + i - 1) + start))
                    max = *(*(mstruct->matrix + i - 1) + start);
            for (int start = j - 1; start < (j - 1) + 2; start++)
                if (max < *(*(mstruct->matrix + i) + start))
                    max = *(*(mstruct->matrix + i) + start);
        } else if (i == 0) {//matrix[0][]
            for (int start = j - 1; start < (j - 1) + 3; start++)
                if (max < *(*(mstruct->matrix + i) + start))
                    max = *(*(mstruct->matrix + i) + start);
            for (int start = j - 1; start < (j - 1) + 3; start++)
                if (max < *(*(mstruct->matrix + i + 1) + start))
                    max = *(*(mstruct->matrix + i + 1) + start);
        } else if (j == 0) {//matrix[][0]
            for (int start = j; start < (j - 1) + 3; start++)
                if (max < *(*(mstruct->matrix + i - 1) + start))
                    max = *(*(mstruct->matrix + i - 1) + start);
            for (int start = j; start < (j - 1) + 3; start++)
                if (max < *(*(mstruct->matrix + i) + start))
                    max = *(*(mstruct->matrix + i) + start);
            for (int start = j; start < (j - 1) + 3; start++)
                if (max < *(*(mstruct->matrix + i + 1) + start))
                    max = *(*(mstruct->matrix + i + 1) + start);
        } else if (i == mstruct->numRows - 1) {//matrix[row-1][]
            for (int start = j - 1; start < (j - 1) + 3; start++)
                if (max < *(*(mstruct->matrix + i - 1) + start))
                    max = *(*(mstruct->matrix + i - 1) + start);
            for (int start = j - 1; start < (j - 1) + 3; start++)
                if (max < *(*(mstruct->matrix + i) + start))
                    max = *(*(mstruct->matrix + i) + start);
        } else if (j == mstruct->numCols - 1) {//matrix[][col-1]
            for (int start = j - 1; start < (j - 1) + 2; start++)
                if (max < *(*(mstruct->matrix + i - 1) + start))
                    max = *(*(mstruct->matrix + i - 1) + start);
            for (int start = j - 1; start < (j - 1) + 2; start++)
                if (max < *(*(mstruct->matrix + i) + start))
                    max = *(*(mstruct->matrix + i) + start);
            for (int start = j - 1; start < (j - 1) + 2; start++)
                if (max < *(*(mstruct->matrix + i + 1) + start))
                    max = *(*(mstruct->matrix + i + 1) + start);
        } else {//matrix elements in normal conditions, no boundaries involved
            for (int start = j - 1; start < (j - 1) + 3; start++)
                if (max < *(*(mstruct->matrix + i - 1) + start))
                    max = *(*(mstruct->matrix + i - 1) + start);
            for (int start = j - 1; start < (j - 1) + 3; start++)
                if (max < *(*(mstruct->matrix + i) + start))
                    max = *(*(mstruct->matrix + i) + start);
            for (int start = j - 1; start < (j - 1) + 3; start++)
                if (max < *(*(mstruct->matrix + i + 1) + start))
                    max = *(*(mstruct->matrix + i + 1) + start);
        }
        // Move to that position
        for (int k = 0; k < mstruct->numRows; k++)
            for (int l = 0; l < mstruct->numCols; l++) {
                if (*(*(mstruct->matrix + k) + l) == max) {
                    i = k;
                    j = l;
                    break;
                }
            }
        // Write that number to outfp
        if (max != beforeMax) {
            char str[100];
            sprintf(str, "%d", max);
            fputs(str, outfp);
            fputs("\t", outfp);
        }
    }

    // Repeat until you can't find anything larger (you may use any loop)
}

/* 
 * This program reads an m by n matrix from the input file
 * and outputs to a file the path determined from the
 * hill climb algorithm.
 *
 * argc: CLA count
 * argv: CLA values
 */
int main(int argc, char *argv[]) {
    // Check if number of command-line arguments is correct
    if (argc != 3) {
        fprintf(stderr, "Usage: ./hill_climb <input_filename> <output_filename>\n");
        exit(1);
    }
    // Open the input file and check if it opened successfully
    FILE *ifp;
    ifp = fopen(argv[1], "r");
    if (ifp == NULL) {
        fprintf(stderr, "Cannot open file for reading.\n");
        fclose(ifp);
        exit(1);
    }
    // Declare local variables including the Matrix structure
    Matrix matrix;
    Matrix *pMatrix = &matrix;
    pMatrix->numRows = 0;
    int *pRowNum = &(pMatrix->numRows);
    pMatrix->numCols = 0;
    int *pColNum = &(pMatrix->numCols);
    // Call the function get_dimensions
    get_dimensions(ifp, pRowNum, pColNum);
    pMatrix->numRows = *pRowNum;
    pMatrix->numCols = *pColNum;
    // Dynamically allocate a 2D array in the Matrix structure
    pMatrix->matrix = (int **) malloc(sizeof(int *) * (pMatrix->numRows));
    for (int i = 0; i < pMatrix->numRows; i++) {
        *(pMatrix->matrix + i) = (int *) malloc(sizeof(int) * (pMatrix->numCols));
    }
    // Read the file line by line base on the number of rows
    // Tokenize each line wrt comma to store the values in the matrix
    char *line = NULL;
    size_t len = 0;
    char *token = NULL;
    for (int i = 0; i < (pMatrix->numRows); i++) {
        if (getline(&line, &len, ifp) == -1) {
            printf("Error while reading the file\\n");
            exit(1);
        }
        token = strtok(line, COMMA);
        for (int j = 0; j < (pMatrix->numCols); j++) {
            *(*(pMatrix->matrix + i) + j) = atoi(token);
            token = strtok(NULL, COMMA);
        }

    }
    // Open the output file
    FILE *ofp;
    ofp = fopen(argv[2], "w");
    if (ofp == NULL) {
        fprintf(stderr, "Can't open output file! \n");
        exit(1);
    }
    // Call the function hill_climb
    hill_climb(ofp, pMatrix);
    // Free the dynamically allocated memory
    for (int i = 0; i < (pMatrix->numRows); i++) {
        int *currentIntPtr = *(pMatrix->matrix + i);
        free(currentIntPtr);
    }
    free(pMatrix->matrix);
    // Close the input file
    if (fclose(ifp) != 0) {
        printf("Error while closing the file\n");
        exit(1);
    }
    // Close the output file
    if (fclose(ofp) != 0) {
        printf("Error while closing the file\n");
        exit(1);
    }
    return 0;
}
