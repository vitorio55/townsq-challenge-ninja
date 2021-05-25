package br.com.townsq.ninjachallenge.repository;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import br.com.townsq.ninjachallenge.fileprocessor.DataFileProcessor;
import br.com.townsq.ninjachallenge.model.FileEntityType;
import br.com.townsq.ninjachallenge.model.UserType;
import br.com.townsq.ninjachallenge.model.entity.Group;
import br.com.townsq.ninjachallenge.model.entity.User;
import br.com.townsq.ninjachallenge.util.ValueTranslationUtil;

public class GroupRepository {

	/***********************************************************
	 * 
	 * Not an actual repository! Just used as an abstraction to concentrate data
	 * querying
	 * 
	 ***********************************************************/

	private DataFileProcessor dataFileProcessor;

	private static int USER_TYPES_IDX = 1;
	private static int CONDO_ID_IDX = 2;
	private static int GROUP_PERMISSIONS_IDX = 3;

	public GroupRepository(DataFileProcessor dataFileProcessor) {
		this.dataFileProcessor = dataFileProcessor;
	}

	/**
	 * Get all groups stored in the data file.
	 * 
	 * @return list with all groups fetched
	 * @throws FileNotFoundException
	 */
	public List<Group> getAllGroups() throws FileNotFoundException {
		List<Group> groupsList = new ArrayList<>();
		List<String> groupLinesFromFile = this.dataFileProcessor.getEntityLinesFromFile(FileEntityType.GROUP);

		groupLinesFromFile.forEach(groupLine -> {
			String[] attrs = groupLine.split(DataFileProcessor.ATTRIBUTE_SEPARATOR);

			Group group = new Group(ValueTranslationUtil.getUserTypeFromString(attrs[USER_TYPES_IDX]),
					Integer.parseInt(attrs[CONDO_ID_IDX]),
					this.dataFileProcessor.getGroupPermissionsFromDataString(attrs[GROUP_PERMISSIONS_IDX]));

			groupsList.add(group);
		});

		return groupsList;
	}

	/**
	 * Get group for a given user type and condo ID
	 * 
	 * @return
	 * @throws FileNotFoundException
	 */
	public Group getGroupByUserTypeAndCondoId(UserType userType, int condoId) throws FileNotFoundException {
		Group group = null;
		List<String> groupLinesFromFile = this.dataFileProcessor.getEntityLinesFromFile(FileEntityType.GROUP);

		for (String line : groupLinesFromFile) {
			String[] attrs = line.split(DataFileProcessor.ATTRIBUTE_SEPARATOR);

			// If the user doesn't belong to the current group in the loop, continue
			if (!attrs[USER_TYPES_IDX].equals(userType.toString()) && Integer.parseInt(attrs[CONDO_ID_IDX]) != condoId) {
				continue;
			}

			group = new Group(ValueTranslationUtil.getUserTypeFromString(attrs[USER_TYPES_IDX]),
					Integer.parseInt(attrs[CONDO_ID_IDX]),
					this.dataFileProcessor.getGroupPermissionsFromDataString(attrs[GROUP_PERMISSIONS_IDX]));
			break;
		}

		return group;
	}

	/**
	 * Get all groups that a user belongs to
	 * 
	 * @param user - Must have at least the user types set for proper group retrieval
	 * @return the groups the user belongs to
	 * @throws FileNotFoundException
	 */
	public List<Group> getAllGroupsByUser(User user) throws FileNotFoundException {
		List<Group> userGroups = new ArrayList<>();
		List<String> groupLinesFromFile = this.dataFileProcessor.getEntityLinesFromFile(FileEntityType.GROUP);

		for (String line : groupLinesFromFile) {
			String[] lineAttrs = line.split(DataFileProcessor.ATTRIBUTE_SEPARATOR);
			boolean userIsNotPartOfGroup = true;

			// Search among user types related to condo IDs if the user belongs to the group
	        for (Map.Entry<UserType, Collection<Integer>> userTypeAndCondoId : user.getTypes().asMap().entrySet()) {
	        	UserType userTypeFromGroupLine = ValueTranslationUtil.getUserTypeFromString(lineAttrs[USER_TYPES_IDX]);
	        	UserType userType = userTypeAndCondoId.getKey();

	        	Collection<Integer> userCondoIdsForType = userTypeAndCondoId.getValue();
				Integer condoIdFromGroupLine = Integer.parseInt(lineAttrs[CONDO_ID_IDX]);

				if (userTypeFromGroupLine.equals(userType) && userCondoIdsForType.contains(condoIdFromGroupLine)) {
					userIsNotPartOfGroup = false;
					break;
				}
	        }

	        if (userIsNotPartOfGroup) {
	        	continue;
	        }

			Group group = new Group(ValueTranslationUtil.getUserTypeFromString(lineAttrs[USER_TYPES_IDX]),
					Integer.parseInt(lineAttrs[CONDO_ID_IDX]),
					this.dataFileProcessor.getGroupPermissionsFromDataString(lineAttrs[GROUP_PERMISSIONS_IDX]));

			userGroups.add(group);
		}

		return userGroups;
	}

}
