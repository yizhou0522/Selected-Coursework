id<-c(1,2,3)
name<-c('b','a','c')
marks<-c(3.4,5.6,7.0)
students<-data.frame(id,name,marks)
students

# slists<-list()注意观察dataframe和list之间区别
students[2,]#retrieve row
students[2:3,]
# 获取元素方式同matrix,list
students[2]#retrieve col
students
students[[3]]
# 这里注意，这是以col为单位组成dataframe,这里获取的是col
students[[3]][1]#获取col后取col里的第一个元素
students$marks[3]

report<-subset(students,marks>4&id<3,select=c(name,marks))
# 同select=name:marks
report
# 有点像select from where
students
report<-subset(students,marks>4&id<3,select=-name)
report

students<-rbind(students,data.frame(id=104,name='l',marks=3.0))
students
# 同时也有cbind

stutable<-edit(students)
stutable
# stutable<-edit会自动跳出一张表，然后可以修改数值，但dataframe本身数值并不变化

#############################missingdata################
x<-c(10,3,NA,9,NaN)
x
is.na(x)
is.nan(x)
# not available, NA
# not a number, NaN
y<-is.na(x)
y
x[!y]
# remove the na, nan values

students
students[[3]][2]<-NA
students
stuNA<-complete.cases(students)
stuNA
# return a list of T/F, depends on if the list has missing val
students
students[stuNA,]
# remove col with missing val

# write dataframe to csv file
write.csv(students,file='output.csv')
