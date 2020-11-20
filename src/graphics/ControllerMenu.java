package graphics;

import javax.swing.JPanel;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;

import jade.core.behaviours.OneShotBehaviour;
import simulation.creatures.CreatureState;
import simulation.creatures.SpeciesState;
import simulation.environment.EnvironmentAgent;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JSpinner;

public class ControllerMenu extends JPanel {
	private static final long serialVersionUID = 1525206838913953208L;

	JLabel lblTitle = new JLabel("Share or Take?");
	JButton btnStart = new JButton("Iniciar");
	JLabel lblSpecies = new JLabel("Especies:");
	JLabel lblDovesAmount = new JLabel("Qtd de Doves (Amigavel):");
	JSpinner spinnerDovesAmount = new JSpinner();
	JLabel lblHawksAmount = new JLabel("Qtd de Hawks (Agressivo):");
	JSpinner spinnerHawksAmount = new JSpinner();
	JSpinner spinnerFoodAmount = new JSpinner();
	JLabel lblFoodAmount = new JLabel("Quantidade de comida:");
	JLabel lblDays = new JLabel("Dia:");
	JLabel lblDaysCount = new JLabel("0");
	JLabel lblDovesCount = new JLabel("0");
	JLabel lblDoves = new JLabel("Doves:");
	JLabel lblHawks = new JLabel("Hawks:");
	JLabel lblHawksCount = new JLabel("0");
	JPanel panelSliderEnd = new JPanel();
	JLabel lblEnd = new JLabel("FIM");
	JLabel lblRestart = new JLabel("Reinicie a simulacao");

	private EnvironmentAgent environment;
	private int boardSize = 0;
	private boolean simulationRunning = false;
	private boolean alreadyStarted = false;

	public ControllerMenu(EnvironmentAgent environment, int boardSize) {
		this.environment = environment;
		this.boardSize = boardSize;

		setBackground(new Color(0, 128, 128));
		setBounds(100, 100, 237, 665);
		setLayout(null);

		lblTitle.setBounds(12, 12, 212, 45);
		add(lblTitle);
		lblTitle.setFont(new Font("Lato Medium", Font.BOLD, 18));
		lblTitle.setForeground(SystemColor.controlHighlight);
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);

		JSeparator separator_1 = new JSeparator();
		separator_1.setBackground(new Color(255, 255, 255));
		separator_1.setBounds(0, 69, 237, 2);
		add(separator_1);

		lblDovesAmount.setForeground(Color.WHITE);
		lblDovesAmount.setBounds(12, 113, 212, 15);
		add(lblDovesAmount);

		spinnerDovesAmount.setBounds(12, 138, 69, 20);
		spinnerDovesAmount.setValue(0);
		add(spinnerDovesAmount);

		lblHawksAmount.setForeground(Color.WHITE);
		lblHawksAmount.setBounds(12, 170, 212, 15);
		add(lblHawksAmount);

		spinnerHawksAmount.setBounds(12, 195, 69, 20);
		spinnerHawksAmount.setValue(0);
		add(spinnerHawksAmount);

		lblFoodAmount.setForeground(Color.WHITE);
		lblFoodAmount.setBounds(12, 227, 212, 15);
		add(lblFoodAmount);
		spinnerFoodAmount.setBounds(12, 252, 69, 20);
		spinnerFoodAmount.setValue(1);
		add(spinnerFoodAmount);

		btnStart.setForeground(new Color(255, 255, 255));
		btnStart.setBackground(new Color(47, 79, 79));
		btnStart.setBounds(0, 313, 236, 40);
		btnStart.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (simulationRunning) {
					stopSimulation();
				} else {
					startSimulation();
				}
			}
		});
		add(btnStart);

		JSeparator separator_2 = new JSeparator();
		separator_2.setBackground(Color.WHITE);
		separator_2.setBounds(0, 365, 237, 2);
		add(separator_2);

		JPanel panelSliderDays = new JPanel();
		panelSliderDays.setLayout(null);
		panelSliderDays.setBackground(new Color(0, 128, 128));
		panelSliderDays.setBounds(0, 373, 237, 80);
		add(panelSliderDays);
		lblDays.setFont(new Font("Dialog", Font.BOLD, 12));

		lblDays.setForeground(Color.WHITE);
		lblDays.setBounds(12, 12, 57, 15);
		panelSliderDays.add(lblDays);

		lblDaysCount.setFont(new Font("Dialog", Font.BOLD, 12));
		lblDaysCount.setForeground(Color.WHITE);
		lblDaysCount.setBounds(81, 12, 57, 15);
		panelSliderDays.add(lblDaysCount);

		lblDoves.setForeground(Color.WHITE);
		lblDoves.setFont(new Font("Dialog", Font.BOLD, 12));
		lblDoves.setBounds(12, 32, 57, 15);
		panelSliderDays.add(lblDoves);

		lblDovesCount.setForeground(Color.WHITE);
		lblDovesCount.setFont(new Font("Dialog", Font.BOLD, 12));
		lblDovesCount.setBounds(81, 32, 57, 15);
		panelSliderDays.add(lblDovesCount);

		lblHawks.setForeground(Color.WHITE);
		lblHawks.setFont(new Font("Dialog", Font.BOLD, 12));
		lblHawks.setBounds(12, 52, 57, 15);
		panelSliderDays.add(lblHawks);

		lblHawksCount.setForeground(Color.WHITE);
		lblHawksCount.setFont(new Font("Dialog", Font.BOLD, 12));
		lblHawksCount.setBounds(81, 52, 57, 15);
		panelSliderDays.add(lblHawksCount);
		lblSpecies.setForeground(new Color(255, 255, 255));
		lblSpecies.setBounds(12, 86, 212, 15);
		add(lblSpecies);
		panelSliderEnd.setLayout(null);
		panelSliderEnd.setBackground(new Color(0, 128, 128));
		panelSliderEnd.setBounds(0, 559, 237, 80);

		add(panelSliderEnd);
		lblEnd.setFont(new Font("Dialog", Font.BOLD, 18));
		lblEnd.setHorizontalAlignment(SwingConstants.CENTER);
		lblEnd.setForeground(new Color(255, 0, 0));
		lblEnd.setBounds(12, 0, 213, 28);

		panelSliderEnd.add(lblEnd);
		lblRestart.setHorizontalAlignment(SwingConstants.CENTER);
		lblRestart.setForeground(Color.WHITE);
		lblRestart.setFont(new Font("Dialog", Font.BOLD, 10));
		lblRestart.setBounds(12, 40, 213, 14);

		panelSliderEnd.add(lblRestart);
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		lblDaysCount.setText(environment.daysCount.toString());
		lblDovesCount.setText(environment.dovesCount.toString());
		lblHawksCount.setText(environment.hawksCount.toString());
		componentsState(environment.running);
	}

	// Adiciona behaviour para iniciar a simulacao
	void startSimulation() {
		List<SpeciesState> species = new ArrayList<>();
		this.alreadyStarted = true;

		if ((int) spinnerDovesAmount.getValue() > 0) {
			species.add(new SpeciesState(EnvironmentAgent.DOVE_NAME, (int) spinnerDovesAmount.getValue(),
					CreatureState.FRIENDLY, "/species_1.png"));
		}
		if ((int) spinnerHawksAmount.getValue() > 0) {
			species.add(new SpeciesState(EnvironmentAgent.HAWK_NAME, (int) spinnerHawksAmount.getValue(),
					CreatureState.AGGRESSIVE, "/species_2.png"));
		}

		if (!species.isEmpty()) {
			componentsState(true);

			environment.addBehaviour(new OneShotBehaviour() {
				private static final long serialVersionUID = 3276741274491102727L;

				public void action() {
					((EnvironmentAgent) myAgent).startSimulation(boardSize, species,
							(int) spinnerFoodAmount.getValue());
				}
			});
		}
	}

	void stopSimulation() {
		componentsState(false);

		environment.addBehaviour(new OneShotBehaviour() {
			private static final long serialVersionUID = 3276741274491102727L;

			public void action() {
				((EnvironmentAgent) myAgent).stopSimulation();
			}
		});
	}

	void componentsState(boolean starting) {
		spinnerDovesAmount.setEnabled(!starting);
		spinnerHawksAmount.setEnabled(!starting);
		spinnerFoodAmount.setEnabled(!starting);
		btnStart.setText(starting ? "Parar" : "Iniciar");
		panelSliderEnd.setVisible(alreadyStarted && !starting);
		this.simulationRunning = starting;
	}
}
