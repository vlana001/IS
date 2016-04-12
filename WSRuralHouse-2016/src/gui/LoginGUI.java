package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;

import domain.Cliente;
import domain.Propietario;

import java.awt.Window.Type;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.*;
import javax.swing.JPasswordField;

import businessLogic.FacadeImplementationWS;
import java.awt.Component;
import javax.swing.Box;
import javax.swing.JToggleButton;
import java.awt.Color;

public class LoginGUI extends JFrame {
private static final long serialVersionUID = 1L;

	private FacadeImplementationWS logicaNegocio;
	
	private JPanel contentPane;
	
	//Mostrar mensaje de error
	private final JLabel lblMsgError = new JLabel("");
	
	private final JLabel lblUsuario = new JLabel("USUARIO:");
	private final JLabel lblPassword = new JLabel("PASSWORD:");
	
	private final JTextField textUsuario = new JTextField();
	private JPasswordField textPassword;
	
	private final JButton buttonAceptar = new JButton("Aceptar");
	
	private final JLabel lblRegistrarse = new JLabel("REGISTRARSE:");
	private final JButton buttonRegistro = new JButton("Registro");
	
	private final JButton btnOlvideLaContrasea = new JButton("Olvide la contraseña");
	//private JPanel jContentPane = null;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginGUI frame = new LoginGUI("a");
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
	public LoginGUI(String email) {
		setTitle("HouseBookingLogin");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		lblUsuario.setBounds(12, 46, 98, 31);
		
		//Mostrar mensaje 
		if(!email.equals("a"))
			lblMsgError.setText("Se ha efectuado el cambio de contraseña");
		
		lblMsgError.setBounds(55, 12, 360, 22);
		contentPane.add(lblMsgError);
		
		contentPane.add(lblUsuario);
		lblPassword.setBounds(12, 89, 98, 31);
		
		contentPane.add(lblPassword);
		textUsuario.setBounds(129, 46, 206, 31);
		
		contentPane.add(textUsuario);
		textUsuario.setColumns(10);
		
		textPassword = new JPasswordField();
		textPassword.setBounds(129, 89, 206, 31);
		contentPane.add(textPassword);
		
		buttonAceptar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				//Limpiar mensaje de error
				lblMsgError.setText("");
				
				String userName = textUsuario.getText().trim();
				char[] password = textPassword.getPassword();
				String strPassword = String.valueOf(password).trim();
				String msg ="";
				
				if(userName.isEmpty() || strPassword.isEmpty())
				{
					//Si alguno de los campos estan vacios mostramos un mensaje indicandolo
					msg = "Inserte su nombre de usuario y contraseña";
					lblMsgError.setText(msg);
					
				}else
				{
					logicaNegocio = new FacadeImplementationWS();
					
					int prueba=logicaNegocio.isLoginValid(userName, strPassword);
					//System.out.println(prueba);
					
					if(prueba == 1)
					{ //Login correcto, es un cliente
					  //Redireccionamos al jframe 
						if(logicaNegocio.checkClientBlocked(userName)==false)
						{
							mostrarCasasGUI a=new mostrarCasasGUI(); //creamos un main para que redireccione a otro 
							a.setVisible(true);
							setVisible(false);//ponemos la ventana de login invisible
						}else
						{
							msg = "Cliente bloqueado por el administrador";
						}		
					}else
					{
						if(prueba == 2)
						{
							//Login correcto, es un cliente propietario
							//Redireccionamos al jframe 
								
							DarAltaCasaGUI a=new DarAltaCasaGUI(); //creamos un main para que redireccione a otro 
							a.setVisible(true);
							setVisible(false);//ponemos la ventana de login invisible
						}else
						{
							//Mostrar mensaje de error
							msg = "Nombre de usuario y/o contraseña incorrectos";
						}
						
					}
				}
				lblMsgError.setText(msg);
			}
		});
		buttonAceptar.setBounds(165, 132, 120, 31);
		contentPane.add(buttonAceptar);
		
		lblRegistrarse.setBounds(10, 211, 98, 31);
		contentPane.add(lblRegistrarse);
		
		
		buttonRegistro.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				RegistroGUI b=new RegistroGUI(); //cuando clickamos en registrar nos redirecciona a la ventana registro
				b.setVisible(true);
				setVisible(false);
			}
		});
		buttonRegistro.setBounds(118, 211, 120, 31);
		contentPane.add(buttonRegistro);	
		
		Component horizontalGlue = Box.createHorizontalGlue();
		horizontalGlue.setBounds(150, 187, 1, 1);
		contentPane.add(horizontalGlue);
		
		btnOlvideLaContrasea.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RestablecerPassword restablecerPassword = new RestablecerPassword();
				restablecerPassword.setVisible(true);
				setVisible(false);
			}
		});
		btnOlvideLaContrasea.setForeground(Color.BLACK);
		btnOlvideLaContrasea.setBounds(12, 175, 186, 25);
		contentPane.add(btnOlvideLaContrasea);
	}
}
