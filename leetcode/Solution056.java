/**
 * Definition for an interval.
 * public class Interval {
 *     int start;
 *     int end;
 *     Interval() { start = 0; end = 0; }
 *     Interval(int s, int e) { start = s; end = e; }
 * }
 */
public class Solution {
    public List<Interval> merge(List<Interval> intervals) {
        int n = intervals.size();
        if (n <= 1)
            return intervals;
            
        PriorityQueue<Interval> pq = new PriorityQueue<Interval>(n, 
            new Comparator<Interval>() {
                @Override
                public int compare(Interval i, Interval j) {
                    return i.start - j.start;
                }
            }
        );
        
        for (int i = 0; i < n; i++) {
            pq.offer(intervals.get(i));
        }
        
        List<Interval> ret = new ArrayList<Interval>();
        Interval pop = pq.poll();
        int lastStart = pop.start;
        int lastEnd = pop.end;
        while (!pq.isEmpty()) {
            pop = pq.poll();
            if (lastEnd < pop.start) {
                ret.add(new Interval(lastStart, lastEnd));
                lastStart = pop.start;
                lastEnd = pop.end;
            } else {
                lastEnd = lastEnd > pop.end ? lastEnd : pop.end;
            }
        }
        ret.add(new Interval(lastStart, lastEnd));
        
        return ret;
    }
}
