package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JRadioButton;
import javax.swing.JFormattedTextField;
import javax.swing.JComboBox;

import businessLogic.FacadeImplementationWS;
import javax.swing.JButton;
import javax.swing.JSlider;

import domain.Offer;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import com.toedter.calendar.JCalendar;

public class RealizarReserva extends JFrame {

	private FacadeImplementationWS logicaNegocio;
	
	private JPanel contentPane;
	
	private JLabel lblLocalizacin = new JLabel("Localización");
	private  JTextField textFieldLocalizacion = new JTextField();
	private JLabel lblNumHab = new JLabel("Número de habitaciones");
	private JSpinner spinnerNumHab = new JSpinner();
	private JLabel lblNumeroDePersonas = new JLabel("Número de personas");
	private JSpinner spinnerNumPersonas = new JSpinner();
	
	private JLabel lblMsg = new JLabel("");

	//General
	private JLabel labelGeneral = new JLabel("General:");
	private JRadioButton rdbtnWifi = new JRadioButton("WiFi");
	private JRadioButton rdbtnTelefono = new JRadioButton("Teléfono");
	private JRadioButton rdbtnCalefaccion = new JRadioButton("Calefacción");
	private JRadioButton rdbtnAire = new JRadioButton("Aire acondicionado");
	private JRadioButton rdbtnBarbacoa = new JRadioButton("Barbacoa");
	private JRadioButton rdbtnPiscina = new JRadioButton("Piscina");
	private JRadioButton rdbtnPerros = new JRadioButton("Se permiten perros");
	private JRadioButton rdbtnFumar = new JRadioButton("Se permite fumar");
	
	//Cocina
	private JLabel labelCocina = new JLabel("Cocina:");
	private JRadioButton rdbtnLavavajillas = new JRadioButton("Lavavajillas");
	private JRadioButton rdbtnLavadora = new JRadioButton("Lavadora");
	private JRadioButton rdbtnMicroondas = new JRadioButton("Microondas");
	private JRadioButton rdbtnHorno = new JRadioButton("Horno");
	
	//Sala
	JLabel labelSala = new JLabel("Sala:");
	JRadioButton rdbtnTV = new JRadioButton("TV");
	JRadioButton rdbtnOrdenador = new JRadioButton("Ordenador");
	JRadioButton rdbtnLineaMusical = new JRadioButton("Línea musical");
	
	private DefaultComboBoxModel offerNamesList = new DefaultComboBoxModel();
	private JComboBox comboBox = new JComboBox();
	
	JButton btnReservar = new JButton("Reservar");
	JButton btnBuscar = new JButton("Buscar");
	
	
	private final JLabel lblPrecioMaximo = new JLabel("Precio Máximo (€)");
	private final JLabel lblPrecioMInimo = new JLabel("Precio Mínimo (€)");

	private  JTextField textFieldPrecioMaximo = new JTextField();
	private  JTextField textFieldPrecioMinimo = new JTextField();
	
	private  static String user;
	
	private final JCalendar calendarFechaInicio = new JCalendar();
	private final JCalendar calendarFechaFin = new JCalendar();
	Date dateInicio  = null;
	Date dateFin=null;
	Date currentDateWithoutTime = null;
	
	private ArrayList<Offer> ofertas;
	private String strOfertas;
	
	//Menu
	JMenuBar menuBar;
	JMenu menuCliente, exit;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RealizarReserva frame = new RealizarReserva(user);
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
	public RealizarReserva(final String user) {
		textFieldPrecioMaximo.setBounds(393, 92, 55, 23);
		textFieldPrecioMaximo.setColumns(10);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 825, 678);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setTitle("Realizar Reserva");
		this.user=user;
		
		//Menu
		menuBar = new JMenuBar();
		
		menuCliente = new JMenu("Menu Principal");
		menuBar.add(menuCliente);
		menuCliente.addMenuListener(new MenuListener() {
			@Override
			public void menuSelected(MenuEvent e) {
				MenuClienteGUI m = new MenuClienteGUI(user);
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
		
		textFieldLocalizacion.setBounds(118, 63, 256, 23);
		contentPane.add(textFieldLocalizacion);
		textFieldLocalizacion.setColumns(10);
		
	
		lblLocalizacin.setBounds(12, 69, 99, 15);
		contentPane.add(lblLocalizacin);
		
		
		spinnerNumHab.setBounds(209, 98, 28, 20);
		contentPane.add(spinnerNumHab);
		
		
		lblNumHab.setBounds(12, 98, 179, 15);
		contentPane.add(lblNumHab);
		
		
		lblNumeroDePersonas.setBounds(12, 125, 179, 15);
		contentPane.add(lblNumeroDePersonas);
		
		
		spinnerNumPersonas.setBounds(209, 130, 28, 20);
		contentPane.add(spinnerNumPersonas);
		
		
		labelGeneral.setFont(new Font("serif", Font.BOLD, 16));
		labelGeneral.setBounds(12, 159, 118, 15);
		contentPane.add(labelGeneral);
		
		
		rdbtnWifi.setBounds(12, 182, 55, 23);
		contentPane.add(rdbtnWifi);
		
		
		rdbtnTelefono.setBounds(330, 206, 87, 23);
		contentPane.add(rdbtnTelefono);
		
	
		rdbtnCalefaccion.setBounds(188, 182, 110, 23);
		contentPane.add(rdbtnCalefaccion);
		
	
		rdbtnAire.setBounds(12, 206, 173, 23);
		contentPane.add(rdbtnAire);
		
		
		rdbtnBarbacoa.setBounds(188, 209, 92, 23);
		contentPane.add(rdbtnBarbacoa);
		
	
		rdbtnPiscina.setBounds(330, 182, 118, 23);
		contentPane.add(rdbtnPiscina);
		
		
		rdbtnPerros.setBounds(12, 233, 173, 23);
		contentPane.add(rdbtnPerros);
		
	
		rdbtnFumar.setBounds(188, 236, 149, 23);
		contentPane.add(rdbtnFumar);
		
	
		labelCocina.setFont(new Font("serif", Font.BOLD, 16));
		labelCocina.setBounds(22, 273, 70, 15);
		contentPane.add(labelCocina);
		
	
		rdbtnLavavajillas.setBounds(12, 296, 110, 23);
		contentPane.add(rdbtnLavavajillas);
		
		
		rdbtnLavadora.setBounds(162, 296, 92, 23);
		contentPane.add(rdbtnLavadora);
		
	
		rdbtnMicroondas.setBounds(12, 323, 123, 23);
		contentPane.add(rdbtnMicroondas);
		
		
		rdbtnHorno.setBounds(258, 296, 149, 23);
		contentPane.add(rdbtnHorno);
		
		
		labelSala.setFont(new Font("serif", Font.BOLD, 16));
		labelSala.setBounds(22, 369, 70, 19);
		contentPane.add(labelSala);
		
	
		rdbtnTV.setBounds(12, 396, 55, 23);
		contentPane.add(rdbtnTV);
		
	
		rdbtnOrdenador.setBounds(96, 396, 110, 23);
		contentPane.add(rdbtnOrdenador);
		
		
		rdbtnLineaMusical.setBounds(223, 391, 151, 32);
		contentPane.add(rdbtnLineaMusical);
		
		
		comboBox.setBounds(209, 474, 370, 96);
		contentPane.add(comboBox);
		
		
		lblPrecioMaximo.setBounds(255, 100, 138, 15);
		
		contentPane.add(lblPrecioMaximo);
		lblPrecioMInimo.setBounds(255, 132, 138, 15);
		
		contentPane.add(lblPrecioMInimo);
		
		contentPane.add(textFieldPrecioMaximo);
		textFieldPrecioMinimo.setColumns(10);
		textFieldPrecioMinimo.setBounds(393, 123, 55, 23);
		
		contentPane.add(textFieldPrecioMinimo);
		
		lblMsg.setBounds(258, 41, 340, 15);
		contentPane.add(lblMsg);
		
		
		
		
		
		//setBounds(int x,int y,int width,int height)
		calendarFechaInicio.setBounds(500, 100, 260, 146);
		contentPane.add(calendarFechaInicio);
		
		calendarFechaFin.setBounds(500, 300, 260, 146);
		contentPane.add(calendarFechaFin);
		
		
		btnBuscar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				lblMsg.setText("");//Limpiar mensajes de error
				offerNamesList.removeAllElements();//limpia la lista
				
				//Obtener valores introducidos por el usuario
				String localizacion = textFieldLocalizacion.getText();
				int numeroHabitaciones = (Integer) spinnerNumHab.getValue();
				int numeroPersonas = (Integer) spinnerNumPersonas.getValue();
				String PrecioMax = textFieldPrecioMaximo.getText();
				String PrecioMin = textFieldPrecioMinimo.getText();
				
				
				//Arrays initialized to false by default
				boolean[] general = new boolean[8];//wifi, telefono, calefaccion, aire acondicionado, barbacoa, piscina, perros, fumar
				boolean[] cocina = new boolean[4];//lavavajillas, lavadora, microndas, horno
				boolean[] sala = new boolean[3]; //tv, ordenador, musica
				
				for(int i=0; i<general.length-1;i++)
					general[i] = false;
				
				for(int i=0; i<cocina.length-1;i++)
					cocina[i] = false;
				
				for(int i=0; i<sala.length-1;i++)
					sala[i] = false;
				
				if(rdbtnWifi.isSelected()) general[0]=true;
				if(rdbtnTelefono.isSelected()) general[1]=true;
				if(rdbtnCalefaccion.isSelected()) general[2]=true;
				if(rdbtnAire.isSelected()) general[3]=true;
				if(rdbtnBarbacoa.isSelected()) general[4]=true;
				if(rdbtnPiscina.isSelected()) general[5]=true;
				if(rdbtnPerros.isSelected()) general[6]=true;
				if(rdbtnFumar.isSelected()) general[7]=true;
				
				if(rdbtnLavavajillas.isSelected()) cocina[0]=true;
				if(rdbtnLavadora.isSelected()) cocina[1]=true;
				if(rdbtnMicroondas.isSelected()) cocina[2]=true;
				if(rdbtnHorno.isSelected()) cocina[3]=true;
				
				if(rdbtnTV.isSelected()) sala[0]=true;
				if(rdbtnOrdenador.isSelected()) sala[1]=true;
				if(rdbtnLineaMusical.isSelected()) sala[2]=true;
				
				
				//Calendarios
				//Fecha de inicio
				
				DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
				
				int diaIni=calendarFechaInicio.getDayChooser().getDay();
				int mesIni=calendarFechaInicio.getMonthChooser().getMonth()+1;
				int añoIni=calendarFechaInicio.getYearChooser().getYear();
				
				String inputStrInicio = diaIni+"/"+mesIni+"/"+añoIni;
				
				try {
					dateInicio = dateFormat.parse(inputStrInicio);
					System.out.println("di "+dateInicio);
				} catch (ParseException e1) {
				
					e1.printStackTrace();
				}
				
				//Fecha de fin
				int diaFin=calendarFechaFin.getDayChooser().getDay();
				int mesFin=calendarFechaFin.getMonthChooser().getMonth()+1;
				int añoFin=calendarFechaFin.getYearChooser().getYear();
				
				String inputStrFin = diaFin+"/"+mesFin+"/"+añoFin;
				
				try {
					dateFin = dateFormat.parse(inputStrFin);
					System.out.println("df "+dateFin);
				} catch (ParseException e1) {
					
					e1.printStackTrace();
				}
				
				//Fecha actual
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");      
				
				try {
					currentDateWithoutTime = sdf.parse(sdf.format(new Date()));
					//System.out.println("c"+currentDateWithoutTime);
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
				
				if(validarDatos() == true)
				{			
					logicaNegocio=new FacadeImplementationWS();
					ofertas= logicaNegocio.buscarOfertasFiltros(localizacion, Integer.parseInt(PrecioMax), Integer.parseInt(PrecioMin), numeroHabitaciones, numeroPersonas, dateInicio, dateFin, general, cocina, sala);
					
					//Populate the combobox
					 if(!ofertas.isEmpty())
					 {
							for (int i = 0; i < ofertas.size(); i++) 
							{
								strOfertas = "Nombre casa: "+ofertas.get(i).getRuralHouse().getNombreRH() +"  Precio"+ofertas.get(i).getPrice();
								//System.out.println(ofertas.get(i));
								//offerNamesList.addElement(i); 
								offerNamesList.addElement(strOfertas); 
							}
							comboBox.setModel(offerNamesList);
					 }else
						 lblMsg.setText("No se han encontrado ofertas con esos datos");
				}
			}
		});
		btnBuscar.setBounds(303, 431, 173, 25);
		contentPane.add(btnBuscar);
		
		
		
		btnReservar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
		
				 //Pasar el usuario al reservar la oferta
				if (comboBox.getSelectedIndex() != -1)
				{
					String selectedOffer = (String) offerNamesList.getSelectedItem();
					//Offer selectedOffer = (Offer) offerNamesList.getSelectedItem();
					System.out.println(selectedOffer);
					int idOfertaSeleccionada = -1;
					
					for(int i=0; i<offerNamesList.getSize();i++)
					{
						/*Obtenemos el objeto Offer que hemos seleccionado en forma de String
						 * en el combobox, para guardar esa oferta como adquirida
						 * 
						 */
						
						System.out.println(offerNamesList.getElementAt(i));
						if(selectedOffer.equals(offerNamesList.getElementAt(i)))
						{
							System.out.println("num "+i);
							System.out.println(offerNamesList.getElementAt(i));
							idOfertaSeleccionada = ofertas.get(i).getOfferNumber();
							System.out.println(idOfertaSeleccionada);
						}
					}
					
					boolean b = logicaNegocio.realizarReservaRH(idOfertaSeleccionada, user);
					if(b== true)
						lblMsg.setText("Reserva realizada con exito");
					else
						lblMsg.setText("No se pudo realizar la reserva");
				}else
					lblMsg.setText("Selecciona una oferta");
			}	
		});
		btnReservar.setBounds(303, 582, 173, 25);
		contentPane.add(btnReservar);
		
		JLabel lblFechaInicio = new JLabel("Fecha Inicio");
		lblFechaInicio.setBounds(582, 71, 118, 15);
		contentPane.add(lblFechaInicio);
		
		JLabel lblNewLabel = new JLabel("Fecha Fin");
		lblNewLabel.setBounds(570, 258, 70, 15);
		contentPane.add(lblNewLabel);
		
	}
	
	
	//Validaciones
	public boolean validarDatos()
	{
		String loc = textFieldLocalizacion.getText();
		String precioMax = textFieldPrecioMaximo.getText();
		String precioMin = textFieldPrecioMinimo.getText();
		int numHab = (Integer) spinnerNumHab.getValue();
		int numPer = (Integer) spinnerNumPersonas.getValue();
		
		boolean b = true;
		boolean e = false;
		
		//localizacion
		if(loc.trim().isEmpty())
		{
			lblMsg.setText("Introduce una localizacion");
			b=false;
		}else if(!isNombreLocalizacionValid(loc))
		{
			lblMsg.setText("Introduce un nombre de localizacion válido");
			b=false;
		}
		
		//Precio Max
		if(precioMax.trim().isEmpty())
		{
			lblMsg.setText("Introduce una precio máximo");
			b=false;
		}else if(!isPrecioNumero(precioMax)){
			lblMsg.setText("Introduce un precio máximo válido");
			b=false;
		}else
			e=true;
		
		
		//Precio Min
		if(precioMin.trim().isEmpty())
		{
			lblMsg.setText("Introduce una precio mínimo");
			b=false;
		}else
		{	
			if(!isPrecioNumero(precioMin)){
				lblMsg.setText("Introduce un precio mínimo válido");
				b=false;
			}else if(e)//Si es un número el precio
			{
				int precioMax1 = Integer.parseInt(precioMax);
			    int precioMin1 =  Integer.parseInt(precioMin);
			    
				if(precioMin1 > precioMax1)
				{
					lblMsg.setText("Introduce un precio mín. menor o igual que el máx.");
					b=false;
				}
			}
		}
		
		//Fecha elegida de inicio es antes o igual que fecha actual
		if(dateInicio.before(currentDateWithoutTime)|| dateInicio.equals(currentDateWithoutTime)) 
		{
			lblMsg.setText("La fecha de inicio debe ser posterior a la fecha actual");
			b = false;
		}
		
		//Fecha fin antes que fecha inicio
		if(dateFin.before(dateInicio)) 
		{
			lblMsg.setText("La fecha de fin debe ser posterior a la de inicio");
			b= false;
		}
		
		//Num hab
		if(!(numHab<5 && numHab>0))
		{
			lblMsg.setText("Introduzca un máximo de 4 habitaciones");
			b=false;
		}

	    //Num per
		if(!(numPer<15 && numPer>0))
		{
			lblMsg.setText("Introduzca un máximo de 14 personas");
			b=false;
		}
		
		
		
		return b;
	}
	
	
	public boolean isPrecioNumero(String num)
	{
		String NUMERO_PATTERN = "^[0-9]+(.[0-9]{1,2})?$";
		
		if(!num.isEmpty())
		{
			if(num.matches(NUMERO_PATTERN))
				return true;
			else
				return false;
		}else
			return false;
	}
	
	public boolean isNombreLocalizacionValid(String nom) 
	{
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
}
