# Yizhou Liu
# liu773@wisc.edu
# cs540 
import random
from collections import OrderedDict

# returns the successor state
def succ(state, static_x, static_y):
    res=[]
    # case return empty list
    has_static=False
    for i in range(0, len(state)):
        if(state[i]==static_x and i==static_y):
            has_static=True
            break
    if(not has_static):
        return res
    for i in range(0, len(state)):
        if(not(i==static_y)):
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

# return the state's f value
def f(state):
    res_list = []  
    # check rows
    for col in range(len(state)):
        # iterates through subsequent columns
        for next_queen in range(col + 1, len(state)):
            curr_queen= state[col]
            sub_queen = state[next_queen]  
            if curr_queen == sub_queen:
                if col not in res_list:
                    res_list.append(col)
                if next_queen not in res_list:
                    res_list.append(next_queen)
                break

    # check diagonal
    for col in range(len(state)):
        # iterates through subsequent columns
        for next_queen in range(col + 1, len(state)):
            curr_queen= state[col]
            sub_queen = state[next_queen]   # curr_queen = queen in column col, sub_queen = queen in subsequent columns
            # checks if two queens in column col and next_queen can attack each other from diagonals
            if col + curr_queen == next_queen + sub_queen or col - curr_queen == next_queen - sub_queen:
                if col not in res_list:
                    res_list.append(col)
                if next_queen not in res_list:
                    res_list.append(next_queen)
    # length of res_list = the f value of the state
    return len(res_list)

# return the next state of the curr state
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

# print n_queens solution
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

# return n_queen solution without prints
def  n_queens_2(initial_state, static_x, static_y):
    curr = initial_state
    while True:
        next = choose_next(curr, static_x, static_y)
        if f(next)>=f(curr):
            if next ==curr:
              return curr
            return next
        curr = next

# randomly generate states; run it k times
def n_queens_restart(n, k, static_x, static_y):
    res = []
    
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
        res.append(sol)
         # Then sort by lowest f()
    res.sort(key=lambda x: f(x))
    min_f=f(res[0])
    res=[elt for elt in res if f(elt)==min_f]

    res.sort()
    ans=[]
    [ans.append(x) for x in res if x not in ans]

    for a in ans:
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
         if(state[i]==static_x and i==static_y):
            valid=True
            return state
        state = []
    
    return state

def main():
    # print(succ([0, 1, 2], 0, 0))
    # print(f([0, 2, 1] ))
    # print( choose_next([0, 1, 0], 0, 1))
    # print(n_queens([0, 7, 3, 4, 7, 1, 2, 2], 0, 0))
    #  print( n_queens([0, 7, 3, 4, 7, 1, 2, 2], 0, 0))
     n_queens_restart(8, 1000, 0, 0)
     n_queens_restart(7, 10, 0, 0)
    # n_queens_restart(8, 1000, 0, 0)
    # double print will cause Python to print None automatically

if __name__ == "__main__":
    main()

    

