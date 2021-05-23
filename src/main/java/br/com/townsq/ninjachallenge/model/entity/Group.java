package br.com.townsq.ninjachallenge.model.entity;

import java.util.LinkedHashMap;
import java.util.Map;

import br.com.townsq.ninjachallenge.model.FunctionalityType;
import br.com.townsq.ninjachallenge.model.PermissionType;
import br.com.townsq.ninjachallenge.model.UserType;

public class Group {
	UserType userType;
	int condoId;

	// LinkedHashMap was chosen instead of Map because of its ability to maintain elements input order
	LinkedHashMap<FunctionalityType, PermissionType> permissions;
	
	public Group(UserType userType, int condoId, LinkedHashMap<FunctionalityType, PermissionType> permissions) {
		this.userType = userType;
		this.condoId = condoId;
		this.permissions = permissions;
	}
	
	public Map<FunctionalityType, PermissionType> getPermissions() {
		return this.permissions;
	}

	public int getCondoId() {
		return condoId;
	}

	public void setCondoId(int condoId) {
		this.condoId = condoId;
	}

}
