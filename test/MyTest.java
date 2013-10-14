class Factorial {
	public static void main(String[] a) {
		System.out.println(new Fac().ComputeFac(10));
	}
}

class Fac {
	int check;
	boolean flag;
	public int ComputeFac(int num) {
		int num_aux;
		if (num < 10)
			num_aux = 1;
		else
			num_aux = num * (this.ComputeFac(num - 1));
		num = 1 + 2 * 3 + 5 * 7;

		return num_aux;
	}
	public int fac(){
		int num_aux;
		
		return 1;
	}
}
