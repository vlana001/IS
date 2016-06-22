package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MenuPropietario extends JFrame {

	private static String user;
	private static String pass;
	
	private JPanel contentPane;
	private JButton btnMensajeAdmin = new JButton("Enviar Mensaje Admin");
	private JButton btnDarAltaCasa = new JButton("Dar Alta Casa");
	private JButton btnMisCasas = new JButton("Mis Casas");
	
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
					MenuPropietario frame = new MenuPropietario(user);
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
	public MenuPropietario(final String user) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setTitle("Menu propietario");
		
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
				
		
		btnMensajeAdmin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendMessageToAdmin f = new sendMessageToAdmin(user);
				f.setVisible(true);
				setVisible(false);
			}
		});
		btnMensajeAdmin.setBounds(76, 61, 274, 31);
		contentPane.add(btnMensajeAdmin);
		
		
	
		btnDarAltaCasa.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DarAltaCasaGUI d = new DarAltaCasaGUI(user);
				d.setVisible(true);
				setVisible(false);
			}
		});
		btnDarAltaCasa.setBounds(76, 119, 274, 31);
		contentPane.add(btnDarAltaCasa);
		
		
		btnMisCasas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MostrarCasasPropietarioGUI m = new MostrarCasasPropietarioGUI(user);
				m.setVisible(true);
				setVisible(false);
			}
		});
		btnMisCasas.setBounds(76, 178, 274, 31);
		contentPane.add(btnMisCasas);
		
	}

}
