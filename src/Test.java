public class Test {
	public static void main(String[] args) throws Exception {
		// Sample code to illustrate how the CatalogueReader could be used:
		try (CatalogueReader reader = new CatalogueReader("StudyCatalogue.txt")) {
			String[] names;
			while ((names = reader.readNextLine()) != null) {
				System.out.print("Module: " + names[0] + " Prerequisite: ");
				for (int i = 1; i < names.length; i++) {
					System.out.print(names[i] + " ");
				}
				System.out.println();
			}
		}
	}
}
