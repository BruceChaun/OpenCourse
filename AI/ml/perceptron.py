import numpy as np
import pandas as pd
from scipy.spatial import distance
from sklearn.preprocessing import MinMaxScaler

class perceptron:
    def __init__(self, feature, label):
        self.feature = np.array(feature)
        self.label = np.array(label) * 2 - 1
        self.w = np.zeros(len(feature[0])) # weight
        self.alpha = 0.5 # learning rate
        self.T = 1000 # max iteration

    def train(self):
        t = 0
        prev_error = len(self.feature)
        while t < self.T:
            error = 0
            for i in xrange(len(self.feature)):
                x = self.feature[i]
                y_pred = np.sign(np.inner(self.w, x))

                if y_pred != self.label[i]:
                    # incorrect prediction
                    self.w += self.alpha * self.label[i] * x
                    error += 1

            # discount learning rate
            if error > prev_error:
                self.alpha *= 0.7
            prev_error = error

            t += 1

    def test(self, data):
        feat = MinMaxScaler(feature_range=(-1, 1)).fit_transform(data[:,1:])
        y = data[:,0] * 2 - 1

        pred = []
        for x in feat:
            pred.append(np.sign(np.inner(self.w, x)))

        # evaluate accuracy
        acc = 0
        for i in xrange(len(pred)):
            if pred[i] == self.label[i]:
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
    percept = perceptron(feature, label)
    percept.train()
    percept.test(test_set)
