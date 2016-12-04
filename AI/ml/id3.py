import numpy as np
import pandas as pd
from scipy.spatial import distance
from Queue import Queue
from sklearn.preprocessing import MinMaxScaler

class Node:
    def __init__(self, index, remaining, entropy, label):
        self.attr = None # which attribute/feature
        self.attr_val = None # threshold
        self.left = None # left child
        self.right = None # right child
        self.remaining = remaining # remaining attributes
        self.entropy = entropy
        self.index = index # data to be split
        self.label = label

    def is_leaf(self):
        return self.left is None and self.right is None

    def set_criteria(self, attr, attr_val):
        self.attr = attr
        self.attr_val = attr_val

    def expand(self, left, right):
        '''
        expand this node by adding left and right subtrees
        '''
        self.left = left
        self.right = right

class ID3:
    def __init__(self, data):
        self.data = np.array(data)
        self.root = None

    def train(self):
        q = Queue()
        index = np.arange(len(self.data))
        remaining = np.arange(1, len(self.data[0]))
        (entropy, major) = self._entropy(self.data)
        node = Node(index, remaining, entropy, major)
        q.put(node)
        self.root = node

        while not q.empty():
            node = q.get()
            s = self.data[node.index]
            #print node.entropy, len(s[np.where(s[:,0] == 1)]), len(s[np.where(s[:,0] == 0)])
            # stop criteria if fully shattered or no attributes 
            # left or no sample left
            if node.entropy == 0 or \
                    len(node.remaining) == 0 or \
                    len(node.index) == 0:
                # this is a leaf node
                continue 

            ratio = 1.0 * len(s[np.where(s[:,0] == 1)]) / len(s[np.where(s[:,0] == 0)])
            if ratio > 10 or ratio < 0.1:
                continue

            (attr, attr_val) = self._best_split(node)
            node.set_criteria(attr, attr_val)

            remaining = node.remaining
            for i in range(len(remaining)):
                if remaining[i] == attr:
                    remaining = np.delete(remaining, i)
                    break

            # grow
            neg = np.where(self.data[:,attr]<=attr_val)[0]
            neg = np.intersect1d(neg, node.index)
            (entropy, major) = self._entropy(self.data[neg])
            left = Node(neg, remaining, entropy, major)

            pos = np.where(self.data[:,attr]>attr_val)[0]
            pos = np.intersect1d(pos, node.index)
            (entropy, major) = self._entropy(self.data[pos])
            right = Node(pos, remaining, entropy, major)
            node.expand(left, right)

            q.put(left)
            q.put(right)


    def _best_split(self, node):
        best_gain = 0
        best_attr = None
        sample = self.data[node.index]
        info = node.entropy

        for col in node.remaining:
            # for each column, check each 10 percentage points
            # and select the one with max info_gain as splitting
            # criteria
            sample_a = sample[:,[0,col]]
            max_val = np.max(sample_a[:,1])
            min_val = np.min(sample_a[:,1])

            best_val = 0
            for i in np.arange(1,10)/10.0:
                split_val = self._percentile(max_val, min_val, i)
                info_a = self._info_a(sample_a, split_val)
                gain = info - info_a

                if gain > best_gain:
                    best_gain = gain
                    best_attr = (col, split_val)

        return best_attr

    def _info_a(self, sample, attr_val):
        n = len(sample)
        pos = sample[np.where(sample[:,1]>attr_val)]
        neg = sample[np.where(sample[:,1]<=attr_val)]

        info_a = 1.0 * len(pos) / n * self._entropy(pos)[0] + \
                1.0 * len(neg) / n * self._entropy(neg)[0]

        return info_a

    def _percentile(self, max_val, min_val, percent):
        return (max_val - min_val) * percent + min_val

    def _entropy(self, sample):
        '''
        return entropy and major label
        '''
        proba = []
        sz = len(sample)

        # percentage for each label
        for label in np.unique(sample[:,0]):
            n = len(sample[np.where(sample[:,0]==label)])
            proba.append(1.0*n/sz)

        # entropy value
        ent = 0
        for p in proba:
            if p != 0:
                ent -= p * np.log2(p)

        return (ent, np.argmax(proba))


    def test(self, data):
        pred = []
        for x in data:
            node = self.root
            while not node.is_leaf():
                '''
                if len(node.index) < 10:
                    break
                '''

                if x[node.attr] > node.attr_val:
                    node = node.right
                else:
                    node = node.left
            pred.append(node.label)

        # evaluate accuracy
        acc = 0
        for i in xrange(len(pred)):
            if pred[i] == self.data[i][0]:
                acc += 1
        print 'accuracy = %f' % (1.0 * acc / len(pred))

def read_file(name):
    df = pd.read_csv(name, skiprows=0)
    data = np.array(df, np.float32)
    return data

if __name__ == '__main__':
    data = read_file('votes-train.csv')

    test_set = read_file('votes-test.csv')
    id3 = ID3(data)
    id3.train()
    id3.test(test_set)
