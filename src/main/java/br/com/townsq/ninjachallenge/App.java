/**
 * 
 * 21/05/2021
 * @author Vitorio Mincarone
 * 
 */
package br.com.townsq.ninjachallenge;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;

import br.com.townsq.ninjachallenge.fileprocessor.DataFileProcessor;
import br.com.townsq.ninjachallenge.model.entity.Group;
import br.com.townsq.ninjachallenge.model.entity.User;
import br.com.townsq.ninjachallenge.repository.GroupRepository;
import br.com.townsq.ninjachallenge.repository.UserRepository;
import br.com.townsq.ninjachallenge.service.GroupService;
import br.com.townsq.ninjachallenge.service.UserService;
import br.com.townsq.ninjachallenge.util.ValueTranslationUtil;

public class App {

	/**
	 * @param args
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException {
		File file = new File("src/resources/data-file.txt");
		DataFileProcessor processor = new DataFileProcessor(file);

		// Initialize repos
		UserRepository userRepo = new UserRepository(processor);
		GroupRepository groupRepo = new GroupRepository(processor);

		// Initialize services
		UserService userService = new UserService(userRepo, groupRepo);
		GroupService groupService = new GroupService(groupRepo);

		List<User> usersList = userService.getUsersFromDatabase();	
		List<Group> groupsList = groupService.getGroupsFromDatabase();

		List<String> emailsList = Arrays.asList(
				"rodrigo.soares@gmail.com",
				"joao.costa@gmail.com",
				"maria.silva.sindica@gmail.com"
		);

		// Print highest permissions on each condo for each user
		App.printHighestUserPermissions(userService, emailsList);

		// Extra - Display information returned from database for each user
		usersList.forEach(u -> App.printUserBanner(u));

		// Extra - Read and print whole database file content
		processor.readAndPrintContent();
		
		// Extra - Print all groups from database
		App.printGroupsList(groupsList);
	}

	/**
	 * Print users highest permissions for each of his condos
	 * 
	 * @param userService
	 * @param emailsList
	 * @throws FileNotFoundException
	 */
	private static void printHighestUserPermissions(UserService userService, List<String> emailsList) throws FileNotFoundException {
		System.out.println("---------------------------------------");
		System.out.println("Highest permissions in each condo");
		System.out.println("---------------------------------------");

		for (String email : emailsList) {
			System.out.println(String.format("Highest permissions for EMAIL (%s):", email));
			System.out.println(userService.getHighestPermsInCondosStrFromEmail(email));
		}		
	}
	
	/**
	 * Print users informations
	 * 
	 * @param user
	 */
	private static void printUserBanner(User user) {
		System.out.println("---------------------------------------");
		System.out.println("EMAIL: " + user.getEmail());
		System.out.println("---------------------------------------");
		user.getTypes().forEach((t, i) -> {
			System.out.println(String.format("User Type: %s (Condo ID %d)", t.toString(), i));
		});
		System.out.println("");
	}
	
	/**
	 * Print groups informations
	 * 
	 * @param groupsList
	 */
	private static void printGroupsList(List<Group> groupsList) {
		System.out.println("");
		System.out.println("---------------------------------------");
		System.out.println("GROUPS: ");
		System.out.println("---------------------------------------");
		for (Group g : groupsList) {
			System.out.println("CONDO ID: " + g.getCondoId());
			System.out.println("USER TYPE: " + g.getUserType());
			System.out.println("------------");
			g.getPermissions().forEach((func, perm) -> {
				System.out.println("FUNCTIONALITY/PERM: "
					+ ValueTranslationUtil.getCapitalizedStrFromFunctionalityType(func) + "/"
					+ ValueTranslationUtil.getCapitalizedStrFromPermissionType(perm));
			});
			System.out.println("");
		}
	}

}
