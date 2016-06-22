package businessLogic;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
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
		 
		int tipoUser = 0;
		 switch (usuarioCorrecto)
		 {
		 	case 1:
		 		tipoUser = 1;//cliente
		 		break;
		 		
	        case 2:
	        	tipoUser = 2;//propietario
		 		break;
		 		
	        case 3:
	        	tipoUser = 3;//admin
		 		break;
	
	        default:
	        	tipoUser = 4;//ningun rol
		 		break;
		 }
		 
		 return tipoUser;
		 
		/* if(usuarioCorrecto == 1)
			 return 1;
		 else
		 {
			 if(usuarioCorrecto == 2)
				 return 2;
			 else
			 {
				 if(usuarioCorrecto == 4)
					 return 4;
				 else
					 return 3;
			 }
				 
		 }*/
			 
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

	 public boolean saveRuralHouseData(String loc, String nomCasa, String desc, int numHab, int numPersonas, boolean[] general, boolean[] cocina, boolean[] sala,String user,String pass) 
	 {
		 nivelDatos = new DataAccess();
		 boolean bool = nivelDatos.storeRuralHouseData(loc, nomCasa, desc, numHab, numPersonas, general, cocina, sala,user);
		 nivelDatos.close(); 
		 
		 return bool;
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
	
	//ITERACION 2
	
	 public Vector<RuralHouse> mostrandoLogica(int x){ 
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
	 
	 //////ITERACION 3
	 
	 //Iteracion 3
	 
	 public void saveMessage(String msgUser, String msgAsunto, String msgCuerpo)
	 {
		 nivelDatos = new DataAccess();
		 
		 //Numero mensaje query
		 //Obtener el numero total de mensajes y asignar numero +1
		 //El primer mensaje sera el numero 1
		 int msgNumber = nivelDatos.getAdministratorsLastMessageNumber();
		 
		 //Rol user
		 boolean userRol = nivelDatos.getUserRolByUsername(msgUser);
		 
		 String msgDate = getCurrentDateTime(); //Fecha actual
		 
		 nivelDatos.saveMessage(msgNumber+1, msgUser, userRol, msgDate, msgAsunto, msgCuerpo);
		 nivelDatos.close(); 
	 } 
	
	 public String getCurrentDateTime()
	 {
		 DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		 Date date = new Date();
		 return dateFormat.format(date); //2014/08/06 15:59:48
	 }
	 
	 //obtener datos mensajes
	 public int[] getMessageNumbers()
	 {
		 nivelDatos = new DataAccess();
		 int[] num = nivelDatos.getMessageNumbers();
		 nivelDatos.close();
		 
		 return num;
	 }
	 
	public String[] getMessageDates()
	{
		nivelDatos = new DataAccess();
		String[] dates = nivelDatos.getMessageDates();
		nivelDatos.close();
		 
		return dates;
	}
	
	public String[] getMessageUsers()
	{
		nivelDatos = new DataAccess();
		String[] users = nivelDatos.getMessageUsers();
		nivelDatos.close();
		 
		return users;
	}
	
	public boolean[] getMessageUsersRoles()
	{
		nivelDatos = new DataAccess();
		
		/*String[] users = nivelDatos.getMessageUsers();
		boolean[] usersRoles = new boolean[users.length];
		for(int i=0;i<users.length;i++)
		{
			usersRoles[i] = nivelDatos.getUserRolByUsername(users[i]);
		}*/
		
		boolean[] usersRoles = nivelDatos.getMessageUsersRoles();
		nivelDatos.close();
		 
		return usersRoles;
	}
	
	public String[] getMessageAsunto()
	{
		nivelDatos = new DataAccess();
		String[] asuntos = nivelDatos.getMessageAsunto();
		nivelDatos.close();
		 
		return asuntos;
	}
	
	public String[] getMessageTexto()
	{
		nivelDatos = new DataAccess();
		String[] textos = nivelDatos.getMessageTexto();
		nivelDatos.close();
		 
		return textos;
	}
	
	public boolean[] getMessageRead()
	{
		nivelDatos = new DataAccess();
		boolean[] read = nivelDatos.getMessageRead();
		nivelDatos.close();
		 
		return read;
	}
	
	public void setUnreadMsgNewStatus()
	{
		nivelDatos = new DataAccess();
		nivelDatos.setUnreadMsgNewStatus();
		nivelDatos.close();
	}
	
	public String getEmailByUserName(String usrName, String userRol)
	{
		nivelDatos = new DataAccess();
		String email = nivelDatos.getEmailByUserName(usrName, userRol);
		nivelDatos.close();
		
		return email;
	}
	
	 //Envia un email el administrador al usuario respondiendole de su mensaje
	 public boolean enviarAdminEmailUsuario(String strEmail, String strMessage)
	 {
		boolean bool;//Devuelve true si el email se envio con exito, false en caso contrario
		 
	 	String to_email = strEmail;//Destinatario del email
	 	
	    //Cuenta hotmail desde la que enviamos el email
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
           message.setSubject("Respuesta a su mensaje");
           //Contenido del Mail
           message.setText("Hola " + to_email +",\n"+ strMessage +"\n\n Administrador");
           
          //Enviar email
          Transport.send(message);       

           System.out.println("Envío del email correcto.");
           bool = true;
           
        }catch (MessagingException e) {
           System.out.println("Error al enviar el email");	
           System.out.println(e);
           bool = false;
        }  
       
       return bool;
   } 
	 
	 public int getUnreadMessagesNumber()
	 {	
		 nivelDatos = new DataAccess();
		 boolean[] arrayMessagesStatus = nivelDatos.getMessageRead();
		 nivelDatos.close();
		 
		 int numUnreadMessages = 0;
		 
		 for(int i=0;i<arrayMessagesStatus.length;i++)
			 if(arrayMessagesStatus[i] == false)
				 numUnreadMessages++;
		 
		 return numUnreadMessages;
	 }
	 
	 public ArrayList<Offer> buscarOfertasFiltros(String loc, int precioMax, int precioMin, int numHab, int numPersonas, Date dateI, Date dateF, boolean[] general, boolean[] cocina, boolean[] sala)
	 {
		 nivelDatos = new DataAccess();
		 ArrayList<Offer> of = nivelDatos.buscarOfertasFiltros(loc, precioMax, precioMin, numHab, numPersonas, dateI, dateF, general,  cocina,  sala);
		 nivelDatos.close();
		 
		 return of;
	 }
	 
	public ArrayList<RuralHouse> getRuralHouseByOwnerId(String user)
	{
		nivelDatos = new DataAccess();
		ArrayList<RuralHouse> listaRuralHouses = nivelDatos.getRuralHouseByOwnerId(user);
		nivelDatos.close();
		
		return listaRuralHouses;
	}
	
	public ArrayList<Offer> getOffersByRHId(int idCasa)
	{
		nivelDatos = new DataAccess();
		ArrayList<Offer> listaOffers = nivelDatos.getOffersByRHId(idCasa);
		nivelDatos.close();
		
		return listaOffers;
	}
	
	public RuralHouse getRHById(int idCasa)
	{
		nivelDatos = new DataAccess();
		RuralHouse rh = nivelDatos.getRHById(idCasa);
		nivelDatos.close();
		
		return rh;
	}
	
	public void eliminarOferta(int idOferta)
	{
		nivelDatos = new DataAccess();
		nivelDatos.eliminarOferta(idOferta);
		nivelDatos.close();
	}
	
	public Offer getOfferByOfferId(int idOferta)
	{
		nivelDatos = new DataAccess();
		Offer of = nivelDatos.getOfferByOfferId(idOferta);
		nivelDatos.close();
		
		return of;
	}
	
	/*
	 public boolean crearOferta(RuralHouse rh, int idCasa, Date firstDay, Date lastDay, float price, String user)
	 {
		 boolean coincide = false;
		 boolean b=false;
		 
		 nivelDatos=new DataAccess(); 
		 b=nivelDatos.existeOfertasEnEsaCasayFecha(idCasa,firstDay,lastDay,price);
		 if(!b){ // Crear oferta si no coincide con otra oferta de esa casa en ese periodo o parte de ese periodo
			nivelDatos.createOffer(rh,firstDay,lastDay,price); 
		 }else
			 coincide = true;
		 
		nivelDatos.close();
		 
		return coincide;
	 }
	 
	 */
	 
	public boolean modificarOferta(int idCasa, int idOferta, Date dateInicio, Date dateFin, float nuevoPrecio, int intEstadoSel)
	{
		boolean coincide = false;
		boolean b = false;
		
		nivelDatos=new DataAccess(); 
	
		 b=nivelDatos.existeOfertasEnEsaCasayFecha(idCasa,dateInicio,dateFin,nuevoPrecio);
		 if(!b){ // Crear oferta si no coincide con otra oferta de esa casa en ese periodo o parte de ese periodo
			 nivelDatos.modificarOferta(idOferta, dateInicio, dateFin, nuevoPrecio, intEstadoSel);
		 }else
			 coincide = true;
		 
		nivelDatos.close();
		 
		return coincide;
	}
	
	public boolean eliminarCasa(int idCasa)
	{
		boolean hayOfertasFuturasReservadasEnLaCasa = false;
		boolean b=false;
		 
		 nivelDatos=new DataAccess(); 
		 b=nivelDatos.existenOfertasFuturasReservadasEnEsaCasa(idCasa);
		 if(!b){ // Crear oferta si no coincide con otra oferta de esa casa en ese periodo o parte de ese periodo
			 nivelDatos.eliminarCasa(idCasa);
		 }else
			 hayOfertasFuturasReservadasEnLaCasa = true;
		 
		nivelDatos.close();
		 
		return hayOfertasFuturasReservadasEnLaCasa;
	}
	
	public boolean realizarReservaRH(int idOfertaSeleccionada, String user)
	{
		nivelDatos=new DataAccess(); 
		boolean b=nivelDatos.realizarReservaRH(idOfertaSeleccionada, user);
		nivelDatos.close();
		
		return b;
	}
	
	public boolean getUserRolByUserName(String user)
	{
		nivelDatos=new DataAccess();
	    boolean userRol = nivelDatos.getUserRolByUsername(user);
	    nivelDatos.close();
		
		return userRol;
	}
}




