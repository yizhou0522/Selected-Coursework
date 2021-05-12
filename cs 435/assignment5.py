from collections import Counter
import numpy as np
import math

text='PPKVFAZUNGSSHUNKZLQYMUNMHFOWKIZYNAWHGSALUBHWKVVBOYTVJOHAPLJRLGIGYTLUGBYAUAPLCRZYCQULOULMAOCSANUVLWATSWHHCLFOVAQZMIKYAFLALLVLXVKJBVLYVPETSQCYRWIONHBZNATZOTKJYCDNYTJLMUCGTUTKJLPXILLDTVOIUWOIMKEMKNHLUTTPKZQIABTJYMHNIUUNGUKVONAYRVOIAQAZWOOWMACTPPETHBOYRABAPJWTJESFIPNEVHTOYBCABSYOMNHGZBYCKLLSYPBOFICYRRVWSMFLLNCULVNOYLEUAWTUKLXEEPAPPEJINVYQIOTPINUHVKMEAOPEOMSMEHMWKU'
dict={
    "A":0.082,
    "B":0.015,
    "C":0.028,
    "D":0.043,
    "E":0.127,
    "G":0.020,
    "F": 0.022,
    "H": 0.061,
    "I": 0.070,
    "J": 0.002,
    "K": 0.008,
    "L": 0.040,
    "M": 0.024,
    "N": 0.067,
    "O": 0.075,
    "P": 0.019,
    "Q": 0.001,
    "R": 0.060,
    "S": 0.063,
    "T": 0.091,
    "U": 0.028,
    "V": 0.010,
    "W": 0.023,
    "X": 0.001,
    "Y": 0.020,
    "Z": 0.001,
}
res = {} 
ans=0 
for keys in text: 
    res[keys] = res.get(keys, 0) + 1

# print(str(res))

for k in res:
    ans=ans+res.get(k)*(res.get(k)-1)

ans=ans/(362*361)

# print(ans)

kr=1/26
ks=0

for elt in dict:
    ks=ks+(dict[elt]*dict[elt])

me=362*(ks-kr)/(361*ans-362*kr+ks)

# print(me)

h1=0
for elt in dict:
    h1=h1-dict.get(elt)*np.log2(dict.get(elt))

# print(h1)

#######
dict2={
    "A": 0.1061,
    "B": 0.0194,
    "C": 0.0362,
    "D": 0.0556, 
    "E": 0.1643,
    "F": 0.0285,
    "G": 0.0259,
    "H": 0.0789,
    "I": 0.0906,
    "M": 0.031 ,
    "N": 0.0867,
    "R": 0.0776,
    "S": 0.0815,
    "T": 0.1177,
}
print(sorted(dict2.items(), key=lambda item: item[1]))
# h=0
# for elt in dict2:
#     h=h-dict2.get(elt)*np.log2(dict2.get(elt))

# print(h)

# # r=np.log2(96)/(np.log2(14)-2)
# r=np.log2(96)-2 
# n=np.log2(96)/r
# print(n)

#####
min1=-1
list1=[]
list2=[]
for k in range(100):
    start=k
    i=0
    print("seed k",start)
    print(k, end=" ")
    while(i<100):
        k=(13*k+0)%96
        print(k,end=" ")
        i=i+1
        if(k==start ):
            print("total number",i)
            list1.append(i)
            print()
    list2.append(min(list1))
print("final result",max(list2))