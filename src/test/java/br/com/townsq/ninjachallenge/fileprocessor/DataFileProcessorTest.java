package br.com.townsq.ninjachallenge.fileprocessor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import com.google.common.collect.Multimap;

import br.com.townsq.ninjachallenge.model.FileEntityType;
import br.com.townsq.ninjachallenge.model.FunctionalityType;
import br.com.townsq.ninjachallenge.model.PermissionType;
import br.com.townsq.ninjachallenge.model.UserType;

public class DataFileProcessorTest {

	private DataFileProcessor dataFileProcessor;

	@BeforeEach
	public void setup() throws IOException {
		this.dataFileProcessor = new DataFileProcessor("data-file.txt");
	}

    @Test
    @DisplayName("Get Entity Lines From File - Null file throws exception")
    public void getEntityLinesFromFile_nullFileThrowsException() {
        this.dataFileProcessor.setFile(null);
        assertThrows(FileNotFoundException.class,
        		() -> this.dataFileProcessor.getEntityLinesFromFile(FileEntityType.USER)
        );
    }

    @Test
    @DisplayName("Get Entity Lines From File - Get user lines success")
    public void getEntityLinesFromFile_getUserLinesSuccess() throws FileNotFoundException {
    	List<String> expected = Arrays.asList(
    			"Usuario;rodrigo.soares@gmail.com;[(Morador,1)]",
    			"Usuario;maria.silva.sindica@gmail.com;[(Morador,1),(Sindico,1)]",
    			"Usuario;joao.costa@gmail.com;[(Morador,1),(Sindico,1),(Sindico,2)]"
    	);
    	List<String> actual = this.dataFileProcessor.getEntityLinesFromFile(FileEntityType.USER);
		assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Get Entity Lines From File - Get group lines success")
    public void getEntityLinesFromFile_getGroupLinesSuccess() throws FileNotFoundException {
    	List<String> expected = Arrays.asList(
    			"Grupo;Morador;1;[(Reservas,Escrita),(Entregas,Nenhuma),(Usuarios,Leitura)]",
    			"Grupo;Sindico;1;[(Reservas,Leitura),(Entregas,Nenhuma),(Usuarios,Escrita)]",
    			"Grupo;Sindico;2;[(Reservas,Escrita),(Entregas,Leitura),(Usuarios,Escrita)]"
    	);
    	List<String> actual = this.dataFileProcessor.getEntityLinesFromFile(FileEntityType.GROUP);
		assertEquals(expected, actual);
    }
    
    @Test
    @DisplayName("Read And Print Content - Null file throws exception")
    public void readAndPrintContent_nullFileThrowsException() {
        this.dataFileProcessor.setFile(null);
        assertThrows(FileNotFoundException.class,
        		() -> this.dataFileProcessor.readAndPrintContent()
        );
    }

    @Test
    @DisplayName("Get User Types From Data String - Success")
    public void getUserTypesFromDataString_success() {
    	String dataStr = "Usuario;rodrigo.soares@gmail.com;[(Morador,1)]";
    	UserType expectedUserType = UserType.MORADOR;
    	int expectedCondoId = 1;
    	Multimap<UserType, Integer> actual = this.dataFileProcessor.getUserTypesFromDataString(dataStr);
    	assertTrue(actual.get(expectedUserType).iterator().next().equals(expectedCondoId));
    }
    
    @Test
    @DisplayName("Get Group Permissions From Data String - Success")
    public void getGroupPermissionsFromDataString_success() {
    	String dataStr = "Grupo;Sindico;2;[(Reservas,Escrita),(Entregas,Leitura),(Usuarios,Escrita)]";
    	LinkedHashMap<FunctionalityType, PermissionType> actual = this.dataFileProcessor.getGroupPermissionsFromDataString(dataStr);
    	assertEquals(actual.get(FunctionalityType.RESERVAS), PermissionType.ESCRITA);
    	assertEquals(actual.get(FunctionalityType.ENTREGAS), PermissionType.LEITURA);
    	assertEquals(actual.get(FunctionalityType.USUARIOS), PermissionType.ESCRITA);
    }

}
