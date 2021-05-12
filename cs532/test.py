import numpy as np
import math
import sklearn


def shift(f, n,P):
#   f=freq[n:]
# #   print(f)
#   f.extend(freq[:n])
# #   print(f)
  i=0
  l=[]
  while(i<len(f)):
      l.append(f[(i+n)%len(f)])
      i+=1
  newf=np.array(l)
  print(newf)
  mult=np.matmul(newf, np.transpose(P))
  return mult

def main():
    P=np.array([0.2195, 0.0488, 0.0488, 0.0976, 0.2927, 0.0488, 0.0976, 0.1462])
    original=np.array([7, 2, 4, 5, 7, 2, 2, 3, 6, 0, 2, 1, 6, 3, 6, 0, 2, 5])

    # freq=np.array([2, 1, 5, 2, 1, 2, 3, 2  ])/18
    f=[1/9,1/18,5/18,1/9,1/18,1/9,1/6,1/9]
    for n in range(8):
        print(n)
        print(shift(f, n,P))
    
    k="NETERECMGTISHENHGATGGCFICHISGSTGEFECMDEHHETHNTIARHEHCICG"
    print(len(k))


    # f=[5/18,1/9,1/18,1/9,1/6,1/9,1/9,1/18]
    # m=np.matmul(f,np.transpose(P))
    # print(m)

if __name__=="__main__":
    main()