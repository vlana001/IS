package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.FontMetrics;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.JComboBox;
import javax.swing.JButton;

import businessLogic.FacadeImplementationWS;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import domain.Offer;
import domain.RuralHouse;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class MostrarOfertasPropietarioGUI extends JFrame {
	private FacadeImplementationWS logicaNegocio;
	
	private JPanel contentPane;
	
	private static String user;
	private static String pass;
	private static String idCasaSeleccionada;
	
	private final JLabel labelMsg = new JLabel("");
	
	private final JButton btnEliminarOferta = new JButton("Eliminar la oferta");
	private final JButton btnModificarOferta = new JButton("Modificar la oferta");
	
	//private final JButton btnAtras = new JButton("Atras");
	
	//Tabla
	private JPanel topPanel;
	
	private DefaultTableModel model;
	private JTable table = new JTable()
	{
		private static final long serialVersionUID = 1L;
	        
		//Disable editing table cells
	 	public boolean isCellEditable(int row, int column)
	 	{                
                return false;               
        };
	};
	private JScrollPane scrollPane = new JScrollPane(table);

	//Menu
	JMenuBar menuBar;
	JMenu menuPropietario, misCasas, exit;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MostrarOfertasPropietarioGUI frame = new MostrarOfertasPropietarioGUI(idCasaSeleccionada,user);
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
	public MostrarOfertasPropietarioGUI(final String idCasaSeleccionada, final String user) {
		setTitle("Ofertas del propietario");
		this.user=user;
		this.pass=pass;
		this.idCasaSeleccionada = idCasaSeleccionada;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 910, 567);
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
			
		
		labelMsg.setBounds(242, 63, 559, 22);
		contentPane.add(labelMsg);	
		
		
		topPanel = new JPanel();
		topPanel.setBounds(12, 92, 884, 207);
		contentPane.add(topPanel);
		topPanel.setLayout(null);
		mostrarTablaOfertas(idCasaSeleccionada);
		
		//Eliminar oferta
		btnEliminarOferta.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int selectedRowNumber = table.getSelectedRow();
				
				if(selectedRowNumber == -1)
				{
					labelMsg.setText("Selecciona una oferta para eliminar");
				}else //eliminamos la oferta seleccionada
				{
					String selectedOfferId = (String) table.getModel().getValueAt(selectedRowNumber, 6);
					String selectedOfferEstado = (String) table.getModel().getValueAt(selectedRowNumber, 1);
					//System.out.println("c "+selectedOfferEstado);
					
					logicaNegocio=new FacadeImplementationWS();	   
				    
				    if(!selectedOfferEstado.equals("Adquirida"))
				    {
				    	if(!selectedOfferEstado.equals("Eliminada prop."))
				    	{
					    	logicaNegocio.eliminarOferta(Integer.valueOf(selectedOfferId));
					    	//model.setRowCount(0); //Limpia el modelo
					    	//model.fireTableDataChanged();
					    	System.out.println("i "+idCasaSeleccionada);
					    	resetModel();
					    	mostrarTablaOfertas(idCasaSeleccionada);
					    	labelMsg.setText("La oferta ha sido eliminada");
				    	}else
				    		labelMsg.setText("La oferta ya se encuentra eliminada");
				    }
				    else
				    	labelMsg.setText("La oferta no se puede eliminar porque esta adquirida");
				}
			}
		});
		btnEliminarOferta.setBounds(282, 345, 287, 36);
		contentPane.add(btnEliminarOferta);
		
		
		
		//Modificar oferta
		btnModificarOferta.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int selectedRowNumber = table.getSelectedRow();
				
				if(selectedRowNumber == -1)
				{
					labelMsg.setText("Selecciona una oferta para modificar");
				}else 
				{
					//Obtener estado de la casa seleccionada
					String selectedOfferEstado = (String) table.getModel().getValueAt(selectedRowNumber, 1);
					if(selectedOfferEstado.equals("Adquirida"))
					{
						labelMsg.setText("No puede modificar una oferta aqquirida");
					}else
					{
						//Obtener id de la casa seleccionada
						String selectedOfferId = (String) table.getModel().getValueAt(selectedRowNumber, 6);
						ModificarOfertaGUI a = new ModificarOfertaGUI(Integer.valueOf(selectedOfferId), Integer.valueOf(idCasaSeleccionada), user);
						a.setVisible(true);
						setVisible(false);	
					}
				}
			}
		});
		btnModificarOferta.setBounds(282, 405, 287, 36);
		contentPane.add(btnModificarOferta);	
		
	}
	
	
	public void mostrarTablaOfertas(String idCasaSeleccionada1)
	{
		//TABLA
		
		// Create columns names
		String columnNames[] = {"Casa Rural", "Estado", "Primer día", "Último día", "Precio", "Cliente", "IdOferta"};

		//create columns data			
		logicaNegocio = new FacadeImplementationWS();
		ArrayList<Offer> of = logicaNegocio.getOffersByRHId(Integer.valueOf(idCasaSeleccionada1));
		
		int numOfertasCasa = of.size();//Numero de ofertas de la casa
		String dataValues[][] = null;
		
		if(numOfertasCasa == 0)
		{
			labelMsg.setText("La casa no dipone de ninguna oferta");
		}else
		{
			//Nombre Casa rural
			String nomCasaOferta[] = new String[numOfertasCasa];
			for(int i=0; i<numOfertasCasa; i++)
				nomCasaOferta[i] = of.get(i).getRuralHouse().getNombreRH();
				
			//Estado
			int estadoOferta[] = new int[numOfertasCasa];
			for(int i=0; i<numOfertasCasa; i++)
				estadoOferta[i] = of.get(i).getEstadoOferta();
			
			//Primer dia
			Date primerDiaOferta[] = new Date[numOfertasCasa];
			for(int i=0; i<numOfertasCasa; i++)
				primerDiaOferta[i] = of.get(i).getFirstDay();
			
			//Ultimo dia
			Date ultimoDiaOferta[] = new Date[numOfertasCasa];
			for(int i=0; i<numOfertasCasa; i++)
				ultimoDiaOferta[i] = of.get(i).getLastDay();
			
			//Precio
			float precioOferta[] = new float[numOfertasCasa];
			for(int i=0; i<numOfertasCasa; i++)
				precioOferta[i] = of.get(i).getPrice();
			
			//Cliente
			String clienteOferta[] = new String[numOfertasCasa];
			for(int i=0; i<numOfertasCasa; i++)
				clienteOferta[i] = of.get(i).getUser();
			
			//Id oferta (hidden)
			int idOferta[] = new int[numOfertasCasa];
			for(int i=0; i<numOfertasCasa; i++)
				idOferta[i] = of.get(i).getOfferNumber();
			
			
		
			//Pasar array int Estado a String
			
			//Array de Strings, un String por cada casa
			String[] estadosOfertas = new String[numOfertasCasa];
			for(int i=0; i<numOfertasCasa; i++)
				estadosOfertas[i]="";
			
			//Para cada oferta pasar los valores de los arrays int a String
			for(int i=0; i<numOfertasCasa; i++)
			{	
				switch (estadoOferta[i])
				{	
					case 0: estadosOfertas[i]="Disponible";break;
					case 1: estadosOfertas[i]="Adquirida";break;
					//case 2: estadosOfertas[i]="Cancelada cliente";break;
					case 2: estadosOfertas[i]="Eliminada prop.";break;
					//default: estadosOfertas[i]="Eliminada prop.";
				}
			}
			
			
			//Para cada oferta si no tiene cliente poner "-"
			for(int i=0; i<numOfertasCasa; i++)
			{	
				if(clienteOferta[i]==null)
				{
					clienteOferta[i]="-";
				}
			}
			
			
			//Para cada oferta pasar fechas Date to String
			SimpleDateFormat sdfr = new SimpleDateFormat("dd/MMM/yyyy");
			String strPrimerDiaOferta[] = new String[numOfertasCasa];
			String strUltimoDiaOferta[] = new String[numOfertasCasa];
			for(int i=0; i<numOfertasCasa; i++)
			{	
				//if(primerDiaOferta[i] != null)
				strPrimerDiaOferta[i] = sdfr.format(primerDiaOferta[i]);
				strUltimoDiaOferta[i] = sdfr.format(ultimoDiaOferta[i]);
			}
			
			//Crear array valores celdas
			dataValues = new String[numOfertasCasa][7];// total_num_ofertas_casa filas y 7 columnas (una hidden: el id de oferta)
			 
			//Inicializar array con los datos de casa
			for(int i=0; i<numOfertasCasa; i++)//num de casas
			{
				dataValues[i][0] = nomCasaOferta[i];
				dataValues[i][1] = estadosOfertas[i]; 
				dataValues[i][2] = strPrimerDiaOferta[i]; 
				dataValues[i][3] = strUltimoDiaOferta[i]; 	
				dataValues[i][4] = Float.toString(precioOferta[i]); 	
				dataValues[i][5] = clienteOferta[i]; 
				dataValues[i][6] = Integer.toString(idOferta[i]);//Numero de oferta hidden
			}
		}
		
		
		// Create a new table instance
		model = new DefaultTableModel(dataValues, columnNames);
		//System.out.println("hi");
		//System.out.println("m "+model.getRowCount());
	
		table.setModel(model);
		System.out.println("hih");
		table.getTableHeader().setReorderingAllowed(false);//disable column reordering
		table.getTableHeader().setResizingAllowed(false); //disable column resizing
		table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12)); //Cabecera tabla negrita
		table.removeColumn(table.getColumnModel().getColumn(6)); //Ocultar la columna 6 (identificador de oferta)
		//table.setRowHeight(1,100);//theTable.setRowHeight(rowNumber, rowHeight);
		
		//model.fireTableDataChanged();
		table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer(){
		    @Override
		    public Component getTableCellRendererComponent(JTable table,Object value, boolean isSelected, boolean hasFocus, int row, int col) {
		    	
		        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
		        
		        setBorder(noFocusBorder);//Al seleccionar una celda no dibuja borde	      
		        
		        //Color filas
				String selectedOfferEstado = (String)table.getModel().getValueAt(row, 1);
				
				if(selectedOfferEstado.equals("Disponible"))
					setBackground(Color.white);
				else
				{
					 if(selectedOfferEstado.equals("Adquirida"))
				        	setBackground(Color.decode("#afed3b"));
					 else
					 {
						if("Eliminada prop.".equals(selectedOfferEstado))
							setBackground(Color.decode("#147a60"));
						 else
							 setBackground(table.getBackground());	
					 }   
				}
 	
		        //Si la fila esta seleccionada mostrar el fondo de otro color
		         if (isSelected) 
		        	 setBackground(Color.LIGHT_GRAY);	
		           
		        return this;
		    }   
		});
		
		
		updateRowHeight(table);
		
		//Anchura columnas porcentajes
		double[] arraySizes={15, 15, 20, 5, 5, 25, 0};
		setJTableColumnsWidth(table, 480, arraySizes);
		
		table.repaint();
		table.invalidate();
		scrollPane.repaint();
		topPanel.repaint();
		
		
		scrollPane.setBounds(12, 12, 860, 183);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		topPanel.add(scrollPane);
		
		
	}
	
	
	public void resetModel() {
		  model.setRowCount(0);
		}
		
	//Anchura columnas
	public static void setJTableColumnsWidth(JTable table, int tablePreferredWidth, double[] percentages) 
	{
	    double total = 0;
	    
	    for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
	        total += percentages[i];
	    }
	 
	    for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
	        TableColumn column = table.getColumnModel().getColumn(i);
	        column.setPreferredWidth((int)(tablePreferredWidth * (percentages[i] / total)));
	    }
	}
	
	//Altura filas
	public static void updateRowHeight(JTable table) {
	    final int rowCount = table.getRowCount();
	    final int colCount = table.getColumnCount();
	    for (int i = 0; i < rowCount; i++) {
	        int maxHeight = 0;
	        for (int j = 0; j < colCount; j++) {
	            final TableCellRenderer renderer = table.getCellRenderer(i, j);
	            maxHeight = Math.max(maxHeight, table.prepareRenderer(renderer, i, j).getPreferredSize().height);
	        }
	        table.setRowHeight(i, maxHeight+6);
	    }
	}
	
}
