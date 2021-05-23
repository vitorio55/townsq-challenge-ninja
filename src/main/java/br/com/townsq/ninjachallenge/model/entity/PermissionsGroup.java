package br.com.townsq.ninjachallenge.model.entity;

import java.util.HashMap;
import java.util.Map;

import br.com.townsq.ninjachallenge.model.FunctionalityType;
import br.com.townsq.ninjachallenge.model.PermissionType;
import br.com.townsq.ninjachallenge.model.UserType;

public class PermissionsGroup {
	Map<FunctionalityType, PermissionType> permissions;
	UserType userType;
	int condoId;

	public PermissionsGroup() {
		this.permissions = new HashMap<>();
	}	

	public PermissionsGroup(UserType userType, int condoId, Map<FunctionalityType, PermissionType> permissions) {
		this.userType = userType;
		this.condoId = condoId;
		this.permissions = permissions;
	}

}
