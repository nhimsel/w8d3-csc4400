class Main {
    public static void main(String[] args) {
        int arrsize = 10;
        int[] arr = RandomizedArray(arrsize, -5, 10);
        arr=Sort.Merge(arr);
        System.out.println("the array was sorted correctly:"+checkSort(arr));
    }

    private static int[] RandomizedArray(int size, int start, int end) {
        if (size > end-start+1) {
            System.out.println("there are enough unique values between "+start+
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

    private static boolean checkSort(int[] arr) {
        for (int i=1; i<arr.length; i++)
            if (arr[i-1] > arr[i]) return false;
        return true;
    }
}

class Sort {
    static int[] Merge(int[] arr) {
        MergeSlave(arr, 0, arr.length);
        return arr;
    }

    private static void MergeSlave(int[] arr, int lo, int hi) {
        int mid=(lo+hi)/2;
        if(lo+1>=hi) return;

        //partition
        MergeSlave(arr,lo,mid);
        MergeSlave(arr,mid,hi);

        //merge
        int[] tmparr = new int[hi-lo];
        int c=0,l=lo,h=mid;
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
        for (int i=0; i<tmparr.length; i++)
            arr[lo+i]=tmparr[i];

        return;
    }
}
