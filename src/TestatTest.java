import static org.junit.Assert.*;
import java.util.ArrayList;
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
		int NUM_UNIQUE_ELEMENTS_IN_CATALOGUE = 12492;
		DepMapper<Module> mapper = new DepMapper<Module>();
		Benchmark.measure(() -> {
			getAllModules("LargeCatalogue.txt", mapper);
			return null;
		});
		assertEquals(NUM_UNIQUE_ELEMENTS_IN_CATALOGUE, mapper.size());
				@SuppressWarnings("unchecked")
				List<Module> firstTerm = (List<Module>) Benchmark.measure(() -> {
					try {
						return mapper.next();
					} catch (Exception e) {
						e.printStackTrace();
						fail("exception");
						return null;
					}
				});
				assertEquals(NUM_UNIQUE_ELEMENTS_IN_CATALOGUE - firstTerm.size(), mapper.size());
	
				ArrayList<String> termOneModuleNames = new ArrayList<String>();
				for(Module m : firstTerm){
					termOneModuleNames.add(m.name);
				}
				assertTrue(termOneModuleNames.contains("AAA"));
				assertTrue(termOneModuleNames.contains("AAG"));
				assertFalse(termOneModuleNames.contains("AAH"));
				
				
				@SuppressWarnings("unchecked")
				List<Module> secondTerm = (List<Module>) Benchmark.measure(() -> {
					try {
						return mapper.next();
					} catch (Exception e) {
						e.printStackTrace();
						fail("exception");
						return null;
					}
				});
				ArrayList<String> termTwoModuleNames = new ArrayList<String>();
				for(Module m : secondTerm){
					termTwoModuleNames.add(m.name);
				}
				assertTrue(termTwoModuleNames.contains("AAE"));
				assertTrue(termTwoModuleNames.contains("AAH"));
				assertFalse(termTwoModuleNames.contains("AAA"));
				assertEquals(NUM_UNIQUE_ELEMENTS_IN_CATALOGUE - firstTerm.size() - secondTerm.size(), mapper.size());
				@SuppressWarnings("unchecked")
				List<Module> lastTerm = (List<Module>) Benchmark.measure(() -> {
					try {
						List<Module> term = null;
						while(mapper.hasNext())
							term = mapper.next();
						return term;
					} catch (Exception e) {
						e.printStackTrace();
						fail("exception");
						return null;
					}
				});
				
				ArrayList<String> termLastModuleNames = new ArrayList<String>();
				for(Module m : lastTerm){
					termLastModuleNames.add(m.name);
				}
				assertTrue(termLastModuleNames.contains("FML"));
				assertTrue(termLastModuleNames.contains("GML"));
				assertFalse(termLastModuleNames.contains("AAE"));
				
				assertEquals(0, mapper.size());
	}
}
