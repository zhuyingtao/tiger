//==============Test Final============
//1 . Final in class
//2 . Final in argument
//3 . Final in variable  //Here not distinguish field or local,although it is in Java
//4 . Final in method   //Not include 

class Sum {
	public static void main(String[] a) {
		System.out.println(new Doit().doit(20));
	}
}

class Doit {
	public int doit(final int n) { // final in argument;
		final int sum; // final in local;
		int i;
		System.out.println(sum);// error: not initilized;
		sum = 10;
		sum = 20; // error:can only be initialized once;
		n = 10; // error: final in argument cannot be changed;
		i = 0;
		sum = 0;
		while (i < n) {
			sum = sum + i;
			i = i + 1;
		}
		return sum;
	}
}

final class A { // final in class
}

class B extends A { // error:can not extend a final class
}