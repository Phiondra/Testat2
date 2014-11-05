import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;


public class TestatTest {
	
	private List<Module> getAllModules(){
		List<Module> modules = new ArrayList<>();
		try (CatalogueReader reader = new CatalogueReader("StudyCatalogue.txt")) {
			String[] names;
			
			while ((names = reader.readNextLine()) != null) {
				Module module = Module.create(names);
				modules.add(module);
			}
		} catch (Exception e) {
			fail("Exception");
		}
		return modules;
	}
	
//	@Test
//	public void testReadFile(){
//		List<Module> modules =  getAllModules();
//		if(modules == null) fail("no modules");
//		assertEquals("DB1", modules.get(0).name);	
//		assertEquals("OO", modules.get(0).deps.get(0).name);
//		assertEquals("DB1", modules.get(1).deps.get(0).name);
//		assertEquals("OO", modules.get(1).deps.get(0).deps.get(0).name);
//		assertEquals("UI1", modules.get(8).deps.get(2).name);
//	}	
//	
//	@Test
//	public void testTermPlacement(){
//		List<Module> modules =  getAllModules();
//		List<List<Module>> allTerms = new ArrayList<List<Module>>();
//		
//		if(modules == null) fail("no modules");
//		List<Module> visited = new ArrayList<Module>();
//		
//		for(int term = 1; term < 6; term++){
//			List<Module> termMods = Module.getDependentOn(visited);
//			visited.addAll(termMods);
//			allTerms.add(termMods);
//		}
//		
//		assertEquals(5, allTerms.size());
//		ArrayList<String> termOneModuleNames = new ArrayList<String>();
//		for(Module m : allTerms.get(0)){
//			termOneModuleNames.add(m.name);
//		}
//		assert(termOneModuleNames.contains("OO"));
//				
//	}

	@Test
	public void testPerformance(){
		Benchmark.measure(() -> {
		try (CatalogueReader reader = new CatalogueReader("LargeCatalogue.txt")) {
			String[] names;
			while ((names = reader.readNextLine()) != null) {
			  Module m = Module.create(names);
			  if(names.length == 1){
				  Module.withoutDeps.add(m);
			  }
			}
		} catch (Exception e) {
			fail("Exception");
		}
			List<List<Module>> allTerms = new ArrayList<List<Module>>();
			//List<Module> visited = new ArrayList<Module>();
			assertEquals(1000, Module.all.size());
			while(Module.all.size() > 0){
				List<Module> termMods = new ArrayList<>();
				allTerms.add(termMods);
				for(Module m : Module.withoutDeps){
					termMods.add(m);
					Module.all.remove(m.name);
				}
				Module.withoutDeps.clear();
				for(Module m : termMods){
					for(Module subM: m.deps){
						if((--subM.unresolvedDeps) == 0){
							Module.withoutDeps.add(subM);
						}
					}
				}
			}
			//assertEquals(1000, Module.withoutDeps.size());

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
			
			return null;
		});
	}
}
