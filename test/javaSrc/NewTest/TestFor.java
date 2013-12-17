//================Test For==================
//1 . test x++;++x;
class TestFor {
	public static void main(String[] a) {
		System.out.println(new Test().doit(20));
	}
}

class Test {
	public int doit(int n) {
		int sum;
		int i;

		i = 0;
		i++;
		// sum = i++;
		System.out.println(i);
		--i;
		System.out.println(i++);
		sum = ++i;
		System.out.println(++i);
		System.out.println(sum);

		sum = 0;
		while (i < n) {
			sum = sum + i;
			i = i + 1;
		}
		return sum;
	}
}