import os
import matplotlib.pyplot as plt
import numpy as np

'''
steps

1. change raw data format 

2. svm-scale -s range train > train.scale

3. 10-fold cross validation set

4. training & testing

5. for each d, plot error vs C
'''

def data_format():
    filename = 'spambase.data.shuffled'
    writefilename = 'spambase.data.formatted'
    output = open(writefilename, 'w')
    from decimal import Decimal
    with open(filename, "r") as f:
        for line in f:
            xy = line.strip().split(',')
            x = xy[0:-1]
            y = xy[-1]
            output.write(y + " ")
            for i in range(1, 1+len(x)):
                if Decimal(x[i-1]) != 0:
                    output.write(str(i) + ':' + x[i-1] + " ")
            output.write("\n")
    output.close()

def split():
    files = [f for f in os.listdir('.') if f[0] == 'x']
    n = len(files)
    for i in range(n):
        f = [x for x in files]
        del f[i]
        cmd = 'cat ' + ' '.join(f) + ' > train/data' + str(i)
        print cmd
        os.system(cmd)

def train():
    files = [f for f in os.listdir('.') if f[0] == 'x']
    path = '../libsvm-3.21'
    D = range(1,5)
    K = range(-5, 6)
    C = [2 ** i for i in K]

    for d in D:
        for c in C:
            for i in range(10):
                model = 'train/model/model_%d_%f_%d.model' % (d, c, i)
                # train
                cmd = './%s/svm-train -d %d -c %f train/data%d %s' % (path, d, c, i, model)
                #print cmd
                os.system(cmd)
                # test
                cmd = './%s/svm-predict %s %s train/result/result_%d_%f_%d' % (path, files[i], model, d, c, i)
                #print cmd
                os.system(cmd)

def getResult():
    import re

    path = 'train/result'
    log_file = 'log'
    result_files = os.listdir(path)

    acc = []
    pattern = "Accuracy = (.*?)%"
    prog = re.compile(pattern)
    with open(log_file, "r") as f:
        for line in f:
            result = prog.match(line)
            if result is not None:
                acc.append(100 - float(result.group(1)))
    print len(acc)

    '''
    D = []
    C = []
    for f in result_files:
        params = f.split('_')
        d = int(params[1])
        c = float(params[2])
        i = int(params[3])
        if d not in D:
            D.append(d)
        if c not in C:
            C.append(c)
    '''

    D = range(1,5)
    K = range(-5, 6)
    C = [2 ** i for i in K]

    print D
    print C
    #print acc

    path = 'train/pic'
    offset = 0
    import numpy
    for d in D:
        avg_d = []
        for c in C:
            #print acc[offset : offset + 10]
            avg = numpy.average(acc[offset : offset + 10])
            #print d, c, avg
            avg_d.append(avg)
            offset = offset + 10

        plt.plot(avg_d) 
        print avg_d
        plt.xlabel('ith value in C')
        plt.ylabel('cross validation average error (%)')
        plt.title('d = %d' % d)
        pic = '%s/pic_%d.png' % (path, d)
        plt.savefig(pic)

#getResult()
#split()
#data_format()
#train()
