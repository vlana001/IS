package dataAccess;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Vector;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.cs.Db4oClientServer;
import com.db4o.cs.config.ClientConfiguration;
import com.db4o.query.Predicate;

import configuration.ConfigXML;
import domain.Admin;
//import domain.Booking;
import domain.Offer;
import domain.Propietario;
import domain.RuralHouse;
import exceptions.OverlappingOfferExists;

import domain.Cliente;

public class DataAccess  {
	protected static ObjectContainer  db;

	private static DB4oManagerAux theDB4oManagerAux;
	private static DB4oManagerAux2 theDB4oManagerAux2;
	private static EmbeddedConfiguration configuration;
	private static ClientConfiguration configurationCS;


	ConfigXML c;

	public DataAccess()  {
		
		c=ConfigXML.getInstance();
		
		if (c.isDatabaseLocal()) {
			configuration = Db4oEmbedded.newConfiguration();
			configuration.common().activationDepth(c.getActivationDepth());
			configuration.common().updateDepth(c.getUpdateDepth());
			db=Db4oEmbedded.openFile(configuration, c.getDb4oFilename());
		} else {
			configurationCS = Db4oClientServer.newClientConfiguration();
			configurationCS.common().activationDepth(c.getActivationDepth());
			configurationCS.common().updateDepth(c.getUpdateDepth());
			configurationCS.common().objectClass(RuralHouse.class).cascadeOnDelete(true);
			db = Db4oClientServer.openClient(configurationCS,c.getDatabaseNode(), 
				 c.getDatabasePort(),c.getUser(),c.getPassword());
				
		}
		System.out.println("Creating DB4oManager instance => isDatabaseLocal: "+c.isDatabaseLocal()+" getDatabBaseOpenMode: "+c.getDataBaseOpenMode());
		}
	
	 class DB4oManagerAux {
		int offerNumber;
		DB4oManagerAux(int offerNumber){
			this.offerNumber=offerNumber;
		}
	}
	 
	 class DB4oManagerAux2{
		 int houseNumber;
		 DB4oManagerAux2(int houseNumber){
			 this.houseNumber = houseNumber;
		 }
	 }
	
	
	public void initializeDB(){
		
		System.out.println("Db initialized");
		RuralHouse rh1=new RuralHouse(1, "Ezkioko etxea","Ezkio");
		 RuralHouse rh2=new RuralHouse(2, "Etxetxikia","Iru�a");
		 RuralHouse rh3=new RuralHouse(3, "Udaletxea","Bilbo");
		 RuralHouse rh4=new RuralHouse(4, "Gaztetxea","Renteria");

		 db.store(rh1);
		 db.store(rh2);
		 db.store(rh3);
		 db.store(rh4);
		 
		 theDB4oManagerAux=new DB4oManagerAux(1);
		 db.store(theDB4oManagerAux);  
		 
		 
		 theDB4oManagerAux2 = new DB4oManagerAux2(5);
		 db.store(theDB4oManagerAux2);
		 
		 
		 db.commit();
		 
		
	}
	
	public Offer createOffer(RuralHouse ruralHouse, Date firstDay, Date lastDay, float price) {

	try {
		//Primero obtenemos de la BD la casa a la que queremos añadir la oferta. De esta forma tenemos la referencia correcta del objeto 
		//y al hacer el store no nos la duplica.
		RuralHouse proto = new RuralHouse(ruralHouse.getHouseNumber(),null,null);
		ObjectSet<RuralHouse> result = db.queryByExample(proto);
		RuralHouse rh=result.next();
		
		ObjectSet<DB4oManagerAux> res =db.queryByExample(DB4oManagerAux.class);
		ListIterator<DB4oManagerAux> listIter = res.listIterator();
		if (listIter.hasNext()) theDB4oManagerAux =  res.next();    
		
		Offer o=rh.createOffer(theDB4oManagerAux.offerNumber++,firstDay, lastDay, price);
		//Offer o=rh.createOffer(1,firstDay, lastDay, price);

		db.store(theDB4oManagerAux); // To store the new value for offerNumber
		db.store(rh);
		
		db.commit(); 
		return o;
	}
	catch (com.db4o.ext.ObjectNotStorableException e){
		System.out.println("Error: com.db4o.ext.ObjectNotStorableException in createOffer");
		return null;
	}
	}
	
	
	public Vector<RuralHouse> getAllRuralHouses() {

		 try {
			 RuralHouse proto = new RuralHouse(null,null,null);
			 ObjectSet<RuralHouse> result = db.queryByExample(proto);
			 Vector<RuralHouse> ruralHouses=new Vector<RuralHouse>();
			 while(result.hasNext()) 
				 ruralHouses.add(result.next());
			 return ruralHouses;
	     } finally {
	         //db.close();
	     }
	}
	 public Vector<Offer> getOffers( RuralHouse rh, Date firstDay,  Date lastDay) {
		 Vector<Offer> offers=new Vector<Offer>();
		 RuralHouse rhn = (RuralHouse) db.queryByExample(new RuralHouse(rh.getHouseNumber(),null,null)).next();	
		  offers=rhn.getOffers(firstDay, lastDay);
		  return offers;
	

	
	 }
	public boolean existsOverlappingOffer(RuralHouse rh,Date firstDay, Date lastDay) throws  OverlappingOfferExists{
		 try {

			RuralHouse rhn = (RuralHouse) db.queryByExample(new RuralHouse(rh.getHouseNumber(),null,null)).next();		
			if (rhn.overlapsWith(firstDay, lastDay)!=null) return true;
			else return false; 
	     } finally {
	         //db.close();
	     }
	}


	public void close(){
		db.close();
		System.out.println("DataBase closed");
	}
	
	//Iteracion 1
	public int getUserByUsernameAndPassword(String username, String password) 
	{
		try
		{
			/*En los registros no deja registrar un propietario o admin con el nombre de un cliente o al reves
			 * 
			 */
			
			Cliente protoCliente = new Cliente(username, password, null,null,null,null, null, 0, false, false);
			ObjectSet<Cliente> result = db.queryByExample(protoCliente);
			
			Propietario protoPropietario = new Propietario(username, password, null,null,null,null, null, 0, null, 0, false);
			ObjectSet<Propietario> result2 = db.queryByExample(protoPropietario);
			
			Admin protoAdmin = new Admin(username, password, 1, null, false, null, null, null, true);
			ObjectSet<Admin> result3 = db.queryByExample(protoAdmin);
			
			//Crear admin nombre: admin, pass: admin
			/*Admin a = new Admin("admin", "admin", 1, "v1", false, "2016/05/08 21:01:04", "Problema login", "Hola", true);
			db.store(a);
			db.commit();
			*/
			
			
			/*
			  En lugar de obtener todos los objetos de ese tipo y verificar cada uno de los objetos obtenidos
			  si tienen los atributos username y password, obtenemos si existe un objeto con esas caracteristicas
			  
			 	Si existe el objeto(solo puede existir un objeto con esos valores en los atributos
				ya que al registrar usuarios no permitimos que se repitan los nombres de usuario)
				devolvemos 1, 2 o 3 dependiendo de su rol
			*/
			if(result.size() == 1)
				return 1; //es un cliente
			else
			{
				if(result2.size() == 1)
					return 2; //es un propietario
				else 
				{
					if(result3.size() == 1)
						return 3;//es el admin
					else
						return 4;//no es cliente, propietario o admin
				}
					
			}
		}finally {
	         //db.close();
	     }
	}
	
	
	public boolean getUserByEmail(String email)
	{
		Cliente proto = new Cliente(null,null,null,null,email,null, null, 0, false, false);
		ObjectSet<Cliente> result = db.queryByExample(proto);
		
		/*	
		System.out.println("d "+result);
		for (Object o : result) {
			System.out.println(o); }
		*/
		System.out.println("size "+result.size());
		if(result.size() == 1)
			return false;
		else
			return true;
	}
	
	public boolean getUserByEmailOwner(String email)
	{
		Propietario proto = new Propietario(null,null,null,null,email,null,null,0, null, 0, false);
		ObjectSet<Propietario> result = db.queryByExample(proto);
			
		if(result.size() == 1)
			return false;
		else
			return true;
	}
	
	public boolean getUserByUserName(String userName)
	{
		 Cliente proto = new Cliente(userName,null,null,null,null,null, null, 0, false,false);
		 ObjectSet<Cliente> result = db.queryByExample(proto);
	
		if(result.size() == 1)
			return false;
		else
			return true;
	}
	
	public boolean getUserByUserNameOwner(String userName)
	{
		Propietario proto = new Propietario(userName,null,null,null,null,null,null,0,null,0,false);
		ObjectSet<Propietario> result = db.queryByExample(proto);
		
		
		if(result.size() == 1)
			return false;
		else
			return true;
	}
	
	public boolean getUserByDni(String dni) 
	{
		Cliente proto = new Cliente(null,null,null,null,null,dni, null, 0, false, false);
		ObjectSet<Cliente>  result = db.queryByExample(proto);
		
		if(result.size() == 1)
			return false;
		else
			return true;
	}
	
	public boolean getUserByDniOwner(String dni) 
	{
		Propietario proto = new Propietario(null,null,null,null,null,dni,null,0, null, 0, false);
		ObjectSet<Propietario> result = db.queryByExample(proto);
		
		if(result.size() == 1)
			return false;
		else
			return true;
	}
	
	
	public void storeUserData(String usuario, String password, String nombre, String apellidos, String email, String dni)
	{
		Cliente c = new Cliente(usuario, password, nombre, apellidos, email, dni, null, 0, false, false);
		
		db.store(c);
		db.commit();
	}
	
	public void storeOwnerData(String usuario, String password, String nombre, String apellidos, String email, String dni, String numC, int telef)
	{
		Propietario c = new Propietario(usuario, password, nombre, apellidos, email, dni, numC, telef, null, 0, false);
		
		db.store(c);
		db.commit();
	}
	
	
	public boolean storeRuralHouseData(String loc, String nombre, String desc, int numHab, int numPersonas, boolean[] general, boolean[] cocina, boolean[] sala, String user)
	{
		
		ObjectSet<DB4oManagerAux2> res1 =db.queryByExample(DB4oManagerAux2.class); //obtenemos todos los objetos DB4oManagerAux2 de la BD
		System.out.println("Size " + res1.size());
		ListIterator<DB4oManagerAux2> listIter = res1.listIterator();//obtener el puntero al iterador
		if (listIter.hasNext()) theDB4oManagerAux2 =  res1.next();//recorremos la lista de objetos hacia adelante hasta el final y movemos el iterador 
		
		RuralHouse rh = new RuralHouse(theDB4oManagerAux2.houseNumber++, nombre, desc, loc, numHab, numPersonas, general, cocina, sala,user, false);
		db.store(rh);
		
		db.store(theDB4oManagerAux2); // To store the new value for houseNumber
		
		db.commit();
		
		return true;
	}
	
	//Iteracion 2
	
	//Cliente: Actualiza los atributos relativos al token de reseteo de contraseña
	public void updateUserByEmail(String email, String code, long dateTime)
	{
		Cliente c = new Cliente(null, null, null, null, email, null, null, 0, false, false);
		ObjectSet<Cliente> result = db.queryByExample(c);
		
		//System.out.println("hola "+result);
		
		if (result.isEmpty()) 
		{
			System.out.println("No ha podido actualizarse el usuario con email " +email+ " porque no estaba en la BD"); 
		}
		else 
		{
			Cliente found = (Cliente) result.get(0);
			found.addCode(code);
			found.addDateTime(dateTime);
			found.addTokenUsed(false);
			db.store(found);
			db.commit();
			System.out.println("El usuario con email " +email+ " ha sido actualizado");
		}
	}
	
	
	//Propietario: Actualiza los atributos relativos al token de reseteo de contraseña
	public void updateUserByEmailOwner(String email, String code, long dateTime)
	{
		Propietario c = new Propietario(null,null,null,null,email,null,null,0,null,0, false);
		ObjectSet<Propietario> result = db.queryByExample(c);
		
		//System.out.println("hola "+result);
		
		if (result.isEmpty()) 
		{
			System.out.println("No ha podido actualizarse el usuario propietario con email " +email+ " porque no estaba en la BD"); 
		}
		else 
		{
			Propietario found = (Propietario) result.get(0);
			found.addCode(code);
			found.addDateTime(dateTime);
			found.addTokenUsed(false);
			db.store(found);
			db.commit();
			System.out.println("El usuario propietario con email " +email+ " ha sido actualizado");
		}
	}
	
	
	//Que es mejor hacerlo en 3 funciones y separar codigo o en una para ahorrar aperturas de db
	
	//Comprueba que el token existe para ese usuario
	public boolean checkExistenceOfUserAndToken(String email, String token)
	{
		/*	Obtengo el unico cliente con ese email (ya que al registrar los clientes, no 
			se ha permitido usar un mismo email para mas de un cliente)
			y posteriormente miro si alguna posicion 
			del ArrayList de tokens tiene el token pasado como parametro
		*/
		Cliente proto = new Cliente(null,null,null,null,email,null,null,0, false, false);
		ObjectSet<Cliente> result = db.queryByExample(proto);
		
		//System.out.println("email " + email);
		
		int i = 1;
		boolean b = false;
		
		//ArrayList con todos los token de ese usuario cliente
		ArrayList<String> code = result.get(0).getCode();
		
		//Recorrer el array hasta encontrar el token o haber recorrido todas las posiciones
		//del array sin encontrar el token
		do{
			//System.out.println("Token "+i+": " +code.get(i));
			//System.out.println(code.size());
			//Si el token del usuario en la BD y el recibido por email coinciden devolver true
			if(code.get(i).equals(token))
				b = true;
			i++;
		}while((b == false) && (i < code.size()));
		
		
		if(b == false)
			return false;
		else
			return true;
	}
	
	public boolean checkExistenceOfUserAndTokenOwner(String email, String token)
	{
		/*	Obtengo el unico propietario con ese email (ya que al registrar los propieatrios, no 
			se ha permitido usar un mismo email para mas de un propietario)
			y posteriormente miro si alguna posicion 
			del ArrayList de tokens tiene el token pasado como parametro
		*/
		Propietario proto = new Propietario(null,null,null,null,email,null,null,0,null,0, false);
		ObjectSet<Propietario> result = db.queryByExample(proto);
		
		int i = 1;
		boolean b = false;
		
		//ArrayList con todos los token de ese usuario propietario
		ArrayList<String> code = result.get(0).getCode();
		
		//Recorrer el array hasta encontrar el token o haber recorrido todas las posiciones
		//del array sin encontrar el token
		do{
			//System.out.println("Token "+i+": " +code.get(i));
			//System.out.println(code.size());
			//Si el token del usuario propieatrio en la BD y el recibido por email coinciden devolver true
			if(code.get(i).equals(token))
				b = true;
			i++;
		}while((b == false) && (i < code.size()));
		
		
		if(b == false)
			return false;
		else
			return true;
	}
	
	//Comprueba si el token ha expirado ya, para cambiar la contraseña.
	public long checkTokenExpired(String email, String token)
	{
		/*	Obtengo el unico cliente con ese email (ya que al registrar los clientes, no 
		 *	se ha permitido usar un mismo email para mas de un usuario)
		*/
		Cliente proto = new Cliente(null,null,null,null,email,null,null,0,false, false);
		ObjectSet<Cliente> result = db.queryByExample(proto);
		
		/*	Ahora ya sabemos que el token existe para ese usuario, asi que obtenemos 
		 * 	la posicion del token en el array de tokens.
		 * 	Los 3 ArrayList tienen la misma longitud, asi que si el token esta 
		 * 	en el array de tokens en la posicion X, la fecha de expiracion de ese token 
		 * 	estara en el array de fechas de expiracion en la posicion X
		 */
		
		//ArrayList de tokens con todos los token de ese usuario
		ArrayList<String> code = result.get(0).getCode();
		//Devuelve la posicion del token en el array de tokens
		int pos_token = code.indexOf(token);
		
		//Obtener la fecha de expiracion del token
		ArrayList<Long> token_expirations = result.get(0).getPassword_reset_expiration();
		long token_expiration = token_expirations.get(pos_token);
		
		return token_expiration;
	}
	
	//Comprueba si el token ha expirado ya, para cambiar la contraseña.
	public long checkTokenExpiredOwner(String email, String token)
	{
		/*	Obtengo el unico propietario con ese email (ya que al registrar los propietario, no 
		 *	se ha permitido usar un mismo email para mas de un usuario)
		*/
		Propietario proto = new Propietario(null,null,null,null,email,null,null,0,null,0, false);
		ObjectSet<Propietario> result = db.queryByExample(proto);
		
		/*	Ahora ya sabemos que el token existe para ese usuario propieatrio, asi que obtenemos 
		 * 	la posicion del token en el array de tokens.
		 * 	Los 3 ArrayList tienen la misma longitud, asi que si el token esta 
		 * 	en el array de tokens en la posicion X, la fecha de expiracion de ese token 
		 * 	estara en el array de fechas de expiracion en la posicion X
		 */
		
		//ArrayList de tokens con todos los token de ese usuario
		ArrayList<String> code = result.get(0).getCode();
		//Devuelve la posicion del token en el array de tokens
		int pos_token = code.indexOf(token);
		
		//Obtener la fecha de expiracion del token
		ArrayList<Long> token_expirations = result.get(0).getPassword_reset_expiration();
		long token_expiration = token_expirations.get(pos_token);
		
		return token_expiration;
	}
	
		
	//Comprueba si el token ha sido usado ya para cambiar la contraseña.
	//Si su estado es false, no se ha sido usado, si es true, ha sido usado
	public boolean checkTokenStatus(String email, String token)
	{
		/*	Obtengo el unico cliente con ese email (ya que al registrar los clientes, no 
		 *	se ha permitido usar un mismo email para mas de un usuario)
		*/
		Cliente proto = new Cliente(null,null,null,null,email,null,null,0,false, false);
		ObjectSet<Cliente> result = db.queryByExample(proto);
		
		/*	Ahora ya sabemos que el token existe para ese usuario, asi que obtenemos 
		 * 	la posicion del token en el array de tokens.
		 * 	Los 3 ArrayList tienen la misma longitud, asi que si el token esta 
		 * 	en el array de tokens en la posicion X, el estado de ese token estara 
		 *	en el array de estados en la posicion X
		 */
		
		//ArrayList de tokens con todos los token de ese usuario
		ArrayList<String> code = result.get(0).getCode();
		//Devuelve la posicion del token en el array de tokens
		int pos_token = code.indexOf(token);
		
		//Obtener el estado del token
		ArrayList<Boolean> token_states = result.get(0).getToken_used();
		boolean token_status = token_states.get(pos_token);
		
		return token_status;
	}
	
	//Comprueba si el token ha sido usado ya para cambiar la contraseña.
	//Si su estado es false, no se ha sido usado, si es true, ha sido usado
	public boolean checkTokenStatusOwner(String email, String token)
	{
		/*	Obtengo el unico propietario con ese email (ya que al registrar los clientes, no 
		 *	se ha permitido usar un mismo email para mas de un usuario)
		*/
		Propietario proto = new Propietario(null,null,null,null,email,null,null,0,null,0, false);
		ObjectSet<Propietario> result = db.queryByExample(proto);
		
		/*	Ahora ya sabemos que el token existe para ese usuario propieatrio, asi que obtenemos 
		 * 	la posicion del token en el array de tokens.
		 * 	Los 3 ArrayList tienen la misma longitud, asi que si el token esta 
		 * 	en el array de tokens en la posicion X, el estado de ese token estara 
		 *	en el array de estados en la posicion X
		 */
		
		//ArrayList de tokens con todos los token de ese usuario
		ArrayList<String> code = result.get(0).getCode();
		//Devuelve la posicion del token en el array de tokens
		int pos_token = code.indexOf(token);
		
		//Obtener el estado del token
		ArrayList<Boolean> token_states = result.get(0).getToken_used();
		boolean token_status = token_states.get(pos_token);
		
		return token_status;
	}
	
	
	//Resetea la contraseña del usuario
	public void resetPassword(String email, String token, String password)
	{
		Cliente proto = new Cliente(null,null,null,null,email,null,null,0,false, false);
		ObjectSet<Cliente> result = db.queryByExample(proto);
		
		if (result.isEmpty()) 
		{
			System.out.println("El usuario con email "+ email +" no ha podido actualizarse porque no estaba en la BD"); 
		}
		else 
		{
			Cliente found = (Cliente) result.get(0);
			found.setPassword(password);
			
			//ArrayList de tokens con todos los token de ese usuario
			ArrayList<String> code = found.getCode();
			//Devuelve la posicion del token en el array de tokens
			int pos_token = code.indexOf(token);
			//cambiamos el estado del token a usado
			ArrayList<Boolean> token_states = result.get(0).getToken_used();
			token_states.set(pos_token, true);
			
			db.store(found);
			db.commit();
			System.out.println("La contraseña del usuario " + email + " ha sido actualizada");
		}
	}
	
	//Resetea la contraseña del usuario propieatrio
	public void resetPasswordOwner(String email, String token, String password)
	{
		Propietario proto = new Propietario(null,null,null,null,email,null,null,0,null,0, false);
		ObjectSet<Propietario> result = db.queryByExample(proto);
		
		if (result.isEmpty()) 
		{
			System.out.println("El usuario con email "+ email +" no ha podido actualizarse porque no estaba en la BD"); 
		}
		else 
		{
			Propietario found = (Propietario) result.get(0);
			found.setPassword(password);
			
			//ArrayList de tokens con todos los token de ese usuario
			ArrayList<String> code = found.getCode();
			//Devuelve la posicion del token en el array de tokens
			int pos_token = code.indexOf(token);
			//cambiamos el estado del token a usado
			ArrayList<Boolean> token_states = result.get(0).getToken_used();
			token_states.set(pos_token, true);
			
			db.store(found);
			db.commit();
			System.out.println("La contraseña del usuario " + email + " ha sido actualizada");
		}
	}
	
	//Devuelve todos los clientes bloqueados
	public String[] getAllBloquedClients()
	{
		Cliente protoCliente = new Cliente(null, null, null,null,null,null, null, 0, false, true);
		ObjectSet<Cliente> result = db.queryByExample(protoCliente);
		
		/*for (Object o : result) 
		System.out.println(o);
	
		System.out.println("result "+result.size());
		*/
		
		int clientsNumber = result.size();
		String[] emailList = new String[clientsNumber]; //Array de Strings con todos los emails de los clientes

		for(int i=0; i<clientsNumber; i++)
		{
			Cliente found = (Cliente) result.get(i);
			emailList[i] = found.getEmail();
			//System.out.println(emailList[i]);
		}		
		
		return emailList;
	}
	
	//Native query
	//Devuelve todos los clientes no bloqueados
	public String[] getAllUnbloquedClients()
	{
		List<Cliente> result = db.query(new Predicate<Cliente>() {
			  public boolean match(Cliente client) {
			    return client.isBloqueado() == false; 
			    
			  }
		}); 
		  
		
		int clientsNumber = result.size();
		String[] emailList = new String[clientsNumber]; //Array de Strings con todos los emails de los clientes

		for(int i=0; i<clientsNumber; i++)
		{
			Cliente found = (Cliente) result.get(i);
			emailList[i] = found.getEmail();
		}		
		
		return emailList;
	}
	
	//Cambia el estado de un usuario
	//Si estado = true, el estado es no bloqueado y lo bloqueamos
	//Si estado = false, el estado es bloqueado y lo desbloqueamos
	public void setClientStatus(String email, boolean estado)
	{
		//prototipo
		Cliente protoCliente = new Cliente(null, null, null,null,email,null, null, 0, false, false);
		ObjectSet<Cliente> result = db.queryByExample(protoCliente);
		
		if (result.isEmpty()) 
		{
			System.out.println("No ha podido actualizarse el estado del usuario cliente con email " +email+ " porque no estaba en la BD"); 
		}
		else 
		{
			//Actualizamos el estado
			Cliente found = (Cliente) result.get(0);
			found.setBloqueado(estado);
			db.store(found);
			db.commit();
			
			System.out.print("El usuario cliente con email " +email+ " ha sido ");
			if(estado == false)
				System.out.print("desbloqueado");
			else
				System.out.print("bloqueado");
		}
	}
	
	public boolean isClientBlocked(String username)
	{
		Cliente proto = new Cliente(username,null,null,null,null,null,null,0, false, false);
		ObjectSet<Cliente> result = db.queryByExample(proto);
		
		boolean status = result.get(0).isBloqueado();
			
		return status;
	}
	
	//ITERACION 2
		public Vector<RuralHouse> mostrandoAccess(int x){  //pasandole el numero nos muestra por cuidades, numHabitaciones o numPersonas
			if(x==0){
				//muestra alfabeticamente por ciudades
				/*RuralHouse casilla=new RuralHouse(null,null,null);
				ObjectSet<RuralHouse>  result = db.queryByExample(casilla);
				Vector<RuralHouse> casas=new Vector<RuralHouse>();
				while(result.hasNext()){
					casas.add(result.next());
				}*/
				/*Vector<RuralHouse> casas=new Vector<RuralHouse>();
				casas=getAllRuralHouses();
				//metemos todas las casas en un array y luego lo ordenamos segun el criterio
				int n=casas.size();
				for(int i=0;i<n-1;i++){
					for(int j=0;j==n-i-1;j++){
						if(casas.get(j+1).getCity().compareTo(casas.get(j).getCity())<0){
							RuralHouse aux=casas.get(j+1); 
							casas.add(j+1,casas.get(j)); 
							casas.add(j, aux);
							
						}
					}*/
				Vector<RuralHouse> casas=new Vector<RuralHouse>();
				
				return casas;
				//}		
			} //por habitaciones
			if(x==1){
				/*RuralHouse casilla=new RuralHouse(null,null,null);
				ObjectSet<RuralHouse>  result = db.queryByExample(casilla);
				Vector<RuralHouse> casas=new Vector<RuralHouse>();
				while(result.hasNext()){
					casas.add(result.next());
				}*/
				//metemos todas las casas en un array y luego lo ordenamos segun el criterio
				Vector<RuralHouse> casas=new Vector<RuralHouse>();
				casas=getAllRuralHouses();
				int n=casas.size();
				for(int i=0;i<n-1;i++){
					for(int j=0;j==n-i-1;j++){
						if(casas.get(j+1).getNumeroHabitaciones()<casas.get(j).getNumeroHabitaciones()){
							RuralHouse aux=casas.get(j+1);
							casas.add(j+1,casas.get(j)); 
							casas.add(j, aux);
							
						}
					}		}
				return casas;
			}
			if(x==2){
				//por personas
				/*RuralHouse casilla=new RuralHouse(null,null,null);
				ObjectSet<RuralHouse>  result = db.queryByExample(casilla);
				Vector<RuralHouse> casas=new Vector<RuralHouse>();
				while(result.hasNext()){
					casas.add(result.next());
				}*/
				Vector<RuralHouse> casas=new Vector<RuralHouse>();
				casas=getAllRuralHouses();
				//metemos todas las casas en un array y luego lo ordenamos segun el criterio
				int n=casas.size();
				for(int i=0;i<n-1;i++){
					for(int j=0;j==n-i-1;j++){
						if(casas.get(j+1).getNumeroPersonas()<casas.get(j).getNumeroPersonas()){
							RuralHouse aux=casas.get(j+1);
							casas.add(j+1,casas.get(j)); 
							casas.add(j, aux);
			}
			}
				}
				return casas;
		}else{
			Vector<RuralHouse> z=new Vector<RuralHouse>();
			return z;
		}
		}
		
		public Vector<Offer> devuelveLasOfertas(RuralHouse x){
			Offer z=new Offer(0,null,null,0,x,null, 0); //busco las ofertas de esa casa(cualquier oferta)
			ObjectSet<Offer>  result = db.queryByExample(z);
			Vector<Offer> nuevo=new Vector<Offer>();
			while(result.hasNext()){ //anado las ofertas a un object set y luego las meto en un vector para poder devolverlas
				nuevo.add(result.next());
			}
			return nuevo;
			
		}
		public Vector<RuralHouse> devuelveLasCasas(String s){
			RuralHouse x=new RuralHouse(null,null,s);
			ObjectSet<RuralHouse>  result = db.queryByExample(x);
			Vector<RuralHouse> casas=new Vector<RuralHouse>();
			while(result.hasNext()){
				casas.add(result.next());
			}
			return casas;
			
		}
		
		//ITERACION 3
		
		/*
		public Offer creandoOferta(RuralHouse casaParaOferta, Date firstDay, Date lastDay, float precio,String user)
		{
			int numOferta = calcularNumeroOferta();
			Offer x = new Offer(numOferta, firstDay, lastDay, precio, casaParaOferta, null, 0);
			//System.out.println("oferta "+x);
			System.out.println("fd "+firstDay);
			db.store(x);
			db.commit();
			
		}
		*/
		
		//Coge todas las ofertas de la BD y devuelve el total de ofertas + 1
		//Al eliminar una oferta no se borra de la BD, sino que cambia su estado a eliminada por el propietario,
		//por eso no se puede repetir el numero de oferta
		/*public int calcularNumeroOferta()
		{
			List<Offer> result = db.query(new Predicate<Offer>() {
				  public boolean match(Offer offer) {
				    return offer.getOfferNumber() >= 0; 
				  }
			});
			
			return result.size()+1;
		}
		*/
		
		
		
		//Iteracion 3
		
		public void saveMessage(int msgNumber, String msgUser, boolean userRol, String msgDate, String msgAsunto, String msgCuerpo)
		{
			//Crear prototipo admin
			Admin protoAdmin = new Admin("admin","admin",1,null,false,null,null,null,true);
			ObjectSet<Admin> result = db.queryByExample(protoAdmin);
			
			if (result.isEmpty()) 
			{
				System.out.println("El admin no estaba en la BD"); 
			}else
			{
				Admin found = (Admin)result.get(0); //Solo hay un admin en total
					
				found.addMsgNumber(msgNumber);
				found.addMsgUser(msgUser);
				found.addMsgUserRol(userRol);
				found.addMsgTime(msgDate);
				found.addMsgAsunto(msgAsunto);
				found.addMsgText(msgCuerpo);
				found.addMsgRead(false);
			
				db.store(found);
				db.commit();
				System.out.println("El mensaje se ha guardado en el buzon del administrador");
			}
		}
		
		public boolean getUserRolByUsername(String username) 
		{
			
			Cliente protoCliente = new Cliente(username, null, null,null,null,null, null, 0, false, false);
			ObjectSet<Cliente> result = db.queryByExample(protoCliente);
			
			if(result.size() == 1)
				return false; //es un cliente
			else
				return true; //es un propietario
		}
		
		public int getAdministratorsLastMessageNumber()
		{
			int numLastMessage = 0; //Numero total de mensajes
			
			//Crear prototipo admin
			Admin proto = new Admin("admin","admin",1,null,false,null,null,null,true);
			ObjectSet<Admin> result = db.queryByExample(proto);
			
			if (result.isEmpty()) 
			{
				System.out.println("El admin no estaba en la BD"); 
			}else
			{
				Admin found = (Admin)result.get(0); //Solo hay un admin en total
				numLastMessage = found.getMsgNumber().size();//null
			
				System.out.println("Enviado el numero total de mensajes del admin");
			}
			return numLastMessage;
		}
		
		//obtener datos mensajes
		public int[] getMessageNumbers()
		{
			//Obtengo el admin
			Admin proto = new Admin("admin","admin",1,null,false,null,null,null,true);//admin con el primer mensaje
			ObjectSet<Admin> result = db.queryByExample(proto);
			
			//ArrayList de numeros de mensaje con todos los numeros de mensaje
			//que tiene el admin
			ArrayList<Integer> msgNumbers = result.get(0).getMsgNumber();
			
			//Convertir ArrayList<Integer> a array int
			int[] num = new int[msgNumbers.size()];
			
		    for (int i=0; i < num.length; i++)
		    {
		        num[i] = msgNumbers.get(i).intValue();
		    }
			
			return num;
		}
		
		public String[] getMessageDates()
		{
			//Obtengo el admin
			Admin proto = new Admin("admin","admin",1,null,false,null,null,null,true);
			ObjectSet<Admin> result = db.queryByExample(proto);
			
			//ArrayList de fechas de mensaje con todos las fechas de mensaje
			//que tiene el admin
			ArrayList<String> msgDates = result.get(0).getMsgDateHour();
			
			//Convertir ArrayList<Integer> a array String
			String[] dates = new String[msgDates.size()];
			
		    for (int i=0; i < dates.length; i++)
		    {
		    	dates[i] = msgDates.get(i).toString();
		    }
			
			return dates;
		}
		
		public String[] getMessageUsers()
		{
			//Obtengo el admin
			Admin proto = new Admin("admin","admin",1,null,false,null,null,null,true);
			ObjectSet<Admin> result = db.queryByExample(proto);
			
			//ArrayList de users de mensaje con todos las users de mensaje
			//que tiene el admin
			ArrayList<String> msgUsers = result.get(0).getMsgUser();
			
			//Convertir ArrayList<Integer> a array String
			String[] users = new String[msgUsers.size()];
			
		    for (int i=0; i < users.length; i++)
		    {
		    	users[i] = msgUsers.get(i).toString();
		    }
			
			return users;
		}
		
		public boolean[] getMessageUsersRoles()
		{
			//Obtengo el admin
			Admin proto = new Admin("admin","admin",1,null,false,null,null,null,true);
			ObjectSet<Admin> result = db.queryByExample(proto);
			
			//ArrayList de roles de users de mensaje con todos las users de mensaje
			//que tiene el admin
			ArrayList<Boolean> msgUserRoles = result.get(0).getMsgUserRol();
			
			//Convertir ArrayList<Integer> a array String
			boolean[] userRoles = new boolean[msgUserRoles.size()];
			
		    for (int i=0; i < userRoles.length; i++)
		    {
		    	userRoles[i] = msgUserRoles.get(i);
		    }
			
			return userRoles;
		}
		
		public String[] getMessageAsunto()
		{
			//Obtengo el admin
			Admin proto = new Admin("admin","admin",1,null,false,null,null,null,true);
			ObjectSet<Admin> result = db.queryByExample(proto);
			
			//ArrayList de users de asuntos con todos las mensajes de mensaje
			//que tiene el admin
			ArrayList<String> msgAsuntos = result.get(0).getMsgAsunto();
			
			//Convertir ArrayList<Integer> a array String
			String[] userAsuntos = new String[msgAsuntos.size()];
			
		    for (int i=0; i < userAsuntos.length; i++)
		    {
		    	userAsuntos[i] = msgAsuntos.get(i).toString();
		    }
			
			return userAsuntos;
		}
		
		public String[] getMessageTexto()
		{
			//Obtengo el admin
			Admin proto = new Admin("admin","admin",1,null,false,null,null,null,true);
			ObjectSet<Admin> result = db.queryByExample(proto);
			
			//ArrayList de texto de mensaje con todos las mensajes de mensaje
			//que tiene el admin
			ArrayList<String> msgTexto = result.get(0).getMsgText();
			
			//Convertir ArrayList<Integer> a array String
			String[] userTexto = new String[msgTexto.size()];
			
		    for (int i=0; i < userTexto.length; i++)
		    {
		    	userTexto[i] = msgTexto.get(i).toString();
		    }
			
			return userTexto;
		}
		
		public boolean[] getMessageRead()
		{
			//Obtengo el admin
			Admin proto = new Admin("admin","admin",1,null,false,null,null,null,true);
			ObjectSet<Admin> result = db.queryByExample(proto);
			
			//ArrayList de estados de mensaje con todos las mensaje
			//que tiene el admin
			ArrayList<Boolean> msgUserRead = result.get(0).getMsgRead();
			
			//Convertir ArrayList<Integer> a array String
			boolean[] msgRead = new boolean[msgUserRead.size()];
			
		    for (int i=0; i < msgRead.length; i++)
		    {
		    	msgRead[i] = msgUserRead.get(i);
		    }
			
			return msgRead;
		}
		
		//cambiar estado mensajes no leidos a leidos
		public void setUnreadMsgNewStatus()
		{
			//Obtengo el admin con todos los mensajes y cambio el estado read a true
			Admin proto = new Admin("admin","admin",1,null,false,null,null,null,true);
			ObjectSet<Admin> result = db.queryByExample(proto);
			
			//Actualizamos el estado
			Admin found = (Admin) result.get(0);
			
			//crear array del tamaño del array de mensajes con todos los mensajes true
			ArrayList<Boolean> msgRead = new ArrayList<Boolean>();
			
			int num = found.getMsgUser().size();//Num total mensajes
			for(int i=0;i<num; i++)
				msgRead.add(true);
			
			found.setMsgRead(msgRead);
			db.store(found);
			db.commit();
			
			System.out.print("El estado de los mensajes no leidos del admin ha sido cambiado a leidos");
		}
		
		//Recibe el nombre de usuario del usuario y devuelve su email
		public String getEmailByUserName(String usrName, String userRol)
		{
			String email = "";
			
			if(userRol.equals("cliente"))
			{
				Cliente protoCliente = new Cliente(usrName, null, null,null,null,null, null, 0, false, false);
				ObjectSet<Cliente> result = db.queryByExample(protoCliente);
				
				email = result.get(0).getEmail();
				
			}else
			{
				Propietario protoPropietario = new Propietario(usrName, null, null,null,null,null, null, 0, null, 0, false);
				ObjectSet<Propietario> result = db.queryByExample(protoPropietario);
				
				email = result.get(0).getEmail();
			}
			
			return email;
		}
		
		public ArrayList<Offer> buscarOfertasFiltros(final String loc, final int precioMax, final int precioMin, final int numHab, final int numPersonas,final Date dInicio, final Date dFin, final boolean[] general, final boolean[] cocina, final boolean[] sala)
		{			
			List<Offer> result = db.query(new Predicate<Offer>() {
				  public boolean match(Offer offer) {	
					  return
				    		offer.getRuralHouse().getCity().equals(loc) &&
				    		(offer.getPrice() <= precioMax) &&
				    		(offer.getPrice() >= precioMin) &&
				    		(offer.getRuralHouse().getNumeroHabitaciones() == numHab) &&
				    		(offer.getRuralHouse().getNumeroPersonas() == numPersonas) && 
				    		(offer.getFirstDay().equals(dInicio)) &&
				    		(offer.getLastDay().equals(dFin)) &&
				    		(offer.getRuralHouse().getGeneral()[0] == general[0]) &&
				    		(offer.getRuralHouse().getGeneral()[1] == general[1]) &&
				    		(offer.getRuralHouse().getGeneral()[2] == general[2]) &&
				    		(offer.getRuralHouse().getGeneral()[3] == general[3]) &&
				    		(offer.getRuralHouse().getGeneral()[4] == general[4]) &&
				    		(offer.getRuralHouse().getGeneral()[5] == general[5]) &&
				    		(offer.getRuralHouse().getGeneral()[6] == general[6]) &&
				    		(offer.getRuralHouse().getGeneral()[7] == general[7]) &&
				    		(offer.getRuralHouse().getCocina()[0] == cocina[0]) &&
				    		(offer.getRuralHouse().getCocina()[1] == cocina[1]) &&
				    		(offer.getRuralHouse().getCocina()[2] == cocina[2]) &&
				    		(offer.getRuralHouse().getCocina()[3] == cocina[3]) &&
				    		(offer.getRuralHouse().getSala()[0] == sala[0]) &&
				    		(offer.getRuralHouse().getSala()[1] == sala[1]) &&
				    		(offer.getRuralHouse().getSala()[2] == sala[2]); 
				  }
			}); 
			
			ArrayList<Offer> ofertas= new ArrayList<Offer>(result);
			System.out.println("e"+ofertas);
			return ofertas;
		}
		
		
		public ArrayList<RuralHouse> getRuralHouseByOwnerId(final String user)
		{
			//query nativa
			List<RuralHouse> result = db.query(new Predicate<RuralHouse>() {
				  public boolean match(RuralHouse rh) {
				    return rh.getUser().equals(user) && !rh.isEliminida(); 
				    
				  }
			}); 
			
			ArrayList<RuralHouse> listaRuralHouses = new ArrayList<RuralHouse>(result);
			
			return listaRuralHouses;
		}
		
		
		public ArrayList<Offer> getOffersByRHId(final int idCasa)
		{
			List<Offer> result = db.query(new Predicate<Offer>() {
				  public boolean match(Offer of) {
				    return of.getRuralHouse().getHouseNumber().equals(idCasa); 
				    
				  }
			}); 
			
			ArrayList<Offer> listaOffers = new ArrayList<Offer>(result);
			
			return listaOffers;
		}
		
		public RuralHouse getRHById(final int idCasa)
		{
			List<RuralHouse> result = db.query(new Predicate<RuralHouse>() {
				  public boolean match(RuralHouse rh) {
				    return rh.getHouseNumber()==idCasa; 
				    
				  }
			}); 
			
			RuralHouse rh = result.get(0);	
			return rh;
		}
		
		
		public void eliminarOferta(final int idOferta)
		{
			//Obtengo la oferta con dicho id 
			List<Offer> result = db.query(new Predicate<Offer>() {
				  public boolean match(Offer of) {
				    return of.getOfferNumber() == idOferta; 
				  }
			}); 

			
			//Actualizamos el estado
			Offer found = (Offer) result.get(0);
			found.setEstadoOferta(2);
			db.store(found);
			db.commit();
		
			System.out.print("El estado de la oferta ha sido cambiado a eliminada");
		}
		
		
		public Offer getOfferByOfferId(final int idOferta)
		{
			List<Offer> result = db.query(new Predicate<Offer>() {
				  public boolean match(Offer of) {
				    return of.getOfferNumber() == idOferta; 
				    
				  }
			}); 
			
			Offer of = result.get(0);	
			
			return of;
		}
		
		
		public void modificarOferta(final int idOferta, Date dateInicio, Date dateFin, float nuevoPrecio, int intEstadoSel)
		{ 	
			List<Offer> result = db.query(new Predicate<Offer>() {
					  public boolean match(Offer of) {
					    return of.getOfferNumber() == idOferta; 
					  }
				}); 
			
			if (result.isEmpty()) 
			{
				System.out.println("No ha podido actualizarse la oferta porque no se ha encontrado en la BD"); 
				//return true;
			}
			else 
			{
				Offer found = (Offer) result.get(0);
				found.setFirstDay(dateInicio);
				found.setLastDay(dateFin);
				found.setPrice(nuevoPrecio);
				found.setEstadoOferta(intEstadoSel);
				db.store(found);
				db.commit();
				System.out.println("La oferta se ha actualizado");
				//return false;
			}
		}
		
		
		//Si se intenta crear una oferta para una casa en un periodo de fechas que ya hay una oferta no dejar crearla
		//A: fecha pasamos funcion:  firstDay - lastDay
		//B: cada fecha de la BD: offer.getFirstDay() - offer.getLastDay()
		//(StartA <= EndB) and (EndA >= StartB) -> Overlap
		public boolean existeOfertasEnEsaCasayFecha(final int idRH, final Date firstDay, final Date lastDay, float price){
			
			List<Offer> result = db.query(new Predicate<Offer>() {
				  public boolean match(Offer offer) {
				    return 
				    	(offer.getRuralHouse().getHouseNumber() == idRH) && 
				    	((offer.getEstadoOferta() == 0) || (offer.getEstadoOferta() == 1)) && //oferta no eliminada
				    	(firstDay.before(offer.getLastDay())) &&
				    	(lastDay.after(offer.getFirstDay()));
				  }
			});
						
			System.out.println("r "+result);
			if(result.size()!=0) //Se solapa, por lo tanto no dejar crear oferta
				return true;
			else
				return false;	
		}
		
		//Si se intenta eliminar una casa que tiene adquiridas ofertas para el futuro no dejar eliminarla
		//Si hay gente en ese momento en la casa dejar eliminarla
		//A: fecha actual
		//B: cada fecha de la BD: offer.getFirstDay() - offer.getLastDay()
		public boolean existenOfertasFuturasReservadasEnEsaCasa(final int idCasa){
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");      
			Date currentDateWithoutTime = null;
			try {
				currentDateWithoutTime = sdf.parse(sdf.format(new Date()));
				//System.out.println("c"+currentDateWithoutTime);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			final Date currentDate = currentDateWithoutTime;
			System.out.println("date "+currentDate);
			
			List<Offer> result = db.query(new Predicate<Offer>() {
				  public boolean match(Offer offer) {
				    return 
				    	(offer.getRuralHouse().getHouseNumber() == idCasa) && //Hay ofertas en esa casa
				    	(offer.getEstadoOferta() == 1)  && //Es oferta adquirida
				    	(offer.getFirstDay().after(currentDate) || offer.getFirstDay().equals(currentDate)); //Es futura la oferta
				  }
			});
			
			System.out.println("tamaño "+result.size());	
			if(result.size()!=0) //Hay futuras reservas, por lo tanto no dejar borrar la casa
				return true;
			else
				return false;	
		}
		
		
		public void eliminarCasa(final int idCasa)
		{
			List<RuralHouse> result = db.query(new Predicate<RuralHouse>() {
				  public boolean match(RuralHouse rh) {
				    return rh.getHouseNumber() == idCasa; 
				  }
			}); 
		
			if (result.isEmpty()) 
			{
				System.out.println("No ha podido eliminarse la casa porque no se ha encontrado en la BD"); 
				//return true;
			}
			else 
			{
				RuralHouse found = (RuralHouse) result.get(0);
				found.setEliminida(true);
				db.store(found);
				db.commit();
				System.out.println("La casa se ha eliminado");
				//return false;
			}
		}
		
		public boolean realizarReservaRH(final int idOfertaSeleccionada, String user)
		{
			boolean b = true;
			List<Offer> result = db.query(new Predicate<Offer>() {
				  public boolean match(Offer of) {
				    return of.getOfferNumber() == idOfertaSeleccionada; 
				  }
			}); 
		
			if (result.isEmpty()) 
			{
				System.out.println("No se ha encontrado la oferta en la BD"); 
				b=false;
			}
			else 
			{
				Offer found = (Offer) result.get(0);
				found.setUser(user);
				found.setEstadoOferta(1);
				db.store(found);
				db.commit();
				System.out.println("La oferta se ha adquirido");
				b=true;
			}
			
			return b;
		}
}
