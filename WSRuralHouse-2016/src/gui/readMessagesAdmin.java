package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Insets;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.text.DefaultCaret;
import javax.swing.text.JTextComponent;
import javax.swing.text.View;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import businessLogic.FacadeImplementationWS;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.BoxLayout;

public class readMessagesAdmin extends JFrame {

	private FacadeImplementationWS logicaNegocio;
	
	private JPanel contentPane;
	private	JPanel topPanel;
	private	JTable table;
	private	JScrollPane scrollPane;
	
	JLabel lblMsg = new JLabel("");
	JButton btnEnviarEmail = new JButton("Enviar email");
	
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
					readMessagesAdmin frame = new readMessagesAdmin();
					frame.setResizable(false);
					//frame.getContentPane().setLayout(new BorderLayout());
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
	public readMessagesAdmin() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 929, 527);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		setTitle("Mensajes recibidos");
		contentPane.setLayout(null);
		//contentPane.setLayout(BorderLayout.CENTER);

		
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
		
		// Create a panel to hold all other components
		topPanel = new JPanel();
		topPanel.setBounds(67, 79, 819, 388);

		
		getContentPane().add(topPanel);

		// Create columns names
		String columnNames[] = {"Num.", "Fecha", "Usuario", "Tipo Usuario", "Asunto", "Mensaje", "Status"};

		//create columns data
		logicaNegocio = new FacadeImplementationWS();
		
		int[] numMsg = logicaNegocio.getMessageNumbers();
		String[] dateMsg = logicaNegocio.getMessageDates();
		String[] userMsg = logicaNegocio.getMessageUsers();
		boolean[] rolUserMsg = logicaNegocio.getMessageUsersRoles();
		String[] asuntoMsg = logicaNegocio.getMessageAsunto();
		String[] textoMsg = logicaNegocio.getMessageTexto();
		boolean[] msgRead = logicaNegocio.getMessageRead();//hidden
		
		int num_length = numMsg.length;//Numero de mensajes
		
		//Pasar de boolean a String
		String[] rolUserMsgStr = new String[num_length];
		for(int i=0; i<num_length; i++)
		{
			if(rolUserMsg[i] == false)
				rolUserMsgStr[i]="cliente";
			else
				rolUserMsgStr[i]="propietario";	
		}
		
		//Pasar de boolean a String
		String[] msgReadStr = new String[num_length];
		for(int i=0; i<num_length; i++)
		{
			if(msgRead[i] == false)
				msgReadStr[i]="read";
			else
				msgReadStr[i]="unread";	
		}
		
		//Crear array
		String dataValues[][] = new String[num_length][7];// total_num_msg filas y 7 columnas (una hidden)
		 
		//Inicializar array con los datos de mensaje
		//Ordenar de mas reciente a mas antiguo
		for(int i=0, j=num_length-1;i<num_length;i++, j--)//num de mensajes
		{
			dataValues[i][0] = Integer.toString(numMsg[j]); //Numero de mensaje
			dataValues[i][1] = dateMsg[j]; //Fecha hora escribio mensaje
			dataValues[i][2] = userMsg[j]; //Usuario escribio mensaje
			dataValues[i][3] = rolUserMsgStr[j]; //Rol usuario escribio mensaje	
			dataValues[i][4] = asuntoMsg[j]; //Asunto mensaje
			dataValues[i][5] = textoMsg[j]; //Texto mensaje
			dataValues[i][6] = msgReadStr[j]; //Estado mensaje
			//dataValues[i][7] = "Responder";
			 
		}
		
		// Create a new table instance
		DefaultTableModel model = new DefaultTableModel(dataValues, columnNames);
		table = new JTable(model)
		{
			private static final long serialVersionUID = 1L;
		        
			//Disable editing table cells
		 	public boolean isCellEditable(int row, int column)
		 	{                
	                return false;               
	        };
	        
            
		};
		table.getTableHeader().setReorderingAllowed(false);//disable column reordering
		table.getTableHeader().setResizingAllowed(false); //disable column resizing
		table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12)); //Cabecera tabla negrita
		table.removeColumn(table.getColumnModel().getColumn(6)); //Ocultar la columna 6 (estado del mensaje)
		//table.setRowHeight(1,100);//theTable.setRowHeight(rowNumber, rowHeight);
		
		//render columna 6(mensaje)
		table.getColumn("Mensaje").setCellRenderer(new RenderTextAreaMessage());
		
		updateRowHeight(table);
		
		//Anchura columnas porcentajes
		double[] arraySizes={2, 20, 8, 8, 15, 47, 0};
		setJTableColumnsWidth(table, 480, arraySizes);
		
		/*  Si los mensajes son nuevos, es decir, si los mensajes son posteriores
		    a la ultima vez que el admin abrio el buzon de mensajes,
			mostrar fondo de esos nuevos mensajes verde
		*/
		table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer(){
		    @Override
		    public Component getTableCellRendererComponent(JTable table,Object value, boolean isSelected, boolean hasFocus, int row, int col) {
		    	
		        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
		        
		        setBorder(noFocusBorder);//Al seleccionar una celda no dibuja borde
		        
		        String status = (String)table.getModel().getValueAt(row, 6);
		        if ("read".equals(status)) 
		            setBackground(Color.green);
		        else 
		            setBackground(table.getBackground());		      
		        
		        //Si la fila esta seleccionada mostrar el fondo de otro color
		         if (isSelected) 
		        	 setBackground(Color.LIGHT_GRAY);
		         
			        
		        return this;
		    }   
		});
		topPanel.setLayout(null);
		
		// Add the table to a scrolling pane
		scrollPane = new JScrollPane(table);
		scrollPane.setBounds(0, 0, 819, 388);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		topPanel.add(scrollPane);
		
		btnEnviarEmail.setBounds(362, 42, 119, 25);
		btnEnviarEmail.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				lblMsg.setText("");
				if(table.getSelectedRow() >= 0)
				{
					int selectedRowNumber= table.getSelectedRow();
					
					String selectedRowUser = (String)table.getValueAt(selectedRowNumber, 2);
					String selectedRowUserRol = (String)table.getValueAt(selectedRowNumber, 3);
					//System.out.println(selectedRowUser);
					
					//Obtener el email del usuario seleccionado
					String email = logicaNegocio.getEmailByUserName(selectedRowUser, selectedRowUserRol);
					//System.out.println("email " + email);
					
					//Redireccionar a frame enviar email
					sendEmailToUser a = new sendEmailToUser(email);
					a.setVisible(true);
					setVisible(false);
				}else
					lblMsg.setText("Seleccione un usuario para enviarle un mensaje");
					
			}
		});
		contentPane.add(btnEnviarEmail);
		
		
		lblMsg.setBounds(207, 12, 412, 15);
		contentPane.add(lblMsg);
		
		
		//Cuando el admin lee (se muestran en pantalla) todos los mensajes
		//cambiar el estado de los mensajes con estado no leido a leido
		logicaNegocio.setUnreadMsgNewStatus();
	}
	
	
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


final class RenderTextAreaMessage extends DefaultTableCellRenderer {
	  
	JTextArea textareaMessage;
	  
	  @Override 
	  public Component getTableCellRendererComponent(JTable aTable, Object aNumberValue, boolean aIsSelected, 
	    boolean aHasFocus, int aRow, int aColumn ) {  
		// System.out.println("aa");
		 String value = (String)aNumberValue;
		  
		 textareaMessage = new JTextArea();
		
	     textareaMessage.setLineWrap(true);
	     textareaMessage.setWrapStyleWord(true);
	     textareaMessage.setText(value);
	     textareaMessage.setBorder(null);
	     textareaMessage.setMargin(null);
	    
	    Component renderer = super.getTableCellRendererComponent(
	    		aTable, aNumberValue, aIsSelected, aHasFocus, aRow, aColumn
	    );
	    
	     // Si un mensaje no ha sido leido de una fila, 
	     //el background del textarea del mismo color, que el resto de la fila (table)
	     String status = (String)aTable.getModel().getValueAt(aRow, 6);
	     
	      if ("read".equals(status)) 
	    	  textareaMessage.setBackground(Color.green);
	      //else 
	        //  textarea.setBackground(Color.RED);
	      
	      //Al seleccionar una fila, el background del textarea del mismo color, que el resto de la fila (table) 
     		if (aIsSelected) 
     			textareaMessage.setBackground(Color.LIGHT_GRAY);
	       //else 
	          //textarea.setBackground(Color.WHITE);    
	      
	    return textareaMessage;
	  }
}





