
#include <stdio.h>
#include <signal.h>
#include <unistd.h>
#include <string.h>
#include <time.h>
#include <stdlib.h>
#include <sys/time.h>

int count = 0;//the number of SIGUSR1 caught, global variable declared

/*
 * the function that handles the signal
 */
void handler_SIGALRM() {
    time_t cur_time;
    time(&cur_time);
    printf("PID: %d | Current Time: %s", getpid(), ctime(&cur_time));
    alarm(3);
}

/**
 * the SIGUSR1 handler
 */

void handler_SIGUSR1() {
    count++;
    printf("SIGUSR1 caught!\n");
}

/**
 * sigInt handler
 */

void handler_SIGINT() {
    printf("^C\n");
    printf("SIGINT received\n");
    printf("SIGUSR1 was received %d times. Exiting now.\n", count);
    exit(0);
}

/**
 * Main function displays result of this program
 * @return 0
 */
int main() {
    printf("Pid and time will be printed every 3 seconds.\n");
    printf("Enter ^C to end the program.\n");

    struct sigaction sa;  // setting up sig alarm
    if (memset(&sa, 0, sizeof(sa)) == 0) {
        printf("Error with memset.\n");
        exit(0);
    }
    sa.sa_handler = handler_SIGALRM;
    // triggering the alarm
    if (alarm(3) != 0) {
        printf("Error with alarm.\n");
        exit(0);
    }
    if (sigaction(SIGALRM, &sa, NULL) != 0) {  // sig alarm
        printf("Error binding SIGALRM handler.\n");
        exit(1);
    }

    struct sigaction sa2;  // setting up user1 signal
    if (memset(&sa2, 0, sizeof(sa2)) == 0) {
        printf("Error with memset.\n");
        exit(0);
    }
    sa2.sa_handler = handler_SIGUSR1;
    if (sigaction(SIGUSR1, &sa2, NULL) != 0) {  // sigusr1
        printf("Error binding SIGUSR1 handler.\n");
        exit(1);
    }

    struct sigaction sa3;  // setting up sigint signal
    if (memset(&sa3, 0, sizeof(sa3)) == 0) {
        printf("Error with memset\n");
        exit(0);
    }
    sa3.sa_handler = handler_SIGINT;
    if (sigaction(SIGINT, &sa3, NULL) != 0) {  // sigint
        printf("Error binding SIGINT handler.\n");
        exit(1);
    }

    while (1) {
    }
}