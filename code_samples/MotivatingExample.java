
import com.ibm.wala.util.intset.Bits;
import gov.nasa.jpf.symbc.Debug;

import java.lang.ref.Reference;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Consumer;
import java.util.function.Function;

public class VeritestingPerf {

    public int countBs(int x) {
        int len = 200;
        List<Integer> list = new ArrayList<>(len);
        for (int i = 0; i < len; i++)
            list.add(Debug.makeSymbolicInteger("i" + i));
        int count = 0;
        for (int i = 0; i < list.size(); i++) {
            if (i%2 == 0 && list.get(i) == 42)
                count++;
            System.out.println("iteration");
        }
        if (count == 75) {
            System.out.println("bug");
        }
        return count;
    }

    public static void main(String[] args) {
        (new VeritestingPerf()).countBs('x');
    }

};


