package br.com.townsq.ninjachallenge.repository;

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

public class UserRepositoryTest {

	private File file;
	private DataFileProcessor dataFileProcessor;
	private UserRepository userRepository;
	
	@BeforeEach
	public void setup() {
		file = new File("src/resources/data-file.txt");
		this.dataFileProcessor = new DataFileProcessor(file);
		this.userRepository = new UserRepository(this.dataFileProcessor);
	}

	@Test
	@DisplayName("Get All Users - Success")
	public void getAllUsers_success() throws FileNotFoundException {
		List<User> actual = this.userRepository.getAllUsers();

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
	@DisplayName("Get User By Email - Success")
	public void getUserByEmail_success() throws FileNotFoundException {
		User actual = this.userRepository.getUserByEmail("joao.costa@gmail.com");
		assertEquals(actual.getEmail(), "joao.costa@gmail.com");
		assertEquals(actual.getTypes().get(UserType.MORADOR), Arrays.asList(1));
		assertEquals(actual.getTypes().get(UserType.SINDICO), Arrays.asList(1,2));
	}
}
