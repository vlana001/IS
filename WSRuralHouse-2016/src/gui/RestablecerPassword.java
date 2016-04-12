package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import businessLogic.FacadeImplementationWS;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;

import domain.Cliente;

public class RestablecerPassword extends JFrame {

	private FacadeImplementationWS logicaNegocio;
	
	private JPanel contentPane;
	private JTextField textCorreo = new JTextField();;

	JLabel lblEmail = new JLabel("EMAIL:");
	
	//Mostrar mensaje
	JLabel lblmsgResetEmail = new JLabel("");
	
	private final ButtonGroup buttonGroup = new ButtonGroup();
	JRadioButton rdbtnCliente = new JRadioButton("Cliente");
	JRadioButton rdbtnPropietario = new JRadioButton("Propietario");
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RestablecerPassword frame = new RestablecerPassword();
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
	public RestablecerPassword() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnRestablecerContrasea = new JButton("Restablecer contraseña");
		btnRestablecerContrasea.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				//Limpiar mensaje de error
				lblmsgResetEmail.setText("");
				
				String msg="";
				String userEmail = textCorreo.getText().trim();
				
				boolean bool = false;
				
				if(userEmail.isEmpty())
				{
					msg = "Inserte su email";
					bool = true;
				}
				else
				{
					if(!isEmailValid(userEmail))
					{
						msg = "El formato del email no es valido";
						bool = true;
					}else
					{
						String u = "";
						if (rdbtnCliente.isSelected())
							u = rdbtnCliente.getText();
						if (rdbtnPropietario.isSelected())
							u = rdbtnPropietario.getText();
						
						boolean bool1 = false;
						logicaNegocio = new FacadeImplementationWS();
						
						if(u.equals("Cliente"))
						{
							if(logicaNegocio.isExistingEmail(userEmail) == true)
							{
								msg = "No existe un usuario cliente con ese email";
								bool = true;
								bool1 = true;
							}
						}
						
						if(u.equals("Propietario"))
						{
							if(logicaNegocio.isExistingEmailOwner(userEmail) == true)
							{
								msg = "No existe un usuario propietario con ese email";
								bool = true;
								bool1 = true;
							}
						}
							
							if(!bool1)
							{//El campo email no esta vacio
							 //El campo email tiene un email con un formato correcto
							 //El campo email tiene un email de un usuario existente
		
								//Enviar email
								boolean b = logicaNegocio.enviarEmail(userEmail, u);
								
								if(b == true)
								{
									//Pasar datos entre JFrames
									ChangePassword changePassword = new ChangePassword(userEmail, u); //redireccionar a la pagina de cambiar contraseña
									changePassword.setVisible(true);
									setVisible(false);
								}
								else
									msg = "Ha ocurrido algun problema al enviar el email a" + userEmail;
							}
					}	
				}
				lblmsgResetEmail.setText(msg);
				
				if(bool == true)
				{
					//Create a line border with the specified color and width
					Border border = BorderFactory.createLineBorder(Color.RED, 3);
					textCorreo.setBorder(border);
				}
			}
		});
		btnRestablecerContrasea.setBounds(94, 200, 265, 25);
		contentPane.add(btnRestablecerContrasea);
		
		
		lblEmail.setBounds(37, 82, 98, 31);
		contentPane.add(lblEmail);
		
		textCorreo.setColumns(10);
		textCorreo.setBounds(122, 82, 206, 31);
		contentPane.add(textCorreo);
		
		
		lblmsgResetEmail.setBounds(68, 24, 336, 15);
		contentPane.add(lblmsgResetEmail);
		
		
		rdbtnCliente.setSelected(true);
		buttonGroup.add(rdbtnCliente);
		rdbtnCliente.setBounds(68, 121, 87, 23);
		contentPane.add(rdbtnCliente);
		
		
		buttonGroup.add(rdbtnPropietario);
		rdbtnPropietario.setBounds(158, 121, 116, 23);
		contentPane.add(rdbtnPropietario);
	}
	
	public boolean isEmailValid(String em) //Si el email sigue el formato devuelve true
	{
		String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		
		if (em.matches(EMAIL_PATTERN))
			return true;
		else
			return false;
	}
}
