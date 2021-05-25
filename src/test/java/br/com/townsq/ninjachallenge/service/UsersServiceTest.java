package br.com.townsq.ninjachallenge.service;

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
import org.mockito.ArgumentMatchers;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import br.com.townsq.ninjachallenge.model.FunctionalityType;
import br.com.townsq.ninjachallenge.model.PermissionType;
import br.com.townsq.ninjachallenge.model.UserType;
import br.com.townsq.ninjachallenge.model.entity.Group;
import br.com.townsq.ninjachallenge.model.entity.User;
import br.com.townsq.ninjachallenge.repository.GroupRepository;
import br.com.townsq.ninjachallenge.repository.UserRepository;

public class UsersServiceTest {

	private UserRepository userRepository;
	private GroupRepository groupRepository;
	private UserService userService;
	
	@BeforeEach
	public void setup() throws FileNotFoundException {
		this.userRepository = Mockito.mock(UserRepository.class);
		this.groupRepository = Mockito.mock(GroupRepository.class);

		this.userService = new UserService(this.userRepository, this.groupRepository);
		this.mockUserRepositoryResponses();
		this.mockGroupRepositoryResponses();
	}
	
	@Test
	@DisplayName("Get Users From Database - Success")
	public void getUsersFromDatabase_success() throws FileNotFoundException {
		List<User> actual = this.userService.getUsersFromDatabase();

		assertEquals(actual.size(), 3);

		assertEquals(actual.get(0).getEmail(), "rodrigo.soares@gmail.com");
		assertEquals(actual.get(0).getTypes().get(UserType.MORADOR), Arrays.asList(1));
		
		assertEquals(actual.get(1).getEmail(), "maria.silva.sindica@gmail.com");
		assertEquals(actual.get(1).getTypes().get(UserType.MORADOR), Arrays.asList(1));
		assertEquals(actual.get(1).getTypes().get(UserType.SINDICO), Arrays.asList(1));
		
		assertEquals(actual.get(2).getEmail(), "joao.costa@gmail.com");
		assertEquals(actual.get(2).getTypes().get(UserType.MORADOR), Arrays.asList(1));
		assertEquals(actual.get(2).getTypes().get(UserType.SINDICO), Arrays.asList(1,2));
	}

	@Test
	@DisplayName("Get Highest Permissions In Condos String From Email - Success")
	public void getHighestPermsInCondosStrFromEmail_success() throws FileNotFoundException {
		String actual = this.userService.getHighestPermsInCondosStrFromEmail("joao.costa@gmail.com");
		String expected =
				"1;[(Reservas,Escrita),(Entregas,Nenhuma),(Usuarios,Escrita)]\n" +
				"2;[(Reservas,Escrita),(Entregas,Leitura),(Usuarios,Escrita)]\n";
		assertEquals(expected, actual);
	}

	private void mockUserRepositoryResponses() throws FileNotFoundException {
		List<User> mockUsersList = this.getMockedUsers();

		BDDMockito.given(this.userRepository.getAllUsers()).willReturn(mockUsersList);

		// User By Email mocking

		BDDMockito.given(this.userRepository.getUserByEmail(mockUsersList.get(0).getEmail())).willReturn(mockUsersList.get(0));
		BDDMockito.given(this.userRepository.getUserByEmail(mockUsersList.get(1).getEmail())).willReturn(mockUsersList.get(1));
		BDDMockito.given(this.userRepository.getUserByEmail(mockUsersList.get(2).getEmail())).willReturn(mockUsersList.get(2));
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

		BDDMockito.given(this.groupRepository.getAllGroupsByUser(ArgumentMatchers.any(User.class)))
			.willReturn(Arrays.asList(mockedGroup0, mockedGroup1, mockedGroup2)) // 1st user triggered (joao.costa@gmail.com)
			.willReturn(Arrays.asList(mockedGroup0, mockedGroup1))				 // 2nd user triggered (maria.silva.sindica@gmail.com)
			.willReturn(Arrays.asList(mockedGroup0));							 // 3rd user triggered (rodrigo.soares@gmail.com)
	}
	
	private List<User> getMockedUsers() {
		Multimap<UserType, Integer> mockTypes0 = ArrayListMultimap.create();
		mockTypes0.put(UserType.MORADOR, 1);
		User mockUser0 = new User("rodrigo.soares@gmail.com", mockTypes0);

		Multimap<UserType, Integer> mockTypes1 = ArrayListMultimap.create();
		mockTypes1.put(UserType.MORADOR, 1);
		mockTypes1.put(UserType.SINDICO, 1);
		User mockUser1 = new User("maria.silva.sindica@gmail.com", mockTypes1);

		Multimap<UserType, Integer> mockTypes2 = ArrayListMultimap.create();
		mockTypes2.put(UserType.MORADOR, 1);
		mockTypes2.put(UserType.SINDICO, 1);
		mockTypes2.put(UserType.SINDICO, 2);
		User mockUser2 = new User("joao.costa@gmail.com", mockTypes2);
		return Arrays.asList(mockUser0, mockUser1, mockUser2);
	}
	
}
