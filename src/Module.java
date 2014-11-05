
public class Module{
	public String name;
	
	public Module(String name){
		this.name = name;
	}
	
	@Override
    public int hashCode() {
        return name.hashCode();
    }

	@Override
	public boolean equals(Object o){
		return name.equals(((Module)o).name);
	}

}
