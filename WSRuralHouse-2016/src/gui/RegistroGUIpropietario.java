package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JPasswordField;

import businessLogic.FacadeImplementationWS;

public class RegistroGUIpropietario extends JFrame {
private static final long serialVersionUID = 1L;

private FacadeImplementationWS logicaNegocio;

	private JPanel contentPane;
	
	private final JLabel lblUsuario = new JLabel("Nombre de Usuario:");
	private final JLabel lblPassword = new JLabel("Password:");
	private final JLabel lblConfirmaPassword = new JLabel("Confirma Password:");											//cp
	private final JLabel lblNombre = new JLabel("Nombre:");
	private final JLabel lblNewLabel = new JLabel("Dni:");
	private final JLabel lblApellidos = new JLabel("Apellidos:");									
	private final JLabel lblEmail = new JLabel("Email:");
	private final JLabel lblNumeroDeCuenta = new JLabel("Numero de Cuenta:");
	private final JLabel lblTelefono = new JLabel("Teléfono:");
	
	private JTextField textUsuario;
	private JPasswordField textPassword;
	private JPasswordField textPasswordConfirmation;
	private JTextField textNombre;
	private JTextField textDni;
	private JTextField textApellidos;
	private JTextField textEmail;
	private JTextField textNumCuenta;
	private JTextField textTelefono;
	
	private final JButton btnFinalizar = new JButton("Finalizar");
	
	
	private final JLabel errorNombreUsuario = new JLabel("");
	private final JLabel errorPassword = new JLabel("");
	private final JLabel errorConfirmaPassword = new JLabel("");
	private final JLabel errorDni = new JLabel("");
	private final JLabel errorNombre = new JLabel("");
	private final JLabel errorApellidos = new JLabel("");
	private final JLabel errorEmail = new JLabel("");
	private final JLabel errorNumeroCuenta = new JLabel("");
	private final JLabel errorTelefono = new JLabel("");
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RegistroGUIpropietario frame = new RegistroGUIpropietario();
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
	public RegistroGUIpropietario() {
		setTitle("Registro de Propietarios");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 602, 412);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		lblUsuario.setBounds(10, 1, 146, 31);
		contentPane.add(lblUsuario);
		lblPassword.setBounds(10, 42, 98, 31);
		contentPane.add(lblPassword);
		lblNewLabel.setBounds(12, 97, 109, 31);
		contentPane.add(lblNewLabel);
		lblNombre.setBounds(10, 124, 98, 31);
		contentPane.add(lblNombre);
		
		
		lblApellidos.setBounds(10, 166, 98, 14);
		contentPane.add(lblApellidos);
		lblEmail.setBounds(10, 205, 98, 31);
		contentPane.add(lblEmail);
		
		textUsuario = new JTextField();
		textUsuario.setBounds(157, 6, 133, 22);
		contentPane.add(textUsuario);
		textUsuario.setColumns(10);
		
		textDni = new JTextField();
		textDni.setBounds(157, 103, 133, 22);
		contentPane.add(textDni);
		textDni.setColumns(10);
		
		textNombre = new JTextField();
		textNombre.setBounds(157, 130, 133, 22);
		contentPane.add(textNombre);
		textNombre.setColumns(10);
		
		textApellidos = new JTextField();
		textApellidos.setBounds(157, 164, 133, 22);
		contentPane.add(textApellidos);
		textApellidos.setColumns(10);
		
		textEmail = new JTextField();
		textEmail.setBounds(157, 210, 133, 22);
		contentPane.add(textEmail);
		textEmail.setColumns(10);
		lblTelefono.setBounds(10, 280, 98, 31);
		contentPane.add(lblTelefono);
		
		textTelefono = new JTextField();
		textTelefono.setBounds(157, 285, 133, 22);
		contentPane.add(textTelefono);
		textTelefono.setColumns(10);
		
		textNumCuenta = new JTextField();
		textNumCuenta.setBounds(157, 244, 133, 22);
		contentPane.add(textNumCuenta);
		textNumCuenta.setColumns(10);
		
		
		errorNombreUsuario.setBounds(293, 1, 295, 23);
		contentPane.add(errorNombreUsuario);
		
		errorPassword.setBounds(324, 42, 264, 20);
		contentPane.add(errorPassword);
		
		errorConfirmaPassword.setBounds(301, 83, 287, 15);
		contentPane.add(errorConfirmaPassword);
		
		errorDni.setBounds(308, 97, 222, 20);
		contentPane.add(errorDni);
		
		errorNombre.setBounds(307, 124, 213, 31);
		contentPane.add(errorNombre);
		
		errorApellidos.setBounds(305, 152, 283, 33);
		contentPane.add(errorApellidos);
		
		errorEmail.setBounds(297, 205, 223, 33);
		contentPane.add(errorEmail);
		
		
		btnFinalizar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				resetErrorMessagesPropietario();//Limpiar mensajes de error
				
				boolean bien = true;			
				
				logicaNegocio = new FacadeImplementationWS();
				
				int num[] = validateInputPropietario();	//Validar datos introducidos
				
				for(int i=0;i<9;i++){
					if(num[i]==0)
						bien=false;
				}
				
				if(num[0] == 2 || num[1] == 2 || num[4] == 2)
					bien=false;
				
				if(!bien){
					printErrorMessagesPropietario(num); //Imprimir mensajes de error
				}else{
					
				
				String nombreUser = textUsuario.getText();
				
				char[] password = textPassword.getPassword();
				String strPassword = String.valueOf(password);
				
				String nombre = textNombre.getText();
				String apellidos = textApellidos.getText();
				String email = textEmail.getText();
				String dni = textDni.getText();
				String numeroCuenta = textNumCuenta.getText();
				int telefono = Integer.parseInt(textTelefono.getText());
				
					
				logicaNegocio.saveOwnerData(nombreUser, strPassword, nombre, apellidos, email, dni, numeroCuenta, telefono);
				
				LoginGUI b = new LoginGUI("a"); 
				b.setVisible(true);
				setVisible(false);
			}
		}});
		btnFinalizar.setBounds(153, 329, 146, 31);
		contentPane.add(btnFinalizar);
		
		
		lblNumeroDeCuenta.setBounds(10, 242, 146, 31);
		contentPane.add(lblNumeroDeCuenta);
		
		
		lblConfirmaPassword.setBounds(10, 80, 146, 15);
		contentPane.add(lblConfirmaPassword);
		
		textPasswordConfirmation = new JPasswordField();
		textPasswordConfirmation.setBounds(157, 78, 133, 22);
		contentPane.add(textPasswordConfirmation);
		
		textPassword = new JPasswordField();
		textPassword.setBounds(157, 43, 133, 22);
		contentPane.add(textPassword);
		
		
		errorNumeroCuenta.setBounds(308, 243, 280, 22);
		contentPane.add(errorNumeroCuenta);
		
		
		errorTelefono.setBounds(303, 280, 285, 31);
		contentPane.add(errorTelefono);
	}
	
	//validaciones de los atributos
	
	public boolean isTelefonoValid(String tel) 
	{
		String TELEFONO_PATTERN = "^[6,9]{1}+[0-9]{8}$";
		
		
		if(!tel.isEmpty())
		{
			if(tel.matches(TELEFONO_PATTERN))
			{
				return true;
			}else
				return false;
		}else
			return false;
			//return true;
	}
	
	public boolean isNumeroCuentaValid(String numCuenta)
	{
		String NUMEROCUENTA_PATTERN = "^[0-9]{20}";
		
		if(!numCuenta.isEmpty())
		{
			if(numCuenta.matches(NUMEROCUENTA_PATTERN))
				return true;
			else
				return false;
		}else
			return false;
	}
	
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
		boolean b = logicaNegocio.isExistingEmailOwner(em);
		
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
		boolean b = logicaNegocio.isExistingDniOwner(dni);
		
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
		boolean b = logicaNegocio.isExistingUserNameOwner(nomUs);
		
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
	
	
	
	public void resetErrorMessagesPropietario(){
		errorEmail.setText("");
		errorDni.setText("");
		errorNombre.setText("");
		errorApellidos.setText("");
		errorNombreUsuario.setText("");
		errorPassword.setText("");
		errorConfirmaPassword.setText("");
		errorNumeroCuenta.setText("");
		errorTelefono.setText("");
		
		//Borra bordes rojos y los deja como estaban al principio
		textEmail.setBorder(null);
		textEmail.updateUI();
		
		textDni.setBorder(null);
		textDni.updateUI();
		
		textNombre.setBorder(null);
		textNombre.updateUI();
		
		textApellidos.setBorder(null);
		textApellidos.updateUI();
		
		textUsuario.setBorder(null);
		textUsuario.updateUI();
		
		textPassword.setBorder(null);
		textPassword.updateUI();
		
		textPasswordConfirmation.setBorder(null);
		textPasswordConfirmation.updateUI();
		
		textNumCuenta.setBorder(null);
		textNumCuenta.updateUI();
		
		textTelefono.setBorder(null);
		textTelefono.updateUI();
	}
	
	private int[] validateInputPropietario(){
			int num[]=new int[9];
			
			for(int i=0;i<9;i++)
				num[i]=0;
			
			String email = textEmail.getText().trim();
			String dni = textDni.getText().trim();
			String nombreUsuario = textUsuario.getText().trim();
			
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
			if(isPasswordConfirmationValid(textPassword.getPassword(), textPasswordConfirmation.getPassword())){
				num[6]=1;
			}
			if(isNumeroCuentaValid(textNumCuenta.getText())){
				num[7]=1;
			}
			if(isTelefonoValid(textTelefono.getText())){
				num[8]=1;
			}
			
			return num;
	}
	
	
	public void printErrorMessagesPropietario(int[] num)
	{
		//Create a line border with the specified color and width
		Border border = BorderFactory.createLineBorder(Color.RED, 3);
		
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
			textUsuario.setBorder(border);
		}
		if(num[5]==0){
			errorPassword.setText("Introduce un password válido");
			textPassword.setBorder(border);
		}
		if(num[6]==0){
			errorConfirmaPassword.setText("Las dos passwords no coinciden");
			textPasswordConfirmation.setBorder(border);
		}
		if(num[7]==0){
			errorNumeroCuenta.setText("Introduce un numero de cuenta valido");
			textNumCuenta.setBorder(border);
		}
		if(num[8]==0){
			errorTelefono.setText("Introduce un telefono valido");
			textTelefono.setBorder(border);
		}
	}
	
	}

