package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JMenuBar;

import businessLogic.FacadeImplementationWS;

public class MenuAdminGUI extends JFrame {

	private FacadeImplementationWS logicaNegocio;
	
	private JPanel contentPane;
	JButton btnBloquearClientes = new JButton("Bloquear/Desbloquear clientes");
	JButton btnLeerMensajes;
	
	//Menu
	JMenuBar menuBar;
	JMenu exit;
    
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MenuAdminGUI frame = new MenuAdminGUI();
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
	public MenuAdminGUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setTitle("Menu Admin");
		
		//Menu
		menuBar = new JMenuBar();
		
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
		
		btnBloquearClientes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BlockUnblockClients f = new BlockUnblockClients();
				f.setVisible(true);
				setVisible(false);
			}
		});
		btnBloquearClientes.setBounds(85, 78, 253, 25);
		contentPane.add(btnBloquearClientes);	
		
		logicaNegocio = new FacadeImplementationWS();
		
		int numMensages = logicaNegocio.getUnreadMessagesNumber();
		String textoBotonLeerMensajes = "Leer mensajes (" + numMensages + ")";
		
		btnLeerMensajes = new JButton(textoBotonLeerMensajes);
		btnLeerMensajes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				readMessagesAdmin f = new readMessagesAdmin();
				f.setVisible(true);
				setVisible(false);
			}
		});
		btnLeerMensajes.setBounds(85, 145, 253, 25);
		contentPane.add(btnLeerMensajes);
	}
}
