import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.diagnostics.logging.Logger;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.event.ActionListener;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.Font;

public class Main extends JFrame {

	private JPanel contentPane;
	private JTable table;

	private JLabel lblColeccionDeMonedas;

	protected static JButton btnActualizar;
	
	private JButton btnInsertar;
	private JButton btnBorrar;
	
	private JScrollPane scrollPane;

	private MongoClient client = new MongoClient();
	private MongoDatabase mongoDatabase = client.getDatabase("monedas");
	private MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("monedas");

	private List<Document> listaMonedas = new ArrayList<Document>();

	private static DefaultTableModel tableModel;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main frame = new Main();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Main() {

		listaMonedas = mongoCollection.find().into(new ArrayList<Document>());

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1050, 450);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		lblColeccionDeMonedas = new JLabel("COLECCI\u00D3N DE MONEDAS");
		lblColeccionDeMonedas.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblColeccionDeMonedas.setBounds(369, 11, 186, 16);
		contentPane.add(lblColeccionDeMonedas);

		btnInsertar = new JButton("A\u00D1ADIR");
		btnInsertar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Insertar frame = new Insertar();
				frame.setVisible(true);
			}
		});

		btnInsertar.setBounds(12, 375, 97, 25);
		contentPane.add(btnInsertar);

		btnBorrar = new JButton("BORRAR");
		btnBorrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (table.getSelectedRow() != -1) {
					if (JOptionPane.showConfirmDialog(rootPane,
							"Se eliminará el registro '" + listaMonedas.get(table.getSelectedRow()).get("identidad")
									+ "', ¿desea continuar?",
							"Eliminar Registro", JOptionPane.WARNING_MESSAGE,
							JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {

						mongoCollection
								.deleteOne(new Document("_id", listaMonedas.get(table.getSelectedRow()).get("_id")));

						refrescarTabla();
					}
				} else {
					JOptionPane.showMessageDialog(null, "Selecciona la fila que deseas borrar", "Operación cancelada",
							JOptionPane.ERROR_MESSAGE);
				}

			}
		});
		btnBorrar.setBounds(218, 375, 97, 25);
		contentPane.add(btnBorrar);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 36, 1012, 328);
		contentPane.add(scrollPane);

		tableModel = new DefaultTableModel(new Object[][] {},
				new String[] { "IDENTIDAD", "FECHA", "PAIS", "MONEDA", "BILLETE", "DESCRIPCION", "VALORES" });

		table = new JTable();
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"IDENTIDAD", "FECHA", "PAIS", "MONEDA", "BILLETE", "DESCRIPCION", "VALORES"
			}
		));
		scrollPane.setViewportView(table);


		table.getColumnModel().getColumn(0).setPreferredWidth(70); //ID
		table.getColumnModel().getColumn(1).setPreferredWidth(70); //FE
		table.getColumnModel().getColumn(2).setPreferredWidth(1); //PA
		table.getColumnModel().getColumn(3).setPreferredWidth(50); //MO
		table.getColumnModel().getColumn(4).setPreferredWidth(50); //BI
		table.getColumnModel().getColumn(5).setPreferredWidth(250); //DESC
		table.getColumnModel().getColumn(6).setPreferredWidth(300); //VAL
		
		JButton btnSalir = new JButton("SALIR");
		btnSalir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		btnSalir.setBounds(935, 376, 89, 23);
		contentPane.add(btnSalir);
		
		/**
		 * Intenté actualizar con un listener como me comentabas en clase y con una pruebas basicas me funcionó,
		 * pero al utilizar mi metodo refrescarTabla() me salían varios errores sin descripción así que no pude saber bien que pasaba.
		 * Por ello he optado por usar un botón oculto que actualiza por si solo al darle al botón insertar en el otro JFrame mediante el metodo .doClick(), para esto
		 * he tenido que hacerlo estatico.
		 */
		
		btnActualizar = new JButton("ACTUALIZAR");
		btnActualizar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				refrescarTabla();
			}
		});
		btnActualizar.setBounds(811, 376, 114, 23);
		contentPane.add(btnActualizar);
		
		JButton btnEditar = new JButton("EDITAR");
		btnEditar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (table.getSelectedRow() != -1) {
					Actualizar.rowPosition = table.getSelectedRow();
				    Actualizar frame = new Actualizar();
				    frame.setVisible(true);
				} else {
					JOptionPane.showMessageDialog(null, "Selecciona la fila que deseas editar", "Operación cancelada",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnEditar.setBounds(119, 376, 89, 23);
		contentPane.add(btnEditar);
		btnActualizar.setVisible(false);
	
		refrescarTabla();

	}

	public void refrescarTabla() {

		if ((table.getModel()).getRowCount() != 0) {
			((DefaultTableModel) table.getModel()).setRowCount(0);
		}

		listaMonedas = mongoCollection.find().into(new ArrayList<Document>());

		for (int j = 0; j < listaMonedas.size(); j++) {

			List<Document> listaValores = (List<Document>) listaMonedas.get(j).get("valores");

			Object filas[] = { listaMonedas.get(j).get("identidad"), listaMonedas.get(j).get("fecha"),
					listaMonedas.get(j).get("pais"), listaMonedas.get(j).get("moneda"),
					listaMonedas.get(j).get("billete"), listaMonedas.get(j).get("descripcion"), listaValores };
			((DefaultTableModel) table.getModel()).addRow(filas);
		}

	}
}
