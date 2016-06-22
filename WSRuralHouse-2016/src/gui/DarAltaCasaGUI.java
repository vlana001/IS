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
import javax.swing.Box;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JEditorPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JSpinner;
import javax.swing.JList;
import javax.swing.JRadioButton;

import businessLogic.FacadeImplementationWS;

public class DarAltaCasaGUI extends JFrame {
private static final long serialVersionUID = 1L;
	
	private FacadeImplementationWS logicaNegocio;

	private JPanel contentPane;
	
	private final JLabel lblLocalizacion =  new JLabel("Localización");
	private final JTextField textLocalizacion;
	
	private final JLabel lblNombreCasa = new JLabel("Nombre casa");
	private final JTextField textNombreCasa;
	
	private final JLabel lblDescripcion = new JLabel("Descripción");
	private final JTextArea textDescripcion = new JTextArea("");
	
	
	private final JLabel lblNumeroDeHabitaciones = new JLabel("Número de habitaciones");
	private final JSpinner spinnerNumeroHuespedes = new JSpinner();
	private final JLabel lblNumeroDeHuespedes = new JLabel("Número de huespedes");
	private final JSpinner spinnerNumeroHabitaciones = new JSpinner();
	
	private final JLabel lblMsg = new JLabel("");
	
	//General
	private final JLabel lblGeneral = new JLabel("General:");
	
	private final JRadioButton rdbtnWifi = new JRadioButton("WiFi");
	private final JRadioButton rdbtnTelefono = new JRadioButton("Teléfono");
	private final JRadioButton rdbtnCalefaccion = new JRadioButton("Calefacción");
	private final JRadioButton rdbtnAireAcondicionado = new JRadioButton("Aire acondicionado");
	private final JRadioButton rdbtnBarbacoa = new JRadioButton("Barbacoa");
	private final JRadioButton rdbtnPisicina = new JRadioButton("Piscina");
	private final JRadioButton rdbtnSePermitenPerros = new JRadioButton("Se permiten perros");
	private final JRadioButton rdbtnSePermiteFumar = new JRadioButton("Se permite fumar");
	
	//Cocina
	private final JLabel lblCocina = new JLabel("Cocina:");
	
	private final JRadioButton rdbtnLavavajillas = new JRadioButton("Lavavajillas");
	private final JRadioButton rdbtnLavadora = new JRadioButton("Lavadora");
	private final JRadioButton rdbtnMicroondas = new JRadioButton("Microondas");
	private final JRadioButton rdbtnHorno = new JRadioButton("Horno");
	
	//Sala
	private final JLabel lblSala = new JLabel("Sala:");
	
	private final JRadioButton rdbtnTv = new JRadioButton("TV");
	private final JRadioButton rdbtnOrdenador = new JRadioButton("Ordenador");
	private final JRadioButton rdbtnLineaMusical = new JRadioButton("Línea musical");
	
	private final JButton btnDarDeAlta = new JButton("Dar de alta");
	
	private  static String user;
	private  static String pass;
	
	private final JButton btnMostrarTodasMis = new JButton("Mostrar todas mis casas");
	private final JButton btnAtras = new JButton("Atras");
	
	//Menu
	JMenuBar menuBar;
	JMenu menuPropietario, exit;
		
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DarAltaCasaGUI frame = new DarAltaCasaGUI(user);
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
	public DarAltaCasaGUI(final String user) {
		this.user=user;
		this.pass=pass;
		setTitle("Dar de Alta Propiedad");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 597, 560);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		//Menu
		menuBar = new JMenuBar();
		
		menuPropietario = new JMenu("Menu Principal");
		menuBar.add(menuPropietario);
		menuPropietario.addMenuListener(new MenuListener() {
			@Override
			public void menuSelected(MenuEvent e) {
				MenuPropietario m = new MenuPropietario(user);
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
				
		lblLocalizacion.setBounds(12, 57, 110, 15);
		contentPane.add(lblLocalizacion);
		
		lblDescripcion.setBounds(12, 350, 110, 19);
		contentPane.add(lblDescripcion);
		
		lblNombreCasa.setBounds(297, 57, 104, 15);
		contentPane.add(lblNombreCasa);
		
		textNombreCasa = new JTextField();
		textNombreCasa.setBounds(401, 55, 141, 25);
		contentPane.add(textNombreCasa);
		textNombreCasa.setColumns(10);
		
		textLocalizacion = new JTextField();
		textLocalizacion.setBounds(133, 53, 151, 21);
		contentPane.add(textLocalizacion);
		textLocalizacion.setColumns(10);
		
		//JTextArea textDescripcion = new JTextArea();
		textDescripcion.setBounds(133, 352, 338, 83);
		contentPane.add(textDescripcion);
		
		btnDarDeAlta.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				resetErrorMessagesCASA();//Limpiar mensajes de error
				
				boolean bien = true;
						
				int num[] = validarAltaCasa();	//Validar datos introducidos
				
				for(int i=0;i<3;i++){ 
					if(num[i]==0)
						bien=false;
				}
				if(!bien){
					printErrorMessagesCASA(num); //Imprimir mensajes de error
				}else{
					//Guardar en BD*/
					String localizacion = textLocalizacion.getText();
					String nombreCasa = textNombreCasa.getText();
					String descripcion = textDescripcion.getText();
					
					int numeroHabitaciones = (Integer) spinnerNumeroHabitaciones.getValue();
					int numeroPersonas = (Integer) spinnerNumeroHuespedes.getValue();
					
					//Arrays initialized to false by default
					boolean[] general = new boolean[8];//wifi, telefono, calefaccion, aire acondicionado, barbacoa, piscina, perros, fumar
					boolean[] cocina = new boolean[4];//lavavajillas, lavadora, microndas, horno
					boolean[] sala = new boolean[3]; //tv, ordenador, musica
					
					if(rdbtnWifi.isSelected()) general[0]=true;
					if(rdbtnTelefono.isSelected()) general[1]=true;
					if(rdbtnCalefaccion.isSelected()) general[2]=true;
					if(rdbtnAireAcondicionado.isSelected()) general[3]=true;
					if(rdbtnBarbacoa.isSelected()) general[4]=true;
					if(rdbtnPisicina.isSelected()) general[5]=true;
					if(rdbtnSePermitenPerros.isSelected()) general[6]=true;
					if(rdbtnSePermiteFumar.isSelected()) general[7]=true;
					
					if(rdbtnLavavajillas.isSelected()) cocina[0]=true;
					if(rdbtnLavadora.isSelected()) cocina[1]=true;
					if(rdbtnMicroondas.isSelected()) cocina[2]=true;
					if(rdbtnHorno.isSelected()) cocina[3]=true;
					
					if(rdbtnTv.isSelected()) sala[0]=true;
					if(rdbtnOrdenador.isSelected()) sala[1]=true;
					if(rdbtnLineaMusical.isSelected()) sala[2]=true;
					
					
					logicaNegocio = new FacadeImplementationWS();	
					boolean b = logicaNegocio.saveRuralHouseData(localizacion, nombreCasa, descripcion, numeroHabitaciones, numeroPersonas, general, cocina, sala,user,pass);
					
					if(b)
						lblMsg.setText("Casa dada de alta correctamente");
						
					/*
					//Imprimir en login registro correcto
					LoginGUI b = new LoginGUI(); // cuando nos registramos vamos al login de nuevo
					b.setVisible(true);
					setVisible(false);
					
				
				}*/
			}
		}});
		btnDarDeAlta.setBounds(200, 476, 141, 25);
		contentPane.add(btnDarDeAlta);
		
		lblNumeroDeHabitaciones.setBounds(12, 86, 181, 15);
		contentPane.add(lblNumeroDeHabitaciones);
		
		
		spinnerNumeroHabitaciones.setBounds(221, 92, 42, 20);
		contentPane.add(spinnerNumeroHabitaciones);
		
		
		lblNumeroDeHuespedes.setBounds(12, 121, 173, 15);
		contentPane.add(lblNumeroDeHuespedes);
		
		spinnerNumeroHuespedes.setBounds(221, 119, 38, 20);
		contentPane.add(spinnerNumeroHuespedes);
		
		lblGeneral.setBounds(12, 167, 70, 15);
		contentPane.add(lblGeneral);
		
		
		rdbtnWifi.setBounds(96, 163, 55, 23);
		contentPane.add(rdbtnWifi);
		
		
		rdbtnTelefono.setBounds(161, 163, 87, 23);
		contentPane.add(rdbtnTelefono);
		
		lblCocina.setBounds(12, 261, 70, 15);
		contentPane.add(lblCocina);
		
		rdbtnLavavajillas.setBounds(106, 253, 110, 23);
		contentPane.add(rdbtnLavavajillas);
		
		rdbtnCalefaccion.setBounds(283, 163, 110, 23);
		contentPane.add(rdbtnCalefaccion);
		
		rdbtnAireAcondicionado.setBounds(96, 187, 173, 23);
		contentPane.add(rdbtnAireAcondicionado);
		
		rdbtnBarbacoa.setBounds(283, 187, 110, 23);
		contentPane.add(rdbtnBarbacoa);
		
		rdbtnPisicina.setBounds(411, 163, 149, 23);
		contentPane.add(rdbtnPisicina);
		
		rdbtnLavadora.setBounds(252, 257, 92, 23);
		contentPane.add(rdbtnLavadora);
		
		rdbtnMicroondas.setBounds(116, 280, 123, 23);
		contentPane.add(rdbtnMicroondas);
		
		rdbtnHorno.setBounds(252, 284, 149, 23);
		contentPane.add(rdbtnHorno);
		
		lblSala.setBounds(12, 313, 70, 19);
		contentPane.add(lblSala);
		
		rdbtnTv.setBounds(109, 311, 55, 23);
		contentPane.add(rdbtnTv);
		
		rdbtnOrdenador.setBounds(200, 311, 110, 23);
		contentPane.add(rdbtnOrdenador);
		
		rdbtnLineaMusical.setBounds(320, 306, 151, 32);
		contentPane.add(rdbtnLineaMusical);
		
		rdbtnSePermitenPerros.setBounds(96, 214, 188, 23);
		contentPane.add(rdbtnSePermitenPerros);
		
		rdbtnSePermiteFumar.setBounds(286, 214, 149, 23);
		contentPane.add(rdbtnSePermiteFumar);
		
		/*
		btnMostrarTodasMis.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				MostrarCasasPropietarioGUI a=new MostrarCasasPropietarioGUI(user,pass);
				a.setVisible(true);
				setVisible(false);
			}
		});
		btnMostrarTodasMis.setBounds(392, 475, 179, 35);
		contentPane.add(btnMostrarTodasMis);
		*/
	
		
		lblMsg.setBounds(117, 26, 380, 19);
		contentPane.add(lblMsg);
	}
	
	
	public int[] validarAltaCasa(){ //validamos los campos de la casa
		int num[]=new int[5];
		
		for(int i=0;i<5;i++)
			num[i]=0;
		
		if(isNumeroHabitacionesValid((Integer) spinnerNumeroHabitaciones.getValue())){
			num[0]=1;
		}
		if(isNumeroPersonasValid((Integer) spinnerNumeroHuespedes.getValue())){
			num[1]=1;
		}
		if(isLocalizacionValid(textLocalizacion.getText().trim())){
			num[2]=1;
		}
		if(isDescripcionValid(textDescripcion.getText().trim())){
			num[3]=1;
		}
		if(isNombreCasaValid(textNombreCasa.getText().trim())){
			num[4]=1;
		}
		return num;
	}
	
	
	//VALIDACIONES
	public boolean isNumeroHabitacionesValid(int x){
		if(x<5 && x>0){
			return true;
		}else{
			return false;
		}
	}
	public boolean isNumeroPersonasValid(int x){
		if(x<15 && x>0){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean isLocalizacionValid(String nom){
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
	
	public boolean isNombreCasaValid(String nomCasa){
		//El nombre solo puede tener caracteres alfabeticos
		//String NOMBRE_PATTERN = "^[A-Za-z]+( [A-Za-z]+)*$";
		String NOMBRECASA_PATTERN = "^[A-Za-z0-9-_]+( [A-Za-z0-9-_]+)*$";
		
		if(!nomCasa.isEmpty())
		{
			if(nomCasa.matches(NOMBRECASA_PATTERN))
				return true;
			else
				return false;
		}else
			return false;
	}
	
	public boolean isDescripcionValid(String x){
		if(x.length()>200){
			return false;
		}else{
			return true;
		}
	}
	
	public void resetErrorMessagesCASA(){
		
		lblMsg.setText("");
		
		textLocalizacion.setBorder(null);
		textLocalizacion.updateUI();
		
		textNombreCasa.setBorder(null);
		textNombreCasa.updateUI();
		
		textDescripcion.setBorder(null);
		textDescripcion.updateUI();
		
		spinnerNumeroHabitaciones.setBorder(null);
		spinnerNumeroHabitaciones.updateUI();
		
		spinnerNumeroHuespedes.setBorder(null);
		spinnerNumeroHuespedes.updateUI();
	}
	
	
	public void printErrorMessagesCASA(int []num){
		Border border = BorderFactory.createLineBorder(Color.RED, 3);
		
		if(num[0]==0){
			lblMsg.setText("Introduzca un maximo de 4 habitaciones");
			spinnerNumeroHabitaciones.setBorder(border);
		}
		if(num[1]==0){
			lblMsg.setText("Introduzca un maximo de 14 personas");
			spinnerNumeroHuespedes.setBorder(border);
		}
		if(num[2]==0){
			lblMsg.setText("Introduce una localizacion válida");
			textLocalizacion.setBorder(border);
		}
		if(num[3]==0){
			lblMsg.setText("Introduce descripcion valida con 200 carácteres o menos");
			textDescripcion.setBorder(border);
		}
		if(num[4]==0){
			lblMsg.setText("Introduce un nombre válido");
			textNombreCasa.setBorder(border);
		}
	}
	
}