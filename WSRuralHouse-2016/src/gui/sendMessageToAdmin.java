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
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JButton;

import businessLogic.FacadeImplementationWS;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JTextArea;

public class sendMessageToAdmin extends JFrame {

	private FacadeImplementationWS logicaNegocio;
	
	private JPanel contentPane;
	private JTextField asunto = new JTextField();
	private JLabel lblAsunto = new JLabel("Asunto mensaje");
	private JLabel lblMensaje = new JLabel("Mensaje");
	private JLabel lblMsg = new JLabel("");
	private final JButton btnEnviar = new JButton("Enviar");
	private JTextArea textAreaMessage = new JTextArea();
	private JScrollPane scrollPane = new JScrollPane();
	
	private static String user;
	
	//Menu
	JMenuBar menuBar;
	JMenu menuPrincipal, exit;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					sendMessageToAdmin frame = new sendMessageToAdmin("user");
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
	public sendMessageToAdmin(final String user) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 453, 356);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setTitle("Enviar Mensaje al admin");
		
		logicaNegocio = new FacadeImplementationWS();
		
		//Menu
		menuBar = new JMenuBar();
		
		menuPrincipal = new JMenu("Menu Principal");
		menuBar.add(menuPrincipal);
		menuPrincipal.addMenuListener(new MenuListener() {
			@Override
			public void menuSelected(MenuEvent e) {
				
				boolean b = logicaNegocio.getUserRolByUserName(user);
				
				if(!b)
				{
					MenuClienteGUI m = new MenuClienteGUI(user);
					m.setVisible(true);
					setVisible(false);
				}else
				{
					MenuPropietario m = new MenuPropietario(user);
					m.setVisible(true);
					setVisible(false);
				}
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
				
		//Create a line border with the specified color and width
		Border border1 = BorderFactory.createLineBorder(Color.cyan, 1);
		
		lblMsg.setBounds(24, 0, 400, 27);
		contentPane.add(lblMsg);
		
		lblAsunto.setBounds(24, 39, 123, 15);
		contentPane.add(lblAsunto);
		
		lblMensaje.setBounds(24, 128, 70, 15);
		contentPane.add(lblMensaje);
		
		asunto.setBounds(24, 66, 370, 24);
		contentPane.add(asunto);
		asunto.setColumns(10);
		asunto.setBorder(border1);
		
		btnEnviar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				resetErrorMessages();//Limpiar mensajes de error
				
				String msgAsunto = asunto.getText().trim();
				String msgCuerpo = textAreaMessage.getText().trim();
				
				//Create a line border with the specified color and width
				Border border = BorderFactory.createLineBorder(Color.RED, 3);
				
				String msg ="";
				
				boolean b_a = false;
				boolean b_m = false;
				
				if(msgAsunto.isEmpty())
					b_a=true;
				if(msgCuerpo.isEmpty())
					b_m=true;
				
				if(b_a == true || b_m == true)
				{
					msg = "Inserte un asunto y un cuerpo de mensaje";
					
					if(b_a == true)
						asunto.setBorder(border);
					if(b_m == true)
						textAreaMessage.setBorder(border);
				}else
				{
					if(isMsgAsuntoValid(msgAsunto) == false)
					{
						msg = "El asunto del mensaje no puede tener mas de 50 caracteres";
						asunto.setBorder(border);
					}else
					{
						if(isMsgCuerpoValid(msgCuerpo)==false)
						{
							msg = "El cuerpo del mensaje no puede tener mas de 300 caracteres";
							textAreaMessage.setBorder(border);
						}else
						{
							msg = "Mensaje enviado con exito";
							logicaNegocio.saveMessage(user, msgAsunto, msgCuerpo);//user, msgAsunto, msgCuerpo
						}
					}
				}
				lblMsg.setText(msg);
			}
		});
		btnEnviar.setBounds(143, 267, 140, 33);
		contentPane.add(btnEnviar);
		
		
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(24, 158, 370, 97);
		scrollPane.setBorder(border1);
		contentPane.add(scrollPane);
		
		//Cuando una linea de texto alcanza el final del cuadro de textArea, pasar a nueva linea
		scrollPane.setViewportView(textAreaMessage);
		textAreaMessage.setWrapStyleWord(true);
		textAreaMessage.setLineWrap(true);
	}
	
	//Borrar mensajes de error
	public void resetErrorMessages(){
		lblMensaje.setText("");
		
		//Borra bordes rojos y los deja como estaban al principio
		asunto.setBorder(null);
		asunto.updateUI();
		
		textAreaMessage.setBorder(null);
		textAreaMessage.updateUI();
	}
	
	//Validar datos introducidos
	//asunto maximo 50 caracteres
	//mensaje maximo 300 caracteres
	public boolean isMsgAsuntoValid(String asunto)
	{
		if(asunto.length() > 50)
			return false;
		else
			return true;	
	}
	
	public boolean isMsgCuerpoValid(String cuerpo)
	{
		if(cuerpo.length() > 300)
			return false;
		else
			return true;	
	}
}
