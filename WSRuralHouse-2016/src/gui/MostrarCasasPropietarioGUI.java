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
import java.util.ArrayList;

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

public class MostrarCasasPropietarioGUI extends JFrame {
	private FacadeImplementationWS logicaNegocio;

	private JPanel contentPane;

	private static String user;
	private static String pass;

	private final JLabel labelMsg = new JLabel("");

	// private final JButton btnMostrarMisCasas = new
	// JButton("Mostrar mis casas");
	private final JButton btnEliminarUnaCasa = new JButton("Eliminar la casa");
	private final JButton btnMostrarOfertasDeCasa = new JButton(
			"Mostrar ofertas de la casa");
	private final JButton btnCrearUnaOferta = new JButton(
			"Crear oferta de la casa");

	
	// Tabla
	private DefaultTableModel model;
	
	private JTable table = new JTable(model) {
		private static final long serialVersionUID = 1L;

		// Disable editing table cells
		public boolean isCellEditable(int row, int column) {
			return false;
		};
	};
	
	private JScrollPane scrollPane = new JScrollPane(table);
	private JPanel topPanel;
	
	//private final JButton btnAtras = new JButton("Atras");

	
	//Menu
	JMenuBar menuBar;
	JMenu menuPropietario, exit;
		
	//private JTable table;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MostrarCasasPropietarioGUI frame = new MostrarCasasPropietarioGUI(user);
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
	public MostrarCasasPropietarioGUI(final String user) {
		setTitle("Casas del propietario");
		this.user = user;
		this.pass = pass;

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 981, 567);
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
				
		labelMsg.setBounds(242, 63, 559, 22);
		contentPane.add(labelMsg);

		topPanel = new JPanel();
		topPanel.setBounds(12, 92, 955, 207);
		contentPane.add(topPanel);
		topPanel.setLayout(null);
		
		mostrarTablaCasas();

		// No dejar eliminar una casa si hay ofertas cogidas para esa casa en el
		// futuro
		btnEliminarUnaCasa.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int selectedRowNumber = table.getSelectedRow();
				labelMsg.setText("");
				
				if (selectedRowNumber == -1) {
					labelMsg.setText("Selecciona una casa para eliminar");
				} else // eliminamos la casa seleccionada
				{
					String selectedHouseId = (String) table.getModel().getValueAt(selectedRowNumber, 8);
					// System.out.println("a "+ selectedHouseId);

					// RuralHouse rh=(RuralHouse)
					// mostrandoCasas.getSelectedItem();
					logicaNegocio = new FacadeImplementationWS();
					boolean b = logicaNegocio.eliminarCasa(Integer.parseInt(selectedHouseId));
					
					if(b == false)
					{
						labelMsg.setText("La casa ha sido eliminada");
						resetModel();
						mostrarTablaCasas();
					}
					else
						labelMsg.setText("No se puede eliminar la casa porque tiene ofertas adquiridas");
					// mostrarCasas();//Mostrar casas
				}
			}
		});
		btnEliminarUnaCasa.setBounds(331, 341, 287, 36);
		contentPane.add(btnEliminarUnaCasa);

		btnMostrarOfertasDeCasa.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int selectedRowNumber = table.getSelectedRow();
				labelMsg.setText("");
				
				if (selectedRowNumber == -1) {
					labelMsg.setText("Selecciona una casa para mostrar sus ofertas");
				} else {
					// Obtener id de la casa seleccionada
					String selectedHouseId = (String) table.getModel().getValueAt(selectedRowNumber, 8);
					// RuralHouse x=(RuralHouse)
					// mostrandoCasas.getSelectedItem();
					MostrarOfertasPropietarioGUI a = new MostrarOfertasPropietarioGUI(selectedHouseId, user);
					a.setVisible(true);
					setVisible(false);

				}
			}
		});
		btnMostrarOfertasDeCasa.setBounds(331, 402, 287, 36);
		contentPane.add(btnMostrarOfertasDeCasa);

		btnCrearUnaOferta.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int selectedRowNumber = table.getSelectedRow();
				labelMsg.setText("");
				
				if (selectedRowNumber == -1) {
					labelMsg.setText("Selecciona una casa para crear la oferta");
				} else {
					String selectedHouseId = (String) table.getModel().getValueAt(selectedRowNumber, 8);
					crearOfertaGUI a = new crearOfertaGUI(Integer.valueOf(selectedHouseId), user);
					a.setVisible(true); 
					setVisible(false);
					 
				}
			}
		});
		btnCrearUnaOferta.setBounds(331, 457, 287, 36);
		contentPane.add(btnCrearUnaOferta);
		
		scrollPane = new JScrollPane(table);
		scrollPane.setBounds(12, 12, 931, 183);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		topPanel.add(scrollPane);
	}
	
	
	
	public void mostrarTablaCasas()
	{
		// TABLA

		// Create columns names
		String columnNames[] = { "Localización", "Nombre", "Descripción",
				"Num Hab.", "Num Pers.", "General", "Cocina", "Sala", "IdCasa" };

		// Create columns data
		logicaNegocio = new FacadeImplementationWS();
		ArrayList<RuralHouse> rh = logicaNegocio.getRuralHouseByOwnerId(user);

		int numCasasPropietario = rh.size();// Numero de casas del propietario
		String dataValues[][] = null;
		
		if(numCasasPropietario == 0)
		{
			labelMsg.setText("No dispone de ninguna casa");
		}else
		{
			// Num id casa
			int numCasa[] = new int[numCasasPropietario];
			for (int i = 0; i < numCasasPropietario; i++)
				numCasa[i] = rh.get(i).getHouseNumber();// hidden

			// Localizacion
			String localizacionCasa[] = new String[numCasasPropietario];
			for (int i = 0; i < numCasasPropietario; i++)
				localizacionCasa[i] = rh.get(i).getCity();

			// Nombre casa
			String nombreCasa[] = new String[numCasasPropietario];
			for (int i = 0; i < numCasasPropietario; i++)
				nombreCasa[i] = rh.get(i).getNombreRH();

			// Descripcion
			String descripcionCasa[] = new String[numCasasPropietario];
			for (int i = 0; i < numCasasPropietario; i++)
				descripcionCasa[i] = rh.get(i).getDescription();

			// Num habitaciones
			int numHabCasa[] = new int[numCasasPropietario];
			for (int i = 0; i < numCasasPropietario; i++)
				numHabCasa[i] = rh.get(i).getNumeroHabitaciones();

			// Num personas
			int numPersCasa[] = new int[numCasasPropietario];
			for (int i = 0; i < numCasasPropietario; i++)
				numPersCasa[i] = rh.get(i).getNumeroPersonas();

			// Caracteristicas generales
			boolean generalCasa[][] = new boolean[numCasasPropietario][8];
			for (int i = 0; i < numCasasPropietario; i++)
				generalCasa[i] = rh.get(i).getGeneral();

			// Caracteristicas cocina
			boolean cocinaCasa[][] = new boolean[numCasasPropietario][8];
			for (int i = 0; i < numCasasPropietario; i++)
				cocinaCasa[i] = rh.get(i).getCocina();

			// Caracteristicas sala
			boolean salaCasa[][] = new boolean[numCasasPropietario][8];
			for (int i = 0; i < numCasasPropietario; i++)
				salaCasa[i] = rh.get(i).getSala();

			// Pasar arrays boolean a String

			// Array de Strings, un String por cada casa
			String[] generalCaracteristicas = new String[numCasasPropietario];
			for (int i = 0; i < numCasasPropietario; i++)
				generalCaracteristicas[i] = "";

			// Para cada casa pasar los valores de los arrays boolean a String
			for (int i = 0; i < numCasasPropietario; i++) {
				// Inicializar array de 8 Strings a "No"
				// Si la casa dispone de esa caracteristica guardar "Si" en el array
				String[][] strGeneralCasa = new String[numCasasPropietario][8];
				for (int l = 0; l < 8; l++)
					strGeneralCasa[i][l] = "No";

				for (int j = 0; j < 8; j++)
					if (generalCasa[i][j])
						strGeneralCasa[i][j] = "Si";

				generalCaracteristicas[i] = "Wifi: " + strGeneralCasa[i][0]
						+ "\nTeléfono: " + strGeneralCasa[i][1] + "\nCalefacción: "
						+ strGeneralCasa[i][2] + "\nAire acond.: "
						+ strGeneralCasa[i][3] + "\nBarbacoa: "
						+ strGeneralCasa[i][4] + "\nPiscina: "
						+ strGeneralCasa[i][5] + "\nPermiten perros: "
						+ strGeneralCasa[i][6] + "\nPermiten fumar: "
						+ strGeneralCasa[i][7];

				// System.out.println(i+"a"+generalCaracteristicas[i]);

			}

			// Array de Strings, un String por cada casa
			String[] cocinaCaracteristicas = new String[numCasasPropietario];
			for (int i = 0; i < numCasasPropietario; i++)
				cocinaCaracteristicas[i] = "";

			// Para cada casa pasar los valores de los arrays boolean a String
			for (int i = 0; i < numCasasPropietario; i++) {
				// Inicializar array de 4 Strings a "No"
				// Si la cocina de la casa dispone de esa caracteristica guardar
				// "Si" en el array
				String[][] strCocina = new String[numCasasPropietario][8];
				for (int l = 0; l < 4; l++)
					strCocina[i][l] = "No";

				for (int j = 0; j < 4; j++)
					if (cocinaCasa[i][j])
						strCocina[i][j] = "Si";

				cocinaCaracteristicas[i] = "Lavavajillas: " + strCocina[i][0]
						+ "\nLavadora: " + strCocina[i][1] + "\nMicroondas: "
						+ strCocina[i][2] + "\nHorno: " + strCocina[i][3];

				// System.out.println(i+"a "+cocinaCaracteristicas[i]);

			}

			// Array de Strings, un String por cada casa
			String[] salaCaracteristicas = new String[numCasasPropietario];
			for (int i = 0; i < numCasasPropietario; i++)
				salaCaracteristicas[i] = "";

			// Para cada casa pasar los valores de los arrays boolean a String
			for (int i = 0; i < numCasasPropietario; i++) {
				// Inicializar array de 3 Strings a "No"
				// Si la cocina de la casa dispone de esa caracteristica guardar
				// "Si" en el array
				String[][] strSala = new String[numCasasPropietario][8];
				for (int l = 0; l < 3; l++)
					strSala[i][l] = "No";

				for (int j = 0; j < 3; j++)
					if (salaCasa[i][j])
						strSala[i][j] = "Si";

				salaCaracteristicas[i] = "TV: " + strSala[i][0] + "\nOrdenador: "
						+ strSala[i][1] + "\nLinea Musica: " + strSala[i][2];

				// System.out.println(i+"a "+salaCaracteristicas[i]);

			}

			// Crear array valores celdas
			// total_num_casas filas y 9  columnas (una hidden: el id de casa)
			dataValues = new String[numCasasPropietario][9];														

			// Inicializar array con los datos de casa
			for (int i = 0; i < numCasasPropietario; i++)// num de casas
			{
				dataValues[i][0] = localizacionCasa[i];
				dataValues[i][1] = nombreCasa[i];
				dataValues[i][2] = descripcionCasa[i];
				dataValues[i][3] = Integer.toString(numHabCasa[i]);
				dataValues[i][4] = Integer.toString(numPersCasa[i]);
				dataValues[i][5] = generalCaracteristicas[i];
				dataValues[i][6] = cocinaCaracteristicas[i];
				dataValues[i][7] = salaCaracteristicas[i];
				dataValues[i][8] = Integer.toString(numCasa[i]); // Numero de casa (hidden)
																	
			}
		}
			
		

		// Create a new table instance
		model = new DefaultTableModel(dataValues, columnNames);
		table.setModel(model);
		
		table.getTableHeader().setReorderingAllowed(false);// disable column
															// reordering
		table.getTableHeader().setResizingAllowed(false); // disable column
															// resizing
		table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12)); // Cabecera tabla  negrita
																				
		table.removeColumn(table.getColumnModel().getColumn(8)); // Ocultar la
																	// columna 8
																	// (identificador
																	// de casa)
		// table.setRowHeight(1,100);//theTable.setRowHeight(rowNumber,
		// rowHeight);

		// render columnas (textarea)
		table.getColumn("Cocina").setCellRenderer(new RenderTextAreaMessage1());
		table.getColumn("Sala").setCellRenderer(new RenderTextAreaMessage1());
		table.getColumn("Descripción").setCellRenderer(
				new RenderTextAreaMessage1());
		table.getColumn("General")
				.setCellRenderer(new RenderTextAreaMessage1());

		table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table,
					Object value, boolean isSelected, boolean hasFocus,
					int row, int col) {

				Component c = super.getTableCellRendererComponent(table, value,
						isSelected, hasFocus, row, col);

				setBorder(noFocusBorder);// Al seleccionar una celda no dibuja
											// borde

				// Si la fila esta seleccionada mostrar el fondo de otro color
				if (isSelected)
					setBackground(Color.LIGHT_GRAY);
				else
					setBackground(table.getBackground());

				return this;
			}
		});

		updateRowHeight(table);

		// Anchura columnas porcentajes
		double[] arraySizes = { 10, 15, 20, 5, 5, 25, 15, 15, 0 };
		setJTableColumnsWidth(table, 480, arraySizes);
	}
	
	public void resetModel() {
		 model.setRowCount(0);
		}

	// Anchura columnas
	public static void setJTableColumnsWidth(JTable table,
			int tablePreferredWidth, double[] percentages) {
		double total = 0;

		for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
			total += percentages[i];
		}

		for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
			TableColumn column = table.getColumnModel().getColumn(i);
			column.setPreferredWidth((int) (tablePreferredWidth * (percentages[i] / total)));
		}
	}
	

	// Altura filas
	public static void updateRowHeight(JTable table) {
		final int rowCount = table.getRowCount();
		final int colCount = table.getColumnCount();
		for (int i = 0; i < rowCount; i++) {
			int maxHeight = 0;
			for (int j = 0; j < colCount; j++) {
				final TableCellRenderer renderer = table.getCellRenderer(i, j);
				maxHeight = Math.max(maxHeight,
						table.prepareRenderer(renderer, i, j)
								.getPreferredSize().height);
			}
			table.setRowHeight(i, maxHeight + 6);
		}
	}

}

final class RenderTextAreaMessage1 extends DefaultTableCellRenderer {

	private static final long serialVersionUID = 1L;

	JTextArea textareaMessage;

	@Override
	public Component getTableCellRendererComponent(JTable aTable,
			Object aNumberValue, boolean aIsSelected, boolean aHasFocus,
			int aRow, int aColumn) {

		String value = (String) aNumberValue;

		textareaMessage = new JTextArea();

		textareaMessage.setLineWrap(true);
		textareaMessage.setWrapStyleWord(true);
		textareaMessage.setText(value);
		textareaMessage.setBorder(null);
		textareaMessage.setMargin(null);

		Component renderer = super.getTableCellRendererComponent(aTable,
				aNumberValue, aIsSelected, aHasFocus, aRow, aColumn);

		// Si la fila esta seleccionada mostrar el fondo de otro color
		if (aIsSelected)
			textareaMessage.setBackground(Color.LIGHT_GRAY);

		return textareaMessage;
	}
}