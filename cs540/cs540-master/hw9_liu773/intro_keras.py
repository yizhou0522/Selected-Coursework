# Yizhou Liu
# liu773@wisc.edu
# cs540
import tensorflow as tf
from tensorflow import keras
from tensorflow.keras import layers
from tensorflow.keras.layers import Dense, Activation, Flatten
import numpy as np


def get_dataset(training=True):
    mnist = keras.datasets.mnist
    (train_images, train_labels), (test_images, test_labels) = mnist.load_data()
    if training==True:
        return (train_images, train_labels)
    else:
        return (test_images, test_labels)

def print_stats(train_images, train_labels):
    class_names = ['Zero', 'One', 'Two', 'Three', 'Four', 'Five', 'Six', 'Seven', 'Eight', 'Nine']
    class_num=np.zeros(10)
    print(len(train_images))
    # print 28x28
    shape_list=train_images.shape #(60000,28,28)
    print(shape_list[len(shape_list)-2],end="",flush=True)
    print("x",end="",flush=True)
    print(shape_list[len(shape_list)-1])
    # print number of images
    i=0
    for elt in train_labels:
        class_num[elt]=class_num[elt]+1
    for i in range(0, len(class_names)):
        print(i,end=". ",flush=True)
        print(class_names[i],end=" - ",flush=True)
        print((int)(class_num[i]))
    return ""

def build_model():
    model = keras.Sequential()
    model.add(keras.Input(shape=(28,28)))
    # model.add(tf.keras.Input(shape=(,)))
    # input_shape=(28,28)
    # model.add(tf.keras.layers.Conv2D(input_shape=(28,28)))
    model.add(Flatten())
    model.add(tf.keras.layers.Dense(128, activation='relu'))
    model.add(tf.keras.layers.Dense(64, activation='relu'))
    model.add(tf.keras.layers.Dense(10))

    #optimizer
    opt = keras.optimizers.SGD(learning_rate=0.001)
    loss_function = tf.keras.losses.SparseCategoricalCrossentropy(from_logits=True)
    model.compile( optimizer=opt,loss=loss_function,metrics=['accuracy'])
    return model

def train_model(model, train_images, train_labels, T):
    model.fit(train_images,train_labels,epochs=T)

def evaluate_model(model, test_images, test_labels, show_loss=True):
    test_loss, test_accuracy = model.evaluate(test_images, test_labels,verbose=0)
    if show_loss==True:
         print("Loss:", "{:.4f}".format(test_loss))
    print("Accuracy:","{0:.2f}%".format(test_accuracy*100))

def predict_label(model, test_images, index):
    res=model.predict(test_images)
    arr=res[index]
    class_names = ['Zero', 'One', 'Two', 'Three', 'Four', 'Five', 'Six', 'Seven', 'Eight', 'Nine']
    # sorted_index_array = np.argsort(res) 
    sorted_array = np.sort(arr)[::-1]
    for elt in sorted_array[0:3]:
       tuple_res=np.where(arr==elt)
       index_in_arr=(int)(tuple_res[0])
       print(class_names[index_in_arr],end=": ",flush=True)
       print("{0:.2f}%".format(elt*100))

    # print(res,"length",len(res))


def main():
    (train_images, train_labels) = get_dataset()
    # print(type(train_images),"xxxx",type(train_labels), "xxx",type(train_labels[0]))
    (test_images, test_labels) = get_dataset(False)
    # print(train_images)
    # print_stats(train_images,train_labels)
    model=build_model()
    # print(model)
    # print(model.loss)
    # print(model.optimizer)
    train_model(model, train_images, train_labels, 10)
    evaluate_model(model, test_images, test_labels)
    model.add(keras.layers.Softmax())
    predict_label(model, test_images, 1)


if __name__ == '__main__':
    main()