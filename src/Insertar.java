import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;

public class Insertar extends JFrame {

	private JPanel contentPane;

	private JTextField tfIdentidad;
	private JTextField tfPais;
	private JTextField tfFecha;

	private JLabel lblIdentificador;
	private JLabel lblDescripcion;
	private JLabel lblPais;
	private JLabel lblFecha;
	private JLabel lblTipo;
	private JLabel lblValores;

	private JRadioButton rdbtnMoneda;
	private JRadioButton rdbtnBillete;

	private JTextArea textArea;

	private JButton btnCancelar;
	private JButton btnAceptar;

	private JComboBox cbValor1;
	private JComboBox cbValor2;
	private JComboBox cbValor3;
	private JComboBox cbValor4;

	private JTextField tfValue1;
	private JTextField tfValue2;
	private JTextField tfValue3;
	private JTextField tfValue4;

	private Integer contadorValores;

	public Insertar() {
		setBounds(100, 100, 290, 460);
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

		lblValores = new JLabel("VALORES");
		lblValores.setBounds(10, 243, 80, 14);
		contentPane.add(lblValores);

		rdbtnMoneda = new JRadioButton("MONEDA");
		rdbtnMoneda.setBounds(54, 131, 79, 23);
		contentPane.add(rdbtnMoneda);

		rdbtnBillete = new JRadioButton("BILLETE");
		rdbtnBillete.setBounds(142, 131, 83, 23);
		contentPane.add(rdbtnBillete);

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

		tfPais = new JTextField();
		tfPais.setBounds(83, 42, 180, 20);
		contentPane.add(tfPais);
		tfPais.setColumns(10);

		tfFecha = new JTextField();
		tfFecha.setBounds(83, 73, 180, 20);
		contentPane.add(tfFecha);
		tfFecha.setColumns(10);

		textArea = new JTextArea();
		textArea.setBounds(10, 194, 253, 38);
		contentPane.add(textArea);

		cbValor1 = new JComboBox();
		cbValor1.setModel(new DefaultComboBoxModel(Valores.values()));
		cbValor1.setBounds(10, 263, 89, 20);
		contentPane.add(cbValor1);

		tfValue1 = new JTextField();
		tfValue1.setBounds(114, 263, 96, 20);
		contentPane.add(tfValue1);
		tfValue1.setColumns(10);

		cbValor2 = new JComboBox();
		cbValor2.setModel(new DefaultComboBoxModel(Valores.values()));
		cbValor2.setBounds(10, 294, 89, 20);
		contentPane.add(cbValor2);
		cbValor2.setVisible(false);

		tfValue2 = new JTextField();
		tfValue2.setColumns(10);
		tfValue2.setBounds(114, 294, 96, 20);
		contentPane.add(tfValue2);
		tfValue2.setVisible(false);

		cbValor3 = new JComboBox();
		cbValor3.setModel(new DefaultComboBoxModel(Valores.values()));
		cbValor3.setBounds(10, 325, 89, 20);
		contentPane.add(cbValor3);
		cbValor3.setVisible(false);

		tfValue3 = new JTextField();
		tfValue3.setColumns(10);
		tfValue3.setBounds(114, 325, 96, 20);
		contentPane.add(tfValue3);
		tfValue3.setVisible(false);

		cbValor4 = new JComboBox();
		cbValor4.setModel(new DefaultComboBoxModel(Valores.values()));
		cbValor4.setBounds(10, 356, 89, 20);
		contentPane.add(cbValor4);
		cbValor4.setVisible(false);

		tfValue4 = new JTextField();
		tfValue4.setColumns(10);
		tfValue4.setBounds(114, 356, 96, 20);
		contentPane.add(tfValue4);
		tfValue4.setVisible(false);

		btnCancelar = new JButton("CANCELAR");
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				/** Cierra la ventana actual */
				dispose();
			}
		});
		btnCancelar.setBounds(167, 387, 96, 23);
		contentPane.add(btnCancelar);

		btnAceptar = new JButton("ACEPTAR");
		btnAceptar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				/**
				 * Me conecto/creo la base de datos y obtengo la colección sobre la que voy a
				 * trabajar en este caso monedas.
				 */

				MongoClient client = new MongoClient();
				MongoDatabase mongoDatabase = client.getDatabase("monedas");
				MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("monedas");

				Document document = new Document();

				if (!tfIdentidad.getText().equals("")) {
					document.put("identidad", tfIdentidad.getText());
				} else {
					JOptionPane.showMessageDialog(null, "Debes introducir un identificador para la moneda",
							"Error en el identificador", JOptionPane.ERROR_MESSAGE);
					return;
				}

				document.put("descripcion", textArea.getText());
				document.put("fecha", tfFecha.getText());
				document.put("pais", tfPais.getText());
				if (rdbtnMoneda.isSelected() || rdbtnBillete.isSelected()) {
					document.put("moneda", rdbtnMoneda.isSelected());
					document.put("billete", rdbtnBillete.isSelected());
				} else {
					JOptionPane.showMessageDialog(null, "Debes seleccionar el tipo de la moneda", "Error en el tipo",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				/** Añadimos tambien el array con los cambios de la moneda seleccionada */

				ArrayList<Document> valores = new ArrayList<Document>();

				if (cbValor1.getSelectedItem() != null) {

					valores.add(new Document().append(cbValor1.getSelectedItem().toString(), tfValue1.getText()));

					System.out.print(contadorValores);

					if (contadorValores > 0 && tfValue2.getText() != null) {
						valores.add(new Document().append(cbValor2.getSelectedItem().toString(), tfValue2.getText()));
					}

					if (contadorValores > 1 && tfValue3.getText() != null) {
						valores.add(new Document().append(cbValor3.getSelectedItem().toString(), tfValue3.getText()));
					}

					if (contadorValores > 2 && tfValue3.getText() != null) {
						valores.add(new Document().append(cbValor4.getSelectedItem().toString(), tfValue4.getText()));
					}

					document.put("valores", valores);
				}

				mongoCollection.insertOne(document);
				client.close();

				JOptionPane.showMessageDialog(null, "Operacion completada", "Se ha insertado la moneda correctamente",
						JOptionPane.INFORMATION_MESSAGE);
				
				dispose();
				
				Main.btnActualizar.doClick();
			}
		});
		btnAceptar.setBounds(68, 387, 89, 23);
		contentPane.add(btnAceptar);

		contadorValores = 0;

		JButton btnMas = new JButton("+");
		btnMas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				switch (contadorValores) {
				case 0:
					cbValor2.setVisible(true);
					tfValue2.setVisible(true);
					break;
				case 1:
					cbValor3.setVisible(true);
					tfValue3.setVisible(true);
					break;
				case 2:
					cbValor4.setVisible(true);
					tfValue4.setVisible(true);
					break;
				default:
					JOptionPane.showMessageDialog(null, "No se pueden insertar más valores",
							"Límite de valores alcanzado", JOptionPane.ERROR_MESSAGE);
					break;
				}

				if (contadorValores < 4)
					contadorValores++;

			}
		});
		btnMas.setBounds(220, 262, 43, 23);
		contentPane.add(btnMas);

	}

}
