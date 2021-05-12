# Yizhou Liu
# liu773@wisc.edu
# cs540 

def fill(state, max,which):
    res=state.copy()
    res[which]=max[which]
    return res

def empty(state,max, which):
    res=state.copy()
    res[which]=0
    return res

def xfer(state,max, source,dest):
    res = state[:]
    # get the result after transfering
    res[dest] += min(max[dest] - state[dest], state[source])
    res[source] -= min(max[dest] - state[dest], state[source])
    return res

def succ(state, max):
    # using set to exclude identical elements
    sList = set()
    for i in [0, 1]:
        sList.add(tuple(fill(state, max, i)))
        sList.add(tuple(empty(state, max, i)))
    sList.add(tuple(xfer(state, max, 0, 1)))
    sList.add(tuple(xfer(state, max, 1, 0)))
    for e in sList:
        print(list(e))


def main():
    # max=[5,7]
    # s0=[0,0]
    # s1=fill(s0,max,1)
    # # print(s1)
    # print(xfer(s1,max,1,0))
    # s0=[3,1]
    # succ(s0,max)
    i=0
    for i in range(0,4):
        print(i)


if __name__ == "__main__":
   main()