data<-read.table('data.txt',header=T)
mydata<-read.table('data.txt',header=T,nrows=5)
# abs path needed
mydata
mydata<-read.table('data.txt',header=T,skip=3)
data #skip first three rows
mydata

# sep is the delimiter (根据表中不同分隔符过滤表中多余元素，使其成为一个可视的表)

mydata<-read.csv('sample.csv')
mydata

#read rds func for rds file

# read data from internet 
url.show('http://softlect.in/datasets/sbuxPrices.csv')
mydata<-read.table('http://softlect.in/datasets/sbuxPrices.csv',sep=',',head=T)
mydata

# read data from ctrl-c
mydata<-read.table('clipboard')
# 将复制的数据显示出来
mydata
