m<-matrix(nrow=2,ncol=3)
m
dim(m)
# return row and col
m<-matrix(c(1,2,3,4,5,6))
m
# matrix default is col-wised

m<-matrix(c(1,2,3,4,5,6),nrow=2, ncol=3,byrow = T)
m
# 设置byrow=T才能横过来
dim(m)<-c(3,2)
m

dim(m)
nrow(m)
ncol(m)
length(m)

m<-matrix(4,3,3)
m
m<-diag(1,3,3)
m
# create diagonal func
# parm: value,nrow,ncol
m<-diag(1:3)
m
# 1,2,3as diag
diag(m)
m

rownames(m)<-c(1,2,3)
colnames(m)<-c('a','b','c')
m

m
m[1,1]
m[2,]#access the entire 2rd row
m[,3]
m[nrow(m),]
# return the last row of m,ncol同理
m[-3,]
# eliminate third row of col
m[2,3]=9
m
m[1:3,1:2]
# 切片取值
m[c(1,3),]
# 取 first row, third row

m=m[,-2]
m
# 删掉第二列
# 超过nrows,ncols范围会报错outofbounds error

n<-rbind(m,c(3,4))
n
# 以row为单位加一行数据
n<-matrix(c(9,0),nrow=1,ncol=2,byrow=T)
o<-rbind(m,n)
o
o<-rbind(n,m)
o
p<-matrix(c(9,0,2),nrow=3,ncol=1)
o<-cbind(m,p)#col bind
o
m<-matrix(c(1,2,3,4,5,6,7,8,9),nrow=3,ncol=3,byrow=T)
m
m+o
m-o
m*o
m%*%o#this is matrix multiplication
m/o
m
t(m)#transpose
solve(m)#inverse of matrix

rowSums(m)
m
colSums(m)
rowMeans(m)
colMeans(m)
apply(m,1,sum)
m
apply(m,3,sum)
# apply 中间的参数好像只能传1（row),或2（col)