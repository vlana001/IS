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

public class ChangePassword extends JFrame {
	private FacadeImplementationWS logicaNegocio;

	private JPanel contentPane;
	
	private JTextField textToken = new JTextField();
	private JPasswordField textPassword = new JPasswordField();
	private JPasswordField textNewPassword = new JPasswordField();
	
	JLabel lblMensaje = new JLabel("");
	JLabel lblNuevaContrasea = new JLabel("Nueva contraseña:");
	JLabel lblRepitaLaNueva = new JLabel("Repita la nueva contraseña:");
	JLabel lblCodigoRecibido = new JLabel("Codigo recibido:");
	
	JButton btnCambiarContraseña = new JButton("Cambiar contraseña");
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ChangePassword frame = new ChangePassword("a", "a");
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
	public ChangePassword(String email, String user_type) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		//Mostrar mensaje 
		if(!email.equals("a"))
			lblMensaje.setText("Se ha enviado un email a " + email);
		
		final String email1 = email;
		final String user1 = user_type;
		lblNuevaContrasea.setBounds(12, 136, 182, 31);
		contentPane.add(lblNuevaContrasea);
		
		
		btnCambiarContraseña.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				//limpiar mensajes de error
				lblMensaje.setText("");
				
				//Borra bordes rojos y los deja como estaban al principio
				textToken.setBorder(null);
				textToken.updateUI();
				textPassword.setBorder(null);
				textPassword.updateUI();
				textNewPassword.setBorder(null);
				textNewPassword.updateUI();
				
				//Obtiene valores introducidos por el usuario
				String token = textToken.getText().trim();
				
				char[] password = textPassword.getPassword();
				String strPassword = String.valueOf(password).trim();
				
				char[] confPassword = textNewPassword.getPassword();
				String strConfPassword = String.valueOf(confPassword).trim();
				
				
				String msg = "";
				boolean bool = false;
				
				//Create a line border with the specified color and width
				Border border = BorderFactory.createLineBorder(Color.RED, 3);
				
				
				if(token.isEmpty())
				{
					msg = "Inserte el token \n";
					bool = true;
					textToken.setBorder(border);
				}
					
				
				if(strPassword.isEmpty())
				{
					msg = msg + "Inserte la  nueva contraseña \n";
					bool = true;
					textPassword.setBorder(border);
				}
					
					
				if(strConfPassword.isEmpty())
				{
					msg = msg + "Inserte la confirmacion de contraseña \n";
					bool = true;
					textNewPassword.setBorder(border);
				}
					
				
				if(!strPassword.isEmpty() && !strConfPassword.isEmpty())
				{
					if(!isPasswordValid(password))
					{
						msg = msg + "La contraseña no es valida \n";
						bool = true;
						textPassword.setBorder(border);
					}
						
					
					if(!isPasswordConfirmationValid(password, confPassword))
					{
						msg = msg + "Las contraseñas no coinciden \n";
						bool = true;
						textNewPassword.setBorder(border);
					}
				}
				
				
				
				if(bool == false)//Todos los datos introducidos son correctos
				{
					/*Comprobamos si el token es valido, si lo es redirigimos a login y 
					 mostramos un mensaje de cambio de contraseña efectuado, si no, 
					 mostramos un mensaje de error en ChangePassword
					*/
					
					
					boolean bool1 = false;
					boolean bool2 = false;;
					boolean bool3 = false;
					
					//Comprobar si el token introducido existe para el usuario
					logicaNegocio = new FacadeImplementationWS();
					bool1 = logicaNegocio.checkTokenValidity(email1, token, user1);
					
					//Si el token existe, comprobar que no ha sido usado, ni ha expirado
					if(bool1 == true)
					{	
						//Comprobar si el token ha expirado
						bool2 = logicaNegocio.isTokenExpired(email1, token, user1);
						
						if (bool2 == false)//Si el token no ha expirado, comprobamos si ha sido usado
						{
							//Comprobar si el token ha sido usado
							bool3 = logicaNegocio.isTokenAlreadyUsed(email1, token, user1);
						}
						
					} 
					
					boolean bool_token = false;
					
					if(bool1 == false)
					{
						msg = "El token no es valido";
						bool_token = true;
					}

					if(bool2 == true)
					{
						msg = "El token ha expirado";
						bool_token = true;
					}

					if(bool3 == true)
					{
						msg = "El token ya ha sido usado";
						bool_token = true;
					}
						
			
					if(bool_token == false)
					{
						//cambiar contraseña
						logicaNegocio.saveNewPassword(email1, token, strPassword, user1);
						
						//Pasar datos entre JFrames
						LoginGUI login = new LoginGUI(email1); //redireccionar a la pagina de login
						login.setVisible(true);
						setVisible(false);
					}
						
				}
				
				lblMensaje.setText(msg);
			}
		});
		btnCambiarContraseña.setBounds(112, 234, 265, 25);
		contentPane.add(btnCambiarContraseña);	
		
		
		lblRepitaLaNueva.setBounds(12, 179, 206, 25);
		contentPane.add(lblRepitaLaNueva);
		
		
		lblCodigoRecibido.setBounds(12, 89, 135, 15);
		contentPane.add(lblCodigoRecibido);
		
		textToken.setColumns(10);
		textToken.setBounds(230, 81, 206, 31);
		contentPane.add(textToken);		
		
		lblMensaje.setBounds(12, 12, 409, 50);
		contentPane.add(lblMensaje);	
		
		textPassword.setBounds(230, 124, 206, 31);
		contentPane.add(textPassword);
		
		textNewPassword.setBounds(230, 176, 206, 31);
		contentPane.add(textNewPassword);
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
