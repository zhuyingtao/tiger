class Factorial {
	public static void main(String[] a) {
		System.out.println(new Fac().ComputeFac(10));
	}
}

class Fac {
	int check;
	boolean flag;

	boolean isthis;

	public int ComputeFac(int num) {
		int num_aux;
		boolean come;
		int[] array;
		boolean barray;
		Test tt;
		if (num < 10)
			num_aux = 1;
		else
			num_aux = num * (this.ComputeFac(num - 1));
		// num = 1 + 2 * 3 + 5 * 7;
		num = (1 + 2) * 3;
		num = 1 + (2 * 3);
		num = 1 + 2 * 3;
		num = 1 + 2 * 3 + 5 * 4 + 6 * 2;
		num = (1 + 2) * (2 * 3);
		// num = 1 * 8 + 4;
		// num = 4 + 1 * 8;

		// come=!num_aux;
		// tt = new Test();
		// num_aux = tt.test(barray);
		// num_aux[10] = 1;
		// array[come] = 10;
		// num_aux = come;
		// num_aux = come + 1;
		// lesss = num_aux + 1;

		return num_aux;
	}

	public int fac() {
		int num_aux;

		return 1;
	}
}

class Test {
	public int test(int number) {
		System.out.println(number);
		return number;
	}
}
