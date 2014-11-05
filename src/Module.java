import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class Module {
	public String name;
	public List<Module> deps = new LinkedList<>();
	public static Map<String, Module> all = new HashMap<>(); 
	public static List<Module> withoutDeps = new ArrayList<>();
	long unresolvedDeps = 0;
//	public Module(String name, String[] dependencies){
//		this.name = name;
//		for(String dep : dependencies){
//			Module module = new Module(dep);
//			this.deps.add(module);
//		}
//	}
	
	public static Module create(String[] names){
		Module m = all.get(names[0]);
		if (m == null){
			m = new Module();
			m.name = names[0];
		}
		for (int i = 1; i < names.length; i++) {
			Module module = firstOrCreate(names[i]);
			module.deps.add(m);
			m.unresolvedDeps++;
			//m.deps.add(module);
		}
		Module.all.put(m.name, m);
		return m;
	}
	
	public static Module firstOrCreate(String name){
		Module m = all.get(name);
		if (m == null){
			String[] a = {name};
			m = create(a);
		}
		return m;
	}

	public static List<Module> getWithoutDependencies(){
		
		return null;
	}
	
	public static List<Module> getDependentOn(List<Module> mods){
		ArrayList<Module> matches = new ArrayList<Module>();
		for(Module m : all.values()){
			if(mods.containsAll(m.deps) && !mods.contains(m)){
				matches.add(m);
			}
		}
		return matches;
	}
}
