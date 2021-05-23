package br.com.townsq.ninjachallenge.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.com.townsq.ninjachallenge.fileprocessor.DataFileProcessor;
import br.com.townsq.ninjachallenge.model.UserType;
import br.com.townsq.ninjachallenge.model.entity.User;
import br.com.townsq.ninjachallenge.repository.GroupRepository;
import br.com.townsq.ninjachallenge.repository.UserRepository;

public class UsersServiceTest {

	private File file;
	private DataFileProcessor dataFileProcessor;
	private UserRepository userRepository;
	private GroupRepository groupRepository;
	private UserService userService;
	
	@BeforeEach
	public void setup() {
		file = new File("src/resources/data-file.txt");
		this.dataFileProcessor = new DataFileProcessor(file);
		this.userRepository = new UserRepository(this.dataFileProcessor);
		this.groupRepository = new GroupRepository(this.dataFileProcessor);
		this.userService = new UserService(this.userRepository, this.groupRepository);
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
	
}
