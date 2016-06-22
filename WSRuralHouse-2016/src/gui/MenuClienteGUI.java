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

public class MenuClienteGUI extends JFrame {

	private JPanel contentPane;
	
	private static String user;
	private static String pass;

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
					MenuClienteGUI frame = new MenuClienteGUI(user);
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
	public MenuClienteGUI(final String user) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
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
				
		JButton btnEnviarMensaje = new JButton("Enviar Mensaje Admin");
		btnEnviarMensaje.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendMessageToAdmin f = new sendMessageToAdmin(user);
				f.setVisible(true);
				setVisible(false);
			}
		});
		btnEnviarMensaje.setBounds(76, 82, 255, 25);
		contentPane.add(btnEnviarMensaje);
		
		JButton btnMostrarCasas = new JButton("Realizar Reserva");
		btnMostrarCasas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RealizarReserva f = new RealizarReserva(user);
				f.setVisible(true);
				setVisible(false);
				
			}
		});
		btnMostrarCasas.setBounds(76, 147, 255, 25);
		contentPane.add(btnMostrarCasas);
		setTitle("Menu cliente");
	}
}
