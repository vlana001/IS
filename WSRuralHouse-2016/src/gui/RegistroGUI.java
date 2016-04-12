package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.UIManager;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JTextArea;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.border.LineBorder;
import javax.swing.ListSelectionModel;
import javax.swing.Box;

import businessLogic.FacadeImplementationWS;

public class RegistroGUI extends JFrame {
private static final long serialVersionUID = 1L;

	private FacadeImplementationWS logicaNegocio;
	
	private JPanel contentPane;
	
	//Label
	private final JLabel lblNombreusuario = new JLabel("NombreUsuario:");
	private final JLabel lblPassword = new JLabel("Password:");
	private final JLabel lblConfirmaPassword = new JLabel("Confirma password:");
	private final JLabel lblDni = new JLabel("Dni:");
	private final JLabel lblNombre = new JLabel("Nombre:");
	private final JLabel lblApellidos = new JLabel("Apellidos:");
	private final JLabel lblEmail = new JLabel("Email:");
	
	//Text Field
	private final JTextField textNombreUsuario; 
	private final JTextField textDni;
	private final JTextField textNombre;
	private final JTextField textApellidos;
	private final JTextField textEmail;
	
	//Password field
	private final JPasswordField textPassword = new JPasswordField();
	private final JPasswordField textConfirmacionPassword = new JPasswordField();
	
	//Botones
	private final JButton buttonErespropietario = new JButton("Eres Propietario?");
	private final JButton buttonFinalizar = new JButton("Finalizar");
	
	//Mensajes de error
	private final JLabel errorNombreUsuario = new JLabel("");
	private final JLabel errorPassword = new JLabel("");
	private final JLabel errorConfirmaPassword = new JLabel("");
	private final JLabel errorDni = new JLabel("");
	private final JLabel errorNombre = new JLabel("");
	private final JLabel errorApellidos = new JLabel("");
	private final JLabel errorEmail = new JLabel("");

	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RegistroGUI frame = new RegistroGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public RegistroGUI() {
		setTitle("Registro de Usuarios");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 669, 392);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
		lblNombreusuario.setBounds(8, 40, 133, 31);
		contentPane.add(lblNombreusuario);
		
		lblPassword.setBounds(8, 83, 111, 33);
		contentPane.add(lblPassword);
		
		lblDni.setBounds(10, 155, 98, 31);
		contentPane.add(lblDni);
		
		lblNombre.setBounds(10, 191, 109, 31);
		contentPane.add(lblNombre);
		
		lblApellidos.setBounds(10, 223, 98, 31);
		contentPane.add(lblApellidos);
		
		lblEmail.setBounds(10, 249, 98, 31);
		contentPane.add(lblEmail);
		
		lblConfirmaPassword.setBounds(8, 128, 163, 15);
		contentPane.add(lblConfirmaPassword);
		
		
		textNombreUsuario = new JTextField();
		textNombreUsuario.setBounds(159, 45, 133, 22);
		contentPane.add(textNombreUsuario);
		textNombreUsuario.setColumns(10);

		textDni = new JTextField();
		textDni.setBounds(157, 161, 133, 22);
		contentPane.add(textDni);
		textDni.setColumns(10);

		textNombre = new JTextField();
		textNombre.setBounds(157, 197, 133, 22);
		contentPane.add(textNombre);
		textNombre.setColumns(10);
		
		textApellidos = new JTextField();
		textApellidos.setBounds(157, 223, 133, 22);
		contentPane.add(textApellidos);
		textApellidos.setColumns(10);

		textEmail = new JTextField();
		textEmail.setBounds(157, 255, 133, 22);
		contentPane.add(textEmail);
		textEmail.setColumns(10);		
		
		
		textPassword.setBounds(157, 89, 133, 22);
		contentPane.add(textPassword);
		
		textConfirmacionPassword.setBounds(157, 126, 133, 22);
		contentPane.add(textConfirmacionPassword);	
		
		
		//Labels mensajes de error
		errorNombreUsuario.setBounds(293, 40, 357, 23);
		contentPane.add(errorNombreUsuario);
		
		errorPassword.setBounds(293, 83, 357, 20);
		contentPane.add(errorPassword);
		
		errorConfirmaPassword.setBounds(308, 128, 342, 15);
		contentPane.add(errorConfirmaPassword);
		
		errorDni.setBounds(298, 166, 352, 20);
		contentPane.add(errorDni);
		
		errorNombre.setBounds(293, 191, 357, 31);
		contentPane.add(errorNombre);
		
		errorApellidos.setBounds(293, 221, 357, 33);
		contentPane.add(errorApellidos);
		
		errorEmail.setBounds(297, 247, 353, 33);
		contentPane.add(errorEmail);
		
		
		buttonErespropietario.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				RegistroGUIpropietario a = new RegistroGUIpropietario(); // redireccionamos a propietrio
				a.setVisible(true);
				setVisible(false);
			}
		});
		buttonErespropietario.setBounds(490, 12, 163, 28);
		contentPane.add(buttonErespropietario);
		
		buttonFinalizar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {		
				
				resetErrorMessages();//Limpiar mensajes de error
				
				boolean bien = true;
				
				logicaNegocio = new FacadeImplementationWS();
				
				int num[] = validateInput();	//Validar datos introducidos
				
				for(int i=0; i<7; i++)
				{	
					if(num[i] == 0)
						bien=false;
				}
				
				if(num[0] == 2 || num[1] == 2 || num[4] == 2)
					bien=false;
				
				if(!bien){
					printErrorMessages(num); //Imprimir mensajes de error
				}else{
					//Guardar en BD
					String nombreUser = textNombreUsuario.getText();
					
					char[] password = textPassword.getPassword();
					String strPassword = String.valueOf(password);
					
					String nombre = textNombre.getText();
					String apellidos = textApellidos.getText();
					String email = textEmail.getText();
					String dni = textDni.getText();
					
						
					logicaNegocio.saveUserData(nombreUser, strPassword, nombre, apellidos, email, dni);
					
					//Imprimir en login registro correcto
					LoginGUI b = new LoginGUI("a"); // cuando nos registramos vamos al login de nuevo
					b.setVisible(true);
					setVisible(false);
				}
			}
		});
		buttonFinalizar.setBounds(157, 301, 166, 31);
		contentPane.add(buttonFinalizar);
		
	}

	
	/*
	public String getTextDni() {
		return textDni.getText();
	}

	public String getTextNombre() {
		return textNombre.getText();
	}

	public String getTextEmail() {
		return textEmail.getText();
	}

	public String getTextNombreUsuario() {
		return textNombreUsuario.getText();
	}

	public String getTextPassword() {
		return textPassword.getText();
	}

	public String getTextApellidos() {
		return textApellidos.getText();
	}
	*/
	
	public void resetErrorMessages(){
		
		//Borrar mensajes de error
		errorEmail.setText("");
		errorDni.setText("");
		errorNombre.setText("");
		errorApellidos.setText("");
		errorNombreUsuario.setText("");
		errorPassword.setText("");
		errorConfirmaPassword.setText("");
		
		//Borra bordes rojos y los deja como estaban al principio
		textEmail.setBorder(null);
		textEmail.updateUI();
		
		textDni.setBorder(null);
		textDni.updateUI();
		
		textNombre.setBorder(null);
		textNombre.updateUI();
		
		textApellidos.setBorder(null);
		textApellidos.updateUI();
		
		textNombreUsuario.setBorder(null);
		textNombreUsuario.updateUI();
		
		textPassword.setBorder(null);
		textPassword.updateUI();
		
		textConfirmacionPassword.setBorder(null);
		textConfirmacionPassword.updateUI();
	}
	
	public int[] validateInput()
	{
		int num[]=new int[7];
		
		//for(int i=0;i<7;i++)
		//	num[i]=0;
		
		String email = textEmail.getText().trim();
		String dni = textDni.getText().trim();
		String nombreUsuario = textNombreUsuario.getText().trim();
		
		if(isEmailValid(email))
		{
			//Si el email no sigue el patron devuelve 0
			//Si el email sigue el patron comprobamos si existe ya en la BD
				//Si el email no existe en la BD devuelve 1
				//Si el email existe en la BD 2
			if(isANewEmail(email))
				num[0]=1;
			else
				num[0]=2;
		}
		if(isDNIValid(dni))
		{
			if(isANewDni(dni))
				num[1]=1;
			else
				num[1]=2;
		}
		if(isNombreValid(textNombre.getText().trim())){
			num[2]=1;
		}
		if(isApellidosValid(textApellidos.getText().trim())){
			num[3]=1;
		}
		if(isNombreUsuarioValid(nombreUsuario))
		{
			if(isANewNombreUsuario(nombreUsuario))
				num[4]=1;
			else
				num[4]=2;
		}
		if(isPasswordValid(textPassword.getPassword())){
			num[5]=1;
		}
		if(isPasswordConfirmationValid(textPassword.getPassword(), textConfirmacionPassword.getPassword())){
			num[6]=1;
		}
		
		return num;
	}
	
	
	public void printErrorMessages(int[] num)
	{
		//Create a line border with the specified color and width
		Border border = BorderFactory.createLineBorder(Color.RED, 3);
		
		//for(int i=0;i<7;i++)
		//	System.out.println(num[i]);
		
		if(num[0]==0 || num[0]==2)
		{
			String msgEmail="";

			if(num[0] == 2)
				msgEmail = "Ya existe un usuario registrado con ese email";
			else
				msgEmail = "Introduce un email válido";
			
			errorEmail.setText(msgEmail);		
			textEmail.setBorder(border);//Set the border of this component
		}
		if(num[1]==0 || num[1]==2)
		{
			String msgDni = "";
			
			if(num[1] == 2)
				msgDni = "Ya existe un usuario registrado con ese DNI";
			else
				msgDni = "Introduce un DNI válido";
				
			errorDni.setText(msgDni);
			textDni.setBorder(border);
		}
		if(num[2]==0){
			errorNombre.setText("Introduce un nombre válido");
			textNombre.setBorder(border);
		}
		if(num[3]==0){
			errorApellidos.setText("Introduce unos apellidos válidos");
			textApellidos.setBorder(border);
		}
		if(num[4]==0 || num[4]==2)
		{
			String msgNombreUsuario = "";
			
			msgNombreUsuario = "Ya existe un usuario registrado con ese nick";
			
			if(num[4]==2)
				msgNombreUsuario = "Ya existe un usuario registrado con ese nick";
			else
				msgNombreUsuario = "Introduce un nick válido";
				
			errorNombreUsuario.setText(msgNombreUsuario);
			textNombreUsuario.setBorder(border);
		}
		if(num[5]==0){
			errorPassword.setText("Introduce un password válido");
			textPassword.setBorder(border);
		}
		if(num[6]==0){
			errorConfirmaPassword.setText("Las dos passwords no coinciden");
			textConfirmacionPassword.setBorder(border);
		}
	}

	/*Validaciones expresiones regulares
	 * 
	 * 
	 */
	
	public boolean isEmailValid(String em) //Si el email sigue el formato devuelve true
	{
		String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		
		if(!em.isEmpty())
		{
			if (em.matches(EMAIL_PATTERN))
				return true;
			else
				return false;
		}else
			return false;
	}
	
	//Comprobamos que no existe un usuario registrado con ese email
	//Si el email no esta dado de alta ya en la BD, permitimos al usuario darse de alta con ese mail
	public boolean isANewEmail(String em)
	{
		boolean b = logicaNegocio.isExistingEmail(em);
		System.out.println("a "+b);
		if(b)
			return true;
		else
			return false;	
	}

	public boolean isDNIValid(String dni) 
	{	
		if(!dni.isEmpty())
		{
			char ultimoCaracter = dni.charAt(dni.length()-1);//Cogemos la letra del DNI
		    int dniNumber = Integer.parseInt(dni.substring(0, dni.length()-1));//Cogemos el numero de DNI sin la letra
			char letraEsperada = "TRWAGMYFPDXBNJZSQVHLCKE".charAt(dniNumber % 23);
			
			if ((dni.matches("^[0-9]{8}+[A-Z]{1}$")) && (ultimoCaracter == letraEsperada))
				return true;
			else
				return false;
				
		}else
			return false;
	}
	
	
	//Comprobamos que no existe un usuario registrado con ese dni
	public boolean isANewDni(String dni)
	{
		boolean b = logicaNegocio.isExistingDni(dni);
		
		if(b)
			return true;
		else
			return false;	
	}
	
	public boolean isNombreValid(String nom) 
	{
		//El nombre solo puede tener caracteres alfabeticos
		String NOMBRE_PATTERN = "^[A-Za-z]+( [A-Za-z]+)*$";
		
		if(!nom.isEmpty())
		{
			if(nom.matches(NOMBRE_PATTERN))
				return true;
			else
				return false;
		}else
			return false;
	}
	
	public boolean isApellidosValid(String ap) //Igual que el nombre
	{
		//El apellido solo puede tener caracteres alfabeticos
		String APELLIDOS_PATTERN = "^[A-Za-z]+( [A-Za-z]+)*$";
		
		if(!ap.isEmpty())
		{
			if(ap.matches(APELLIDOS_PATTERN))
				return true;
			else
				return false;
		}else
			return false;
	}
	
	public boolean isNombreUsuarioValid(String nomUs) 
	{
		String NOMBREUSUARIO_PATTERN = "^[A-Za-z0-9-_]+( [A-Za-z0-9-_]+)*$";
		
		if(!nomUs.isEmpty())
		{
			if(nomUs.matches(NOMBREUSUARIO_PATTERN) )
				return true;	
			else
				return false;
		}else
			return false;
	}
	
	//Comprobamos que no existe un usuario registrado con ese email
	public boolean isANewNombreUsuario(String nomUs) 
	{
		boolean b = logicaNegocio.isExistingUserName(nomUs);
		
		if(b)
			return true;
		else
			return false;	
	}
	
	public boolean isPasswordValid(char[] pass) 
	{
		 //Longitud entre 8 y 16 caracteres
	     //Al menos una letra minuscula
	     //Al menos una letra mayuscula
	     //Al menos un numero
	     //Al menos un caracter especial
	  
		/*String PASSWORD_PATTERN = "^(?=.{8,16}$)(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[^a-zA-Z0-9_]).*";
		String strPass = String.valueOf(pass).trim();
		
		if(!strPass.isEmpty())
		{
		    if (strPass.matches(PASSWORD_PATTERN))
		    	return true;
		    else
				return false;
		}else
			return false;
			*/
		return true;
	}
	
	public boolean isPasswordConfirmationValid(char[] pass, char[] passConf) 
	{
		String strPass = String.valueOf(pass).trim();
		String strPassConf = String.valueOf(passConf).trim();
		
		if(strPass.equals(strPassConf))
		{
			return true;
		}else
			return false;
	}
}
