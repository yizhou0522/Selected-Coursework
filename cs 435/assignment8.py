import numpy
import copy
def berlekamp_massey_algorithm(block_data):
    """
    An implementation of the Berlekamp Massey Algorithm. Taken from Wikipedia [1]
    [1] - https://en.wikipedia.org/wiki/Berlekamp-Massey_algorithm
    The Berlekamp–Massey algorithm is an algorithm that will find the shortest linear feedback shift register (LFSR)
    for a given binary output sequence. The algorithm will also find the minimal polynomial of a linearly recurrent
    sequence in an arbitrary field. The field requirement means that the Berlekamp–Massey algorithm requires all
    non-zero elements to have a multiplicative inverse.
    :param block_data:
    :return:
    """
    n = len(block_data)
    c = numpy.zeros(n)
    b = numpy.zeros(n)
    c[0], b[0] = 1, 1
    l, m, i = 0, -1, 0
    int_data = [int(el) for el in block_data]
    while i < n:
        v = int_data[(i - l):i]
        v = v[::-1]
        cc = c[1:l + 1]
        d = (int_data[i] + numpy.dot(v, cc)) % 2
        if d == 1:
            temp = copy.copy(c)
            p = numpy.zeros(n)
            for j in range(0, l):
                if b[j] == 1:
                    p[j + i - m] = 1
            c = (c + p) % 2
            if l <= 0.5 * i:
                l = i + 1 - l
                m = i
                b = temp
        i += 1
    return l

p=[1,1,1,0,0,0]
print(berlekamp_massey_algorithm(p))