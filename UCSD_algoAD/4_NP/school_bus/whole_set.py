'''
sample code to calculate all subset 
1 \in S \subset {1, ..., n} with size s.

Suppose there are n numbers, i.e. whole set 
with size n (in my code, I use @bit).

This is the loop in exact TSP problem using 
DP, loop time is O(2^n).
'''

def where0(S, bit):
    array = []
    max1 = 0
    i = 0
    while S != 0:
        if S & 1 is 0:
            array.append(i)
        else:
            max1 = i

        i += 1
        S = S >> 1

    while i < bit:
        array.append(i)
        i += 1

    return [x for x in array if x > max1]

def set(S, n, bit):
    if n == 0:
        print S
        # calculate C(S, i) = min(C(S, i), C(S-{i},j) + d_ji) here
        # for all i != 1 and j != i

    array = where0(S, bit)
    for i in array:
        set(S ^ (1 << i), n-1, bit)

if __name__ == "__main__":
    set(1, 2, 5) # start with S = 1, excluding 1 in the least 
                 # significant bit
