package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Rectangle;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JScrollPane;
import javax.swing.ListModel;

import businessLogic.FacadeImplementationWS;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;

import com.db4o.ObjectSet;

import domain.Cliente;
import domain.Offer;
import javax.swing.JSpinner;

import java.util.EventListener;
import javax.swing.ScrollPaneConstants;

public class BlockUnblockClients extends JFrame {

	private FacadeImplementationWS logicaNegocio;
	
	private JPanel contentPane;
	
	//Mensaje
	JLabel lblMsg = new JLabel("");
	
	//No bloqueado
	private DefaultListModel listUnbloquedInfo = new DefaultListModel();//Modelo
	private JList listClientesNoBloqueados = new JList(listUnbloquedInfo);//Lista
	private JLabel lblClientesNoBloqueados = new JLabel("Clientes No bloqueados");
	private JButton btnBloquear = new JButton("Bloquear ");
	private final JScrollPane scrollPaneClientesNoBloqueados = new JScrollPane();//scrollPane
	
	//Bloqueado
	private DefaultListModel listBloquedInfo = new DefaultListModel();
	private JList listCLientesBloqueados = new JList(listBloquedInfo);
	private JLabel lblClientesBloqueados = new JLabel("Clientes Bloqueados");
	private JButton btnDesbloquear = new JButton("Desbloquear ");
	private final JScrollPane scrollPaneClientesBloqueados = new JScrollPane();
	
	private String correoBloquear;
	private String correoDesbloquear;
	
	
	//Menu
	JMenuBar menuBar;
	JMenu menuAdmin, exit;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BlockUnblockClients frame = new BlockUnblockClients();
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
	public BlockUnblockClients() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 439, 361);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		lblClientesNoBloqueados.setBounds(31, 83, 178, 15);
		contentPane.add(lblClientesNoBloqueados);
		
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
		
		btnBloquear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(listClientesNoBloqueados.getSelectedValue() != null)
					getNewLists(correoBloquear, true);
				//System.out.println(correoBloquear);
			}
		});
		btnBloquear.setBounds(51, 271, 147, 32);
		contentPane.add(btnBloquear);
		
	
		lblClientesBloqueados.setBounds(252, 83, 154, 15);
		contentPane.add(lblClientesBloqueados);
		
		
		btnDesbloquear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(listCLientesBloqueados.getSelectedValue() != null)
					getNewLists(correoDesbloquear, false);
			}
		});
		btnDesbloquear.setBounds(259, 271, 147, 32);
		contentPane.add(btnDesbloquear);
		
		lblMsg.setBounds(62, 23, 296, 15);
		contentPane.add(lblMsg);
		scrollPaneClientesBloqueados.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPaneClientesBloqueados.setBounds(245, 110, 168, 127);
		contentPane.add(scrollPaneClientesBloqueados);
		scrollPaneClientesBloqueados.setViewportView(listCLientesBloqueados);
		
		
		listCLientesBloqueados.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
			public void valueChanged(javax.swing.event.ListSelectionEvent e) {
				// El evento se dispara dos veces: antes de cambiar el valor y una vez cambiado
				// Interesa solo actuar una vez cambiado
				if (e.getValueIsAdjusting()) return; 
				if(!listBloquedInfo.isEmpty())
				{
					String selectedEmail = listCLientesBloqueados.getSelectedValue().toString();
					correoDesbloquear = selectedEmail;
				}
				
			}
		});
		scrollPaneClientesNoBloqueados.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPaneClientesNoBloqueados.setBounds(41, 110, 168, 127);
		contentPane.add(scrollPaneClientesNoBloqueados);
		scrollPaneClientesNoBloqueados.setViewportView(listClientesNoBloqueados);
		
		
				
				
		//Cuando se selecciona un cliente de la lista pasar el valor del email 
		listClientesNoBloqueados.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
			public void valueChanged(javax.swing.event.ListSelectionEvent e) {
				// El evento se dispara dos veces: antes de cambiar el valor y una vez cambiado
				// Interesa solo actuar una vez cambiado
				if (e.getValueIsAdjusting()) return; 
				
				//if (!listClientesNoBloqueados.isSelectionEmpty()){ // A este metodo se le llama tambien cuando se hace un clear del JList, 
				// por lo que podria estar la seleccion vacia y dar un error
				if(!listUnbloquedInfo.isEmpty())
				{
					String selectedEmail = listClientesNoBloqueados.getSelectedValue().toString();
					correoBloquear = selectedEmail;
				}
				
				/*ListModel model = listClientesNoBloqueados.getModel();

				System.out.println("Imprimir lista");
				for(int i=0; i < model.getSize(); i++)
				     System.out.println(model.getElementAt(i)); 
				*/
			}
		});

		//Obtener las 2 listas
		loadLists();
	}
	
	
	
	
	//Cada vez que se pulsa cualquiera de los 2 botones, volver a obtener la nueva lista de bloqueados y no bloqueados
	// y mostrar nuevo mensaje
	public void getNewLists(String email, boolean estado)
	{
		//Cambiar estado cliente
		logicaNegocio.changeClientStatus(email, estado);
		
		//LImpiar modelos
		listUnbloquedInfo.removeAllElements();
		listBloquedInfo.removeAllElements();
		
		//Obtener nuevas listas
		loadLists();
		
		//Mensaje
		if(estado == false)
			lblMsg.setText("El cliente ha sido desbloqueado");
		else
			lblMsg.setText("El cliente ha sido bloqueado");
			
	}
	
	public void loadLists()
	{
		logicaNegocio = new FacadeImplementationWS();
		
		//No bloqueados
		String[] result = logicaNegocio.obtainAllUnbloquedClients();
		
		/*System.out.println("List no bloqueados");
		 for(int i= 0; i<result.length; i++)
			System.out.println(result[i]);
		*/

		for (Object v : result)
			listUnbloquedInfo.addElement(v);//Populate the list
		
		
		//Bloqueados
		String[] result2 = logicaNegocio.obtainAllBloquedClients();
		
		/*System.out.println("List bloqueados");
		for(int i= 0; i<result2.length; i++)
			System.out.println(result2[i]);
		*/
		
		for (Object v : result2)
			listBloquedInfo.addElement(v);
		
	}
}
