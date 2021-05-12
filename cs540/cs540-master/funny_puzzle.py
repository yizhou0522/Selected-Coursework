# Yizhou Liu
# liu773@wisc.edu
# cs540 
import numpy as np
import heapq
from copy import copy, deepcopy

# print next state
def print_succ(state):
    # Transforms state as list to matrix
    state=create_state(state)
    # return all the possible 2-d array
    succs = generate_succ(state)
    print_list = []
    for s in succs:
        print_list.append(s)
    print_list = sorted(print_list)
    for s in print_list:
        print(s, 'h=' + str(calculate_manhatten(s)))

# Converts numpy array to list 
def to_list(state):
    return list(state.reshape(1, 9)[0])

# Creates a numpy matrix representation of state from a list
def create_state(state_list):
    state = np.array(state_list)
    return state.reshape((3, 3))

# solve the puzzle using A* algorithm
def solve(state):
    initial=Node(state)
    goal=Node([1,2,3,4,5,6,7,8,0])
    search = Searcher(initial, goal)
    search.astar()
    return 0

# This is the base class; representing a state object
class Node(object):
    n = 0
    def __init__(self, board, prev_state = None):
        assert len(board) == 9
        self.n=0
        self.board = board[:]
        self.prev = prev_state
        self.step = 0
        Node.n += 1
        if self.prev:
            self.step = self.prev.step + 1 

    def __eq__(self, other):
        """Check wether two state is equal."""
        return self.board == other.board

    def __hash__(self):
        h = [0, 0, 0]
        h[0] = self.board[0] << 6 | self.board[1] << 3 | self.board[2]
        h[1] = self.board[3] << 6 | self.board[4] << 3 | self.board[5]
        h[2] = self.board[6] << 6 | self.board[7] << 3 | self.board[8]
        h_val = 0
        for h_i in h:
            h_val = h_val * 31 + h_i
        return h_val

    def __str__(self):
        string_list = [i for i in self.board]
        sub_list=(string_list[:])
        return sub_list

    # return the manhatten_distance
    def manhattan_distance(self):
        distance = 0
        goal = [1,2,3,4,5,6,7,8,0]
        for i in range(1,9):
            xs, ys = self.to2dArray(self.board.index(i))
            xg, yg = self.to2dArray(goal.index(i))
            distance += abs(xs-xg) + abs(ys-yg)
        return distance

    # to string func
    def string_manhatten(self):
        return "h="+(str)(self.manhattan_distance())

    # return next state
    def next(self):
        next_moves = []
        i = self.board.index(0)
        next_moves = (self.move_up(i), self.move_down(i), self.move_left(i), self.move_right(i))
        return [s for s in next_moves if s]

    #return right move state if possible
    def move_right(self, i):
        x, y = self.to2dArray(i)
        if y < 2:
            right_state = Node(self.board, self)
            right = self.to1dArray(x, y+1)
            right_state.swap(i, right)
            return right_state

     #return left move state if possible
    def move_left(self, i):
        x, y = self.to2dArray(i)
        if y > 0:
            left_state = Node(self.board, self)
            left = self.to1dArray(x, y - 1)
            left_state.swap(i, left)
            return left_state

     #return up move state if possible
    def move_up(self, i):
        x, y = self.to2dArray(i)
        if x > 0:
            up_state = Node(self.board, self)
            up = self.to1dArray(x - 1, y)
            up_state.swap(i, up)
            return up_state

     #return down move state if possible
    def move_down(self, i):
        x, y = self.to2dArray(i)
        if x < 2:
            down_state = Node(self.board, self)
            down = self.to1dArray(x + 1, y)
            down_state.swap(i, down)
            return down_state

    #swap two indexes in board
    def swap(self, i, j):
        self.board[j], self.board[i] = self.board[i], self.board[j]

    def to2dArray(self, index):
        return (int(index / 3), index % 3)

    def to1dArray(self, x, y):
        return x * 3 + y

# return manhatten distance for print_succ
def calculate_manhatten(state):
    ans=0
    now=[]
    origin=[
       (1,0,0),
       (2,0,1),
       (3,0,2),
       (4,1,0),
       (5,1,1),
       (6,1,2),
       (7,2,0),
       (8,2,1)
    ]
    # convert state to 2-d array
    state=create_state(state)

    for i in range(len(state)):
        for j in range(len(state[i])):
            now.append((state[i][j],i,j))
    for elt1 in now:
        for elt2 in origin:
            if(elt1[0]==elt2[0]):
                ans+=abs(elt1[1] -elt2[1])+abs(elt1[2] -elt2[2])
    return ans

# create next states
def generate_succ(state):
    # get the empty space indexes
    res=[]
    empty=np.where(state==0)
    if(empty[0]-1>=0):
        copy=deepcopy(state)
        copy=swap(empty[0].item(),empty[1].item(),empty[0].item()-1,empty[1].item(),copy)
        res.append(to_list(copy))

    if(empty[0]+1<3):
         copy=deepcopy(state)
         copy=swap(empty[0].item(),empty[1].item(),empty[0].item()+1,empty[1].item(),copy)
         res.append(to_list(copy))

    if(empty[1]-1>=0):
        copy=deepcopy(state)
        copy=swap(empty[0].item(),empty[1].item(),empty[0].item(),empty[1].item()-1,copy)
        res.append(to_list(copy))

    if(empty[1]+1<3):
        copy=deepcopy(state)
        copy=swap(empty[0].item(),empty[1].item(),empty[0].item()-1,empty[1].item()+1,copy)
        res.append(to_list(copy))
    return res

# swap elements in 2-d array
def swap(index1, index2, index3, index4, state):
    cp=deepcopy(state)
    replace=cp[index1][index2]
    cp[index1][index2]=cp[index3][index4]
    cp[index3][index4]=replace
    return cp

# use heapq implement priority queue; conventional methods
class PriorityQueue:
    def  __init__(self):
        self.heap = []
        self.count = 0

    def push(self, item, priority):
        entry = (priority, self.count, item)
        heapq.heappush(self.heap, entry)
        self.count += 1

    def pop(self):
        (_, _, item) = heapq.heappop(self.heap)
        return item

    def isEmpty(self):
        return len(self.heap) == 0

class Searcher(object):

    def __init__(self, start, goal):
        self.start = start
        self.goal = goal

    # print the solution process
    def print_path(self, state):
        path = []
        while state:
            path.append(state)
            state = state.prev
        path.reverse()
        i=0
        for state in path:
            cp=np.array(state)
            print(state.__str__(),state.string_manhatten(),"moves:",i)
            i+=1    

    # a* algorithm provided
    def astar(self, depth = 75):
        """Run A* search."""
        priotity_queue = PriorityQueue()
        h_val = self.start.manhattan_distance() 
        # g_val always is start.step
        f_val = h_val + self.start.step
        priotity_queue.push(self.start, f_val)
        visited = set()
        found = False

        while not priotity_queue.isEmpty():
            state = priotity_queue.pop()
            if state == self.goal:
                found = state
                break
            if state in visited or state.step > depth:
                continue
            visited.add(state)
            for s in state.next():
                h_val_s = s.manhattan_distance()
                f_val_s = h_val_s + s.step
                priotity_queue.push(s, f_val_s)

        if found:
            self.print_path(found)
        else:
            print("No solution found")


def main():
    print_succ([1,2,3,4,5,0,6,7,8])
    solve([8,7,6,5,4,3,2,1,0])
    
if __name__=="__main__":
    main()

