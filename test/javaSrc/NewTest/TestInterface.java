//=====================TestInterface====================

class TestInterface {
	public static void main(String[] a) {
		System.out.println(20);
	}
}

class B implements C, D {
	int a = 10;
	int b;
	int c;

	public int setA() {
		return 1;
	}
}

interface C extends D {
	public int setC();
}

interface D {
	int d = 10;
	int e;

	public boolean setD();
}