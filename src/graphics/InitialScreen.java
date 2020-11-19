package graphics;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Component;
import java.awt.CardLayout;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.BoxLayout;
import java.awt.Insets;
import javax.swing.JList;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JButton;

public class InitialScreen extends JPanel {
	private JTextField textName;
	
	
	/**
	 * Create the panel.
	 */
	public InitialScreen() {
		
		setForeground(new Color(255, 255, 255));
		setBackground(new Color(0, 128, 128));
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.rowHeights = new int[]{62, 53, 0};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 1.0};
		gridBagLayout.columnWeights = new double[]{1.0, 1.0};
		setLayout(gridBagLayout);
		
		JLabel lblTitle = new JLabel("Share or Take?");
		GridBagConstraints gbc_lblTitle = new GridBagConstraints();
		gbc_lblTitle.gridwidth = 2;
		gbc_lblTitle.insets = new Insets(0, 0, 5, 0);
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setForeground(SystemColor.controlHighlight);
		lblTitle.setFont(new Font("Lato Medium", Font.BOLD, 18));
		gbc_lblTitle.gridx = 0;
		gbc_lblTitle.gridy = 0;
		add(lblTitle, gbc_lblTitle);
		
		JLabel lblSubtitle = new JLabel("Simulador de sobrevivência");
		GridBagConstraints gbc_lblSubtitle = new GridBagConstraints();
		gbc_lblSubtitle.gridwidth = 2;
		gbc_lblSubtitle.insets = new Insets(0, 0, 5, 0);
		lblSubtitle.setForeground(new Color(47, 79, 79));
		lblSubtitle.setHorizontalAlignment(SwingConstants.CENTER);
		gbc_lblSubtitle.gridx = 0;
		gbc_lblSubtitle.gridy = 1;
		add(lblSubtitle, gbc_lblSubtitle);
		
		JPanel panelUserInput = new JPanel();
		GridBagConstraints gbc_panelUserInput = new GridBagConstraints();
		gbc_panelUserInput.gridwidth = 2;
		gbc_panelUserInput.insets = new Insets(0, 0, 0, 0);
		gbc_panelUserInput.gridx = 0;
		gbc_panelUserInput.gridy = 2;
		gbc_panelUserInput.fill = GridBagConstraints.BOTH;
		add(panelUserInput, gbc_panelUserInput);
		panelUserInput.setLayout(null);
		
		JLabel lblName = new JLabel("Nome:");
		lblName.setBounds(12, 20, 268, 20);
		panelUserInput.add(lblName);
		
		textName = new JTextField();
		textName.setBounds(12, 41, 149, 20);
		panelUserInput.add(textName);
		textName.setColumns(10);
		
		JLabel lblBehaviour = new JLabel("Comportamento:");
		lblBehaviour.setBounds(12, 77, 268, 20);
		panelUserInput.add(lblBehaviour);
		
		JComboBox comboBehaviour = new JComboBox();
		comboBehaviour.setBounds(12, 97, 149, 20);
		panelUserInput.add(comboBehaviour);
		
		JButton btnIniciar = new JButton("Iniciar");
		btnIniciar.setBackground(new Color(0, 128, 128));
		btnIniciar.setForeground(new Color(255, 255, 255));
		btnIniciar.setBounds(12, 153, 149, 25);
		panelUserInput.add(btnIniciar);

	}
}
