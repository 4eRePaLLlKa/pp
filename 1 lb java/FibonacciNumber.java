public class FibonacciNumber {
    private int index;  
    private long value; 

    public FibonacciNumber(int index){
        this.index = index;
        this.value = calculateFibonacci(index);
    }

    public int getIndex() {
        return index;
    }

    public long getValue() {
        return value;
    }


    private long calculateFibonacci(int n){
        if (n == 1 || n == 2){
            return 1;
        }
        long a = 1, b = 1;
        for(int i = 3; i <= n; i++){
            long c = a + b;
            a = b;
            b = c;
        }
        return b;
    }

    public boolean isCubePlusOne(){
        if(value < 1) return false;
        long candidate = value - 1;
        long w = Math.round(Math.cbrt(candidate)); 
        return w * w * w == candidate;
    }
}
