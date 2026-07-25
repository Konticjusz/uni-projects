public class Main {
    public static void main(String[] args) {
        MySet<Integer> setA = new MySet<Integer>(new Integer[]{1, 2, 3, 4});
        MySet<Integer> setB = new MySet<Integer>(new Integer[]{1, 2, 3});
        System.out.println(setA);
        System.out.println(setB);
        System.out.println(setB.difference(setB));
    }
}