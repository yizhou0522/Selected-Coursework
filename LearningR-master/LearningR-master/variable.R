# r is a dynamic language, meaning its variable's property( like double, character) is not declared 
# 同python
age<-24
typeof(age)
mode(age)
storage.mode(age)
class(age)
age<-20.9
typeof(age)
mode(age)

x<-'hello'
typeof(x)
# R是一行一行执行的，要观察右边的envir确定这个value有没有被赋值。如果不运行赋值语句，直接跳下面的运行结果，可能会出错
class(x)

bool<-TRUE
class(bool)
# class func return the data type of object   
# 布尔值被归于logical 类

# R 有22key words
 is.integer(age)
age<-as.integer(10.25)
# as integer 是声明int的方式，否则默认值为double

y<-3+4i
class(y)#复数

bool<-F
# true false 可以用简写

str<-"45"
class(str)#这里同java

x<-as.character("3.5")
# 同java tostring
is.character(x)

# 三种赋值方式
x<-19
y=19
assign('z',19)
# 这里z要加引号
50->w
a<-b<-c<-8
# 同时赋值

# 特殊字符
d<-pi
e<-LETTERS
f<-letters
g<-month.abb
# stands for abbrevation

#in R, every number except 0 is True
12&-2

# sequences generator
h<-1:10
h
g<-h*2
g<-10:1
g<-g-1
# CAVEAT:注意括号的优先级
x<-5
1:x-1
1:(x-1)
j<-seq(1,5)
j
j<-seq(5)
j#by default, the sequence starts from 1
k<-seq(from=1,to=10,by=2)
k
# 很像python,by是间隔值
l<-seq(1,10,length=5)
l
# 1到10平均分为5份
# 当by=0时，有可能会报错；by需要取个合理的值

# replicate func
m<-rep('z',times=3)
m
x<-1:3
y<-rep(x,each=5)
y
