
#include <stdlib.h>
#include <stdio.h>
#include <signal.h>
#include <string.h>

// global fields counter initialized
int count = 0;
/**
* setting SIGFPE handler
*/
void handler_SIGFPE() {
    printf("Error: a division by 0 operation was attempted.\n");
    printf("Total number of operations completed successfully: %d\n", count);
    printf("The program will be terminated.\n");
    exit(0);
}

/**
*setting SIGINT handler
*/
void handler_SIGINT() {
    printf("^C");
    printf("\nTotal number of operations successfully completed: %d\n", count);
    printf("The program will be terminated.\n");
    exit(0);
}
/**
* The main function displays results for this program
*/
int main() {
    int quotient,remainder,first,second;//local variables declared
    char buffer[100];

    struct sigaction sa; // setting up SIGFPE handler
    if (memset(&sa, 0, sizeof(sa)) == 0) {
        printf("error with memset");
        exit(0);
    }
    sa.sa_handler = handler_SIGFPE;
    if (sigaction(SIGFPE, &sa, NULL) != 0) {
        printf("Error binding SIGFPE handler");
        exit(1);
    }

    struct sigaction sa2;  // setting up SIGINT signal
    if (memset(&sa2, 0, sizeof(sa2)) == 0) {
        printf("error with memset");
        exit(0);
    }
    sa2.sa_handler = handler_SIGINT;
    if (sigaction(SIGINT, &sa2, NULL) != 0) { 
        printf("Error binding SIGINT handler.\n");
        exit(1);
    }

    while (1) { 
        printf("Enter first integer:"); // getting the first number
        fgets(buffer, 100, stdin);
        first = atoi(buffer);

        printf("Enter second integer:");  // getting the second number
        fgets(buffer, 100, stdin);
        second = atoi(buffer);

        quotient = first / second;//calculate and display the result
        remainder = first % second;
        count++;
        printf("%d / %d is %d with a remainder of %d\n",
               first, second, quotient, remainder);
    }

    return 0;
}