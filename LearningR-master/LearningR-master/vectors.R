mysum=function(x,y){
  z=x+y
  return (z)
}

# caveat：这里方法传进去的参数顺序是可变的
# 设置mysum(x,y=2,z=5)相当于给y,z设置默认值2，5，
# 这样的话，调用mysum(2),我们就是只给x赋值，x=2,
# 结果为2+2+5=9
# 同时，default值可以被覆盖，但parm不能少于不是default的arg,且param 顺序要跟arg顺序一一对应
# 如 sum=func(x=2,y,w=5)
# sum(3)报错，因为y未被赋值，这里只是赋值了x
# 若改为sum=func(y,x=2,w=5),则没问题
# 同时，若param在函数里未被使用，则没有arg对应之也是可以的

myeval=function(x,y){
  w=x+y
  z=x*y
  result=list('sum'=w,'mul'=z)
  return (result)
}

# lambda func
myexp=function(x) x^3
myexp(3)

# 6 基础类：atomic type
# integer, double, complex,logical, character,
# raw (raw data)

# vector////////////////////////////////
x<-c(1,2,3,4)
typeof(x)
# note that vector can only contains the same type of objects
x<-c(1,2,"a",4)
x
# 这里数字Implicitly convert to characters

y<-c(33,6)
z<-c(x,y)#vector相加
z

x<-vector('character',length=3)
x
# 这里是有默认值的，同java
length(z)#同java array.length

# caveat: R里面，index从1开始算
# 每个scalar都被算作长度为1的vector
z[1]
z[-1]
# -x是将当前vector里的第x号元素抹掉，保留其余元素
z[2:7]
z[3:6]
# 这里首位index都包括，若超出范围，则用NA替代
z[9]<-0
z
z[-3]<-0
z
x<-c(1,2,3,4,5)
y<-c(T,F,T,F,T)
x[y]
# vector的嵌套
# 若y的值的个数少于x的，则repeat 之前的顺序，并使之适用于x

# print vector方法
for (i in 1:length(x)){
  print(x[i])
}
# 方法2
for (i in seq_along(x)){
  print(x[i])
}
# 方法3
for (i in x){
  print(x[i])
}
35%in%x
# see if 35 is in the vector x
y<-c(3,9)
y%in% x
sqrt(x)    
x+y
# vector相加则两个vector必须元素数量一致
sum(x)
prod(x) 
rev(x)
sort(x)
sort(x,decreasing = T)

y<-c(3,4,5,6,7)
x %*% y
crossprod(x,y)
# 每个元素互相乘，然后算它们的和

x %o% y
# 返回一个表格
tcrossprod(x,y)

# object type在vector里的coercion,即强制数据类型转换
# double,integer->character
# logical->double;true->1,false->0
# logical->character

n<-0:5
n
class(n)
as.logical(n)
as.character(n)

y<-x>3
y
# y是个logical vector,每个y里的元素与3比大小
y<-c(6,3,7,3,8)
x<y
which(y>3)
# which返回满足条件的index
y[which(y>3)]
# 返回元素

a<-factor(c("Male","Male","Male","Female","Female"))
a
# 这里会输出vector,和它的levels:(vector里面的unique元素)
table(a)

# vector里的数学方法
# abs(x)绝对值方法
# ceiling(x)
# floor(x)
# round(x)四舍五入法
# round(x,2)保留2位小数
# trunc(x)直接取消后面的小数
# sqrt(x)
# exp(x)
# log(x)
# log(x,base=2)以2为底数
# 或者log2(x)
# factorial(x)

c<-rnorm(10)
# 默认值，mean=0,sd=1,生成10个随机数
c
c<-rnorm(10,mean=10, sd=2)
c
