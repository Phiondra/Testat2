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
	
	@Test
	public void testReadFile(){
		List<Module> modules =  getAllModules();
		if(modules == null) fail("no modules");
		assertEquals("DB1", modules.get(0).name);	
		assertEquals("OO", modules.get(0).deps.get(0).name);
		assertEquals("DB1", modules.get(1).deps.get(0).name);
		assertEquals("OO", modules.get(1).deps.get(0).deps.get(0).name);
		assertEquals("UI1", modules.get(8).deps.get(2).name);
	}	
	
	@Test
	public void testTermPlacement(){
		List<Module> modules =  getAllModules();
		List<List<Module>> allTerms = new ArrayList<List<Module>>();
		
		if(modules == null) fail("no modules");
		List<Module> visited = new ArrayList<Module>();
		
		for(int term = 1; term < 6; term++){
			List<Module> termMods = Module.getDependentOn(visited);
			visited.addAll(termMods);
			allTerms.add(termMods);
		}
		
		assertEquals(5, allTerms.size());
		ArrayList<String> termOneModuleNames = new ArrayList<String>();
		for(Module m : allTerms.get(0)){
			termOneModuleNames.add(m.name);
		}
		assert(termOneModuleNames.contains("OO"));
				
	}
	

}
