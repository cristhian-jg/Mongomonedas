import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class Actualizar extends JFrame {

	private JPanel contentPane;

	private MongoClient client = new MongoClient();
	private MongoDatabase mongoDatabase = client.getDatabase("monedas");
	private MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("monedas");

	private List<Document> listaMonedas = new ArrayList<Document>();

	private JTextField tfIdentidad;
	private JTextField tfPais;
	private JTextField tfFecha;

	private JLabel lblIdentificador;
	private JLabel lblDescripcion;
	private JLabel lblPais;
	private JLabel lblFecha;
	private JLabel lblTipo;

	private JRadioButton rdbtnMoneda;
	private JRadioButton rdbtnBillete;

	private JTextArea textArea;

	private JButton btnCancelar;
	private JButton btnAceptar;

	private Integer contadorValores;

	protected static int rowPosition;

	private Set<String> keysNested;

	public Actualizar() {
		setBounds(100, 100, 290, 315);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);

		lblIdentificador = new JLabel("IDENTIDAD");
		lblIdentificador.setBounds(10, 14, 80, 14);
		contentPane.add(lblIdentificador);

		lblDescripcion = new JLabel("DESCRIPCION");
		lblDescripcion.setBounds(10, 169, 89, 14);
		contentPane.add(lblDescripcion);

		lblPais = new JLabel("PAIS");
		lblPais.setBounds(10, 45, 43, 14);
		contentPane.add(lblPais);

		lblFecha = new JLabel("FECHA");
		lblFecha.setBounds(10, 76, 43, 14);
		contentPane.add(lblFecha);

		lblTipo = new JLabel("TIPO");
		lblTipo.setBounds(7, 111, 46, 14);
		contentPane.add(lblTipo);

		listaMonedas = mongoCollection.find().into(new ArrayList<Document>());

		rdbtnMoneda = new JRadioButton("MONEDA");
		rdbtnMoneda.setBounds(54, 131, 79, 23);
		contentPane.add(rdbtnMoneda);

		rdbtnMoneda.setSelected((boolean) listaMonedas.get(rowPosition).get("moneda"));

		rdbtnBillete = new JRadioButton("BILLETE");
		rdbtnBillete.setBounds(142, 131, 83, 23);
		contentPane.add(rdbtnBillete);

		rdbtnBillete.setSelected((boolean) listaMonedas.get(rowPosition).get("billete"));

		/**
		 * Creo un grupo para que solo se pueda seleccionar uno de los checkbox,
		 * evistando que los dos puedan estar a true ya que sería incoherente
		 *
		 */

		ButtonGroup group = new ButtonGroup();
		group.add(rdbtnMoneda);
		group.add(rdbtnBillete);

		tfIdentidad = new JTextField();
		tfIdentidad.setBounds(83, 11, 180, 20);
		contentPane.add(tfIdentidad);
		tfIdentidad.setColumns(10);

		tfIdentidad.setText(listaMonedas.get(rowPosition).get("identidad").toString());

		tfPais = new JTextField();
		tfPais.setBounds(83, 42, 180, 20);
		contentPane.add(tfPais);
		tfPais.setColumns(10);

		tfPais.setText(listaMonedas.get(rowPosition).get("pais").toString());

		tfFecha = new JTextField();
		tfFecha.setBounds(83, 73, 180, 20);
		contentPane.add(tfFecha);
		tfFecha.setColumns(10);

		tfFecha.setText(listaMonedas.get(rowPosition).get("fecha").toString());

		textArea = new JTextArea();
		textArea.setBounds(10, 194, 253, 38);
		contentPane.add(textArea);

		textArea.setText(listaMonedas.get(rowPosition).get("descripcion").toString());

		btnCancelar = new JButton("CANCELAR");
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				/** Cierra la ventana actual */
				dispose();
			}
		});
		btnCancelar.setBounds(167, 243, 96, 23);
		contentPane.add(btnCancelar);

		btnAceptar = new JButton("ACEPTAR");
		btnAceptar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				listaMonedas.get(rowPosition).get("_id").toString();

				Document query = new Document();
				query.append("_id", listaMonedas.get(rowPosition).get("_id"));

				Document setData = new Document();
				setData.append("identidad", tfIdentidad.getText().toString()).append("descripcion", textArea.getText())
						.append("fecha", tfFecha.getText()).append("pais", tfPais.getText())
						.append("moneda", rdbtnMoneda.isSelected()).append("billete", rdbtnBillete.isSelected());
				
				Document update = new Document();
				update.append("$set", setData);
				// To update single Document
				mongoCollection.updateOne(query, update);

				client.close();

				JOptionPane.showMessageDialog(null, "Operacion completada", "Se ha insertado la moneda correctamente",
						JOptionPane.INFORMATION_MESSAGE);

				dispose();

				Main.btnActualizar.doClick();
			}
		});
		btnAceptar.setBounds(73, 243, 89, 23);
		contentPane.add(btnAceptar);

		contadorValores = 0;
	}
}