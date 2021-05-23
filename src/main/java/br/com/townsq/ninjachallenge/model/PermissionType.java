package br.com.townsq.ninjachallenge.model;

public enum PermissionType {
	// The permissions below are ordered this way on purpose.
	// This facilitates the calculation for permission hierarchy.

	// Permission levels logic here:
	//   Lower values mean higher permissions

	ESCRITA, // WRITE,
	LEITURA, // READ,
	NENHUMA, // NONE
}
