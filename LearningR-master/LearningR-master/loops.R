p<-0
x<-5
ifelse(x>=0,'positive','negative')
x%%2==0
x%%2

y<-1:10
for(i in y){
  print(i)
}

x<-letters
y<-x[1:5]
# print first 5 characters
for (i in y){
  print(i)
}

# while 同java

i<-1
repeat{
  print(i)
  if(i>=5)
    break
  i<-i+1 #必须有一个指令更改condition variable
}

# next 同java continue
