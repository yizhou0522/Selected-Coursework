# Yizhou Liu
# liu773@wisc.edu
# cs540 
import csv
import numpy as np
import scipy.cluster.hierarchy as sci

def load_data(filepath):
    dict_list = []
    with open(filepath, newline='') as csvfile:
        reader = csv.DictReader(csvfile)
        for elt in reader:
            del elt['Generation']
            del elt['Legendary']
            dict_list.append(elt)
    for elt in dict_list[:20]:
        for x,y in elt.items():
         if (x=="Attack" or x=="Sp. Atk" or x=="Speed" or x== "Defense"
          or x=="Sp. Def" or x=="HP" or x=="#" or x=="Total"):
            elt[x]=int(y)
    return dict_list[:20]

def calculate_x_y(stats):
   x=0
   y=0
   for name, values in stats.items():
       if(name=="Attack" or name=="Sp. Atk" or name=="Speed"):
           x=x+int(values)
       if(name=="Defense" or name=="Sp. Def" or name=="HP"):
           y=y+int(values)
   return (x,y)

def hac(dataset):
    min=float("inf")
    res=[]
    clusters = []
    for i in dataset:
        clusters.append([i])
    for i in range((len(dataset) - 1)):
        res.append([0, 0, 0, 0])
    new_cluster= res.copy()
    optimal = 0
    for elt in res:
        merge, index, distance = get_cluster(min, clusters, optimal)
        if optimal>=distance:
            new_cluster.append(optimal-distance)
        clusters.append(merge[0] + merge[1])
        clusters[index[0]] = None
        clusters[index[1]] = None
        elt[0], elt[1], elt[2],elt[3] = index[0], index[1],distance, len(clusters[-1])
        optimal = distance
    return np.asarray(res)

def calcuate_dist(min, curr, clusters,optimal):
    distance = np.linalg.norm(np.subtract(curr, clusters))
    if distance==0:
        return 0
    if distance < min :
        min  = distance
    if min  <= optimal:
       return min 

def get_cluster(min,clusters, optimal):
    res=0
    merge = ()
    index = ()
    for i in range(len(clusters)):
        if clusters[i] is None:
            continue
        for j in range(i + 1, len(clusters)):
            curr = clusters[j]
            if curr is None:
                continue
            if get_distance(min,clusters[i], curr, optimal) < min:
                min  = get_distance(min,clusters[i], curr, optimal)
                merge = (clusters[i], curr)
                index = (i, j)
                if optimal >=min:
                    return merge, index, min 
                res+=1
        # if res>min:
        #     res=calcuate_dist(min, curr, clusters,optimal)
    return merge, index, min 


def get_distance(min,clusters, curr, optimal):
    for i in range(len(clusters)):
        for j in range(len(curr)):
            distance = np.linalg.norm(np.subtract(curr[j], clusters[i]))
            if distance==0:
                return 0
            if distance < min :
                min  = distance
            if min  <= optimal:
                return min 
    return min 


if __name__ == "__main__":
    series = load_data('Pokemon.csv')
    print(series)
    # print(calculate_x_y(series[0]))

    print(hac([calculate_x_y(stats) for stats in load_data('Pokemon.csv')]))
    print("scipy.cluster.hierarchy.linkage()")
    print(sci.linkage([calculate_x_y(stats) for stats in load_data('Pokemon.csv')]))
    
    # res=hac([calculate_x_y(stats) for stats in load_data('Pokemon.csv')])