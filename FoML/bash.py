import os
import matplotlib.pyplot as plt
import numpy as np

def split():
    files = [f for f in os.listdir('.') if f[0] == 'x']
    n = len(files)
    for i in range(n):
        f = [x for x in files]
        del f[i]
        cmd = 'cat ' + ' '.join(f) + ' > data' + str(i)
        print cmd
        os.system(cmd)

def train():
    files = [f for f in os.listdir('.') if f[0] == 'x']
    path = '../../libsvm-3.21'
    D = range(1,5)
    K = range(-5, 6)
    C = [2 ** i for i in K]

    for d in D:
        for c in C:
            for i in range(10):
                # train
                cmd = './%s/svm-train -d %d -c %f data/data%d' % (path, d, c, i)
                os.system(cmd)
                # test
                cmd = './%s/svm-predict %s data%d.model result/result_%d_%f_%d' % (path, files[i], i, d, c, i)
                os.system(cmd)

def getResult():
    import re

    path = 'result'
    log_file = 'log'
    result_files = os.listdir(path)

    acc = []
    pattern = "Accuracy = (\d+\.\d+)%"
    prog = re.compile(pattern)
    with open(log_file, "r") as f:
        for line in f:
            result = prog.match(line)
            if result is not None:
                acc.append(float(result.group(1)))
    print len(acc)

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

    print D
    print C
    #print acc

    path = 'pic'
    offset = 0
    for d in D:
        for c in C:
            res = acc[offset : offset + 10]
            plt.bar(range(1, len(res) + 1), res, 1) 
            plt.xlabel('Cross Validation')
            plt.ylabel('Accuracy')
            plt.title('d = %d, c = %f' % (d, c))
            pic = '%s/pic_%d_%f.png' % (path, d, c)
            plt.savefig(pic)
            offset = offset + 10


def plot():
    a = []
    import random
    for i in range(10):
        a.append(random.randrange(1, 20))

    plt.bar(range(1, len(a) + 1), a, 1)
    plt.xlabel('Cross Validation')
    plt.ylabel('Accuracy')
    plt.savefig("a.png")

getResult()
