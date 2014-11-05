import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;


public class TestatTest {
	
	private List<Module> getAllModules(String filename, DepMapper<Module> mapper){
		List<Module> modules = new ArrayList<>();
		try (CatalogueReader reader = new CatalogueReader(filename)) {
			String[] names;
			
			while ((names = reader.readNextLine()) != null) {
				
				Module m = new Module(names[0]);
				mapper.add(m);
				for (int i = 1; i < names.length; i++) {
					Module module = new Module(names[i]);
					mapper.addDep(module, m);
				}
			}
		} catch (Exception e) {
			fail("Exception");
		}
		return modules;
	}

	@Test
	public void testPerformance(){
		Benchmark.measure(() -> {
			DepMapper<Module> mapper = new DepMapper<Module>();
			getAllModules("LargeCatalogue.txt", mapper);
			try {
				List<List<Module>> allTerms = mapper.getSteps();

				assertEquals(0, mapper.size());
	
				ArrayList<String> termOneModuleNames = new ArrayList<String>();
				for(Module m : allTerms.get(0)){
					termOneModuleNames.add(m.name);
				}
				assert(termOneModuleNames.contains("AAA"));
				assert(termOneModuleNames.contains("AAG"));
				assertFalse(termOneModuleNames.contains("AAH"));
				int sum = 0;
				for(List<Module> term: allTerms){
					sum += term.size();
				}
				assertEquals(1000, sum);
			} catch (Exception e) {
				e.printStackTrace();
				fail("exception");
			}
			return null;
		});
	}
}
