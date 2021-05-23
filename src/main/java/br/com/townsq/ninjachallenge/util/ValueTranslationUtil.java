package br.com.townsq.ninjachallenge.util;

import br.com.townsq.ninjachallenge.model.FunctionalityType;
import br.com.townsq.ninjachallenge.model.PermissionType;
import br.com.townsq.ninjachallenge.model.UserType;

public class ValueTranslationUtil {

	/**
	 * Get user type from a given string
	 * 
	 * @param typeStr - the string to be returned as user type enum
	 * @return user type
	 */
	public static UserType getUserTypeFromString(String typeStr) {
		switch (typeStr.toLowerCase()) {
			case "morador": return UserType.MORADOR;
			case "síndico":
			case "sindico": return UserType.SINDICO;	
			case "funcionário":
			case "funcionario": return UserType.FUNCIONARIO;
			
			default:
				System.out.println("Unknown user type.");
				return null;
		}
	}
	
	/**
	 * Get functionality type from a given string
	 * 
	 * @param typeStr - the string to be returned as functionality type enum 
	 * @return functionality type
	 */
	public static FunctionalityType getFunctionalityTypeFromStr(String typeStr) {
		switch (typeStr.toLowerCase()) {
			case "entregas": return FunctionalityType.ENTREGAS;
			case "reservas": return FunctionalityType.RESERVAS;
			case "usuários":
			case "usuarios": return FunctionalityType.USUARIOS;
			
			default:
				System.out.println("Unknown functionality type.");
				return null;
		}
	}
	
	/**
	 * Get permission type from a give string
	 * 
	 * @param typeStr - the string to be returned as permission type enum
	 * @return permission type
	 */
	public static PermissionType getPermissionTypeFromStr(String typeStr) {
		switch (typeStr.toLowerCase()) {
			case "escrita": return PermissionType.ESCRITA;
			case "leitura": return PermissionType.LEITURA;
			case "nenhuma": return PermissionType.NENHUMA;
			
			default:
				System.out.println("Unknown permission type.");
				return null;
		}
	}
	
	/**
	 * Get permission type as capitalized string
	 * 
	 * @param fType - the permission type as enum to be returned as a capitalized string
	 * @return capitalized string
	 */
	public static String getCapitalizedStrFromPermissionType(PermissionType pType) {
		String firstLetterCap = pType.toString().substring(0, 1).toUpperCase();
		String restOfOriginalStr = pType.toString().substring(1).toLowerCase();
		return String.format("%s%s", firstLetterCap, restOfOriginalStr);
	}

}
