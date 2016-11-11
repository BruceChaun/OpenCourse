'''
AdaBoost algorthm
refer to https://gist.github.com/tristanwietsma/5486024
https://www.hdm-stuttgart.de/~maucher/Python/ComputerVision/html/Adaboost.html
'''

from numpy import *
from sklearn import linear_model
from sklearn import metrics

def data_distr(filename):
    '''
    find data distribution s.t. choose base classifiers
    '''
    #output = open('distr.txt', 'w')
    x_data = array([])
    label = []
    with open(filename, 'r') as f:
        flag = True
        for line in f:
            xy = line.strip().split(' ')
            y = int(xy[0])
            if y == 0:
                y = -1
            label.append(y)
            xs = xy[1:len(xy)]
            last_num = 0
            data = []
            for x in xs:
                num_val = x.strip().split(':')
                num = int(num_val[0])
                val = float(num_val[1])
                for i in range(num - last_num - 1):
                    data.append(0)
                data.append(val)
                last_num = num
            for i in range(57 - last_num):
                data.append(0)
            if flag:
                x_data = hstack((x_data, array(data)))
                flag = False
            else:
                x_data = vstack((x_data, array(data)))
    label = array(label)

    return (x_data, label)

class AdaBoost:
    '''
    use logistic regreesion to build base classifier
    but the performance is bad
    '''
    def __init__(self, data_tuple, T):
        # number of iteration 
        self.T = T
        # sample size
        self.m = data_tuple[0].shape[0]
        # number of attributes
        self.n = data_tuple[0].shape[1]
        # initial distribution weights
        self.D = ones(self.m) / self.m

        self.__base_classifier(data_tuple)
        #self.__train()

    def __base_classifier(self, data_tuple):
        # sample data and label
        sample = data_tuple[0]
        label = data_tuple[1]
        
        reg = linear_model.LogisticRegression()
        self.hypo_set = [] 
        self.alpha = []

        for t in range(self.T):
            best_index = -1
            min_error = 1 
            threshold = 0
            for i in range(self.n):
                data = transpose(atleast_2d(sample[:,i]))
                reg.fit(data, label)
                thres = -1 * reg.intercept_ / reg.coef_
                pred = reg.predict(data)
                dist = abs(pred - label)
                error = dot(dist, self.D) / 2
                if error < min_error:
                    min_error = error
                    best_index = i
                    threshold = thres 

            hypo = lambda x: int(x[best_index] > threshold) * 2 - 1
            self.hypo_set.append(hypo)
            alpha_t = 0.5 * log((1-min_error)/min_error)
            self.alpha.append(alpha_t)
            Z_t = 2 * sqrt(min_error*(1-min_error))
            for i in range(self.m):
                h_t_i = hypo(sample[i])
                self.D[i] = self.D[i] * exp(-alpha_t * label[i] * h_t_i) / Z_t

    def test(self, test_data_tuple):
        x_data = test_data_tuple[0]
        y_label = test_data_tuple[1]
        acc = 0
        n_test = len(y_label)
        for i in range(n_test):
            x = x_data[i]
            y = y_label[i]
            h_s = array([self.hypo_set[s](x) for s in range(self.T)])
            f = (h_s * self.alpha).sum()
            if f * y > 0:
                acc += 1
        print "Accuracy = %f" % (1.0 * acc / n_test)

def adaboost(train, test):
    '''
    use adaboost classifier in sklearn 
    I am desperate about my own adaboost
    and I don't want to write a decision tree
    '''
    from sklearn.ensemble import AdaBoostClassifier
    from sklearn.tree import DecisionTreeClassifier
    from sklearn.datasets import make_gaussian_quantiles

    training_data = data_distr(train)
    testing_data = data_distr(test)
    for t in range(1, 1000):
        abdt = AdaBoostClassifier(DecisionTreeClassifier(max_depth=4), algorithm="SAMME", n_estimators=t)
        abdt.fit(training_data[0], training_data[1])
        pred = abdt.predict(testing_data[0])
        n = len(pred)
        acc = 0
        for i in range(n):
            if pred[i] * testing_data[1][i] > 0:
                acc += 1
        print 1.0 * acc / n

if __name__ == '__main__': 
    #k = 1
    #ab = AdaBoost(data_distr('xaa'), 10 ** k)
    #ab.test(data_distr('xab'))

    adaboost('xaa', 'xab')
