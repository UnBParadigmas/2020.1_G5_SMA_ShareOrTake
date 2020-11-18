package graphics;

import javax.swing.JPanel;
import java.awt.SystemColor;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;

import java.awt.Font;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JSeparator;
import javax.swing.JSlider;

public class SideMenu extends JPanel {
	private static final long serialVersionUID = 1525206838913953208L;

	JLabel lblTitle = new JLabel("Share or Take?");
	JButton btnStart = new JButton("Início");
	JButton btnRestart = new JButton("Reiniciar");
	JPanel panelSlider = new JPanel();
	JSlider sliderVelocity = new JSlider();
	JLabel lblVelocity = new JLabel("Velocidade");
	JLabel lblSliderVelocity = new JLabel();
	
	
	public SideMenu() {
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
		
		btnStart.setForeground(new Color(255, 255, 255));
		btnStart.setBackground(new Color(47, 79, 79));
		btnStart.setBounds(0, 100, 236, 40);
		add(btnStart);
		
		btnRestart.setForeground(Color.WHITE);
		btnRestart.setBackground(new Color(47, 79, 79));
		btnRestart.setBounds(0, 145, 236, 40);
		add(btnRestart);
		
		JSeparator separator_2 = new JSeparator();
		separator_2.setBackground(Color.WHITE);
		separator_2.setBounds(0, 208, 237, 2);
		add(separator_2);
		
		panelSlider.setBackground(new Color(0, 128, 128));
		panelSlider.setBounds(0, 225, 237, 80);
		add(panelSlider);
		panelSlider.setLayout(null);
		
		lblVelocity.setForeground(new Color(255, 255, 255));
		lblVelocity.setBounds(12, 12, 213, 28);
		panelSlider.add(lblVelocity);
		
		sliderVelocity.setBounds(12, 52, 170, 16);
		panelSlider.add(sliderVelocity);
		sliderVelocity.setForeground(new Color(255, 255, 255));
		sliderVelocity.setBackground(new Color(0, 128, 128));
		sliderVelocity.setValue(1);
		sliderVelocity.setMaximum(10);
		sliderVelocity.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                velocityChanged();
            }
        });
		
		lblSliderVelocity.setForeground(new Color(255, 255, 255));
		lblSliderVelocity.setBounds(188, 52, 37, 16);
		lblSliderVelocity.setText(Integer.toString(sliderVelocity.getValue()));
		panelSlider.add(lblSliderVelocity);
	}
	
	void velocityChanged() {
		lblSliderVelocity.setText( Integer.toString( sliderVelocity.getValue() ) );
    }
}
