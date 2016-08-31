package symbolic;
@SuppressWarnings("ALL")
public class NumberExample {
	
	private int value = 0;
	private int[] array = new int[1];
	
	public int run(int val) {

		int alpha = val + 5;
		value = alpha - 3;
		array[0] = val;
		array[0] = value;

		if (alpha == 10) {
			return 0;
		} else {
			throw new RuntimeException("error");
		}
	}

	public static void main(String[] args) {
		NumberExample num = new NumberExample();
		num.run(1);
	}
	
}