# objects in lists dont' need to be the same type
x<-list(10,'John',T)
x

rollno<-c(1,2,3)
snames<-c('J','B','A')
marks<-c(2.3,3.5,6.7)
students<-list(rollno,snames,marks)
students

# 同以下方式
stu=list('id'=c(1,2,3),'name'=c('b'),'scores'=c(2,3))
stu
stu[1]
# return the list
stu[[1]]
# return the elt of the list
stu[[1]][2]<-90
stu

stu$id
stu$scores
stu[c(1,3)]
stu[c('id','scores')]

students<-c(students,stu)
students
# concatentaion
# test for git pull
