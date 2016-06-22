package domain;

import java.io.*;
import java.util.Calendar;
import java.util.Date;	

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlIDREF;


@SuppressWarnings("serial")
@XmlAccessorType(XmlAccessType.FIELD)

public class Offer implements Serializable {
	

	private Integer offerNumber;
	private Date firstDayOffer; // Dates are stored as java.util.Date objects instead of java.sql.Date objects
	private Date lastDayOffer;  // because, they are not well stored in db4o as java.util.Date objects
	private float price;   // This is coherent because objects of java.sql.Date are objects of java.util.Date 
	private String user;
	//private boolean eliminada; //Si es true la oferta esta guardada pero no activa
	
	/*0: oferta Disponible para comprar
	1:oferta adquirida por el cliente
	//2:oferta cancelada por el cliente
	2:eliminada por el propietario
	*/
	private int estadoOferta;
	
	//Para ver el estado de la oferta de pasada, presente o futura mirar las fechas
	
	
	@XmlIDREF
	private RuralHouse ruralHouse;

	public Offer(){}
	public Offer(int offerNumber, Date firstDay, Date lastDay, float price, RuralHouse ruralHouse, String user, int estadoOferta){
		this.user=user;
		  this.firstDayOffer=firstDay;
		  this.lastDayOffer=lastDay;
		  this.price=price;
		  this.ruralHouse=ruralHouse;
		  this.offerNumber=offerNumber;
		  this.estadoOferta = estadoOferta;
	}
	
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	/**
	 * Get the house number of the offer
	 * 
	 * @return the house number
	 */
	public RuralHouse getRuralHouse() {
		return this.ruralHouse;
	}

	/**
	 * Set the house number to a offer
	 * 
	 * @param house number
	 */
	public void setRuralHouse(RuralHouse ruralHouse) {
		this.ruralHouse = ruralHouse;
	}


	/**
	 * Get the offer number
	 * 
	 * @return offer number
	 */
	public int getOfferNumber() {
		return this.offerNumber;
	}

	
	/**
	 * Get the first day of the offer
	 * 
	 * @return the first day
	 */
	public Date getFirstDay() {
		return firstDayOffer;
	}

	/**
	 * Set the first day of the offer
	 * 
	 * @param firstDay
	 *            The first day
	 */

	
	public void setFirstDay(Date firstDay) {
		this.firstDayOffer = firstDay;
	}
	
	/**
	 * Get the last day of the offer
	 * 
	 * @return the last day
	 */
	public Date getLastDay() {
		return lastDayOffer;
	}

	/**
	 * Set the last day of the offer
	 * 
	 * @param lastDay
	 *            The last day
	 */
	public void setLastDay(Date lastDay) {
		this.lastDayOffer = lastDay;
	}

	
	/**
	 * Get the price
	 * 
	 * @return price
	 */
	public float getPrice() {
		return this.price;
	}

	/**
	 * Set the price
	 * 
	 * @param price
	 */
	public void setPrice(float price) {
		this.price = price;
	}
	
	public int getEstadoOferta() {
		return estadoOferta;
	}
	public void setEstadoOferta(int estadoOferta) {
		this.estadoOferta = estadoOferta;
	}
	public String toString(){
		return offerNumber+";"+firstDayOffer.toString()+";"+lastDayOffer.toString()+";"+price;
	}
}