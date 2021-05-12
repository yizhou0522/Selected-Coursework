# Yizhou Liu
# liu773@wisc.edu
# cs540 
import numpy as np
import os
import math

def create_bow(vocab, filepath):
    """ Create a single dictionary for the data
        Note: label may be None
    """
    bow = {}
    # TODO: add your code here
    oov = 0
    f = open(filepath, 'r', encoding='utf-8')
    for word in f:
        word = word.rstrip('\n')
        if word in vocab and word in bow:
            bow[word] = bow[word]+1
        elif word in vocab:
            bow[word] = 1
        else:
            oov += 1
    # Add OOV to None Key
    if oov > 0:
        bow[None] = oov
    return bow

def load_training_data(vocab, directory):
    """ Create the list of dictionaries """
    dataset = []
    # TODO: add your code here
    for root, dirs, files in os.walk(directory):
        for file in files:
            filepath = os.path.join(root, file)
            dataset.append({'label': os.path.basename(root), 'bow':create_bow(vocab, filepath)})
    return dataset

def create_vocabulary(directory, cutoff):
    """ Create a vocabulary from the training directory
        return a sorted vocabulary list
    """
    vocab = []
    # TODO: add your code here
    vocab_dict = {}
    for root, dirs, files in os.walk(directory):
        for file in files:
            f = open(os.path.join(root, file), 'r', encoding='utf-8')
            for word in f:
                word = word.rstrip('\n')
                vocab_dict[word]=vocab_dict[word]+1 if word in vocab_dict else 1
    for key, value in vocab_dict.items():
        if value>=cutoff:
          vocab.append(key)
    return sorted(vocab)

def prior(training_data, label_list):
    """ return the prior probability of the label in the training set
        => frequency of DOCUMENTS
    """
    smooth = 1 # smoothing factor
    logprob = {}
    # TODO: add your code here
    for l in label_list:
        count = 0
        for e in training_data:
            if e['label'] == l:
                count += 1
        logprob[l] = count

    # total = sum(logprob.values())+len(label_list)
    total = len(training_data)+2.0
    logprob = {k: np.log((v+smooth)/ total) for k, v in logprob.items()}
    return logprob

def p_word_given_label(vocab, training_data, label):
    """ return the class conditional probability of label over all words, with smoothing """
    smooth = 1 # smoothing factor
    word_prob = {}
    # TODO: add your code here
    word_prob = {k: 0 for k in vocab}
    word_prob[None] = 0

    for bow in training_data:
        if bow['label'] != label:
            continue
        for k, v in bow['bow'].items():
            word_prob[k] += v
    denom = len(word_prob.keys()) + sum(word_prob.values())
    word_prob = {k: np.log((v + smooth) / denom) for k, v in word_prob.items()}
    return word_prob

    
##################################################################################
def train(training_directory, cutoff):
    """ return a dictionary formatted as follows:
            {
             'vocabulary': <the training set vocabulary>,
             'log prior': <the output of prior()>,
             'log p(w|y=2016)': <the output of p_word_given_label() for 2016>,
             'log p(w|y=2020)': <the output of p_word_given_label() for 2020>
            }
    """
    retval = {}
    # TODO: add your code here
    vocab = create_vocabulary(training_directory, cutoff)
    training_data = load_training_data(vocab, training_directory)
    res_prior = prior(training_data, ['2016', '2020'])
    res_2016 = p_word_given_label(vocab, training_data, '2016')
    res_2020 = p_word_given_label(vocab, training_data, '2020')

    retval = {'vocabulary': vocab,
         'log prior': res_prior,
         'log p(w|y=2016)': res_2016,
         'log p(w|y=2020)': res_2020}

    return retval


def classify(model, filepath):
    """ return a dictionary formatted as follows:
            {
             'predicted y': <'2016' or '2020'>, 
             'log p(y=2016|x)': <log probability of 2016 label for the document>, 
             'log p(y=2020|x)': <log probability of 2020 label for the document>
            }
    """
    retval = {}
    # TODO: add your code here
    bow = create_bow(model['vocabulary'], filepath)

    res_2016 = model['log prior']['2016']
    res_2020 = model['log prior']['2020']

    for word in bow:
        res_2016 = res_2016+model['log p(w|y=2016)'][word]*bow[word]
        res_2020 = res_2020+model['log p(w|y=2020)'][word]*bow[word]

    if res_2016<=res_2020:
        predict_y='2020'
    else:
        predict_y='2016'

    retVal = {'predicted y': predict_y,
         'log p(y=2016|x)': res_2016,
         'log p(y=2020|x)': res_2020}

    return retVal


def main():
    # print(create_vocabulary('./EasyFiles/', 1))
    # print(create_vocabulary('./EasyFiles/', 2))
    # vocab=create_vocabulary('./EasyFiles/',2)
    # print(create_bow(vocab, './EasyFiles/2016/1.txt'))
    # vocab=create_vocabulary('./corpus/training/',2)
    # vocab = create_vocabulary('./EasyFiles/', 1)
    # training_data = load_training_data(vocab, './EasyFiles/')
    # print(p_word_given_label(vocab, training_data, '2020'))
    # print(p_word_given_label(vocab, training_data, '2016'))
    # print(create_bow(vocab, './EasyFiles/2016/1.txt'))
    # print(create_bow(vocab, './EasyFiles/2020/2.txt'))
    # print(train('./EasyFiles/', 2))
    model = train('./corpus/training/', 2)
    print(classify(model, './corpus/test/2016/0.txt'))

    # print(load_training_data(vocab,'./EasyFiles/'))


if __name__ == "__main__":
    main()