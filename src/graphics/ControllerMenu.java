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
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JCheckBox;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class ControllerMenu extends JPanel {
	private static final long serialVersionUID = 1525206838913953208L;

	JLabel lblTitle = new JLabel("Share or Take?");
	JButton btnStart = new JButton("Iniciar");
	JPanel panelSlider = new JPanel();
	JSlider sliderVelocity = new JSlider();
	JLabel lblVelocity = new JLabel("Velocidade");
	JLabel lblSliderVelocity = new JLabel();
	private final JLabel lblSpecies = new JLabel("Espécies:");
	private final JCheckBox chckbxDove = new JCheckBox("Dove (Amigavel)");
	private final JCheckBox chckbxHawk = new JCheckBox("Hawk (Agressivo)");
	private final JSpinner spinnerCreaturesAmount = new JSpinner();
	private final JLabel lblCreaturesAmount = new JLabel("Quantidade de criaturas:");
	private final JSpinner spinnerFoodAmount = new JSpinner();
	private final JLabel lblFoodAmount = new JLabel("Quantidade de comida:");

	private EnvironmentAgent environment;
	private int boardSize = 0;
	private boolean simulationRunning = false;

	public ControllerMenu(EnvironmentAgent environment, int boardSize) {
		this.environment = environment;
		this.boardSize = boardSize;

		setBackground(new Color(0, 128, 128));
		setBounds(100, 100, 237, 494);
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

		chckbxDove.setForeground(new Color(47, 79, 79));
		chckbxDove.setBackground(new Color(0, 128, 128));
		chckbxDove.setBounds(12, 119, 212, 23);
		add(chckbxDove);

		chckbxHawk.setForeground(new Color(47, 79, 79));
		chckbxHawk.setBackground(new Color(0, 128, 128));
		chckbxHawk.setBounds(12, 146, 212, 23);
		add(chckbxHawk);

		lblCreaturesAmount.setForeground(Color.WHITE);
		lblCreaturesAmount.setBounds(12, 188, 212, 15);
		add(lblCreaturesAmount);

		spinnerCreaturesAmount.setBounds(12, 213, 69, 20);
		add(spinnerCreaturesAmount);
		spinnerCreaturesAmount.setValue(1);

		lblFoodAmount.setForeground(Color.WHITE);
		lblFoodAmount.setBounds(12, 256, 212, 15);
		add(lblFoodAmount);
		spinnerFoodAmount.setBounds(12, 281, 69, 20);
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

		panelSlider.setBackground(new Color(0, 128, 128));
		panelSlider.setBounds(0, 379, 237, 80);
		panelSlider.setLayout(null);
		add(panelSlider);

		lblVelocity.setForeground(new Color(255, 255, 255));
		lblVelocity.setBounds(12, 12, 213, 28);
		panelSlider.add(lblVelocity);

		sliderVelocity.setBounds(12, 52, 170, 16);
		panelSlider.add(sliderVelocity);
		sliderVelocity.setForeground(new Color(255, 255, 255));
		sliderVelocity.setBackground(new Color(0, 128, 128));
		sliderVelocity.setValue(1);
		sliderVelocity.setMaximum(10);
		sliderVelocity.setEnabled(false);
		sliderVelocity.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				velocityChanged();
			}
		});

		lblSliderVelocity.setForeground(new Color(255, 255, 255));
		lblSliderVelocity.setBounds(188, 52, 37, 16);
		lblSliderVelocity.setText(Integer.toString(sliderVelocity.getValue()));
		panelSlider.add(lblSliderVelocity);
		lblSpecies.setForeground(new Color(255, 255, 255));
		lblSpecies.setBounds(12, 86, 212, 15);
		add(lblSpecies);
	}

	void velocityChanged() {
		lblSliderVelocity.setText(Integer.toString(sliderVelocity.getValue()));
	}
	
	// Adiciona behaviour para iniciar a simulacao
	void startSimulation() {
		List<SpeciesState> species = new ArrayList<>();

		if (chckbxDove.isSelected()) {
			species.add(new SpeciesState("dove", CreatureState.FRIENDLY, "/species_1.png"));
		}
		if (chckbxHawk.isSelected()) {
			species.add(new SpeciesState("hawk", CreatureState.AGGRESSIVE, "/species_2.png"));
		}

		if (chckbxDove.isSelected() || chckbxHawk.isSelected()) {
			componentsState(true);

			environment.addBehaviour(new OneShotBehaviour() {
				private static final long serialVersionUID = 3276741274491102727L;

				public void action() {
					((EnvironmentAgent) myAgent).startSimulation(boardSize, species,
							(int) spinnerCreaturesAmount.getValue(), (int) spinnerFoodAmount.getValue());
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
		chckbxDove.setEnabled(!starting);
		chckbxHawk.setEnabled(!starting);
		spinnerCreaturesAmount.setEnabled(!starting);
		spinnerFoodAmount.setEnabled(!starting);
		handleSliderVelocity(starting);
		btnStart.setText(starting ? "Parar" : "Iniciar");
		this.simulationRunning = starting;
	}
	
	private void handleSliderVelocity(boolean active) {
		sliderVelocity.setEnabled(active);
		sliderVelocity.setValue(!active? 1 : sliderVelocity.getValue());
	}
}
