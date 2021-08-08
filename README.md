# Community User Permissions Retrieval

This project is a solution to the challenge of the TownSq backend developer job application.  
In the challenge description there is an input file which contains:
* Community residents (users) data and permissions in the community.
* Groups that describe the permissions of their users in the community.

File content:
~~~
Usuario;rodrigo.soares@gmail.com;[(Morador,1)]
Usuario;maria.silva.sindica@gmail.com;[(Morador,1),(Sindico,1)]
Usuario;joao.costa@gmail.com;[(Morador,1),(Sindico,1),(Sindico,2)]
Grupo;Morador;1;[(Reservas,Escrita),(Entregas,Nenhuma),(Usuarios,Leitura)]
Grupo;Sindico;1;[(Reservas,Leitura),(Entregas,Nenhuma),(Usuarios,Escrita)]
Grupo;Sindico;2;[(Reservas,Escrita),(Entregas,Leitura),(Usuarios,Escrita)]
~~~

Each user entry is composed by the following info:  
~~~
'Usuario;' <- hardcoded string indicating the line is user data  
'<EMAIL>;' <- user's email  
'[(<GROUP_NAME>,<COMMUNITY_ID>)...]' <- a list indicating which group the user belongs to, in which community  
~~~

Each group entry is composed by the following info:
~~~
'Grupo;' <- hardcoded string indicating the line is group data  
'<GROU_NAME>;' <- the group name  
'<COMMUNITY_ID>;' <- ID of the community that this group's entry affects  
'[(<FUNCTIONALITY>,<PERMISSION_LEVEL>)...]' <- a list of permission levels by functionality  
~~~

Functionalities can be:  
* Reservas (reservations)  
* Entregas (deliveries)  
* Usuarios (users)  

Permission levels can be:  
* Nenhuma (none)  
* Leitura (read-only)  
* Escrita (write)  

We must create a function to retrieve all the permissions a given user has in each of his/her communities.  

The desired output would be something like this:  

~~~
Permissions for <EMAIL>:  
'<CONDO_ID>;[(<FUNCTIONALITY>,<PERMISSION_LEVEL>)...]'
~~~

Example:  

~~~
Highest permissions for EMAIL (rodrigo.soares@gmail.com):
1;[(Reservas,Escrita),(Entregas,Nenhuma),(Usuarios,Leitura)]

Highest permissions for EMAIL (joao.costa@gmail.com):
1;[(Reservas,Escrita),(Entregas,Nenhuma),(Usuarios,Escrita)]
2;[(Reservas,Escrita),(Entregas,Leitura),(Usuarios,Escrita)]

Highest permissions for EMAIL (maria.silva.sindica@gmail.com):
1;[(Reservas,Escrita),(Entregas,Nenhuma),(Usuarios,Escrita)]
~~~
