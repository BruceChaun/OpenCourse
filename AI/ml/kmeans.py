import numpy as np
import pandas as pd
from scipy.spatial import distance

class kmeans:
    def __init__(self, data, k):
        self.data = np.array(data)
        self.k = k

    def cluster(self):
        self._init()
        old_center = None
        while not self._converge(old_center):
            old_center = np.copy(self.center)
            self._e_step()
            self._m_step()

    def _init(self):
        index = []
        n = len(self.data)

        # randomly select k points as center
        while len(index) < self.k:
            rand = np.random.randint(0, n)
            while rand in index:
                rand = (rand + 1) % n
            index.append(rand)
        self.center = np.copy(self.data[index])

        self._m_step()

    def _converge(self, old_center):
        '''
        whether k means algorithm is converged
        '''
        if old_center is None:
            return False

        eps = 0.0001
        c_diff = np.absolute(self.center - old_center)
        for diff in c_diff:
            for i in diff:
                if i > eps:
                    return False
        return True

    def _e_step(self):
        '''
        re-estimate the cluster means
        '''
        for i in range(len(self.center)):
            self.center[i] = np.average(self.data[self.label==i], axis=0)

    def _m_step(self):
        '''
        calculate each points belongs to which center
        '''
        label = []
        for d in self.data:
            dist = []
            for c in self.center:
                dist.append(distance.euclidean(d, c))
            label.append(np.argmin(dist))
        self.label = np.array(label)

    def sihouette(self):
        '''
        sihouette score to evaluate the result
        '''
        from sklearn.metrics import silhouette_score

        score = silhouette_score(self.data, self.label)
        return score

def read_file(name):
    df = pd.read_csv('votes-train.csv', skiprows=0)
    data = np.array(df, np.float32)
    return data[:,1:]

if __name__ == '__main__':
    data = read_file('votes-train.csv')
    from sklearn.preprocessing import MinMaxScaler
    data = MinMaxScaler(feature_range=(-1, 1)).fit_transform(data)

    for k in range(2,6):
        km = kmeans(data, k)
        km.cluster()
        score = km.sihouette()
        print k, score
        '''
        2 0.372074
        3 0.296673
        4 0.334923
        5 0.229717
        '''
