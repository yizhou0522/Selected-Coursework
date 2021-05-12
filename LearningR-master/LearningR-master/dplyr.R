library(dplyr)
mydata<-read.csv('murders.csv')
mydata
dim(mydata)
str(mydata)
# display the structure of the data
mydata[c(1,4,6)]
# return the col 
mydata[c("state","population", "murders")]
# 取col名字
names(mydata)
subdata<-select(mydata,state:population)
# select from state to population col
subdata
subdata<-select(mydata,-c(abb,region))
subdata

subdata<-filter(mydata,murders>100)
# like SQL where
subdata
subdatamurders<-select(subdata,state,murders)
# like SQL select
subdatamurders

subdata<-arrange(mydata,murders)
# 以升序排列murders
subdata
head(subdata,5)
tail(subdata,5)
subdata<-arrange(mydata,desc(murders))
# 降序排之
subname

names(mydata)
mydata<-rename(mydata,abbr=abb，homicide=murders)
# 这里重命名的方法值得注意一下，新名=旧名,而且必须用=,不能用<-,原因我不是很清楚
names(mydata)

mydata<-mutate(mydata,state=state,ratio=murders/population)
names(mydata)
# 自己定义并新增一列
mydata<-transmute(mydata,ratio=murders/population)
mydata
# 自定义并只返回参数里指定的列的数据

subdata<-group_by(mydata,region)
subdata
summarise(subdata,mean(murders))
# 后边可传入max,mean,sum,median (like sql aggregate function),仍以group 为单位

#pipeline operator:简化语句
mydata<-read.csv('murders.csv')
arrange(mydata,murders)%>%select(state,murders)%>%head(3) 
# 自左向右，且左边执行后的数据将传入右边执行
# 所以，上面一句指令里，只有第一个param传入table