package dataAccess;


import java.util.Date;
import java.util.ListIterator;
import java.util.Vector;

import javax.jws.WebMethod;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.cs.Db4oClientServer;
import com.db4o.cs.config.ClientConfiguration;

import configuration.ConfigXML;
//import domain.Booking;
import domain.Offer;
import domain.Propietario;
import domain.RuralHouse;
import exceptions.OverlappingOfferExists;

import java.util.List;
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
			Cliente protoCliente = new Cliente(username, password, null,null,null,null);
			ObjectSet<Cliente> result = db.queryByExample(protoCliente);
			
			Propietario protoPropietario = new Propietario(username, password, null,null,null,null, null, 0);
			ObjectSet<Propietario> result2 = db.queryByExample(protoPropietario);
			

			/*
			  En lugar de obtener todos los objetos de ese tipo y verificar cada uno de los objetos obtenidos
			  si tienen los atributos username y password, obtenemos si existe un objeto con esas caracteristicas
			  
			 	Si existe el objeto(solo puede existir un objeto con esos valores en los atributos
				ya que al registrar usuarios no permitimos que se repitan los nombres de usuario)
				devolvemos 1 o 2 dependiendo de su rol
			*/
			if(result.size() == 1)
				return 1; //es un cliente
			else
			{
				if(result2.size() == 1)
					return 2; //es un propietario
				else 
					return 3;//no es cliente ni propietario
			}
		}finally {
	         //db.close();
	     }
	}
	
	
	public boolean getUserByEmail(String email)
	{
		Cliente proto = new Cliente(null,null,null,null,email,null);
		ObjectSet<Cliente> result = db.queryByExample(proto);
		
		if(result.size() == 1)
			return false;
		else
			return true;
	}
	
	public boolean getUserByEmailOwner(String email)
	{
		Propietario proto = new Propietario(null,null,null,null,email,null,null,0);
		ObjectSet<Propietario> result = db.queryByExample(proto);
			
		if(result.size() == 1)
			return false;
		else
			return true;
	}
	
	public boolean getUserByUserName(String userName)
	{
		 Cliente proto = new Cliente(userName,null,null,null,null,null);
		 ObjectSet<Cliente> result = db.queryByExample(proto);
	
		if(result.size() == 1)
			return false;
		else
			return true;
	}
	
	public boolean getUserByUserNameOwner(String userName)
	{
		Propietario proto = new Propietario(userName,null,null,null,null,null,null,0);
		ObjectSet<Propietario> result = db.queryByExample(proto);
		
		
		if(result.size() == 1)
			return false;
		else
			return true;
	}
	
	public boolean getUserByDni(String dni) 
	{
		Cliente proto = new Cliente(null,null,null,null,null,dni);
		ObjectSet<Cliente>  result = db.queryByExample(proto);
		
		if(result.size() == 1)
			return false;
		else
			return true;
	}
	
	public boolean getUserByDniOwner(String dni) 
	{
		Propietario proto = new Propietario(null,null,null,null,null,dni,null,0);
		ObjectSet<Propietario> result = db.queryByExample(proto);
		
		if(result.size() == 1)
			return false;
		else
			return true;
	}
	
	
	public void storeUserData(String usuario, String password, String nombre, String apellidos, String email, String dni)
	{
		Cliente c = new Cliente(usuario, password, nombre, apellidos, email, dni);
		
		db.store(c);
		db.commit();
	}
	
	public void storeOwnerData(String usuario, String password, String nombre, String apellidos, String email, String dni, String numC, int telef)
	{
		Propietario c = new Propietario(usuario, password, nombre, apellidos, email, dni, numC, telef);
		
		db.store(c);
		db.commit();
	}
	
	
	public void storeRuralHouseData(String loc, String desc, int numHab, int numPersonas, boolean[] general, boolean[] cocina, boolean[] sala)
	{
		
		ObjectSet<DB4oManagerAux2> res1 =db.queryByExample(DB4oManagerAux2.class); //obtenemos todos los objetos DB4oManagerAux2 de la BD
		System.out.println("Size " + res1.size());
		ListIterator<DB4oManagerAux2> listIter = res1.listIterator();//obtener el puntero al iterador
		if (listIter.hasNext()) theDB4oManagerAux2 =  res1.next();//recorremos la lista de objetos hacia adelante hasta el final y movemos el iterador 
		
		RuralHouse rh = new RuralHouse(theDB4oManagerAux2.houseNumber++, desc, loc, numHab, numPersonas, general, cocina, sala);
		db.store(rh);
		
		db.store(theDB4oManagerAux2); // To store the new value for houseNumber
		
		db.commit();
	}
}
