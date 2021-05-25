package br.com.townsq.ninjachallenge.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import br.com.townsq.ninjachallenge.fileprocessor.DataFileProcessor;
import br.com.townsq.ninjachallenge.model.FileEntityType;
import br.com.townsq.ninjachallenge.model.FunctionalityType;
import br.com.townsq.ninjachallenge.model.PermissionType;
import br.com.townsq.ninjachallenge.model.UserType;
import br.com.townsq.ninjachallenge.model.entity.Group;
import br.com.townsq.ninjachallenge.model.entity.User;

public class GroupRepositoryTest {

	private DataFileProcessor dataFileProcessor;
	private GroupRepository groupRepository;

	@BeforeEach
	public void setup() throws FileNotFoundException {
		this.dataFileProcessor = Mockito.mock(DataFileProcessor.class);
		this.groupRepository = new GroupRepository(this.dataFileProcessor);
		this.mockDataProcessorResponses();
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
	
	private void mockDataProcessorResponses() throws FileNotFoundException {
		String line0 = "Grupo;Morador;1;[(Reservas,Escrita),(Entregas,Nenhuma),(Usuarios,Leitura)]\n";
		String line0PermissionsStr = "[(Reservas,Escrita),(Entregas,Nenhuma),(Usuarios,Leitura)]\n";

		String line1 = "Grupo;Sindico;1;[(Reservas,Leitura),(Entregas,Nenhuma),(Usuarios,Escrita)]\n";
		String line1PermissionsStr = "[(Reservas,Leitura),(Entregas,Nenhuma),(Usuarios,Escrita)]\n";
		
		String line2 = "Grupo;Sindico;2;[(Reservas,Escrita),(Entregas,Leitura),(Usuarios,Escrita)]\n";
		String line2PermissionsStr = "[(Reservas,Escrita),(Entregas,Leitura),(Usuarios,Escrita)]\n";
		
		List<String> mockGroupLines = Arrays.asList(line0, line1, line2);
		BDDMockito.given(this.dataFileProcessor.getEntityLinesFromFile(FileEntityType.GROUP)).willReturn(mockGroupLines);

		LinkedHashMap<FunctionalityType, PermissionType> mockGroupPermissions0 = new LinkedHashMap<>();
		mockGroupPermissions0.put(FunctionalityType.RESERVAS, PermissionType.ESCRITA);
		mockGroupPermissions0.put(FunctionalityType.ENTREGAS, PermissionType.NENHUMA);
		mockGroupPermissions0.put(FunctionalityType.USUARIOS, PermissionType.LEITURA);

		LinkedHashMap<FunctionalityType, PermissionType> mockGroupPermissions1 = new LinkedHashMap<>();
		mockGroupPermissions1.put(FunctionalityType.RESERVAS, PermissionType.LEITURA);
		mockGroupPermissions1.put(FunctionalityType.ENTREGAS, PermissionType.NENHUMA);
		mockGroupPermissions1.put(FunctionalityType.USUARIOS, PermissionType.ESCRITA);
		
		LinkedHashMap<FunctionalityType, PermissionType> mockGroupPermissions2 = new LinkedHashMap<>();
		mockGroupPermissions2.put(FunctionalityType.RESERVAS, PermissionType.ESCRITA);
		mockGroupPermissions2.put(FunctionalityType.ENTREGAS, PermissionType.LEITURA);
		mockGroupPermissions2.put(FunctionalityType.USUARIOS, PermissionType.ESCRITA);
		BDDMockito.given(this.dataFileProcessor.getGroupPermissionsFromDataString(line0PermissionsStr)).willReturn(mockGroupPermissions0);
		BDDMockito.given(this.dataFileProcessor.getGroupPermissionsFromDataString(line1PermissionsStr)).willReturn(mockGroupPermissions1);
		BDDMockito.given(this.dataFileProcessor.getGroupPermissionsFromDataString(line2PermissionsStr)).willReturn(mockGroupPermissions2);
	}
}
