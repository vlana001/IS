package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.BorderFactory;
import javax.swing.Box;
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
	
	private final JLabel lblDescripcion = new JLabel("Descripción");
	private final JTextArea textDescripcion = new JTextArea("");
	
	
	private final JLabel lblNumeroDeHabitaciones = new JLabel("Número de habitaciones");
	private final JSpinner spinnerNumeroHuespedes = new JSpinner();
	private final JLabel lblNumeroDeHuespedes = new JLabel("Número de huespedes");
	private final JSpinner spinnerNumeroHabitaciones = new JSpinner();
	
	
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
	
	private final JLabel errorLocalizacion = new JLabel("");
	private final JLabel errorHabitaciones = new JLabel("");
	private final JLabel errorPersonas = new JLabel("");
	private final JLabel errorDescripcion = new JLabel("");
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DarAltaCasaGUI frame = new DarAltaCasaGUI();
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
	public DarAltaCasaGUI() {
		setTitle("Dar de Alta Propiedad");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 597, 560);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		lblLocalizacion.setBounds(12, 57, 110, 15);
		contentPane.add(lblLocalizacion);
		
		lblDescripcion.setBounds(12, 350, 110, 19);
		contentPane.add(lblDescripcion);
		
		textLocalizacion = new JTextField();
		textLocalizacion.setBounds(133, 55, 151, 19);
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
					logicaNegocio.saveRuralHouseData(localizacion, descripcion, numeroHabitaciones, numeroPersonas, general, cocina, sala);
					
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
		
		rdbtnCalefaccion.setBounds(252, 163, 110, 23);
		contentPane.add(rdbtnCalefaccion);
		
		rdbtnAireAcondicionado.setBounds(96, 187, 173, 23);
		contentPane.add(rdbtnAireAcondicionado);
		
		rdbtnBarbacoa.setBounds(276, 190, 92, 23);
		contentPane.add(rdbtnBarbacoa);
		
		rdbtnPisicina.setBounds(372, 187, 149, 23);
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
		
		rdbtnSePermitenPerros.setBounds(103, 214, 181, 23);
		contentPane.add(rdbtnSePermitenPerros);
		
		rdbtnSePermiteFumar.setBounds(286, 214, 149, 23);
		contentPane.add(rdbtnSePermiteFumar);
		
		
		errorLocalizacion.setBounds(302, 57, 281, 15);
		contentPane.add(errorLocalizacion);
		
		
		errorHabitaciones.setBounds(278, 92, 305, 19);
		contentPane.add(errorHabitaciones);
		
		
		errorPersonas.setBounds(277, 121, 306, 25);
		contentPane.add(errorPersonas);
		
		
		errorDescripcion.setBounds(30, 445, 553, 19);
		contentPane.add(errorDescripcion);
	}
	public int[] validarAltaCasa(){ //validamos los campos de la casa
		int num[]=new int[4];
		
		for(int i=0;i<4;i++)
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
	public boolean isDescripcionValid(String x){
		if(x.length()>200){
			return false;
		}else{
			return true;
		}
	}
	public void resetErrorMessagesCASA(){
		errorLocalizacion.setText("");
		errorHabitaciones.setText("");
		errorPersonas.setText("");
		errorDescripcion.setText("");
		
		textLocalizacion.setBorder(null);
		textLocalizacion.updateUI();
		
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
			errorHabitaciones.setText("Introduzca un maximo de 4 habitaciones");
			spinnerNumeroHabitaciones.setBorder(border);
		}
		if(num[1]==0){
			errorPersonas.setText("Introduzca un maximo de 14 personas");
			spinnerNumeroHuespedes.setBorder(border);
		}
		if(num[2]==0){
			errorLocalizacion.setText("Introduce una localizacion valida");
			textLocalizacion.setBorder(border);
		}
		if(num[3]==0){
			errorDescripcion.setText("Introduce descripcion valida con 200 caracteres o menos");
			textDescripcion.setBorder(border);
		}
	}
}
