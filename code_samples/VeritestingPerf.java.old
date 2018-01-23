import gov.nasa.jpf.symbc.Debug;
public class VeritestingPerf {
  public static void main (String[] args){
	(new VeritestingPerf()).testMe(0,0);
  }
  public void testMe (int x, int y) {
    int i = Debug.makeSymbolicInteger("i");
    int a, b;
    while(i < 1) {
      // Begin region 
      if (x + i < 0 ) a = -1;
      else if (x + i == 0 ) a = 0;
      else a = 1;
      if (y + i < 0 ) b = -1;
      else if (y + i == 0 ) b = 0;
      else b = 1;
      // End region
      i++; 
    }
  }
}
