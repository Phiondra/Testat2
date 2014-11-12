package util;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;


// Takes any class T which extends abstract class util.Dependency allows to
// map dependencies between instances of T.
// Mappings in T#deps are discarded during calculation of steps.
// For an implementation that doesn't require extending util.Dependency see util.DynDepMapper
public class DepMapper<T extends Dependency> implements Iterator<List<T>>{

	// HashMap is used to get as fast as possible the already created object with its dependencies
	// ArrayList is used because clearing an  ArrayList performs better than clearing a LinkedList
	private Map<String, T> all = new HashMap<>();  
	private List<T> withoutDeps = new ArrayList<>();
	
	public int size(){
		return all.size();
	}
	
	public boolean contains(String key){
		return all.containsKey(key);
	}
	
	public T add(T item){
		T search = all.get(item.getKey());
		if(search == null){
			all.put(item.getKey(), item);
			withoutDeps.add(item);
		}else{
			item = search;
		}
		return item;
	}

	public T addDep(T item, T dependent){
		
		T depItem = add(item);
		T depDependent = add(dependent);
		
		depItem.deps.add(depDependent);
		depDependent.unresolvedDeps++;
		withoutDeps.remove(depDependent);
		return depDependent;
	}
	
	// returns a list of lists containing every step in resolving the dependencies between known instances of T
	// The first list will contain only instances of T which are not dependent on anything.
	// The second list will contain only instances of T which are dependent on exactly the instances of T
	// contained in the first list.
	// The third list will contain only instances of T which are dependent on exactly the instances of T
	// contained in the first and second list.
	public List<List<T>> getSteps() throws NoSuchElementException{
		List<List<T>> allTerms = new ArrayList<>();

		while(this.hasNext()){
			allTerms.add(this.next());
		}
		return allTerms;
	}

	@Override
	public boolean hasNext() {
		return all.size() > 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> next() throws NoSuchElementException {
		if(withoutDeps.size() == 0){
			throw new NoSuchElementException("No Items without dependencies added");
		}
		//ArrayList is used because the final size is known at creation 
		List<T> termMods = new ArrayList<>(withoutDeps);
		withoutDeps.stream().forEach(d -> all.remove(d.getKey()));
		withoutDeps.clear();
		termMods.stream().forEach(dep -> {
			dep.deps.stream().forEach(subD ->{
				if((--subD.unresolvedDeps) == 0){
					withoutDeps.add((T) subD);
				}
			});
		});
		return termMods;
	}
		
}
