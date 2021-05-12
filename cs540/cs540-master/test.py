import random
from collections import OrderedDict

def succ(state, static_x, static_y):
    res=[]
    # case return empty list
    has_static=False
    for i in range(0, len(state)):
        if(state[i]==static_y and i==static_x):
            has_static=True
            break
    if(not has_static):
        return res
    for i in range(0, len(state)):
        if(not(i==static_x)):
            if(state[i]+1<len(state)):
                # copy a list using slice
                new_list=list(state)
                new_list[i]= new_list[i]+1
                res.append(new_list)
            if(state[i]-1>=0):
                # copy a list using slice
                new_list=list(state)
                new_list[i]=new_list[i]-1
                res.append(new_list)
    return sorted(res)

def f(state):
    """ calculates the f value of state """
    hits = []  # stores all columns that have a queen that can be attacked
    # check rows
    for col in range(len(state)):
        # iterates through subsequent columns
        for next_queen in range(col + 1, len(state)):
            cq, tq = state[col], state[next_queen]  # cq = queen in column col, tq = queen in subsequent columns
            # checks if two queens in column col and next_queen can attack each other from rows
            if cq == tq:
                if col not in hits:
                    hits.append(col)
                if next_queen not in hits:
                    hits.append(next_queen)
                break

    # check diagonal
    for col in range(len(state)):
        # iterates through subsequent columns
        for next_queen in range(col + 1, len(state)):
            cq, tq = state[col], state[next_queen]  # cq = queen in column col, tq = queen in subsequent columns
            # checks if two queens in column col and next_queen can attack each other from diagonals
            if col + cq == next_queen + tq or col - cq == next_queen - tq:
                if col not in hits:
                    hits.append(col)
                if next_queen not in hits:
                    hits.append(next_queen)
    # length of hits = the f value of the state
    return len(hits)

def choose_next(curr, static_x, static_y):
    res=[]
    min_val=float('inf')
    succ_list=succ(curr, static_x, static_y)
    # an empty list don't need ""
    if(succ_list==[]):
        return None
    succ_list.append(curr)
    # sort it first with ascending order
    succ_list.sort()
    # lambda is the gold
    succ_list.sort(key=lambda e: f(e))
    return succ_list[0]


def n_queens(initial_state, static_x, static_y):
    curr = initial_state
    print(curr, '- f='+str(f(curr)))
    while True:
        next = choose_next(curr, static_x, static_y)
        if f(next)>=f(curr):
            if next ==curr:
              return curr
            print(next, '- f='+str(f(next)))
            return next
        curr = next
        print(curr, '- f='+str(f(curr)))

def  n_queens_2(initial_state, static_x, static_y):
    curr = initial_state
    while True:
        next = choose_next(curr, static_x, static_y)
        if f(next)>=f(curr):
            if next ==curr:
              return curr
            return next
        curr = next


def n_queens_restart(n, k, static_x, static_y):
    attempt_list = []
    
    # print("state",state)
    for attempt in range(k):
        # random.seed(0)
        state = random_state(n, static_x, static_y)
        # n——queens without print
        # print("xxx",state)
        sol = n_queens_2(state, static_x, static_y)
        # if goal state achieved: print goal state
        if f(sol) == 0:
            print(sol, '- f='+str(f(sol)))
            return
        # if goal not achieved, add state to list of attempts
        attempt_list.append(sol)
         # Then sort by lowest f()
    attempt_list.sort(key=lambda x: f(x))
    min_f=f(attempt_list[0])
    attempt_list=[elt for elt in attempt_list if f(elt)==min_f]
    # No goal achieved, so print out best solutions in sorted order

    # First sort by ascending order
    attempt_list.sort()
    res=[]
    [res.append(x) for x in attempt_list if x not in res]

    for a in res:
        if (a !=None):
         print(a, '- f='+str(f(a)))

def random_state(n,static_x, static_y):
    random.seed(1)
    state = []
    valid = False
    while not valid:
        for j in range(n):
            rand = random.randint(0, n-1)
            state.append(rand)
        for i in range(0, len(state)):
         if(state[i]==static_y and i==static_x):
            valid=True
            return state
        state = []
    
    return state

def main():
    # print(succ([0, 1, 2], 0, 0))
    # print(f([0, 2, 1] ))
    # print( choose_next([0, 1, 0], 0, 1))
    # print(n_queens([0, 7, 3, 4, 7, 1, 2, 2], 0, 0))
    # print( n_queens([0, 1, 2, 3, 5, 6, 6, 7], 1, 1))
     n_queens_restart(8, 1000, 0, 0)
    # n_queens_restart(8, 1000, 0, 0)
    # double print will cause Python to print None automatically

if __name__ == "__main__":
    main()

    

