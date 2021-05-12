# Yizhou Liu
# liu773@wisc.edu
# cs540 
from scipy.linalg import eigh  
import numpy as np
import matplotlib.pyplot as plt  
# import matplotlib
# matplotlib.use('Agg')

# Load the dataset from a provided .mat file,
# re-center it around the origin
# and return it as a NumPy array of floats
def load_and_center_dataset(filename):
    x = np.load(filename)
    x = np.reshape(x,(2000,784))
    x= x - np.mean(x, axis=0)
    return x

# Calculate and return the covariance matrix
# of the dataset as a NumPy matrix (d x d array)
def get_covariance(dataset):
    n = len(dataset)
    covariance = (1 / (n - 1)) * np.dot(np.transpose(dataset), dataset)
    return covariance

# Perform eigen decomposition on the covariance matrix S
# and return a diagonal matrix (NumPy array) with the
# largest m eigenvalues on the diagonal, and a matrix (NumPy array)
# with the corresponding eigenvectors as columns
def get_eig(S, m):
    n = len(S)
    w, v = eigh(S, eigvals=(n - m, n - 1))
    v = v[:, ::-1]
    w = np.diag(w[::-1])
    return w, v 

#similar to get_eig, but instead of returning the first m, 
# return all eigenvectors that explains more than perc % of variance
def get_eig_perc(S, perc):
    n=len(S)
    w,v=eigh(S)
    w=w[::-1]
    arr=[]
    count=0
    for i in w:
        if(i/sum(w)>perc):
            arr.append(i)
            count+=1
    w, v = eigh(S, eigvals=(n - count, n - 1))
    v = v[:, ::-1]
    w = np.diag(w[::-1])
    return w, v 

# Project each image into your m-dimensional space and
# return the new representation as a d x 1 NumPy array
def project_image(image, U):
    A = image @ U
    X = A @ U.T
    return X

# Use matplotlib to display a visual representation of the
# original image and the projected image side-by-side
def display_image(orig, proj):
    orig = np.reshape(orig, (28, 28))
    proj = np.reshape(proj, (28, 28))

    fig, (ax1, ax2) = plt.subplots(1, 2)
    fig.subplots_adjust(wspace=0.5)
    bar1 = ax1.imshow(orig, aspect='equal',cmap='gray')
    ax1.set_title("Original")

    bar2 = ax2.imshow(proj, aspect='equal',cmap='gray')
    ax2.set_title("Projection")

    fig.colorbar(bar1, ax=ax1, fraction=.047, pad=.05)
    fig.colorbar(bar2, ax=ax2, fraction=.047, pad=.058)
    plt.show()
    # plt.savefig("filename.jpg")

def main():
    x = load_and_center_dataset('mnist.npy')
    # print(len(x),"xxx",len(x[0]),"xxx",np.average(x))
    d=get_covariance(x)
    # Lambda, U = get_eig(d, 2)
    # print(Lambda)
    # print(U)
    # print(d[350][350])
    # print(len(d),"xx",len(d[0]))
    # print(np.shape(d))
    # Lambda, U = get_eig_perc(d, 0.07)
    # print(Lambda)
    # print(np.sum(U))

    images = load_and_center_dataset('mnist.npy')
    s = get_covariance(images)
    Lambda, U = get_eig(s, 20) 
    #diagonal matrix for eigenValues; corresponding first 20 eigenVector
    image = images[3] # third index image in the dataset
    image_proj = project_image(image, U) # project the image
    # print(image_proj)
    display_image(image, image_proj)




if __name__ == '__main__':
    main()
    