import java.util.Comparator;
import java.util.Map;

class ValueComparator implements Comparator<Integer> {
 
    Map<Integer, Integer> map;
 
    public ValueComparator(Map<Integer, Integer> base) {
        this.map = base;
    }

	@Override
	public int compare(Integer a, Integer b) {
		if (map.get(a) >= map.get(b)) {
            return -1;
        } else {
            return 1;
        } // returning 0 would merge keys 
	}
}