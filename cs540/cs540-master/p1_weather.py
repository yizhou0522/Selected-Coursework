# Yizhou Liu
# liu773@wisc.edu
# cs540 

def manhattan_distance(data_point1, data_point2):
    return abs(data_point1.get("TMAX")-data_point2.get("TMAX"))+abs(data_point1.get("TMIN")-data_point2.get("TMIN"))+abs(data_point1.get("PRCP")-data_point2.get("PRCP"))

# probably have some format-related problems
# fix it later
def read_dataset(filename):
    data_list = []
    KEY_LIST = ['DATE', 'PRCP', 'TMAX', 'TMIN', 'RAIN']
    with open(filename, 'r') as f:
        for line in f:
            d = {}
            val_list = line.rstrip().split(' ')
            for key, val in zip(KEY_LIST, val_list):
                if key in KEY_LIST[1:4]:
                    d[key] = float(val)
                else:
                    d[key] = val
        # add dict to list
            data_list.append(d)

    return data_list

def majority_vote(nearest_neighbors):
    i=0
    for elt in nearest_neighbors:
        if(elt["RAIN"]=="TRUE"):
            i+=1    
    return "TRUE" if i>=len(nearest_neighbors)/2 else "FALSE"

def k_nearest_neighbors(filename, test_point, k, year_interval):
   # get proper year interval
   min_year=int(test_point["DATE"][0:4])-year_interval/2
   max_year=int(test_point["DATE"][0:4])+year_interval/2
   d=read_dataset(filename)
   valid_time=[]
   for elt in d:
       if(int(elt["DATE"][0:4])>min_year and int(elt["DATE"][0:4])<max_year ):
        #    print(elt["DATE"][0:4])
           valid_time.append(elt)
    # sort the neighbours based on manhatten_distance
   valid_time.sort(key= lambda e: manhattan_distance(test_point,e))
# extract first k distance
   if(len(valid_time)==0):
       return "TRUE"
   if(len(valid_time)<k):
       return majority_vote(valid_time)
   nearest_neighbors=valid_time[:k]
   return majority_vote(nearest_neighbors)



def main():
    res=manhattan_distance({'DATE': '2015-08-12', 'TMAX': 83.0, 'PRCP': 0.3, 'TMIN': 62.0, 'RAIN': 'TRUE'}, {'DATE': '2014-05-19', 'TMAX': 70.0, 'PRCP': 0.0, 'TMIN': 50.0, 'RAIN': 'FALSE'}) 
    # print(res)
    ds=read_dataset("rain.txt")
    print(   k_nearest_neighbors("rain.txt", {'DATE': '1968-01-01', 'TMAX': 123.0, 'PRCP': 0.23, 'TMIN': 42.0}, 8, 10))
    print(  k_nearest_neighbors("rain.txt", {'DATE': '1998-01-01', 'TMAX': 34.0, 'PRCP': 0.73, 'TMIN': 33.0}, 15, 100))
    print(   k_nearest_neighbors("rain.txt", {'DATE': '1989-01-01', 'TMAX': 76.0, 'PRCP': 0.346, 'TMIN': 54.0}, 23, 9))
    print(  k_nearest_neighbors("rain.txt", {'DATE': '1985-01-01', 'TMAX': 19.0, 'PRCP': 0.53, 'TMIN': 0.0}, 10, 10))
    print(   k_nearest_neighbors("rain.txt", {'DATE': '1987-02-14', 'TMAX': 45.0, 'PRCP': 0.76, 'TMIN': 34.0}, 8, 20))
    print(  k_nearest_neighbors("rain.txt", {'DATE': '1987-05-01', 'TMAX': 53.0, 'PRCP': 0.39, 'TMIN': 39.0}, 40, 15))

if __name__ == "__main__":
    main()