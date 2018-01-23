for (int i = 0; i < len; i++) {
  if (x[i] < 0) sum += -1;
  else if (x[i] > 0) sum += 1;
  else return;
}
