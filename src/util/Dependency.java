package util;

import java.util.ArrayList;
import java.util.List;

public abstract class Dependency{
		List<Dependency> deps = new ArrayList<>();
		short unresolvedDeps = 0;
	    abstract public String getKey();
}
