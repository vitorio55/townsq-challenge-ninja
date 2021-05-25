package br.com.townsq.ninjachallenge.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;

import br.com.townsq.ninjachallenge.fileprocessor.DataFileProcessor;
import br.com.townsq.ninjachallenge.model.FunctionalityType;
import br.com.townsq.ninjachallenge.model.PermissionType;
import br.com.townsq.ninjachallenge.model.UserType;
import br.com.townsq.ninjachallenge.model.entity.Group;
import br.com.townsq.ninjachallenge.repository.GroupRepository;

public class GroupServiceTest {

	private GroupRepository groupRepository;
	private GroupService groupService;
	
	@BeforeEach
	public void setup() throws FileNotFoundException {
		this.groupRepository = Mockito.mock(GroupRepository.class);
		this.groupService = new GroupService(this.groupRepository);
		this.mockGroupRepositoryResponses();
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

	private void mockGroupRepositoryResponses() throws FileNotFoundException {
		LinkedHashMap<FunctionalityType, PermissionType> mockedPermissions0 = new LinkedHashMap<>();
		mockedPermissions0.put(FunctionalityType.RESERVAS, PermissionType.ESCRITA);
		mockedPermissions0.put(FunctionalityType.ENTREGAS, PermissionType.NENHUMA);
		mockedPermissions0.put(FunctionalityType.USUARIOS, PermissionType.LEITURA);
		Group mockedGroup0 = new Group(UserType.MORADOR, 1, mockedPermissions0);

		LinkedHashMap<FunctionalityType, PermissionType> mockedPermissions1 = new LinkedHashMap<>();
		mockedPermissions1.put(FunctionalityType.RESERVAS, PermissionType.LEITURA);
		mockedPermissions1.put(FunctionalityType.ENTREGAS, PermissionType.NENHUMA);
		mockedPermissions1.put(FunctionalityType.USUARIOS, PermissionType.ESCRITA);
		Group mockedGroup1 = new Group(UserType.SINDICO, 1, mockedPermissions1);

		LinkedHashMap<FunctionalityType, PermissionType> mockedPermissions2 = new LinkedHashMap<>();
		mockedPermissions2.put(FunctionalityType.RESERVAS, PermissionType.ESCRITA);
		mockedPermissions2.put(FunctionalityType.ENTREGAS, PermissionType.LEITURA);
		mockedPermissions2.put(FunctionalityType.USUARIOS, PermissionType.ESCRITA);
		Group mockedGroup2 = new Group(UserType.SINDICO, 2, mockedPermissions2);

		List<Group> mockGroupsList = Arrays.asList(mockedGroup0, mockedGroup1, mockedGroup2);

		BDDMockito.given(this.groupRepository.getAllGroups()).willReturn(mockGroupsList);
	}
	
}
