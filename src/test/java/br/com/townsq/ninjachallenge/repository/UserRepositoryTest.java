package br.com.townsq.ninjachallenge.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.FileNotFoundException;
import java.util.Arrays;
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
import br.com.townsq.ninjachallenge.model.UserType;
import br.com.townsq.ninjachallenge.model.entity.User;

public class UserRepositoryTest {

	private DataFileProcessor dataFileProcessor;
	private UserRepository userRepository;
	
	@BeforeEach
	public void setup() throws FileNotFoundException {
		this.dataFileProcessor = Mockito.mock(DataFileProcessor.class);
		this.userRepository = new UserRepository(this.dataFileProcessor);
		this.mockDataProcessorResponses();
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

	private void mockDataProcessorResponses() throws FileNotFoundException {
		String line0 = "Usuario;rodrigo.soares@gmail.com;[(Morador,1)]\n";
		String line0UserTypesStr = "[(Morador,1)]\n";

		String line1 = "Usuario;maria.silva.sindica@gmail.com;[(Morador,1),(Sindico,1)]\n";
		String line1UserTypesStr = "[(Morador,1),(Sindico,1)]\n";
		
		String line2 = "Usuario;joao.costa@gmail.com;[(Morador,1),(Sindico,1),(Sindico,2)]\n";
		String line2UserTypesStr = "[(Morador,1),(Sindico,1),(Sindico,2)]\n";

		List<String> mockGroupLines = Arrays.asList(line0, line1, line2);
		BDDMockito.given(this.dataFileProcessor.getEntityLinesFromFile(FileEntityType.USER)).willReturn(mockGroupLines);

		Multimap<UserType, Integer> userTypes0 = ArrayListMultimap.create();
		userTypes0.put(UserType.MORADOR, 1);

		Multimap<UserType, Integer> userTypes1 = ArrayListMultimap.create();
		userTypes1.put(UserType.MORADOR, 1);
		userTypes1.put(UserType.SINDICO, 1);
		
		Multimap<UserType, Integer> userTypes2 = ArrayListMultimap.create();
		userTypes2.put(UserType.MORADOR, 1);
		userTypes2.put(UserType.SINDICO, 1);
		userTypes2.put(UserType.SINDICO, 2);

		BDDMockito.given(this.dataFileProcessor.getUserTypesFromDataString(line0UserTypesStr)).willReturn(userTypes0);
		BDDMockito.given(this.dataFileProcessor.getUserTypesFromDataString(line1UserTypesStr)).willReturn(userTypes1);
		BDDMockito.given(this.dataFileProcessor.getUserTypesFromDataString(line2UserTypesStr)).willReturn(userTypes2);
	}

}
