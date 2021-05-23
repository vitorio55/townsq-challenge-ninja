package br.com.townsq.ninjachallenge.service;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import br.com.townsq.ninjachallenge.model.FunctionalityType;
import br.com.townsq.ninjachallenge.model.PermissionType;
import br.com.townsq.ninjachallenge.model.entity.Group;
import br.com.townsq.ninjachallenge.model.entity.User;
import br.com.townsq.ninjachallenge.repository.GroupRepository;
import br.com.townsq.ninjachallenge.repository.UserRepository;
import br.com.townsq.ninjachallenge.util.ValueTranslationUtil;

public class UserService {

	private UserRepository userRepository;
	private GroupRepository groupRepository;

	public UserService(UserRepository userRepository, GroupRepository groupRepository) {
		this.userRepository = userRepository;
		this.groupRepository = groupRepository;
	}

	/**
	 * Get all the users from the database.
	 * 
	 * @return a list with all the users
	 * @throws FileNotFoundException
	 */
	public List<User> getUsersFromDatabase() throws FileNotFoundException {
		List<User> usersList = this.userRepository.getAllUsers();
		return usersList;
	}

	/**
	 * Get the highest permissions in each condo as string from a given user email
	 * 
	 * @param email
	 * @return permissions
	 * @throws FileNotFoundException
	 */
	public String getHighestPermsInCondosStrFromEmail(String email) throws FileNotFoundException {
		if (email.isBlank() || email == null ) {
			System.out.println("Email arg is empty or null.");
			return null;
		}

		User user = this.userRepository.getUserByEmail(email);
		List<Group> groupsOfTheUser = this.groupRepository.getAllGroupsByUser(user);

		Map<Integer, LinkedHashMap<FunctionalityType, PermissionType>> highestPermsInCondo = new HashMap<>();

		for (Group g : groupsOfTheUser) {
			// Start highest permissions with lowest possible values for each type
			LinkedHashMap<FunctionalityType, PermissionType> highestPermissionsMap = new LinkedHashMap<>();
			highestPermissionsMap.put(FunctionalityType.RESERVAS, PermissionType.NENHUMA);
			highestPermissionsMap.put(FunctionalityType.ENTREGAS, PermissionType.NENHUMA);
			highestPermissionsMap.put(FunctionalityType.USUARIOS, PermissionType.NENHUMA);

			// Permission levels enum ordinal logic here:
			//   Lower values mean higher permissions

			LinkedHashMap<FunctionalityType, PermissionType> condoPerms = highestPermsInCondo.get(g.getCondoId());
			
			if (condoPerms == null) {
				PermissionType groupReservationsPerm = g.getPermissions().get(FunctionalityType.RESERVAS);
				if (groupReservationsPerm.ordinal() < highestPermissionsMap.get(FunctionalityType.RESERVAS).ordinal()) {
					highestPermissionsMap.put(FunctionalityType.RESERVAS, groupReservationsPerm);
				}

				PermissionType groupDeliveriesPerm = g.getPermissions().get(FunctionalityType.ENTREGAS);
				if (groupDeliveriesPerm.ordinal() < highestPermissionsMap.get(FunctionalityType.ENTREGAS).ordinal()) {
					highestPermissionsMap.put(FunctionalityType.ENTREGAS, groupDeliveriesPerm);
				}					

				PermissionType groupUsersPerm = g.getPermissions().get(FunctionalityType.USUARIOS);
				if (groupUsersPerm.ordinal() < highestPermissionsMap.get(FunctionalityType.USUARIOS).ordinal()) {
					highestPermissionsMap.put(FunctionalityType.USUARIOS, groupUsersPerm);
				}
				highestPermsInCondo.put(g.getCondoId(), highestPermissionsMap);
			}
			else {
				PermissionType groupReservationsPerm = g.getPermissions().get(FunctionalityType.RESERVAS);
				if (groupReservationsPerm.ordinal() < condoPerms.get(FunctionalityType.RESERVAS).ordinal()) {
					highestPermissionsMap.put(FunctionalityType.RESERVAS, groupReservationsPerm);
				}
				else {
					highestPermissionsMap.put(FunctionalityType.RESERVAS, condoPerms.get(FunctionalityType.RESERVAS));
				}
				
				PermissionType groupDeliveriesPerm = g.getPermissions().get(FunctionalityType.ENTREGAS);
				if (groupDeliveriesPerm.ordinal() < condoPerms.get(FunctionalityType.ENTREGAS).ordinal()) {
					highestPermissionsMap.put(FunctionalityType.ENTREGAS, groupDeliveriesPerm);
				}					
				else {
					highestPermissionsMap.put(FunctionalityType.ENTREGAS, condoPerms.get(FunctionalityType.ENTREGAS));
				}
				
				PermissionType groupUsersPerm = g.getPermissions().get(FunctionalityType.USUARIOS);
				if (groupUsersPerm.ordinal() < condoPerms.get(FunctionalityType.USUARIOS).ordinal()) {
					highestPermissionsMap.put(FunctionalityType.USUARIOS, groupUsersPerm);
				}
				else {
					highestPermissionsMap.put(FunctionalityType.USUARIOS, condoPerms.get(FunctionalityType.USUARIOS));
				}
				highestPermsInCondo.put(g.getCondoId(), highestPermissionsMap);
			}
		}

		StringBuilder highestPermsStrBuilder = new StringBuilder();

		highestPermsInCondo.forEach((condoId, perms) -> {
			highestPermsStrBuilder.append(String.format(
				"%d;[(Reservas,%s),(Entregas,%s),(Usuarios,%s)]\n", condoId,
				ValueTranslationUtil.getCapitalizedStrFromPermissionType(perms.get(FunctionalityType.RESERVAS)),
				ValueTranslationUtil.getCapitalizedStrFromPermissionType(perms.get(FunctionalityType.ENTREGAS)),
				ValueTranslationUtil.getCapitalizedStrFromPermissionType(perms.get(FunctionalityType.USUARIOS))
			));
		});

		return highestPermsStrBuilder.toString();
	}

}
