import numpy as np
import pandas as pd
from scipy.spatial import distance
from Queue import PriorityQueue
from sklearn.preprocessing import MinMaxScaler

class knn:
    def __init__(self, k, feature, label):
        self.k = k
        self.feature = np.array(feature)
        self.label = np.array(label)

    def test(self, data):
        feat = MinMaxScaler(feature_range=(-1, 1)).fit_transform(data[:,1:])
        y = data[:,0]

        pred = []
        for d in feat:
            pq = PriorityQueue()
            for i in xrange(len(self.feature)):
                dist = distance.euclidean(d, self.feature[i])
                pq.put((dist, i))

            # majority vote
            neibor = {}
            for k in range(self.k):
                i = pq.get()[1]
                l = self.label[i]
                if l in neibor:
                    neibor[l] += 1
                else:
                    neibor[l] = 1

            mode = -1
            y_hat = 0
            for k,v in neibor.items():
                if v > mode:
                    y_hat = k
            pred.append(y_hat)

        # evaluate accuracy
        acc = 0
        for i in xrange(len(pred)):
            if pred[i] == self.label[i]:
                print pred[i],
                acc += 1
        print 'accuracy = %f' % (1.0 * acc / len(pred))

def read_file(name):
    df = pd.read_csv(name, skiprows=0)
    data = np.array(df, np.float32)
    return data

if __name__ == '__main__':
    data = read_file('votes-train.csv')
    feature = data[:,1:]
    label = data[:,0]
    feature = MinMaxScaler(feature_range=(-1, 1)).fit_transform(feature)

    test_set = read_file('votes-test.csv')
    for k in range(1, 11, 2):
        knn_ = knn(k, feature, label)
        knn_.test(test_set)
    
    '''
    k   acc
    1   0.754183
    3   0.696268
    5   0.658945
    7   0.640927
    9   0.637066
    '''
