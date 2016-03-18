package domain;

public class Propietario {
	private String usuario;
	private String password;
	private String nombre;
	private String apellidos;
	private String email;
	private String dni;
	private String cuentaBancaria;
	private int telefono;

	public Propietario(String usuario,String password,String nombre,String apellidos,String email,String dni, String cuentaBancaria,int telefono){
		this.usuario=usuario;
		this.password=password;
		this.nombre=nombre;
		this.apellidos=apellidos;
		this.email=email;
		this.dni=dni;
		this.cuentaBancaria=cuentaBancaria;
		this.telefono=telefono;
	}
	public String getCuentaBancaria() {
		return cuentaBancaria;
	}
	public void setCuentaBancaria(String cuentaBancaria) {
		this.cuentaBancaria = cuentaBancaria;
	}
	public int getTelefono() {
		return telefono;
	}
	public void setTelefono(int telefono) {
		this.telefono = telefono;
	}
	public Propietario(){
		
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
