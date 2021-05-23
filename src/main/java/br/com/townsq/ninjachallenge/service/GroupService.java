package br.com.townsq.ninjachallenge.service;

import java.io.FileNotFoundException;
import java.util.List;

import br.com.townsq.ninjachallenge.model.entity.Group;
import br.com.townsq.ninjachallenge.repository.GroupRepository;

public class GroupService {

	private GroupRepository groupRepository;

	public GroupService(GroupRepository groupRepository) {
		this.groupRepository = groupRepository;
	}

	/**
	 * Get all groups from the database.
	 * 
	 * @return a list with all the groups
	 * @throws FileNotFoundException
	 */
	public List<Group> getGroupsFromDatabase() throws FileNotFoundException {
		List<Group> groupsList = this.groupRepository.getAllGroups();
		return groupsList;
	}

}
