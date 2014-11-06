import static org.junit.Assert.*;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.junit.Test;

import util.DepMapper;


public class TestatTest {
	
	private List<Module> getAllModules(String filename, DepMapper<Module> mapper){
		List<Module> modules = new ArrayList<>();
		try (CatalogueReader reader = new CatalogueReader(filename)) {
			String[] names;
			while ((names = reader.readNextLine()) != null) {
				Module m = mapper.add(new Module(names[0]));
				for (int i = 1; i < names.length; i++) {
					mapper.addDep(new Module(names[i]), m);
				}			
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception");
		}
		return modules;
	}

	@Test
	public void testPerformance(){
		DepMapper<Module> mapper = new DepMapper<Module>();
		Benchmark.measure(() -> {
			getAllModules("LargeCatalogue.txt", mapper);
			//getAllModules("keysOnly", mapper);
			return null;
		});
		assertEquals(12492, mapper.size());
				@SuppressWarnings("unchecked")
				List<List<Module>> allTerms = (List<List<Module>>) Benchmark.measure(() -> {
					try {
						return  mapper.getSteps();
					} catch (Exception e) {
						e.printStackTrace();
						fail("exception");
						return null;
					}
				});
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
				assertEquals(12492, sum);
	}
}
