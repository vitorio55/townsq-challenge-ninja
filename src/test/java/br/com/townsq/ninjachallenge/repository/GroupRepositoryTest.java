package br.com.townsq.ninjachallenge.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import br.com.townsq.ninjachallenge.fileprocessor.DataFileProcessor;
import br.com.townsq.ninjachallenge.model.FunctionalityType;
import br.com.townsq.ninjachallenge.model.PermissionType;
import br.com.townsq.ninjachallenge.model.UserType;
import br.com.townsq.ninjachallenge.model.entity.Group;
import br.com.townsq.ninjachallenge.model.entity.User;

public class GroupRepositoryTest {

	private File file;
	private DataFileProcessor dataFileProcessor;
	private GroupRepository groupRepository;

	@BeforeEach
	public void setup() {
		file = new File("src/resources/data-file.txt");
		this.dataFileProcessor = new DataFileProcessor(file);
		this.groupRepository = new GroupRepository(this.dataFileProcessor);
	}

	@Test
	@DisplayName("Get All Groups - Success")
	public void getAllGroups_success() throws FileNotFoundException {
		List<Group> actual = this.groupRepository.getAllGroups();
		assertEquals(actual.size(), 3);

		// Assert condo IDs
		assertEquals(actual.get(0).getCondoId(), 1);
		assertEquals(actual.get(1).getCondoId(), 1);
		assertEquals(actual.get(2).getCondoId(), 2);
		
		// Assert user types
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

	@Test
	@DisplayName("Get Group By User Type And Condo ID - Success")
	public void getGroupByUserTypeAndCondoId_success() throws FileNotFoundException {
		Group actual = this.groupRepository.getGroupByUserTypeAndCondoId(UserType.SINDICO, 2);
		assertEquals(actual.getCondoId(), 2);
		assertEquals(actual.getUserType(), UserType.SINDICO);

		assertEquals(actual.getPermissions().get(FunctionalityType.RESERVAS), PermissionType.ESCRITA);
		assertEquals(actual.getPermissions().get(FunctionalityType.ENTREGAS), PermissionType.LEITURA);
		assertEquals(actual.getPermissions().get(FunctionalityType.USUARIOS), PermissionType.ESCRITA);
	}
	
	@Test
	@DisplayName("Get All Groups By User - Success")
	public void getAllGroupsByUser_success() throws FileNotFoundException {
		Multimap<UserType, Integer> userTypesMap = ArrayListMultimap.create();
		userTypesMap.put(UserType.MORADOR, 1);
		userTypesMap.put(UserType.SINDICO, 1);
		userTypesMap.put(UserType.SINDICO, 2);

		User user = new User();
		user.setTypes(userTypesMap);

		List<Group> actual = this.groupRepository.getAllGroupsByUser(user);
		assertEquals(actual.size(), 3);
		assertEquals(actual.get(0).getCondoId(), 1);
		assertEquals(actual.get(1).getCondoId(), 1);
		assertEquals(actual.get(2).getCondoId(), 2);
		
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
