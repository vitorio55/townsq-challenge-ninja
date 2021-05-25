package br.com.townsq.ninjachallenge.fileprocessor;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import br.com.townsq.ninjachallenge.model.FileEntityType;
import br.com.townsq.ninjachallenge.model.FunctionalityType;
import br.com.townsq.ninjachallenge.model.PermissionType;
import br.com.townsq.ninjachallenge.model.UserType;
import br.com.townsq.ninjachallenge.util.ValueTranslationUtil;

public class DataFileProcessor {

	private File file;
	
	// Useful parsing definitions
	public static final String USER_LINE_PREFIX = "Usuario";
	public static final String GROUP_LINE_PREFIX = "Grupo";
	public static final String ATTRIBUTE_SEPARATOR = ";";
	public static final String USER_TYPE_SEPARATOR = ",";
	public static final String FUNCTIONALITY_TYPE_SEPARATOR = ",";

	// Regex group indexes
	public static final int REGEX_GROUP_USER_TYPE_IDX = 0;
	public static final int REGEX_GROUP_CONDO_ID_IDX = 1;
	public static final int REGEX_GROUP_GROUP_FUNC_TYPE_IDX = 0;
	public static final int REGEX_GROUP_PERM_TYPE_IDX = 1;


	public DataFileProcessor(File file) {
		this.file = file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	/**
	 * Get only the lines of specified entity from the data file
	 * 
	 * @param entity - USER or GROUP accepted to retrieved respective data from file
	 * @return lines of the specified entity
	 * @throws FileNotFoundException
	 */
	public List<String> getEntityLinesFromFile(FileEntityType entity) throws FileNotFoundException {
		if (file == null) {
			throw new FileNotFoundException("File is null");
		}

		List<String> usersLines = new ArrayList<>();
		String prefix = entity == FileEntityType.USER ? USER_LINE_PREFIX : GROUP_LINE_PREFIX; 
		Scanner scanner = null;

		try {
			scanner = new Scanner(this.file);

			while (scanner.hasNextLine()) {
				String data = scanner.nextLine();
				String[] attributes = data.split(ATTRIBUTE_SEPARATOR);
				if (attributes[0].equals(prefix)) {
					usersLines.add(data);
				}
			}
		}
		catch (FileNotFoundException e) {
			System.out.println("An error occurred fetching users lines from file.");
			e.printStackTrace();
		}
		finally {
			if (scanner != null) {
				scanner.close();	
			}
		}

		return usersLines;
	}

	/**
	 * Read and print the whole content of the data file
	 * 
	 * @throws FileNotFoundException
	 */
	public void readAndPrintContent() throws FileNotFoundException {
		if (file == null) {
			throw new FileNotFoundException("File is null");
		}

		Scanner scanner = null;
		try {
			scanner = new Scanner(this.file);

			System.out.println("------Data file content:");
			while (scanner.hasNextLine()) {
				String data = scanner.nextLine();
				System.out.println(data);
			}
		}
		catch (FileNotFoundException e) {
			System.out.println("An error occurred fetching file content.");
			e.printStackTrace();
		}
		finally {
			if (scanner != null) {
				scanner.close();	
			}
		}
	}

	/**
	 * Get a map with user types and condo IDs from a data line.
	 * 
	 * @param dataStr - the data line
	 * @return map with user types and condo IDs
	 */
	public Multimap<UserType, Integer> getUserTypesFromDataString(String dataStr) {
		Multimap<UserType, Integer> userTypesMap = ArrayListMultimap.create();

		Pattern pattern = Pattern.compile("\\(([^)]+)\\)");
		Matcher matcher = pattern.matcher(dataStr);

		while (matcher.find()) {
			String userTypeStr = matcher.group(1);
			String[] t = userTypeStr.split(DataFileProcessor.USER_TYPE_SEPARATOR);

			userTypesMap.put(ValueTranslationUtil.getUserTypeFromString(t[REGEX_GROUP_USER_TYPE_IDX]),
					Integer.parseInt(t[REGEX_GROUP_CONDO_ID_IDX]));
		}

		return userTypesMap;
	}

	/**
	 * Get a map with functionality types and permission types from a data line.
	 * 
	 * @param dataStr - the data line
	 * @return map with functionality types and permission types
	 */
	public LinkedHashMap<FunctionalityType, PermissionType> getGroupPermissionsFromDataString(String dataStr) {
		LinkedHashMap<FunctionalityType, PermissionType> permissionsMap = new LinkedHashMap<>();

		Pattern pattern = Pattern.compile("\\(([^)]+)\\)");
		Matcher matcher = pattern.matcher(dataStr);
		
		while (matcher.find()) {
			String userTypeStr = matcher.group(1);
			String[] t = userTypeStr.split(DataFileProcessor.FUNCTIONALITY_TYPE_SEPARATOR);

			permissionsMap.put(ValueTranslationUtil.getFunctionalityTypeFromStr(t[REGEX_GROUP_GROUP_FUNC_TYPE_IDX]),
					ValueTranslationUtil.getPermissionTypeFromStr(t[REGEX_GROUP_PERM_TYPE_IDX]));
		}

		return permissionsMap;
	}

}
