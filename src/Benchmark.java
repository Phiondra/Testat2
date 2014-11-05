interface Measurement {
	Object start();
}
public class Benchmark {
	public static Object measure(Measurement m){
		long start = System.currentTimeMillis();
		Object ret = m.start();
		System.out.println(System.currentTimeMillis() - start);
		return ret;
	}
}
