public class Main {

	NeuralNetwork nnDM;
	NeuralNetwork nnMan;

	public static void main(String[] args) {
		Main main = new Main();
		main.run();
	}

	public void run() {
//		RocketGame r = new RocketGame();
		ResourcePool.outputMode = ResourcePool.outputProject;
		Controller c = new Controller();
		c.run();
	}

}
