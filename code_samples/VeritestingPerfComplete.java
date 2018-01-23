public class VeritestingPerfComplete {
	

public void foo(int [] x) {
// x = array of symbolic integers
// len = concrete length of x
int sum = 0;
int len = x.length;
for (int i = 0; i < len; i++) {
  // Begin region for static unrolling
  if (x[i] < 0) sum += -1;
  else if (x[i] > 0) sum += 1;
  else return;
  // End region for static unrolling
}
if (sum < 0) System.out.println("neg");
else if (sum > 0) System.out.println("pos");
else System.out.println("bug");
}

}