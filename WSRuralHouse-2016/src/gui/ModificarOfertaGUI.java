package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Rectangle;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import domain.Offer;
import domain.RuralHouse;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JTextField;
import javax.swing.JButton;

import com.toedter.calendar.JCalendar;

import businessLogic.FacadeImplementationWS;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.swing.JComboBox;

public class ModificarOfertaGUI extends JFrame {
	private FacadeImplementationWS logicaNegocio;
	
	private JPanel contentPane;
	private static int idOfertaSel;
	private static int idCasaSel;
	private JTextField introducePrecio=new JTextField();
	private JLabel lblPrecio = new JLabel("Precio(€)");
	private JLabel lblEstadoOferta = new JLabel("Estado oferta");
	private JLabel lblFechaInicio = new JLabel("Fecha inicio estancia");
	private JLabel lblFechaFin = new JLabel("Fecha fin estancia");
	private JButton btnCrearOferta = new JButton("Modificar oferta de la casa");
	
	private JLabel lblMsgError = new JLabel("");
	
	private JComboBox cEstados = new JComboBox();
	
	private final JCalendar calendarFechaInicio = new JCalendar();
	private final JCalendar calendarFechaFin = new JCalendar();

	//Menu
	JMenuBar menuBar;
	JMenu menuPropietario, misCasas, ofertasCasa, exit;

	private static String user;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ModificarOfertaGUI frame = new ModificarOfertaGUI(idOfertaSel, idCasaSel, user);
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
	public ModificarOfertaGUI(final int idOferta, final int idCasa, final String user) {
		setTitle("Modificar oferta para la casa seleccionada");
		this.idOfertaSel=idOferta;
		this.idCasaSel=idCasa;
		this.user=user;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 682, 455);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		lblPrecio.setBounds(29, 91, 82, 50);
		contentPane.add(lblPrecio);
		
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
		
		misCasas = new JMenu("Mis Casas");
		menuBar.add(misCasas);
		misCasas.addMenuListener(new MenuListener() {
			@Override
			public void menuSelected(MenuEvent e) {
				MostrarCasasPropietarioGUI m = new MostrarCasasPropietarioGUI(user);
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
		
		ofertasCasa = new JMenu("Ofertas Casa");
		menuBar.add(ofertasCasa);
		ofertasCasa.addMenuListener(new MenuListener() {
			@Override
			public void menuSelected(MenuEvent e) {
				MostrarOfertasPropietarioGUI m = new MostrarOfertasPropietarioGUI(Integer.toString(idCasaSel), user);
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

		introducePrecio.setBounds(123, 100, 142, 33);
		contentPane.add(introducePrecio);
		introducePrecio.setColumns(10);
		
		lblFechaInicio.setBounds(94, 135, 185, 33);
		contentPane.add(lblFechaInicio);
		
		lblFechaFin.setBounds(417, 135, 145, 33);
		contentPane.add(lblFechaFin);	
		
		
		//Iniciar el precio y fechas con los de esa oferta
		
		logicaNegocio=new FacadeImplementationWS();
		Offer of = logicaNegocio.getOfferByOfferId(idOferta);
		
		final float precio = of.getPrice();
		String price = Float.toString(precio);
		introducePrecio.setText(price);
		
		//Estados
		final int estadoOf = of.getEstadoOferta();
		
		cEstados.removeAllItems();
		if(estadoOf == 0)//Disponible
		{
			cEstados.addItem("Disponible");
			cEstados.addItem("Eliminada propiet.");
		}
		
		if(estadoOf == 2)//Eliminada
		{
			cEstados.addItem("Eliminada propiet.");
			cEstados.addItem("Disponible");
		}
		
		if(estadoOf == 1)//Adquirida por cliente
		{
			cEstados.addItem("Adquirida cliente");
		}
		
		
		final Date diaInicio = of.getFirstDay();
		final Date diaFinalizacion = of.getLastDay();
		Calendar cal = Calendar.getInstance();
		
	    cal.setTime(diaInicio);
	    int dayInicio = cal.get(Calendar.DAY_OF_MONTH);
	    int monthInicio = cal.get(Calendar.MONTH);
	    int yearInicio = cal.get(Calendar.YEAR);
		
		calendarFechaInicio.getDayChooser().setDay(dayInicio);
		calendarFechaInicio.getMonthChooser().setMonth(monthInicio);
		calendarFechaInicio.getYearChooser().setYear(yearInicio);
		
		cal.setTime(diaFinalizacion);
	    int dayFin = cal.get(Calendar.DAY_OF_MONTH);
	    int monthFin = cal.get(Calendar.MONTH);
	    int yearFin = cal.get(Calendar.YEAR);
		
		calendarFechaFin.getDayChooser().setDay(dayFin);
		calendarFechaFin.getMonthChooser().setMonth(monthFin);//1
		calendarFechaFin.getYearChooser().setYear(yearFin);
		
		btnCrearOferta.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				lblMsgError.setText("");			
		
				DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
				
				//Creamos la fecha de incio segun la seleccionamos
				int diaIni=calendarFechaInicio.getDayChooser().getDay();
				int mesIni=calendarFechaInicio.getMonthChooser().getMonth()+1;
				int añoIni=calendarFechaInicio.getYearChooser().getYear();
		
				String inputStrInicio = diaIni+"/"+mesIni+"/"+añoIni;
				Date dateInicio  = null;
				try {
					dateInicio = dateFormat.parse(inputStrInicio);
					//System.out.println("di "+dateInicio);
				} catch (ParseException e) {
				
					e.printStackTrace();
				}
				
				//Creamos la fecha de fin segun la seleccionamos
				int diaFin=calendarFechaFin.getDayChooser().getDay();
				int mesFin=calendarFechaFin.getMonthChooser().getMonth()+1;
				int añoFin=calendarFechaFin.getYearChooser().getYear();
				
				String inputStrFin = diaFin+"/"+mesFin+"/"+añoFin;
				Date dateFin = null;
				try {
					dateFin = dateFormat.parse(inputStrFin);
					//System.out.println("df "+dateFin);
				} catch (ParseException e) {
					
					e.printStackTrace();
				}
				
				
				//Date currentDate = Calendar.getInstance().getTime();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");      
				Date currentDateWithoutTime = null;
				try {
					currentDateWithoutTime = sdf.parse(sdf.format(new Date()));
					//System.out.println("c"+currentDateWithoutTime);
				} catch (ParseException e) {
					e.printStackTrace();
				}

				
				int[] verErrores=new int[3];	
				for(int i=0;i<3;i++)
					verErrores[i]=0;
				
				
				if(isPrecioNumero(introducePrecio.getText()))
					verErrores[0]=1;
				
				//Fecha elegida de inicio es antes o igual que fecha actual
				if(!dateInicio.before(currentDateWithoutTime)|| dateInicio.equals(currentDateWithoutTime)) 
					verErrores[1]=1;
				
				//Fecha fin antes que fecha inicio
				if(!dateFin.before(dateInicio)) 
					verErrores[2]=1;
				
				//Mensajes de error
				if(verErrores[0]==0)
					lblMsgError.setText("El precio introducido no es correcto");
				
				if(verErrores[2]==0)
					lblMsgError.setText("La fecha de fin debe ser posterior a la de inicio");
				
				if(verErrores[1]==0)
					lblMsgError.setText("La fecha de inicio debe ser posterior a la fecha actual");
				
				if(verErrores[0]==1 && verErrores[1]==1 && verErrores[2]==1)
				{
					String user=null; //usuario que reserva la oferta, no propietrio
					   //Si ya existe una oferta en esa casa en esas fechas no dejar modificar la oferta
					
					String strEstadSel = cEstados.getSelectedItem().toString();
					int intEstadoSel = 4;
					
					if(strEstadSel.equals("Disponible"))
						intEstadoSel = 0;
					else if(strEstadSel.equals("Adquirida cliente"))
						intEstadoSel = 1;
					else
						intEstadoSel = 2;
					
					//Si no se ha cambiado nada no guardar en la BD
					float nuevoPrecio = Float.parseFloat(introducePrecio.getText());
					
					if((nuevoPrecio == precio) && (estadoOf == intEstadoSel) &&
					(diaInicio.equals(dateInicio)) && (diaFinalizacion.equals(dateFin)))
					{
						lblMsgError.setText("No ha modificado ningún dato de la oferta");
					}else
					{
						//RuralHouse rh = logicaNegocio.getRHById(idCasaSel);
						boolean b =logicaNegocio.modificarOferta(idCasaSel, idOferta, dateInicio, dateFin, nuevoPrecio, intEstadoSel);
						if(!b)
							lblMsgError.setText("Se ha modificado la oferta");
						else 
							lblMsgError.setText("Error, hay otra oferta creada en ese periodo");
					}
				}
			}
		});
		
		btnCrearOferta.setBounds(229, 367, 240, 33);
		contentPane.add(btnCrearOferta);
		
		lblMsgError.setBounds(123, 47, 454, 33);
		contentPane.add(lblMsgError);
		
		//setBounds(int x,int y,int width,int height)
		calendarFechaInicio.setBounds(50, 170, 260, 146);
		contentPane.add(calendarFechaInicio);
		
		calendarFechaFin.setBounds(340, 170, 260, 146);
		contentPane.add(calendarFechaFin);
		
		lblEstadoOferta.setBounds(355, 109, 114, 15);
		contentPane.add(lblEstadoOferta);
		
		cEstados.setBounds(473, 104, 162, 24);
		contentPane.add(cEstados);
	}
	
	//Validaciones
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
}