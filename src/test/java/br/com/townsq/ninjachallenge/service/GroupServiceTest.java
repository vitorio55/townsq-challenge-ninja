package br.com.townsq.ninjachallenge.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.com.townsq.ninjachallenge.fileprocessor.DataFileProcessor;
import br.com.townsq.ninjachallenge.model.FunctionalityType;
import br.com.townsq.ninjachallenge.model.PermissionType;
import br.com.townsq.ninjachallenge.model.UserType;
import br.com.townsq.ninjachallenge.model.entity.Group;
import br.com.townsq.ninjachallenge.repository.GroupRepository;

public class GroupServiceTest {

	private File file;
	private DataFileProcessor dataFileProcessor;
	private GroupRepository groupRepository;
	private GroupService groupService;
	
	@BeforeEach
	public void setup() {
		file = new File("src/resources/data-file.txt");
		this.dataFileProcessor = new DataFileProcessor(file);
		this.groupRepository = new GroupRepository(this.dataFileProcessor);
		this.groupService = new GroupService(this.groupRepository);
	}

	@Test
	@DisplayName("Get Groups From Database - Success")
	public void getGroupsFromDatabase_success() throws FileNotFoundException {
		List<Group> actual = this.groupService.getGroupsFromDatabase();
		assertEquals(actual.size(), 3);
		assertEquals(actual.get(0).getCondoId(), 1);
		assertEquals(actual.get(1).getCondoId(), 1);
		assertEquals(actual.get(2).getCondoId(), 2);
		
		assertEquals(actual.get(0).getUserType(), UserType.MORADOR);
		assertEquals(actual.get(1).getUserType(), UserType.SINDICO);
		assertEquals(actual.get(2).getUserType(), UserType.SINDICO);
		
		// Assert permissions
		assertEquals(actual.get(0).getPermissions().get(FunctionalityType.RESERVAS), PermissionType.ESCRITA);
		assertEquals(actual.get(0).getPermissions().get(FunctionalityType.ENTREGAS), PermissionType.NENHUMA);
		assertEquals(actual.get(0).getPermissions().get(FunctionalityType.USUARIOS), PermissionType.LEITURA);

		assertEquals(actual.get(1).getPermissions().get(FunctionalityType.RESERVAS), PermissionType.LEITURA);
		assertEquals(actual.get(1).getPermissions().get(FunctionalityType.ENTREGAS), PermissionType.NENHUMA);
		assertEquals(actual.get(1).getPermissions().get(FunctionalityType.USUARIOS), PermissionType.ESCRITA);

		assertEquals(actual.get(2).getPermissions().get(FunctionalityType.RESERVAS), PermissionType.ESCRITA);
		assertEquals(actual.get(2).getPermissions().get(FunctionalityType.ENTREGAS), PermissionType.LEITURA);
		assertEquals(actual.get(2).getPermissions().get(FunctionalityType.USUARIOS), PermissionType.ESCRITA);
	}
	
}
