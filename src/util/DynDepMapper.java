package util;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;


// Takes any class T which implements a unique hashCode function and allows to
// map dependencies between instances of T.
public class DynDepMapper<T> {
	
	// Helper Object that wraps the dependency behavior for generic types.
	// Alternatively an abstract class could be provided for the base model to extend.
	// This implementation adds some memory overhead because for every instance of T there needs
	// to be an instance of Dependency. It does make it much more flexible in use though which is
	// a trade off I'm willing to make.
	private class Dependency{
		LinkedList<Dependency> deps = new LinkedList<>();
		int unresolvedDeps = 0;
		T item;
		@Override
	    public int hashCode() {
	        return item.hashCode();
	    }
	}

	private Hashtable<T, Dependency> all = new Hashtable<>();
	private List<Dependency> withoutDeps = new ArrayList<>();
	
	public int size(){
		return all.size();
	}
	
	public T add(T item){
		return firstOrCreate(item).item;
	}

	public T addDep(T item, T dependent){
		
		Dependency depItem = firstOrCreate(item);
		Dependency depDependent = firstOrCreate(dependent);
		
		depItem.deps.add(depDependent);
		depDependent.unresolvedDeps++;
		withoutDeps.remove(depDependent);
		return depDependent.item;
	}
	
	// returns a list of lists containing every step in resolving the dependencies between known instances of T
	// The first list will contain only instances of T which are not dependent on anything.
	// The second list will contain only instances of T which are dependent on exactly the instances of T
	// contained in the first list.
	// The third list will contain only instances of T which are dependent on exactly the instances of T
	// contained in the first and second list.
	public List<List<T>> getSteps() throws Exception{
		List<List<T>> allTerms = new ArrayList<List<T>>();
		if(withoutDeps.size() == 0){
			throw new Exception("No Items without dependencies added");
		}
		while(all.size() > 0){
			List<T> termMods = new ArrayList<>();
			allTerms.add(termMods);
			List<Dependency> termDeps = new LinkedList<>();
			for(Dependency d : withoutDeps){
				termMods.add(d.item);
				all.remove(d.item);
				termDeps.add(d);
			}
			withoutDeps.clear();
			for(Dependency dep : termDeps){
				for(Dependency subD: dep.deps){
					if((--subD.unresolvedDeps) == 0){
						withoutDeps.add(subD);
					}
				}
			}
		}
		return allTerms;
	}
	
	
	private Dependency firstOrCreate(T item){
		Dependency dep = all.get(item);
		if(dep == null){
			dep = new Dependency();
			dep.item = item;
			all.put(dep.item, dep);
			withoutDeps.add(dep);
		}
		return dep;
	}
	
}
