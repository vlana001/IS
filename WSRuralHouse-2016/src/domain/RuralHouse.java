package domain;

import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
@XmlAccessorType(XmlAccessType.FIELD)

public class RuralHouse implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@XmlID
	@XmlJavaTypeAdapter(IntegerAdapter.class)
	private Integer houseNumber;
	private String nombreRH;
	private String description;
	private String city;
	
	private String user;
	//private String pass;
	
	//General
	private boolean[] general;
	
	/*private boolean Wifi;
	private boolean telefono;
	private boolean calefaccion;
	private boolean aireAcondicionado;
	private boolean barbacoa;
	private boolean piscina;
	private boolean permitenPerros;
	private boolean permitenFumar;
	*/
	
	//Cocina
	private boolean[] cocina;
	
	/*
	private boolean lavavajillas;
	private boolean lavadora;
	private boolean microondas;
	private boolean horno;
	*/
	
	//Salon
	private boolean[] sala;
	
	/*
	private boolean tv;
	private boolean ordenador;
	private boolean lineaMusica;
	*/
	
	//habitaciones
	private int numeroHabitaciones;
	private int numeroPersonas;
	
	private boolean eliminida;
	
	public Vector<Offer> offers;

	public RuralHouse() {
		super();
	}

	public RuralHouse(Integer houseNumber, String description, String city) {
		this.houseNumber = houseNumber;
		this.description = description;
		this.city = city;
		offers=new Vector<Offer>();
	}
	
	public RuralHouse(Integer houseNumber,String nombreRH, String description, String city, int roomNumber, int personNumber, boolean[] general, boolean[] kitchen, boolean[] livingRoom, String user, boolean eliminada) {
		this.houseNumber = houseNumber;
		this.nombreRH = nombreRH;
		this.description = description;
		this.city = city;
		this.numeroHabitaciones = roomNumber;
		this.numeroPersonas = personNumber;
		this.general = general;
		this.cocina = kitchen;
		this.sala = livingRoom;
		offers=new Vector<Offer>();
		this.user=user;
		//this.pass=pass;
		this.eliminida=eliminada;
	}

	public Integer getHouseNumber() {
		return houseNumber;
	}

	public void setHouseNumber(Integer houseNumber) {
		this.houseNumber = houseNumber;
	}
	

	public String getNombreRH() {
		return nombreRH;
	}

	public void setNombreRH(String nombreRH) {
		this.nombreRH = nombreRH;
	}

	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description=description;
	}

	
	public String getCity() {
		return city;
	}
	
	public void setCity(String city) {
		this.city=city;
	}

	
	public String toString() {
		return this.houseNumber + ": " + this.city;
	}

	public boolean[] getGeneral() {
		return general;
	}

	public void setGeneral(boolean[] general) {
		this.general = general;
	}

	public boolean[] getCocina() {
		return cocina;
	}

	public void setCocina(boolean[] cocina) {
		this.cocina = cocina;
	}

	public boolean[] getSala() {
		return sala;
	}

	public void setSala(boolean[] sala) {
		this.sala = sala;
	}

	public int getNumeroHabitaciones() {
		return numeroHabitaciones;
	}

	public void setNumeroHabitaciones(int numeroHabitaciones) {
		this.numeroHabitaciones = numeroHabitaciones;
	}

	public int getNumeroPersonas() {
		return numeroPersonas;
	}

	public void setNumeroPersonas(int numeroPersonas) {
		this.numeroPersonas = numeroPersonas;
	}
	
	

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	
	
	public boolean isEliminida() {
		return eliminida;
	}

	public void setEliminida(boolean eliminida) {
		this.eliminida = eliminida;
	}

	public Vector<Offer> getOffers() {
		return offers;
	}

	public void setOffers(Vector<Offer> offers) {
		this.offers = offers;
	}

	/**
	 * This method creates an offer with a house number, first day, last day and price
	 * 
	 * @param House
	 *            number, start day, last day and price
	 * @return None
	 */
	
	public Offer createOffer(int offerNumber,Date firstDay, Date lastDay, float price)  {//, RuralHouse rh
        Offer off=new Offer(offerNumber,firstDay,lastDay,price,this, null,0);//offerNumber,firstDay,lastDay,price,rh,null, 0)
        offers.add(off);
        return off;
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + houseNumber;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RuralHouse other = (RuralHouse) obj;
		if (houseNumber != other.houseNumber)
			return false;
		return true;
	}
	
	
	/**
	 * This method obtains available offers for a concrete house in a certain period 
	 * 
	 * @param houseNumber, the house number where the offers must be obtained 
	 * @param firstDay, first day in a period range 
	 * @param lastDay, last day in a period range
	 * @return a vector of offers(Offer class)  available  in this period
	 */
	public Vector<Offer> getOffers( Date firstDay,  Date lastDay) {
		
		Vector<Offer> availableOffers=new Vector<Offer>();
		Iterator<Offer> e=offers.iterator();
		Offer offer;
		while (e.hasNext()){
			offer=e.next();
			if ( (offer.getFirstDay().compareTo(firstDay)>=0) && (offer.getLastDay().compareTo(lastDay)<=0)  )
				availableOffers.add(offer);
		}
		return availableOffers;
		
	}
	

	/**
	 * This method obtains the first offer that overlaps with the provided dates
	 * 
	 * @param firstDay, first day in a period range 
	 * @param lastDay, last day in a period range
	 * @return the first offer that overlaps with those dates, or null if there is no overlapping offer
	 */

	public Offer overlapsWith( Date firstDay,  Date lastDay) {
		
		Iterator<Offer> e=offers.iterator();
		Offer offer=null;
		while (e.hasNext()){
			offer=e.next();
			if ( (offer.getFirstDay().compareTo(lastDay)<0) && (offer.getLastDay().compareTo(firstDay)>0))
				return offer;
		}
		return null;
		
	}

}
