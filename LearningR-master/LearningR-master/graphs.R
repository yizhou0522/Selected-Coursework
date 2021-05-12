library(dplyr)
mydata<-read.csv('murders.csv')
subdata<-select(mydata,state,population,murders)
barplot(subdata$population,
        xlab = 'states',
        ylab='populatiylab',
        main='States VS Population',
        names.arg = subdata$state,
        col='blue'
        ) 

sortdata<-arrange(mydata,desc(murders)) 
subdata<-select(sortdata,state,population,murders)
highdata<-head(subdata,5)
barplot(highdata$murders,
        xlab = 'states',
        ylab='murders',
        main='States VS Murders',
        names.arg = highdata$state,
        # names.args assign the val of y axis
        col='blue'
) 

# horizontal bar graph
# barplot(...,horiz=T,
          # xlabs=...)

# stacked bar plot
mydata<-read.csv("murdersmini.csv")
changedata=mutate(mydata,popu=population/10000)
changedata=changedata[c(1,3,4)]
mymatrix<-data.matrix(changedata)
mymatrix
mymatrixTranspose<-t(mymatrix)
# 要获取transpose
barplot(mymatrixTranspose,
        xlab = 'states',
        main='States VS Population',
        col=c('blue',"red"),
        names.arg = changedata$state,
) 
legend('topleft',c('pop.','murders'),fill=c('blue','red'))


# historgam
mydata<-read.csv('GEStock.csv')
subdata<-select(mydata,Date,Price)
hist(subdata$Price,
     xlab='Stock Price',
     main="Stocks data",
     col='blue',
     border="red",
     breaks=20)

# scatter plots
mydata<-read.csv('murders.csv')
subdata<-select(mydata,state,population,murders)
plot(subdata$population,subdata$murders,xlab="pop.",ylab="murders",
     main="pop vs murders",col="red",pch=10)#pch是一个统计参数

# line graphs
mydata<-read.csv('murders.csv')
subdata<-select(mydata,state,population,murders)
plot(subdata$murders,type='l',
     xlab='States',
     ylab='murders',
     main='states vs murders',
     col="blue")

# box plots
mydata<-read.csv('murders.csv')
subdata<-select(mydata,state,population,murders,region)
boxplot(subdata$murders~subdata$region,xlab = 'region',ylab = "murders",main="region vs murders",col="red",border="blue",notch=T)
# notch是boxplot一个参数

# multple plots in layout
# fommat:par(mfrow=c(1,2))
# 这表示我们创建的表，将在row=1,col=2的矩阵中排列，以建表顺序一一排列
# 我们就在这个语句后建表
