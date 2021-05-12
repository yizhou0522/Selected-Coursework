
#include <sys/types.h>
#include <signal.h>
#include <stdio.h>
#include <string.h>
#include <stdlib.h>

/**
 * Main function displays the result of the program
 * @param argc argument length of char array
 * @param argv argument char array
 * @return 0
 */
int main(int argc, char *argv[]) {
    //displays usage if argc not equal to 3
    if (argc != 3) {
        printf("Usage: <signal type> <pid>\n");
        exit(0);
    }
    if (strcmp(argv[1], "-i") == 0) {   //kill for SIGINT
        if (kill(atoi(argv[2]), SIGINT) == -1) {
            printf("Error for kill function.\n");
            exit(0);
        }
    } else if (strcmp(argv[1], "-u") == 0) {  //kill for SIGUSR1
        if (kill(atoi(argv[2]), SIGUSR1) == -1) {
            printf("Error for kill function.\n");
            exit(0);
        }
    } else {  // other cases
        printf("Invalid parameter\n");
        exit(0);
    }
    return 0;
}