// x = ArrayList of symbolic integers with 
// concrete length
for (int i = 0; i < x.size(); i++) {
  // Begin region for static unrolling
  if (x.get(i) < 0) sum += -1;
  else if (x.get(i) > 0) sum += 1;
  // End region for static unrolling
}
if (sum < 0) System.out.println("neg");
else if (sum > 0) System.out.println("pos");
else System.out.println("bug");
