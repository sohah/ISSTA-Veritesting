// x is symbolic, bound is concrete
public void testSharing(int x, int bound) {
  for(int i=0; i < bound; i++) x = x + x;
  if ( x < -50 || x > 50) ...;
  else ... 
}
