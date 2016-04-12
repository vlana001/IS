package businessLogic;

import java.io.File;

import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.Vector;

import javax.jws.WebMethod;
import javax.jws.WebService;


import com.db4o.ObjectSet;

import configuration.ConfigXML;
import dataAccess.DataAccess;

//import domain.Booking;
import domain.Offer;
import domain.RuralHouse;
import exceptions.BadDates;
import exceptions.OverlappingOfferExists;
import domain.Cliente;


import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.PasswordAuthentication;
 
//Service Implementation
@WebService(endpointInterface = "businessLogic.ApplicationFacadeInterfaceWS")
public class FacadeImplementationWS  implements ApplicationFacadeInterfaceWS {

	private DataAccess nivelDatos;
	/**
	 * 
	 */

	//Vector<Owner> owners;
	//Vector<RuralHouse> ruralHouses;
	//DataAccessInterface dB4oManager;
 

	public FacadeImplementationWS()  {
		
		System.out.println("Executing FacadeImplementationWS");
		ConfigXML c=ConfigXML.getInstance();

		if (c.getDataBaseOpenMode().equals("initialize") && (c.isDatabaseLocal())) {
			System.out.println("File deleted");
			new File(c.getDb4oFilename()).delete();
		}
		DataAccess dB4oManager=new DataAccess();
		if (c.getDataBaseOpenMode().equals("initialize")) {
			dB4oManager.initializeDB();
			}
		else {// check if it is opened
			 //Vector<Owner> owns=dataAccess.getOwners();
		}
		dB4oManager.close();
	}
	
	

	/**
	 * This method creates an offer with a house number, first day, last day and price
	 * 
	 * @param House
	 *            number, start day, last day and price
	 * @return the created offer, or null, or an exception
	 */
	public Offer createOffer(RuralHouse ruralHouse, Date firstDay, Date lastDay,
			float price) throws OverlappingOfferExists, BadDates {
		System.out.println("Executing createOffer");
		System.out.println(firstDay);
		System.out.println(lastDay);

		
		DataAccess dB4oManager=new DataAccess();
		Offer o=null;
		if (firstDay.compareTo(lastDay)>=0) throw new BadDates();
		
		boolean b = dB4oManager.existsOverlappingOffer(ruralHouse,firstDay,lastDay); // The ruralHouse object in the client may not be updated
		if (!b) o=dB4oManager.createOffer(ruralHouse,firstDay,lastDay,price);		

		dB4oManager.close();
		
		return o;
	}
 

	
		
	public Vector<RuralHouse> getAllRuralHouses()  {
		System.out.println("Start: getAllRuralHouses");

		DataAccess dB4oManager=new DataAccess();

		Vector<RuralHouse>  ruralHouses=dB4oManager.getAllRuralHouses();
		dB4oManager.close();
		System.out.println("End: getAllRuralHouses");

		return ruralHouses;

	}
	
	/**
	 * This method obtains the  offers of a ruralHouse in the provided dates 
	 * 
	 * @param ruralHouse, the ruralHouse to inspect 
	 * @param firstDay, first day in a period range 
	 * @param lastDay, last day in a period range
	 * @return the first offer that overlaps with those dates, or null if there is no overlapping offer
	 */

	@WebMethod public Vector<Offer> getOffers( RuralHouse rh, Date firstDay,  Date lastDay) {
		
		DataAccess dB4oManager=new DataAccess();
		Vector<Offer>  offers=new Vector<Offer>();
		  offers=dB4oManager.getOffers(rh,firstDay,lastDay);
		dB4oManager.close();

		return offers;
	}	
		
		
	
	public void close() {
		DataAccess dB4oManager=new DataAccess();

		dB4oManager.close();

	}


	 public void initializeBD(){
		
		DataAccess dB4oManager=new DataAccess();
		dB4oManager.initializeDB();
		dB4oManager.close();

	}

	 //Iteracion 1
	 public int isLoginValid(String username, String password)
	 {
		 nivelDatos = new DataAccess();
		 int usuarioCorrecto = nivelDatos.getUserByUsernameAndPassword(username, password);
		 nivelDatos.close();				 
		 
		 if(usuarioCorrecto == 1)
			 return 1;
		 else
		 {
			 if(usuarioCorrecto == 2)
				 return 2;
			 else
				 return 3;
		 }
			 
	 }
	 
	 public boolean isExistingEmail(String em)
	 {
		 nivelDatos = new DataAccess();
		 boolean email = nivelDatos.getUserByEmail(em);
		 nivelDatos.close();
		 
		 //System.out.println("f "+email);

		 if(email)
			 return true;
		 else
			 return false;
	 }
	 
	 public boolean isExistingEmailOwner(String em)
	 {
		 nivelDatos = new DataAccess();
		 boolean email = nivelDatos.getUserByEmailOwner(em);
		 nivelDatos.close();

		 if(email)
			 return true;
		 else
			 return false;
	 }
	 
	 
	 public boolean isExistingUserName(String un)
	 {
		 nivelDatos = new DataAccess();
		 boolean userName = nivelDatos.getUserByUserName(un);
		 nivelDatos.close();
		 
		 if(userName)
			 return true;
		 else
			 return false;
	 }
	 
	 public boolean isExistingUserNameOwner(String un)
	 {
		 nivelDatos = new DataAccess();
		 boolean userName = nivelDatos.getUserByUserNameOwner(un);
		 nivelDatos.close();
		 
		 if(userName)
			 return true;
		 else
			 return false;
	 }
	 
	 public boolean isExistingDni(String dni)
	 {
		 nivelDatos = new DataAccess();
		 boolean dniUsuario = nivelDatos.getUserByDni(dni);
		 nivelDatos.close();
		 
		 if(dniUsuario)
			 return true;
		 else
			 return false;
	 }
	 
	 public boolean isExistingDniOwner(String dni)
	 {
		 nivelDatos = new DataAccess();
		 boolean dniUsuario = nivelDatos.getUserByDniOwner(dni);
		 nivelDatos.close();
		 
		 if(dniUsuario)
			 return true;
		 else
			 return false;
	 }
	 
	 public void saveUserData(String nomUser, String password, String nombre, String apellidos, String email, String dni) 
	 {
		 nivelDatos = new DataAccess();
		 nivelDatos.storeUserData(nomUser, password, nombre, apellidos, email, dni);
		 nivelDatos.close(); 
	 }
	 
	 public void saveOwnerData(String nomUser, String password, String nombre, String apellidos, String email, String dni, String numCuenta, int telefono) 
	 {
		 nivelDatos = new DataAccess();
		 nivelDatos.storeOwnerData(nomUser, password, nombre, apellidos, email, dni, numCuenta, telefono);
		 nivelDatos.close(); 
	 }

	 public void saveRuralHouseData(String loc, String desc, int numHab, int numPersonas, boolean[] general, boolean[] cocina, boolean[] sala) 
	 {
		 nivelDatos = new DataAccess();
		 nivelDatos.storeRuralHouseData(loc, desc, numHab, numPersonas, general, cocina, sala);
		 nivelDatos.close(); 
	 }
	 
	 
	 //Iteracion 2
	 
	 //Envia un email al usuario con un código
	 public boolean enviarEmail(String strEmail, String user_type)
	 {
		boolean bool;
		 
	 	String to_email = strEmail;//Destinatario del email
	 	String codigo = generateCode();
	 	long time_exp = setTokenExpirationTime();
	 	
	    //Cuenta hotmail
	    final String from_email = "ruralhouse_g10@hotmail.com";
	    final String password = "rh_av_234234@";
	
	    Properties props = new Properties();
	    props.setProperty("mail.transport.protocol", "smtp");
	    props.setProperty("mail.host", "smtp.live.com");
	    props.put("mail.smtp.port", "587");
	    props.put("mail.smtp.starttls.enable", "true");
	    props.put("mail.smtp.auth", "true");
	
	    Session session2 = Session.getInstance(props,new javax.mail.Authenticator() 
	    {
	        protected PasswordAuthentication getPasswordAuthentication() 
	        {
	            return new PasswordAuthentication(from_email, password);
	        }
	    });
	    
        try{
                    
            MimeMessage message = new MimeMessage(session2);
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to_email));
           
            //Asunto del Mail
            message.setSubject("Restablecimiento de contraseña");
            //Contenido del Mail
            message.setText("Hola,\n\nSu email es: " + to_email +
            		".\nPara restablecer su contraseña, ingrese el siguiente código: "+ codigo +
            		"\nSi no realiza el restablecimiento de contraseña en las proximas 24 h debera volver a solicitar un nuevo código."+
            		"\n\n Victor y Adrian");
            
           //Enviar email
           Transport.send(message);
            
            nivelDatos = new DataAccess();
            if(user_type.equals("Cliente"))
            	 nivelDatos.updateUserByEmail(to_email, codigo, time_exp);
    		if(user_type.equals("Propietario"))	
    			 nivelDatos.updateUserByEmailOwner(to_email, codigo, time_exp);
    		
            nivelDatos.close(); 
            
            System.out.println("Token :" + codigo);
            System.out.println("Envío del email correcto.");
            bool = true;
            
         }catch (MessagingException e) {
            System.out.println("Error al enviar el email");	
            System.out.println(e);
            bool = false;
         }  
        
        return bool;
    }  
	 
	 //Genera un código aleatorio que el usuario utilizara para identificarse y poder
	 //restablecer la contraseña olvidada
	public String generateCode()
	{
		//https://kodejava.org/how-do-i-generate-uuid-guid-in-java/
		//Creating a random UUID (Universally unique identifier).
        UUID uuid = UUID.randomUUID();
        String randomUUIDString = uuid.toString();        
		return randomUUIDString;
	}
	
	public long setTokenExpirationTime()
	{
		//java.util.Date.getTime() returns the number of milliseconds 
		//since January 1, 1970, 00:00:00 GMT represented by this Date object.
		//Nosotros guardamos los segundos transcurridos desde esa fecha + un dia
		long dateTime = (new Date().getTime())/1000;//segundos
		dateTime = dateTime + (24 * 60 * 60);
		
		return dateTime;
	}
	
	
	
	public boolean checkTokenValidity(String email, String token, String user_type)
	{
		boolean bool = false;
		
		nivelDatos = new DataAccess();
		if(user_type.equals("Cliente"))
			bool = nivelDatos.checkExistenceOfUserAndToken(email, token);
		if(user_type.equals("Propietario"))	
			bool = nivelDatos.checkExistenceOfUserAndTokenOwner(email, token);
		
		nivelDatos.close(); 
         
		return bool;
	}
	
	
	
	public boolean isTokenExpired(String email, String token, String user_type)
	{
		boolean bool = false;
		long token_expiration_time = 0;
		
		nivelDatos = new DataAccess();
		if(user_type.equals("Cliente"))
			token_expiration_time = nivelDatos.checkTokenExpired(email, token);
		if(user_type.equals("Propietario"))	
			token_expiration_time = nivelDatos.checkTokenExpiredOwner(email, token);
		
		nivelDatos.close(); 
        
		long actual_time = (new Date().getTime())/1000;
		
		//System.out.println(actual_time);
		
		if((token_expiration_time < actual_time))
		{
			System.out.println("El token ha expirado");
			return true;
		}else
		{
			return false;
		}	
	}
	
	public boolean isTokenAlreadyUsed(String email, String token, String user_type)
	{
		boolean token_used = false;
		
		nivelDatos = new DataAccess();
		if(user_type.equals("Cliente"))
			token_used = nivelDatos.checkTokenStatus(email, token);
		if(user_type.equals("Propietario"))	
			token_used = nivelDatos.checkTokenStatusOwner(email, token);
		
		nivelDatos.close(); 
		
		return token_used;
	}
	 
	
	public void saveNewPassword(String email, String token, String password, String user_type)
	{
		nivelDatos = new DataAccess();
		if(user_type.equals("Cliente"))
			nivelDatos.resetPassword(email, token, password);
		if(user_type.equals("Propietario"))	
			nivelDatos.resetPasswordOwner(email, token, password);
		
		nivelDatos.close(); 
	}
	
	//Bloquear/desbloquear clientes
	public String[] obtainAllBloquedClients()
	{
		nivelDatos = new DataAccess();
		String[] result = nivelDatos.getAllBloquedClients();
		nivelDatos.close(); 
		
		Arrays.sort(result); //Ordena los emails alfabeticamente
		
		return result;
	}
	
	public String[] obtainAllUnbloquedClients()
	{
		nivelDatos = new DataAccess();
		String[] result = nivelDatos.getAllUnbloquedClients();
		nivelDatos.close(); 
		
		Arrays.sort(result); //Ordena los emails alfabeticamente
		
		return result;
	}
	
	public void changeClientStatus(String email, boolean estado)
	{
		nivelDatos = new DataAccess();
		nivelDatos.setClientStatus(email, estado);
		nivelDatos.close(); 
	}
	
	public boolean checkClientBlocked(String username)
	{
		nivelDatos = new DataAccess();
		boolean b = nivelDatos.isClientBlocked(username);
		nivelDatos.close(); 
		
		return b;
	}
	
	
	 public Vector<RuralHouse> mostrandoLogica(int x){ //ITERACION 2
		 nivelDatos=new DataAccess();
		 Vector<RuralHouse> casasHouses=new Vector<RuralHouse>();
		 if(x==0){
			 casasHouses= nivelDatos.mostrandoAccess(0);//por ciudades		 
		 }
		  if(x==1){
			  casasHouses= nivelDatos.mostrandoAccess(1); //por numHabitaciones
		  }
		  if(x==2){
			  casasHouses= nivelDatos.mostrandoAccess(2); //por numPersonas
		  }
		  nivelDatos.close();
		  return casasHouses;//TENGOOOOO QUE HACER UN nivelDatos.close(); ?????
		  
	 }
	 public Vector<Offer> dameLasOfertas(RuralHouse x){
		 nivelDatos = new DataAccess();
		 return nivelDatos.devuelveLasOfertas(x);
		//llamo al nivel de datos y me pasa todas las ofertas
	 }
	 public Vector<RuralHouse> dameCasa(String x){
		 nivelDatos= new DataAccess();
		 Vector<RuralHouse> casas=new Vector<RuralHouse>();
		 casas= nivelDatos.devuelveLasCasas(x);
		 nivelDatos.close();
		 return casas;
	 }
}

