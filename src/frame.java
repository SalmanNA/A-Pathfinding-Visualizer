import javax.swing.JFrame;

public class frame extends JFrame{
	Map canvas = new Map();
	public frame() {
		this.setSize(1000,1030);
		this.add(canvas);
		this.setResizable(false);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}
}
