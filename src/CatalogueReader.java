import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class CatalogueReader implements AutoCloseable {
	private BufferedReader reader;
	
	public CatalogueReader(String filePath) throws FileNotFoundException {
		reader = new BufferedReader(new FileReader(filePath));
	}
	
	// null if end of input reached
	public String[] readNextLine() throws IOException {
		String line;
		line = reader.readLine();
		if (line != null) {
			return line.split(" ");
		} else {
			return null;
		}
	}

	@Override
	public void close() throws Exception {
		reader.close();
	}
}
