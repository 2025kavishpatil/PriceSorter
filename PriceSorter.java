import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class PriceSorter {

    // ---------- Merge Sort ----------
    static void mergeSort(double[] a, int left, int right) {
        if (left >= right) return;
        int mid = left + (right - left) / 2;
        mergeSort(a, left, mid);
        mergeSort(a, mid + 1, right);
        merge(a, left, mid, right);
    }

    static void merge(double[] a, int left, int mid, int right) {
        double[] L = Arrays.copyOfRange(a, left, mid + 1);
        double[] R = Arrays.copyOfRange(a, mid + 1, right + 1);
        int i = 0, j = 0, k = left;
        while (i < L.length && j < R.length) {
            if (L[i] <= R[j]) a[k++] = L[i++];
            else a[k++] = R[j++];
        }
        while (i < L.length) a[k++] = L[i++];
        while (j < R.length) a[k++] = R[j++];
    }

    // ---------- Quick Sort (Lomuto partition, random pivot) ----------
    static final Random rand = new Random();

    static void quickSort(double[] a, int low, int high) {
        if (low < high) {
            int p = partition(a, low, high);
            quickSort(a, low, p - 1);
            quickSort(a, p + 1, high);
        }
    }

    static int partition(double[] a, int low, int high) {
        // random pivot avoids the O(n^2) worst case on already sorted data
        int r = low + rand.nextInt(high - low + 1);
        swap(a, r, high);
        double pivot = a[high];
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (a[j] <= pivot) {
                i++;
                swap(a, i, j);
            }
        }
        swap(a, i + 1, high);
        return i + 1;
    }

    static void swap(double[] a, int i, int j) {
        double t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

    // ---------- Display helper (shows first 20 for large arrays) ----------
    static void display(String label, double[] a) {
        StringBuilder sb = new StringBuilder(label + "[");
        int limit = Math.min(a.length, 20);
        for (int i = 0; i < limit; i++) {
            sb.append(String.format("%.2f", a[i]));
            if (i < limit - 1) sb.append(", ");
        }
        if (a.length > limit) sb.append(", ... (" + a.length + " items total)");
        sb.append("]");
        System.out.println(sb);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        double[] prices = null;
        int choice;

        do {
            System.out.println("\n===== PRODUCT PRICE SORTER =====");
            System.out.println("1. Enter prices manually");
            System.out.println("2. Generate random prices");
            System.out.println("3. Sort using Merge Sort");
            System.out.println("4. Sort using Quick Sort");
            System.out.println("5. Compare Merge Sort and Quick Sort");
            System.out.println("6. Display original prices");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");

            while (!sc.hasNextInt()) {
                System.out.print("Invalid input. Enter a number: ");
                sc.next();
            }
            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Number of products: ");
                    int n = sc.nextInt();
                    if (n <= 0) {
                        System.out.println("Number must be positive.");
                        break;
                    }
                    prices = new double[n];
                    for (int i = 0; i < n; i++) {
                        System.out.print("Price of product " + (i + 1) + ": ");
                        prices[i] = sc.nextDouble();
                    }
                    System.out.println("Prices stored.");
                    break;

                case 2:
                    System.out.print("How many prices to generate (e.g. 10000): ");
                    int cnt = sc.nextInt();
                    if (cnt <= 0) {
                        System.out.println("Number must be positive.");
                        break;
                    }
                    prices = new double[cnt];
                    Random r = new Random();
                    for (int i = 0; i < cnt; i++) {
                        prices[i] = Math.round((10 + r.nextDouble() * 99990) * 100) / 100.0;
                    }
                    System.out.println(cnt + " random prices generated.");
                    break;

                case 3:
                case 4:
                    if (prices == null) {
                        System.out.println("Please enter or generate prices first.");
                        break;
                    }
                    double[] copy = prices.clone();
                    long start = System.nanoTime();
                    if (choice == 3) {
                        mergeSort(copy, 0, copy.length - 1);
                        System.out.println("Sorted using Merge Sort:");
                    } else {
                        quickSort(copy, 0, copy.length - 1);
                        System.out.println("Sorted using Quick Sort:");
                    }
                    long time = System.nanoTime() - start;
                    display("Ascending order: ", copy);
                    System.out.printf("Time taken: %.3f ms%n", time / 1_000_000.0);
                    break;

                case 5:
                    if (prices == null) {
                        System.out.println("Please enter or generate prices first.");
                        break;
                    }
                    double[] m = prices.clone();
                    double[] q = prices.clone();

                    long t1 = System.nanoTime();
                    mergeSort(m, 0, m.length - 1);
                    long mergeTime = System.nanoTime() - t1;

                    long t2 = System.nanoTime();
                    quickSort(q, 0, q.length - 1);
                    long quickTime = System.nanoTime() - t2;

                    display("Merge Sort output: ", m);
                    display("Quick Sort output: ", q);
                    System.out.println("Outputs identical: " + Arrays.equals(m, q));
                    System.out.printf("Merge Sort time: %.3f ms%n", mergeTime / 1_000_000.0);
                    System.out.printf("Quick Sort time: %.3f ms%n", quickTime / 1_000_000.0);
                    System.out.println("Faster: " + (mergeTime < quickTime ? "Merge Sort" : "Quick Sort"));
                    break;

                case 6:
                    if (prices == null) System.out.println("No prices entered yet.");
                    else display("Original prices: ", prices);
                    break;

                case 7:
                    System.out.println("Exiting program.");
                    break;

                default:
                    System.out.println("Invalid choice. Try again.");
            }
        } while (choice != 7);

        sc.close();
    }
}
