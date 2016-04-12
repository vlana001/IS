package domain;

import java.util.ArrayList;

public class Cliente { //nose si hay que hacer implements INTERFAZ
private String usuario;
private String password;
private String nombre;
private String apellidos;
private String email;
private String dni;

//Iteracion 2
ArrayList<String> code = new ArrayList<String>();//PasswordResetToken 
ArrayList<Long> password_reset_expiration = new ArrayList<Long>();
ArrayList<Boolean> token_used = new ArrayList<Boolean>();

private boolean bloqueado;

public Cliente(String usuario,String password,String nombre,String apellidos,String email, String dni, String code, long date, boolean token_used, boolean bloqueado){
	this.usuario=usuario;
	this.password=password;
	this.nombre=nombre;
	this.apellidos=apellidos;
	this.email=email;
	this.dni=dni;
	
	//int tam = password_reset_expiration.size();
	//System.out.println("t "+tam);
	
	//Añade en la ultima posicion
	this.code.add(code);
	this.password_reset_expiration.add(date);
	this.token_used.add(token_used);
	
	//Usuario bloqueado
	this.bloqueado = bloqueado;
	
}
/*public Cliente(){
	
}
*/
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


//Iteracion 2
public ArrayList<String> getCode() {
	return code;
}
public void setCode(ArrayList<String> code) {
	this.code = code;
}

public ArrayList<Long> getPassword_reset_expiration() {
	return password_reset_expiration;
}
public void setPassword_reset_expiration(ArrayList<Long> password_reset_expiration) {
	this.password_reset_expiration = password_reset_expiration;
}

public ArrayList<Boolean> getToken_used() {
	return token_used;
}
public void setToken_used(ArrayList<Boolean> token_used) {
	this.token_used = token_used;
}


public boolean isBloqueado() {
	return bloqueado;
}
public void setBloqueado(boolean bloqueado) {
	this.bloqueado = bloqueado;
}


public void addCode(String code){
	this.code.add(code);
}

public void addDateTime(long dateTime){
	this.password_reset_expiration.add(dateTime);
}

public void addTokenUsed(boolean b){
	token_used.add(b);
}

}
