# Yizhou Liu
# liu773@wisc.edu
# cs540
import numpy as np
import random
from numpy import genfromtxt

# Feel free to import other packages, if needed.
# As long as they are supported by CSL machines.


def get_dataset(filename):
    """
    TODO: implement this function.

    INPUT: 
        filename - a string representing the path to the csv file.

    RETURNS:
        An n by m+1 array, where n is # data points and m is # features.
        The labels y should be in the first column.
    """
   
    dataset = genfromtxt(filename, delimiter=',')
    # remove first row
    dataset = np.delete(dataset, (0), axis=0)
    #remove first col
    dataset = np.delete(dataset, (0), axis=1)

    # for l in f:
    #         xy_pair = l.split()
    #         xy_pair = [int(i) for i in xy_pair]
    #         dataset.append(xy_pair)
    return np.array(dataset)


def print_stats(dataset, col):
    """
    TODO: implement this function.

    INPUT: 
        dataset - the body fat n by m+1 array
        col     - the index of feature to summarize on. 
                  For example, 1 refers to density.

    RETURNS:
        None
    """
    n = len(dataset)
    arr=dataset[:,col]
    print(n)
    print("{:.2f}".format(np.mean(arr)))
    print("{:.2f}".format(np.std(arr)))
    return


def regression(dataset, cols, betas):
    """
    TODO: implement this function.

    INPUT: 
        dataset - the body fat n by m+1 array
        cols    - a list of feature indices to learn.
                  For example, [1,8] refers to density and abdomen.
        betas   - a list of elements chosen from [beta0, beta1, ..., betam]

    RETURNS:
        mse of the regression model
    """
    mse = 0.0
    fx= betas[0]
    # for i in cols:
    #     fx+=betas[count]*dataset[:,i]
    for d in dataset:
        index=0
        fx=betas[index]
        for count in range(len(cols)):
            fx+=d[cols[count]]*betas[index+1]
            index+=1
        mse += (fx- d[0])**2 / len(dataset)
    return mse


def gradient_descent(dataset, cols, betas):
    grads = np.zeros(len(betas))
    db0=0
    for d in dataset:
        index=0
        fx=betas[index]
        for count in range(len(cols)):
            fx+=d[cols[count]]*betas[index+1]
            index+=1
        fx=(fx-d[0])*2/len(dataset)
        for i in range(0,len(grads)):
            if i==0:
                grads[i]+=fx
            else:
                grads[i]+=fx*d[cols[i-1]]
    return grads


def iterate_gradient(dataset, cols, betas, T, eta):
    res=np.zeros(len(betas))
    for i in range(T):
        grad = gradient_descent(dataset, cols, betas)
        for j in range(0, len(res)):
            betas[j]=betas[j]-eta*grad[j]
        mse=regression(dataset,cols,betas)
        print(i+1, "{:.2f}".format(mse),end=" ",flush=True)
        for i in range(0,len(betas)):
            if i==len(betas)-1:
              print("{:.2f}".format(betas[i]),end="",flush=True)
            else:
              print("{:.2f}".format(betas[i]),end=" ",flush=True) 
        if i!=T-1:
         print()
    return ""


def compute_betas(dataset, cols):
    """
    TODO: implement this function.

    INPUT: 
        dataset - the body fat n by m+1 array
        cols    - a list of feature indices to learn.
                  For example, [1,8] refers to density and abdomen.

    RETURNS:
        A tuple containing corresponding mse and several learned betas
    """
    # add the first col
    d=np.zeros([len(dataset),len(cols)+1])
    a= np.empty(len(dataset))
    a.fill(1)
    d[:,0]=a.T
    index=1
    for elt in cols:
         d[:,index]=dataset[:,elt]
         index+=1
    # print(d)
    # first=
    betas = np.dot(np.linalg.inv(np.dot(d.T,d)), np.dot(d.T,dataset[:,0]))
    mse = regression(dataset,cols, betas)
    # interesting *
    return (mse, *betas)


def predict(dataset, cols, features):
    """
    TODO: implement this function.

    INPUT: 
        dataset - the body fat n by m+1 array
        cols    - a list of feature indices to learn.
                  For example, [1,8] refers to density and abdomen.
        features- a list of observed values

    RETURNS:
        The predicted body fat percentage value
    """
    result=0.0
    betas=compute_betas(dataset,cols)
    for i in range(1,len(betas)):
        if i!=1:
         result+=betas[i]*features[i-2]
        else:
          result+=betas[i]

    return result


def random_index_generator(min_val, max_val, seed=42):
    """
    DO NOT MODIFY THIS FUNCTION.
    DO NOT CHANGE THE SEED.
    This generator picks a random value between min_val and max_val,
    seeded by 42.
    """
    random.seed(seed)
    while True:
        yield random.randrange(min_val, max_val)

def gradient_descent2(d, cols, betas):
    grads = np.zeros(len(betas))
    # for d in dataset:
    index=0
    fx=betas[index]
    for count in range(len(cols)):
            second=betas[index+1]
            fx+=d[cols[count]]*second
            index+=1
    fx=(fx-d[0])*2
    for i in range(0,len(grads)):
            if i==0:
                grads[i]+=fx
            else:
                grads[i]+=fx*d[cols[i-1]]
    return grads

def sgd(dataset, cols, betas, T, eta):
    """
    TODO: implement this function.
    You must use random_index_generator() to select individual data points.

    INPUT: 
        dataset - the body fat n by m+1 array
        cols    - a list of feature indices to learn.
                  For example, [1,8] refers to density and abdomen.
        betas   - a list of elements chosen from [beta0, beta1, ..., betam]
        T       - # iterations to run
        eta     - learning rate

    RETURNS:
        None
    """ 
    random_xy=random_index_generator(0, 251, seed=42)
    # print(next(random_xy))
    res=np.zeros(len(betas))
    for i in range(T):
        rand=next(random_xy)
        grad = gradient_descent2(dataset[rand], cols, betas)
        for j in range(0, len(res)):
            betas[j]=betas[j]-eta*grad[j]
        mse=regression(dataset,cols,betas)
        print(i+1, "{:.2f}".format(mse),end=" ",flush=True)
        # for elt in betas:
        #     print("{:.2f}".format(elt),end=" ",flush=True)
        for i in range(0,len(betas)):
            if i==len(betas)-1:
              print("{:.2f}".format(betas[i]),end="",flush=True)
            else:
              print("{:.2f}".format(betas[i]),end=" ",flush=True) 
        if i!=T-1:
         print()
    return ""

   
def main():
    dataset = get_dataset('bodyfat.csv')    
    print(get_dataset('bodyfat.csv'))
    print_stats(dataset, 1)
    print(regression(dataset, cols=[2,3], betas=[0,0,0]))
    print(regression(dataset, cols=[2,3,4], betas=[0,-1.1,-.2,3]))
    print(gradient_descent(dataset, cols=[2,3], betas=[0,0,0]))
    iterate_gradient(dataset, cols=[1,8], betas=[400,-400,300], T=10, eta=1e-4)
    print(compute_betas(dataset, cols=[1,2]))
    print(predict(dataset, cols=[1,2], features=[1.0708, 23]))
    sgd(dataset, cols=[2,8], betas=[-40,0,0.5], T=10, eta=1e-5)


if __name__ == '__main__':
    # Your debugging code goes here.
    main()