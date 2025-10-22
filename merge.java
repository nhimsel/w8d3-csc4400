class Main {
    public static void main(String[] args) {
        int arrsize;
        try {
            arrsize=Integer.parseInt(args[0]);
        }
        catch (Exception e) {
            arrsize = 100000;
        }
        int[] arr = RandomizedArray(arrsize, -10000000, 10000000); 

        int[] mergearr = arr.clone();
        int[] quickarr = arr.clone();
        int[] shellarr = arr.clone();
        int[] bubarr = arr.clone();
        int[] selarr = arr.clone();
        int[] insarr = arr.clone();

        long mergestart = System.currentTimeMillis();
        Sort.Merge(mergearr);
        long mergefin = System.currentTimeMillis();

        long mergetime = mergefin-mergestart;
        System.out.println("algorithm: merge\ttime: "+mergetime+"ms\tarraysize:"+arrsize);

        long quickstart = System.currentTimeMillis();
        Sort.Quick(quickarr);
        long quickfin = System.currentTimeMillis();

        long quicktime = quickfin-quickstart;
        System.out.println("algorithm: quick\ttime: "+quicktime+"ms\tarraysize:"+arrsize);

        long shellstart = System.currentTimeMillis();
        Sort.Shell(shellarr);
        long shellfin = System.currentTimeMillis();

        long shelltime = shellfin-shellstart;
        System.out.println("algorithm: shell\ttime: "+shelltime+"ms\tarraysize:"+arrsize);

        long insstart = System.currentTimeMillis();
        Sort.Insertion(insarr);
        long insfin = System.currentTimeMillis();

        long instime = insfin-insstart;
        System.out.println("algorithm: insertion\ttime: "+instime+"ms\tarraysize:"+arrsize);

        long selstart = System.currentTimeMillis();
        Sort.Selection(selarr);
        long selfin = System.currentTimeMillis();

        long seltime = selfin-selstart;
        System.out.println("algorithm: selection\ttime: "+seltime+"ms\tarraysize:"+arrsize);

        long bubstart = System.currentTimeMillis();
        Sort.Bubble(bubarr);
        long bubfin = System.currentTimeMillis();

        long bubtime = bubfin-bubstart;
        System.out.println("algorithm: bubble\ttime: "+bubtime+"ms\tarraysize:"+arrsize);
    }

    private static int[] RandomizedArray(int size, int start, int end) {
        if (size > (end-start+1)) {
            System.out.println("there are not enough unique values between "+start+
                    " and "+end+" to get "+size+" unique vlaues.");
            int[] nullarr = {};
            return nullarr;
        }

        int[] arr = new int[end-start+1];

        for (int i=0; i<arr.length; i++) {
            arr[i] = start+i;
        }

        for (int i=0; i<arr.length-1; i++) {
            int tval = arr[i];
            int tindex = (int) (Math.random() * (arr.length));
            arr[i] = arr[tindex];
            arr[tindex] = tval;
        }
        int[] result = new int[size];
        for (int i=0; i<size; i++) {
            result[i] = arr[i];
        }
        return result;
    }
}

class Sort {

    static int[] Merge(int[] arr) {
        int[] tmp = new int[arr.length];
        MergeSlave(arr, tmp, 0, arr.length);
        return arr;
    }

    private static void MergeSlave(int[] arr, int[] tmparr, int lo, int hi) {
        int mid=(lo+hi)/2;
        if(lo+1>=hi) return;

        //partition
        MergeSlave(arr,tmparr,lo,mid);
        MergeSlave(arr,tmparr,mid,hi);

        //merge
        int c=lo,l=lo,h=mid;
        while (l<mid && h<hi) {
            //compare and fill accordingly
            if (arr[l]<arr[h]) tmparr[c++]=arr[l++];
            else tmparr[c++]=arr[h++];
        }
        //fill with lo vals if out of hi
        while (l<mid) tmparr[c++]=arr[l++];

        //fill with hi vals if out of lo
        while (h<hi) tmparr[c++]=arr[h++];

        //insert temparr into main arr
        for (int i=lo; i<hi; i++)
            arr[i]=tmparr[i];

        return;
    }

    static void Quick(int[] arr, int lo, int hi) {
        //don't run if only 1 value
        if (hi<=lo)
            return;

        int olo=lo,ohi=hi;
        int piv = arr[lo];
        while (lo<=hi) {
            while (arr[lo]<piv) 
                //while val lower than piv, increase lo
                lo++;
            while (arr[hi]>piv)
                //while val higher than piv, lower hi
                hi--;
            if(lo>hi)
                //we've checked the whole arr
                break;
            else{
                //swap
                int tmp=arr[lo];
                arr[lo]=arr[hi];
                arr[hi]=tmp;
                lo++;
                hi--;
            }
        }
        //call recursively on each partition
        Quick(arr,olo,hi);
        Quick(arr,lo,ohi);
        return;
    }

    //makes life a bit easier. quicksorts whole arr
    static int[] Quick(int[] arr) {
        Quick(arr, 0, arr.length-1);
        return arr;
    }

    static int[] Shell(int[] arr, int[] intervals) {
        //check that the intervals are legal
        if (intervals[intervals.length-1]!=1) {
            System.out.println("the intervals do not end in 1. bailing");
            return arr;
        }
        else {
            for (int i=0; i<intervals.length-1; i++) {
                if (intervals[i]<=intervals[i+1]) {
                    System.out.println("the intervals are not in descending order. bailing");
                    return arr;
                }
            }
        }

        //loop through all intervals
        for (int i=0; i<intervals.length; i++) {
            int offset = 0;
            while (offset < intervals[i]) {
                // make the temp arr
                int tmpsize = arr.length / intervals[i];
                //adjust size if there is one more possible value
                if ((arr.length)>tmpsize*intervals[i]+offset) tmpsize++; 
                int[] tmp = new int[tmpsize];
                //fill tmp
                for (int j=0; j<tmpsize; j++)
                    tmp[j]=arr[intervals[i]*j+offset];
                //sort the tmp arr
                Insertion(tmp);
                //fill the sorted vals back into arr
                for (int j=0; j<tmpsize; j++)
                    arr[intervals[i]*j+offset]=tmp[j];
                offset++;
            }
        }
        return arr;
    }

    static int[] Shell(int[] arr) {
        int n = 0; 
        //determine max interval
        while (Math.pow(2.0, (double)++n)<arr.length);
        int[] intervals = new int[n--];
        //fill interval arr
        for (int i=0; i<=n; i++)
            intervals[i]=(int)Math.pow(2.0,(double)(n-i));
        //shell sort with the intervals
        Shell(arr, intervals);
        return arr;
    }

    static int[] Insertion(int[] arr) {
        int key, tpos;
        for (int i=1; i<arr.length; i++) {
            key = arr[i];
            for (tpos=i-1; tpos>=0; tpos--) {
                if (arr[tpos]>key) {
                   arr[tpos+1]=arr[tpos];
                }
                else break;
            }
            arr[tpos+1] = key;
        }
        return arr;
    }

    static int[] Selection(int[] arr) {
        int mindex, tmp;
        for (int pos=0; pos<arr.length-1; pos++) {
            mindex=pos;
            for (int i=pos+1; i<arr.length; i++) {
                if (arr[i]<arr[mindex]) mindex=i;
            }
            tmp = arr[mindex];
            arr[mindex]=arr[pos];
            arr[pos]=tmp;
        }
        return arr;
    }

    static int[] Bubble(int[] arr) {
        int tmp;
        for (int i=0; i<arr.length-1; i++) {
            for (int j=0; j<arr.length-1-i; j++) {
                if (arr[j]>arr[j+1]) {
                    tmp = arr[j+1];
                    arr[j+1]=arr[j];
                    arr[j]=tmp;
                }
            }
        }
        return arr;
    }
}
