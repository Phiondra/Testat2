
import util.Dependency;
public class Module extends Dependency{
	public String name;
	
	public Module(String name){
		this.name = name;
	}
	
    public String getKey() {
        return name;
    }

	@Override
	public boolean equals(Object o){
		return this.name.equals(((Module)o).name);
	}

}
