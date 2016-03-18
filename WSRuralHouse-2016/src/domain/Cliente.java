package domain;

public class Cliente { //nose si hay que hacer implements INTERFAZ
private String usuario;
private String password;
private String nombre;
private String apellidos;
private String email;
private String dni;

public Cliente(String usuario,String password,String nombre,String apellidos,String email, String dni){
	this.usuario=usuario;
	this.password=password;
	this.nombre=nombre;
	this.apellidos=apellidos;
	this.email=email;
	this.dni=dni;
}
public Cliente(){
	
}

public String getUsuario() {
	return usuario;
}

public void setUsuario(String usuario) {
	this.usuario = usuario;
}

public String getPassword() {
	return password;
}

public void setPassword(String password) {
	this.password = password;
}

public String getNombre() {
	return nombre;
}

public void setNombre(String nombre) {
	this.nombre = nombre;
}

public String getApellidos() {
	return apellidos;
}

public void setApellidos(String apellidos) {
	this.apellidos = apellidos;
}

public String getEmail() {
	return email;
}

public void setEmail(String email) {
	this.email = email;
}

public String getDni() {
	return dni;
}

public void setDni(String dni) {
	this.dni = dni;
}

}
