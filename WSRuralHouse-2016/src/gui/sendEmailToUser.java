package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.BorderFactory;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.ScrollPaneConstants;

import businessLogic.FacadeImplementationWS;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;

public class sendEmailToUser extends JFrame {
	
	private FacadeImplementationWS logicaNegocio;
	
	private JPanel contentPane;
	private JTextField textFieldEmail;
	private JScrollPane scrollPane = new JScrollPane();
	
	private JLabel lblMsg = new JLabel("");
	private JLabel lblMsg2 = new JLabel("");
	private JLabel lblEmail = new JLabel("Email");
	private JLabel lblMensaje = new JLabel("Mensaje");
	
	private final String emailUser;
	private final JTextArea textAreaMessage = new JTextArea();
	
	//Menu
	JMenuBar menuBar;
	JMenu menuAdmin, leerMensajes, exit;
		
	/**
	 * Launch the application.
	 */
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					sendEmailToUser frame = new sendEmailToUser("a");
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
	public sendEmailToUser(String email) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 447, 391);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setTitle("Responder email");
		
		//Menu
		menuBar = new JMenuBar();
		
		menuAdmin = new JMenu("Menu Admin");
		menuBar.add(menuAdmin);
		menuAdmin.addMenuListener(new MenuListener() {
			@Override
			public void menuSelected(MenuEvent e) {
				MenuAdminGUI m = new MenuAdminGUI();
				m.setVisible(true);
				setVisible(false);
			}
			
			 @Override
		        public void menuDeselected(MenuEvent e) {	
		     }
			 
			@Override
			public void menuCanceled(MenuEvent e) {
			}
	    });
		
		leerMensajes = new JMenu("Leer Mensajes");
		menuBar.add(leerMensajes);
		leerMensajes.addMenuListener(new MenuListener() {
			@Override
			public void menuSelected(MenuEvent e) {
				readMessagesAdmin m = new readMessagesAdmin();
				m.setVisible(true);
				setVisible(false);
			}
			
			 @Override
		        public void menuDeselected(MenuEvent e) {	
		     }
			 
			@Override
			public void menuCanceled(MenuEvent e) {
			}
	    });
		
		exit = new JMenu("Exit");
		menuBar.add(exit);
		exit.addMenuListener(new MenuListener() {
			@Override
			public void menuSelected(MenuEvent e) {
				System.exit(0);
			}
			
			 @Override
		        public void menuDeselected(MenuEvent e) {	
				 //System.exit(0);
		     }
			 
			@Override
			public void menuCanceled(MenuEvent e) {
			}
	    });
		this.setJMenuBar(menuBar);		
				
		emailUser = email;
		//Create a line border with the specified color and width
		Border border = BorderFactory.createLineBorder(Color.cyan, 1);
			
		textFieldEmail = new JTextField("");
		textFieldEmail.setBounds(30, 90, 354, 26);
		contentPane.add(textFieldEmail);
		textFieldEmail.setColumns(10);
		textFieldEmail.setBorder(border);
		textFieldEmail.disable();
		textFieldEmail.setText(emailUser);
		
		lblEmail.setBounds(30, 63, 70, 15);
		contentPane.add(lblEmail);
		
		
		lblMensaje.setBounds(25, 144, 70, 15);
		contentPane.add(lblMensaje);
		
		
		JButton btnEnviar = new JButton("Enviar");
		btnEnviar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				//Limpiar mensajes de error
				resetErrorMessages();
				
				String mensaje = textAreaMessage.getText().trim();
				
				if(mensaje.length() == 0)
					lblMsg.setText("No puede enviar un email vacio");
				else
				{
					//System.out.println("email "+emailUser);
					//System.out.println("mensaje "+mensaje);
					
					//lblMsg.setText("Enviando email ...");
					
					//Enviamos el mensaje al user
					logicaNegocio = new FacadeImplementationWS();
					boolean b = logicaNegocio.enviarAdminEmailUsuario(emailUser, mensaje);
					
					if(b == true)
					{
						lblMsg.setText("Envio correcto del email");
						lblMsg2.setText("Puede tardar varios minutos en recibirlo");
					}
					else
						lblMsg.setText("No se pudo enviar el email");
				}
			}
		});
		btnEnviar.setBounds(157, 296, 117, 25);
		contentPane.add(btnEnviar);
		
		lblMsg.setBounds(98, 19, 324, 15);
		contentPane.add(lblMsg);
	
		lblMsg2.setBounds(86, 46, 325, 15);
		contentPane.add(lblMsg2);
		
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(30, 176, 354, 97);
		scrollPane.setBorder(border);
		contentPane.add(scrollPane);
		
		//Cuando una linea de texto alcanza el final del cuadro de textArea, pasar a nueva linea
		scrollPane.setViewportView(textAreaMessage);
		textAreaMessage.setWrapStyleWord(true);
		textAreaMessage.setLineWrap(true);
	
	}
	
	
	public void resetErrorMessages(){
		lblMsg.setText("");
		lblMsg2.setText("");
	}
}
