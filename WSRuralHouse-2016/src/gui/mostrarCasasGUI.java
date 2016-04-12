package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JTextPane;

import businessLogic.FacadeImplementationWS;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Vector;

import javax.swing.JComboBox;

import domain.Offer;
import domain.RuralHouse;

import javax.swing.JLabel;
import javax.swing.JTextField;

public class mostrarCasasGUI extends JFrame {
	//ITERACION 2
	private FacadeImplementationWS logicaNegocio;

	private JPanel contentPane;
		
	private final JButton mostrarPorCiudad = new JButton("mostrarPorCiudad");
	private final JButton mostrarPorNumHabitaciones = new JButton("mostrarPorNumHabitaciones");
	private final JButton mostrarPorNumPersonas = new JButton("mostrarPorNumPersonas");
	private final JButton redireccionaAlMain = new JButton("redireccionaAlMain");
	private final JComboBox todasLasOfertas = new JComboBox();
	private final JButton mostrarOfertasDeLaCasaSeleccionada = new JButton("mostrarOfertasDeLaCasaSeleccionada");
	private final JComboBox ofertasDeLaCasa = new JComboBox();
	private final JButton reservarOferta = new JButton("reservarOferta");
	private final JLabel ofertaSeleccionadaPor = new JLabel("Mostrando oferta: Primer dia,ultimo dia, precio");
	private final JLabel mensajeSinOfertas = new JLabel("");
	private JTextField ciudadIntroducida;
	private final JLabel errorNombreCiudad = new JLabel("");

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					mostrarCasasGUI frame = new mostrarCasasGUI();
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
	public mostrarCasasGUI() {
		setTitle("Mostrando todas las casas");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 523, 524);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
		mostrarPorCiudad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				todasLasOfertas.removeAllItems();
				logicaNegocio=new FacadeImplementationWS();
				Vector<RuralHouse> casas=new Vector<RuralHouse>();
				casas=logicaNegocio.dameCasa(ciudadIntroducida.getText());
				if(casas.size()==0){
					errorNombreCiudad.setText("No existen casas para esa ciudad");						//llamamos a logica con 0 par q nos muestre por localizacion
				}else{
					errorNombreCiudad.setText("");
				for(int i=0;i<casas.size();i++){
					String s=casas.get(i).getCity()+" "+casas.get(i).getNumeroHabitaciones()+" "+casas.get(i).getNumeroPersonas()+" "+casas.get(i).getHouseNumber();
					todasLasOfertas.addItem(s);
				}
				
				}
				
				
			}
		});
		mostrarPorCiudad.setBounds(10, 11, 206, 23);
		contentPane.add(mostrarPorCiudad);
		
		
		mostrarPorNumHabitaciones.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				todasLasOfertas.removeAllItems();
				logicaNegocio=new FacadeImplementationWS();
				Vector<RuralHouse> casas=new Vector<RuralHouse>();
				casas=logicaNegocio.mostrandoLogica(1); //llamamos a logica 1 para q nos muestre por numHabitaciones
				for(int i=0;i<casas.size();i++){
					String s=casas.get(i).getCity()+" "+casas.get(i).getNumeroHabitaciones()+" "+casas.get(i).getNumeroPersonas()+" "+casas.get(i).getHouseNumber();
					todasLasOfertas.addItem(s);
				}
			}
		});
		mostrarPorNumHabitaciones.setBounds(10, 45, 414, 23);
		contentPane.add(mostrarPorNumHabitaciones);
		
		
		mostrarPorNumPersonas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				todasLasOfertas.removeAllItems();
				logicaNegocio=new FacadeImplementationWS();
				Vector<RuralHouse> casas=new Vector<RuralHouse>();
				casas=logicaNegocio.mostrandoLogica(2); //llamamos a logica 2 para q nos muestre por numPersonas
				for(int i=0;i<casas.size();i++){
					String s=casas.get(i).getCity()+" "+casas.get(i).getNumeroHabitaciones()+" "+casas.get(i).getNumeroPersonas()+" "+casas.get(i).getHouseNumber();
					todasLasOfertas.addItem(s);
				}
			}
		});
		mostrarPorNumPersonas.setBounds(10, 79, 414, 23);
		contentPane.add(mostrarPorNumPersonas);
		
		
		redireccionaAlMain.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				MainGUI a=new MainGUI();
				a.setVisible(true);
				setVisible(false);
			}
		});
		redireccionaAlMain.setBounds(10, 113, 414, 23);
		contentPane.add(redireccionaAlMain);
		
		
		todasLasOfertas.setBounds(10, 172, 414, 78);
		contentPane.add(todasLasOfertas);
		
		JLabel MuestraCasas = new JLabel("Muestra casas: Localizacion,NumHabitaciones,NumPersonas,idCasa");
		MuestraCasas.setBounds(20, 147, 404, 14);
		contentPane.add(MuestraCasas);  //TODAVIA NO SALE NADA, PORQUE NO HEMOS IMPLEMENTADO EL CASO DE USO CREAR OFERTA
		mostrarOfertasDeLaCasaSeleccionada.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				RuralHouse seleccionada=(RuralHouse) todasLasOfertas.getSelectedItem(); //cogemos la casa seleccionada y llamamos a la logica d negocio
				logicaNegocio=new FacadeImplementationWS();
				Vector<Offer> nuevo=new Vector<Offer>(); //desde la logica nos pasan todas las ofertas para esa casa n un vector
				nuevo=logicaNegocio.dameLasOfertas(seleccionada);
				if(nuevo.size()==0){
					mensajeSinOfertas.setText("No hay ofertas disponibles para esta casa");
				}else{
					mensajeSinOfertas.setText("");
				for(int i=0;i<nuevo.size();i++){ //recorro el vector y añado al combobox todas las ofertas
					String x=nuevo.get(i).getFirstDay()+" "+nuevo.get(i).getLastDay()+" "+nuevo.get(i).getPrice();
					ofertasDeLaCasa.addItem(x);				
				}
				}
							
				
				
			}
		});
		mostrarOfertasDeLaCasaSeleccionada.setBounds(10, 257, 414, 23);
		
		contentPane.add(mostrarOfertasDeLaCasaSeleccionada);
		ofertasDeLaCasa.setBounds(10, 320, 414, 107);
		
		contentPane.add(ofertasDeLaCasa);
		reservarOferta.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//reservamos la oferta bookOffer(ofertasDeLaCasa.selectedItem())
			}
		});
		reservarOferta.setBounds(10, 438, 414, 36);
		
		contentPane.add(reservarOferta);
		ofertaSeleccionadaPor.setBounds(10, 295, 414, 14);
		
		contentPane.add(ofertaSeleccionadaPor);
		
		
		mensajeSinOfertas.setBounds(434, 320, 63, 107);
		contentPane.add(mensajeSinOfertas);
		
		JLabel IntroduceCiudad = new JLabel("Introduce ciudad:");
		IntroduceCiudad.setBounds(225, 11, 123, 23);
		contentPane.add(IntroduceCiudad);
		
		ciudadIntroducida = new JTextField();
		ciudadIntroducida.setBounds(338, 12, 86, 20);
		contentPane.add(ciudadIntroducida);
		ciudadIntroducida.setColumns(10);
		errorNombreCiudad.setBounds(435, 11, 72, 23);
		
		contentPane.add(errorNombreCiudad);
	}
}