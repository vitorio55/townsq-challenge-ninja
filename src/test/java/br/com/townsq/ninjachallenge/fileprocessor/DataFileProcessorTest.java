package br.com.townsq.ninjachallenge.fileprocessor;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.FileNotFoundException;

import org.junit.Before;
import org.junit.Test;

import br.com.townsq.ninjachallenge.model.FileEntityType;

public class DataFileProcessorTest {

	private DataFileProcessor dataFileProcessor;
	private File file;
	
	@Before
	public void setup() {
		file = new File("src/resources/data-file.txt");
		dataFileProcessor = new DataFileProcessor(file);
	}
	
    @Test
    public void getEntityLinesFromFile_nullFileThrowsException() {
        this.dataFileProcessor.setFile(null);
        
        assertThrows(FileNotFoundException.class,
        		() -> this.dataFileProcessor.getEntityLinesFromFile(FileEntityType.USER)
        );

    }

}
