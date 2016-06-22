package domain;

import java.util.ArrayList;
import java.util.Date;

public class Admin 
{
	//Logearse
	private String usuario;
	private String password;
	
	//Mensajes
	ArrayList<Integer> msgNumber = new ArrayList<Integer>();//Numero de mensaje 
	ArrayList<String> msgUser = new ArrayList<String>();
	ArrayList<Boolean> msgUserRol = new ArrayList<Boolean>();//0(false):cliente, 1(true):propietario
	ArrayList<String> msgDateHour = new ArrayList<String>();
	ArrayList<String> msgAsunto = new ArrayList<String>();
	ArrayList<String> msgText = new ArrayList<String>();
	ArrayList<Boolean> msgRead = new ArrayList<Boolean>();//leido: true, no leido: false
	
	public Admin(String usuario, String password, int msgNum, String mUser, boolean mUserRol, String msgDate, String asunto, String msg, boolean read)
	{
		this.usuario=usuario;
		this.password=password;
		
		//Añade en la ultima posicion
		msgNumber.add(msgNum);	//Numero de mensaje
		msgUser.add(mUser);
		msgUserRol.add(mUserRol);
		msgDateHour.add(msgDate);
		msgAsunto.add(asunto);
		msgText.add(msg);
		msgRead.add(read);
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

	//Message
	public ArrayList<Integer> getMsgNumber() {
		return msgNumber;
	}

	public void setMsgNumber(ArrayList<Integer> msgNumber) {
		this.msgNumber = msgNumber;
	}

	public ArrayList<String> getMsgUser() {
		return msgUser;
	}

	public void setMsgUser(ArrayList<String> msgUser) {
		this.msgUser = msgUser;
	}

	public ArrayList<Boolean> getMsgUserRol() {
		return msgUserRol;
	}

	public void setMsgUserRol(ArrayList<Boolean> msgUserRol) {
		this.msgUserRol = msgUserRol;
	}

	public ArrayList<String> getMsgDateHour() {
		return msgDateHour;
	}

	public void setMsgDateHour(ArrayList<String> msgDateHour) {
		this.msgDateHour = msgDateHour;
	}

	public ArrayList<String> getMsgAsunto() {
		return msgAsunto;
	}

	public void setMsgAsunto(ArrayList<String> msgAsunto) {
		this.msgAsunto = msgAsunto;
	}

	public ArrayList<String> getMsgText() {
		return msgText;
	}

	public void setMsgText(ArrayList<String> msgText) {
		this.msgText = msgText;
	}
	
	public ArrayList<Boolean> getMsgRead() {
		return msgRead;
	}

	public void setMsgRead(ArrayList<Boolean> msgRead) {
		this.msgRead = msgRead;
	}

	//Add
	public void addMsgNumber(int number){
		this.msgNumber.add(number);
	}

	public void addMsgUser(String user){
		this.msgUser.add(user);
	}

	public void addMsgUserRol(boolean b){
		msgUserRol.add(b);
	}
	
	public void addMsgTime(String datetime){ //fecha
		msgDateHour.add(datetime);
	}
	
	public void addMsgAsunto(String msgAsunto){
		this.msgAsunto.add(msgAsunto);
	}
	
	public void addMsgText(String msgText){
		this.msgText.add(msgText);
	}
	
	public void addMsgRead(boolean msgRead){
		this.msgRead.add(msgRead);
	}
	
}
