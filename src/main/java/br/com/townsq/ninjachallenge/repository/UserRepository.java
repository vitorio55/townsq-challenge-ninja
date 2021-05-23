package br.com.townsq.ninjachallenge.repository;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import br.com.townsq.ninjachallenge.fileprocessor.DataFileProcessor;
import br.com.townsq.ninjachallenge.model.FileEntityType;
import br.com.townsq.ninjachallenge.model.entity.User;

public class UserRepository {

	/***********************************************************
	 * 
	 * Not an actual repository!
	 * Just used as an abstraction to concentrate data querying
	 * 
	 ***********************************************************/
	
	private DataFileProcessor dataFileProcessor;
	
	public static final int USER_EMAIL_IDX = 1;
	public static final int USER_TYPES_IDX = 2;

	
	public UserRepository(DataFileProcessor dataFileProcessor) {
		this.dataFileProcessor = dataFileProcessor;
	}

	/**
	 * Get all users stored in the data file.
	 * 
	 * @return list with all users fetched
	 * @throws FileNotFoundException
	 */
	public List<User> getAllUsers() throws FileNotFoundException {
		List<User> usersList = new ArrayList<>();
		List<String> userLinesFromFile = this.dataFileProcessor.getEntityLinesFromFile(FileEntityType.USER);

		userLinesFromFile.forEach(userLine -> {
			String[] attrs = userLine.split(DataFileProcessor.ATTRIBUTE_SEPARATOR);

			User user = new User(
					attrs[USER_EMAIL_IDX],
					this.dataFileProcessor.getUserTypesFromDataString(attrs[USER_TYPES_IDX])
			);

			usersList.add(user);
		});

		return usersList;
	}
	
	/**
	 * Get a user by a given email.
	 * 
	 * @return the requested user, or null if not found
	 * @throws FileNotFoundException 
	 */
	public User getUserByEmail(String email) throws FileNotFoundException {
		User user = null;
		List<String> userLinesFromFile = this.dataFileProcessor.getEntityLinesFromFile(FileEntityType.USER);

		for (String line : userLinesFromFile) {
			String[] attrs = line.split(DataFileProcessor.ATTRIBUTE_SEPARATOR);

			if (!attrs[USER_EMAIL_IDX].equals(email)) {
				continue;
			}

			user = new User(attrs[USER_EMAIL_IDX], this.dataFileProcessor.getUserTypesFromDataString(attrs[USER_TYPES_IDX]));
			break;	
		}

		return user;
	}

}
