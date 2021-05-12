# python imports
import os
from tqdm import tqdm

# torch imports
import torch
import torch.nn as nn
import torch.nn.functional as F
import torch.optim as optim

# helper functions for computer vision
import torchvision
import torchvision.transforms as transforms

import time
import numpy as np
from torch.utils.data import DataLoader



class SimpleFCNet(nn.Module):
    """
    A simple neural network with fully connected layers
    """
    def __init__(self, input_shape=(28, 28), num_classes=10):
        super(SimpleFCNet, self).__init__()
        # create the model by adding the layers
        layers = []

        ###################################
        #     fill in the code here       #
        ###################################
        # Add a Flatten layer to convert the 2D pixel array to a 1D vector
        layers.append(nn.Flatten())
        # Add a fully connected / linear layer with 128 nodes
        layers.append(nn.Linear(784,128))

        # Add ReLU activation
        layers.append(nn.ReLU(inplace=True))

        # Append a fully connected / linear layer with 64 nodes
        layers.append(nn.Linear(128,64))

        # Add ReLU activation
        layers.append(nn.ReLU(inplace=True))

        # Append a fully connected / linear layer with num_classes (10) nodes
        layers.append(nn.Linear(64,num_classes))

        self.layers = nn.Sequential(*layers)

        self.reset_params()

    def forward(self, x):
        # the forward propagation
        out = self.layers(x)
        if self.training:
            # softmax is merged into the loss during training
            # out = nn.functional.softmax(out, dim=1)
            return out
        else:
            # attach softmax during inference
            out = nn.functional.softmax(out, dim=1)
            return out

    def reset_params(self):
        # to make our model a faithful replica of the Keras version
        for m in self.modules():
            if isinstance(m, nn.Linear):
                nn.init.xavier_uniform_(m.weight)
                if m.bias is not None:
                    nn.init.constant_(m.bias, 0.0)

# net=SimpleFCNet()


class SimpleConvNet(nn.Module):
    """
    A simple convolutional neural network
    """
    def __init__(self, input_shape=(32, 32), num_classes=100):
        super(SimpleConvNet, self).__init__()
        ####################################################
        # you can start from here and create a better model
        ####################################################

    #     self.block_1 = nn.Sequential(
    #             nn.Conv2d(in_channels=3,
    #                       out_channels=6,
    #                       kernel_size=(3, 3),
    #                       stride=(1, 1),
    #                       # (1(32-1)- 32 + 3)/2 = 1
    #                       padding=1), 
    #             nn.ReLU(),
    #             nn.Conv2d(in_channels=6,
    #                       out_channels=6,
    #                       kernel_size=(3, 3),
    #                       stride=(1, 1),
    #                       padding=1),
    #             nn.ReLU(),
    #             nn.MaxPool2d(kernel_size=(2, 2),
    #                          stride=(2, 2))
    #     )
        
    #     self.block_2 = nn.Sequential(
    #             nn.Conv2d(in_channels=6,
    #                       out_channels=16,
    #                       kernel_size=(3, 3),
    #                       stride=(1, 1),
    #                       padding=1),
    #             nn.ReLU(),
    #             nn.Conv2d(in_channels=16,
    #                       out_channels=16,
    #                       kernel_size=(3, 3),
    #                       stride=(1, 1),
    #                       padding=1),
    #             nn.ReLU(),
    #             nn.MaxPool2d(kernel_size=(2, 2),
    #                          stride=(2, 2))
    #     )
        
    #     self.block_3 = nn.Sequential(        
    #             nn.Conv2d(in_channels=16,
    #                       out_channels=32,
    #                       kernel_size=(3, 3),
    #                       stride=(1, 1),
    #                       padding=1),
    #             nn.ReLU(),
    #             nn.Conv2d(in_channels=32,
    #                       out_channels=32,
    #                       kernel_size=(3, 3),
    #                       stride=(1, 1),
    #                       padding=1),
    #             nn.ReLU(),        
    #             nn.Conv2d(in_channels=32,
    #                       out_channels=32,
    #                       kernel_size=(3, 3),
    #                       stride=(1, 1),
    #                       padding=1),
    #             nn.ReLU(),
    #             nn.MaxPool2d(kernel_size=(2, 2),
    #                          stride=(2, 2))
    #     )
        
          
    #     # self.block_4 = nn.Sequential(   
    #     #         nn.Conv2d(in_channels=32,
    #     #                   out_channels=64,
    #     #                   kernel_size=(3, 3),
    #     #                   stride=(1, 1),
    #     #                   padding=1),
    #     #         nn.ReLU(),        
    #     #         nn.Conv2d(in_channels=64,
    #     #                   out_channels=64,
    #     #                   kernel_size=(3, 3),
    #     #                   stride=(1, 1),
    #     #                   padding=1),
    #     #         nn.ReLU(),        
    #     #         nn.Conv2d(in_channels=64,
    #     #                   out_channels=64,
    #     #                   kernel_size=(3, 3),
    #     #                   stride=(1, 1),
    #     #                   padding=1),
    #     #         nn.ReLU(),            
    #     #         nn.MaxPool2d(kernel_size=(2, 2),
    #     #                      stride=(2, 2))
    #     # )
        
    #     # self.block_5 = nn.Sequential(
    #     #         nn.Conv2d(in_channels=64,
    #     #                   out_channels=64,
    #     #                   kernel_size=(3, 3),
    #     #                   stride=(1, 1),
    #     #                   padding=1),
    #     #         nn.ReLU(),            
    #     #         nn.Conv2d(in_channels=64,
    #     #                   out_channels=64,
    #     #                   kernel_size=(3, 3),
    #     #                   stride=(1, 1),
    #     #                   padding=1),
    #     #         nn.ReLU(),            
    #     #         nn.Conv2d(in_channels=64,
    #     #                   out_channels=64,
    #     #                   kernel_size=(3, 3),
    #     #                   stride=(1, 1),
    #     #                   padding=1),
    #     #         nn.ReLU(),    
    #     #         nn.MaxPool2d(kernel_size=(2, 2),
    #     #                      stride=(2, 2))             
    #     # )
            
    #     self.classifier = nn.Sequential(
    #         nn.Linear(512, 1024),
    #         nn.ReLU(True),
    #         #nn.Dropout(p=0.5),
    #         nn.Linear(1024, 1024),
    #         nn.ReLU(True),
    #         #nn.Dropout(p=0.5),
    #         nn.Linear(1024, num_classes),
    #     )
            
    #     for m in self.modules():
    #         if isinstance(m, torch.nn.Conv2d) or isinstance(m, torch.nn.Linear):
    #             nn.init.kaiming_uniform_(m.weight, mode='fan_in', nonlinearity='relu')
    #             if m.bias is not None:
    #                 m.bias.detach().zero_()
                    
    #     #self.avgpool = nn.AdaptiveAvgPool2d((7, 7))
        
        
    # def forward(self, x):

    #     x = self.block_1(x)
    #     x = self.block_2(x)
    #     x = self.block_3(x)
    #     # x = self.block_4(x)
    #     # x = self.block_5(x)
    #     #x = self.avgpool(x)
    #     x = x.view(x.size(0), -1)
    #     logits = self.classifier(x)
    #     probas = F.softmax(logits, dim=1)

    #     return logits, probas

        self.features = nn.Sequential(
            nn.Conv2d(3, 32, kernel_size=3, stride=1, padding=2),
            nn.ReLU(inplace=True),
            nn.MaxPool2d(kernel_size=3, stride=2),
            nn.Conv2d(32, 64, kernel_size=3, padding=2),
            nn.ReLU(inplace=True),
            nn.Conv2d(64, 128, kernel_size=3, padding=2),
            nn.ReLU(inplace=True),
            # nn.MaxPool2d(kernel_size=3, stride=2),
            # 6 to 16 13.47
            # nn.MaxPool2d(kernel_size=3, 
            # stride=2),
            # nn.Conv2d(16, 26, kernel_size=3, padding=1),
            # nn.ReLU(inplace=True),
            # nn.Conv2d(384, 256, kernel_size=3, padding=1),
            # nn.ReLU(inplace=True),
            # nn.Conv2d(256, 256, kernel_size=3, padding=1),
            # nn.ReLU(inplace=True),
            # nn.MaxPool2d(kernel_size=3, stride=2),
        )
        self.avgpool = nn.AdaptiveAvgPool2d((6, 6))
        self.classifier = nn.Sequential(
            nn.Dropout(),
            nn.Linear(6*6*128, 2048),
            nn.ReLU(inplace=True),
            nn.Dropout(),
            nn.Linear(2048, 2048),
            nn.ReLU(inplace=True),
            nn.Linear(2048, num_classes),
        )





    def forward(self, x):
        #################################
        # Update the code here as needed
        #################################
        x = self.features(x)
        x = self.avgpool(x)
        x = torch.flatten(x, 1)
        x = self.classifier(x)
        return x



def train_model(model, train_loader, optimizer, criterion, epoch):
    """
    model (torch.nn.module): The model created to train
    train_loader (pytorch data loader): Training data loader
    optimizer (optimizer.*): A instance of some sort of optimizer, usually SGD
    criterion (nn.CrossEntropyLoss) : Loss function used to train the network
    epoch (int): Current epoch number
    """
    model.train()
    train_loss = 0.0
    for input, target in tqdm(train_loader, total=len(train_loader)):
        
        ######################################################
        # fill in the standard training loop of forward pass,
        # backward pass, loss computation and optimizer step
        ######################################################
        # 1) zero the parameter gradients
        optimizer.zero_grad()

        # 2) forward + backward + optimize
        outputs = model(input)
        # outputs.aux_logits=False
        loss = criterion(outputs, target)
        loss.backward()
        optimizer.step()


        # Update the train_loss variable
        # .item() detaches the node from the computational graph
        # Uncomment the below line after you fill block 1 and 2
        train_loss += loss.item()

    train_loss /= len(train_loader)
    print('[Training set] Epoch: {:d}, Average loss: {:.4f}'.format(epoch+1, train_loss))

    return train_loss


def test_model(model, test_loader, epoch):
    model.eval()
    correct = 0
    with torch.no_grad():
        for input, target in test_loader:
            output = model(input)
            # output.aux_logits=False
            pred = output.max(1, keepdim=True)[1]
            correct += pred.eq(target.view_as(pred)).sum().item()

    test_acc = correct / len(test_loader.dataset)
    print('[Test set] Epoch: {:d}, Accuracy: {:.2f}%\n'.format(
        epoch+1, 100. * test_acc))

    return test_acc
