import numpy as np
import pandas as pd
from scipy.spatial import distance

class node:
    '''
    tree-like node, each contains a list of data indices and level
    '''
    def __init__(self, level, index=None, left=None, right=None):
        self.level = level
        self.left = left
        self.right = right

        self.index = []
        if index is not None:
            self.index.append(index)

        if left is not None:
            for i in left.get_index():
                self.index.append(i)

        if right is not None:
            for i in right.get_index():
                self.index.append(i)

    def get_level(self):
        return self.level

    def get_index(self):
        return self.index

    def get_children(self):
        return (self.left, self.right)

class agnes:
    def __init__(self, data, k):
        self.data = np.array(data)
        self.k = k
        self.nodes = []
        for i in xrange(len(data)):
            n = node(0, i)
            self.nodes.append(n)

        #self.dist_mat = np.loadtxt('dist_mat.txt', delimiter=',')
        n = len(data)
        self.dist_mat = np.zeros((n,n))
        for i in range(0, n):
            self.dist_mat[i][i] = 999999
            for j in range(i+1, n):
                dist = distance.euclidean(self.data[i], self.data[j])
                self.dist_mat[i][j] = dist
                self.dist_mat[j][i] = dist
        #np.savetxt('dist_mat.txt', self.dist_mat, delimiter=',', fmt='%1.4f')

    def cluster(self):
        m = 1
        n = len(self.data)
        while m < n:
            print m
            # get closest two cluster indices
            (r, s) = self._least_dist()

            # create new node(cluster) and delete one old node
            # always delete s and replace r with parent
            parent = node(m, None, self.nodes[r], self.nodes[s])
            del self.nodes[s]
            self.nodes[r] = parent

            self.dist_mat = np.delete(self.dist_mat, (s), axis=0)
            self.dist_mat = np.delete(self.dist_mat, (s), axis=1)

            # calculate distance between new cluster and other clusters
            for i in xrange(len(self.dist_mat)):
                if i != r:
                    dist = self._distance(parent, self.nodes[i])
                    self.dist_mat[i][r] = dist
                    self.dist_mat[r][i] = dist

            m += 1

    def _least_dist(self):
        '''
        find two clusters that are the most close
        '''
        pair = np.unravel_index(self.dist_mat.argmin(), self.dist_mat.shape)
        '''
        n = len(self.dist_mat)
        pair = (-1, -1)
        min_dist = 999999
        for i in range(0, n):
            for j in range(i+1, n):
                dist = self.dist_mat[i][j]
                if dist < min_dist:
                    min_dist = dist
                    pair = (i, j)
        '''

        return pair

    def _distance(self, c1, c2):
        '''
        return distance between two clusters c1 and c2

        distance is defined as the min distance between point 
        in c1 and and one in c2
        '''
        min_dist = 999999
        for i in c1.get_index():
            for j in c2.get_index():
                dist = distance.euclidean(self.data[i], self.data[j])
                if dist < min_dist:
                    min_dist = dist

        return min_dist

    def sihouette(self):
        '''
        sihouette score to evaluate the result
        '''
        from sklearn.metrics import silhouette_score

        n = len(self.data)
        level = np.zeros(n, np.uint32)
        nodes = self.nodes
        m = 1
        while m < self.k:
            lvl = n - 1 - m
            for i in xrange(len(nodes)):
                node = nodes[i]
                (left, right) = node.get_children()
                if left.get_level() == lvl or right.get_level() == lvl:
                    if left.get_level() == m:
                        level[left.get_index()] = m
                    else:
                        level[right.get_index()] = m
                    del nodes[i]
                    nodes.append(left)
                    nodes.append(right)

                # leaf node
                if left.get_level() + right.get_level() == 0:
                    del nodes[i]

            m += 1

        score = silhouette_score(self.data, level)
        return score

def read_file(name):
    df = pd.read_csv('votes-train.csv', skiprows=0)
    data = np.array(df, np.float32)
    return data[:,1:]

if __name__ == '__main__':
    data = read_file('votes-train.csv')
    from sklearn.preprocessing import MinMaxScaler
    data = MinMaxScaler(feature_range=(-1, 1)).fit_transform(data)

    k = 2
    agnes_ = agnes(data, 2)
    agnes_.cluster()
    print agnes_.sihouette()
