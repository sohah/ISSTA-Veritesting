public int countWords(int x1, int x2, int x3, int x4, int x5, int x6) {

        ArrayList<Integer> numberList = new ArrayList<Integer>(Arrays.asList(x1, x2, x3, x4, x5, x6));

        int wordCount = 0;
        boolean inWord;

        if (numberList.size() > 0) {
            int firstEement = numberList.get(0);

            if (firstEement == 0)
                inWord = false;
            else
                inWord = true;

            for (int i = 0; i < numberList.size(); i++) {
                if (inWord) {
                    if (numberList.get(i) == 0) { //0 means there is a whitespace
                        ++wordCount;
                        inWord = false;
                    }
                } else {
                    if (numberList.get(i) != 0) { // non whitespace.
                        inWord = true;
                    }
                }
            }
        }
        System.out.println("Number of words is:" + wordCount);
        return wordCount;
    }
