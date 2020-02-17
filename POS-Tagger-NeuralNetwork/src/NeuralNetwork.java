
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import org.jblas.DoubleMatrix;

public class NeuralNetwork implements Runnable {

	private static final long serialVersionUID = 1L;

	// private DoubleMatrix yHat;

	protected int[] neuronNumbers;
	protected List<DoubleMatrix> weights;
	protected List<DoubleMatrix> biases;
	protected List<DoubleMatrix> a;
	protected List<WordIns> x;
	protected List<DoubleMatrix> y;
	protected DoubleMatrix w1;
	protected DoubleMatrix w2;
	protected DoubleMatrix w3;
	protected DoubleMatrix w4;
	protected DoubleMatrix w5;
	protected BufferedWriter bw;
	protected long time;

	DoubleMatrix b1;
	DoubleMatrix b2;
	DoubleMatrix b3;
	DoubleMatrix b4;
	DoubleMatrix b5;

	protected boolean isValidating = true;
	protected double learningRate = 1;
	protected int noOfLayers = 0;
	protected List<List<double[]>> ans;
	protected List<List<double[]>> quests;

	protected boolean isShowCost = true;
	protected String ID;
	protected boolean shouldLoop = true;
	protected CountDownLatch latch;
	protected int times = 0;
	protected double lamda = 0;
	protected Draw f;
	protected DrawNetwork draw;
	protected NeuralNetwork n;
	protected int trainingTickRate = 1000;
	protected int correctTickRate = 200;
	protected Graph graph;
	protected TextArea output;
	protected JTextArea scaleX;
	protected JTextArea scaleY;
	protected JTextArea learningRateText;
	protected JTextArea lamdaText;
	protected BufferedImage img;

	protected static String[] LEGEND = { "CC", "CD", "DT", "EX", "FW", "IN", "JJ", "JJR", "JJS", "LS", "MD", "NN",
			"NNS", "NNP", "NNPS", "PDT", "POS", "PRP", "PRP$", "RB", "RBR", "RBS", "RP", "SYM", "TO", "UH", "VB", "VBD",
			"VBG", "VBN", "VBP", "VBZ", "WDT", "WP", "WP$", "WRB" };

	public NeuralNetwork(List<DoubleMatrix> weights, List<DoubleMatrix> biases, int[] neuronNumbers,
			double learningRate, double lamda, List<DoubleMatrix> trainingData, List<DoubleMatrix> trainingAns,
			List<DoubleMatrix> validationData, List<DoubleMatrix> validationAns, String ID) {

		noOfLayers = neuronNumbers.length;
		this.n = this;
		this.ID = ID;
		this.lamda = lamda;
		this.neuronNumbers = neuronNumbers;
		this.weights = weights;
		this.biases = biases;
		this.learningRate = learningRate;

		if (neuronNumbers.length == 4) {
			w1 = weights.get(0);
			w2 = weights.get(1);
			w3 = weights.get(2);

			b1 = biases.get(0);
			b2 = biases.get(1);
			b3 = biases.get(2);
		}

		else if (neuronNumbers.length == 5) {
			w1 = weights.get(0);
			w2 = weights.get(1);
			w3 = weights.get(2);
			w4 = weights.get(3);

			b1 = biases.get(0);
			b2 = biases.get(1);
			b3 = biases.get(2);
			b4 = biases.get(3);
		}

		init();
	}

	public NeuralNetwork(List<DoubleMatrix> weights, List<DoubleMatrix> biases, int[] neuronNumbers,
			List<DoubleMatrix> trainingData, List<DoubleMatrix> trainingAns, List<DoubleMatrix> validationData,
			List<DoubleMatrix> validationAns, String ID) {

		noOfLayers = neuronNumbers.length;
		this.ID = ID;
		this.n = this;
		this.neuronNumbers = neuronNumbers;
		this.weights = weights;
		this.biases = biases;
		init();

		if (neuronNumbers.length == 4) {
			w1 = weights.get(0);
			w2 = weights.get(1);
			w3 = weights.get(2);

			b1 = biases.get(0);
			b2 = biases.get(1);
			b3 = biases.get(2);
		}

		else if (neuronNumbers.length == 5) {
			w1 = weights.get(0);
			w2 = weights.get(1);
			w3 = weights.get(2);
			w4 = weights.get(3);

			b1 = biases.get(0);
			b2 = biases.get(1);
			b3 = biases.get(2);
			b4 = biases.get(3);
		}

		train(times);
		if (shouldLoop) {
			runLoop();
		}

	}

	// public void loop() {
	// Scanner s = new Scanner(System.in);
	// String text = "";
	//
	// while (1 < 3) {
	//
	// System.out.println("enter text to check");
	// if ((text = s.nextLine()).equalsIgnoreCase("end")) {
	// s.close();
	// System.out.println("close called");
	// break;
	// } else {
	// List<String> list = new ArrayList<String>();
	// list.add(text);
	// Pair res = new Controller().getWordIns(list);
	// List<WordIns> wordIns = res.getWordIns();
	// List<double[]> answers = res.getAnswers();
	//
	// DoubleMatrix input;
	// DoubleMatrix yHat;
	// System.out.println("Word ----- pred ans ------ real ans");
	// for (int i = 0; i < wordIns.size(); i++) {
	//
	// input = new
	// DoubleMatrix(wordIns.get(i).getComponentArray().getArray()).transpose();
	// yHat = run(input);
	//
	// updateDrawArea(input, yHat, wordIns.get(i).getText(),
	// NeuralNetwork.outputDecoder(answers.get(i)));
	//
	// System.out.print(wordIns.get(i).getText() + "-----");
	// System.out.print(outputDecoder(yHat.toArray()) + "-----");
	// System.out.println(outputDecoder(answers.get(i)));
	// }
	// }
	// }
	// }

	public NeuralNetwork(List<DoubleMatrix> weights, List<DoubleMatrix> biases, int[] neuronNumbers, String ID) {

		this.ID = ID;
		this.n = this;
		this.neuronNumbers = neuronNumbers;
		this.weights = weights;
		this.biases = biases;
		noOfLayers = neuronNumbers.length;

		init();

		if (neuronNumbers.length == 4) {
			w1 = weights.get(0);
			w2 = weights.get(1);
			w3 = weights.get(2);

			b1 = biases.get(0);
			b2 = biases.get(1);
			b3 = biases.get(2);
		}

		if (neuronNumbers.length == 5) {
			w1 = weights.get(0);
			w2 = weights.get(1);
			w3 = weights.get(2);
			w4 = weights.get(3);

			b1 = biases.get(0);
			b2 = biases.get(1);
			b3 = biases.get(2);
			b4 = biases.get(3);
		}

//		loop();
	}

	public NeuralNetwork(int[] neuronNumbers, List<DoubleMatrix> weights, List<DoubleMatrix> biases,
			double learningRate, double lamda, List<WordIns> questions, List<DoubleMatrix> answers, String id) {
		this.weights = weights;
		this.biases = biases;
		this.x = questions;
		this.y = answers;
		this.ID = id;
		this.neuronNumbers = neuronNumbers;
		this.learningRate = learningRate;
		this.lamda = lamda;
		this.noOfLayers = neuronNumbers.length;
		this.n = this;

		if (neuronNumbers.length == 4) {
			w1 = weights.get(0);
			w2 = weights.get(1);
			w3 = weights.get(2);

			b1 = biases.get(0);
			b2 = biases.get(1);
			b3 = biases.get(2);
		} else if (neuronNumbers.length == 5) {
			w1 = weights.get(0);
			w2 = weights.get(1);
			w3 = weights.get(2);
			w4 = weights.get(3);

			b1 = biases.get(0);
			b2 = biases.get(1);
			b3 = biases.get(2);
			b4 = biases.get(3);
		}

		init();

	}

	public NeuralNetwork(int[] neuronNumbers, double learningRate, double lamda, List<WordIns> questions,
			List<DoubleMatrix> answers, String ID) {
		weights = new ArrayList<DoubleMatrix>();
		biases = new ArrayList<DoubleMatrix>();
		this.y = answers;
		this.x = questions;

		noOfLayers = neuronNumbers.length;

		System.out.println(noOfLayers);

		if (noOfLayers == 4) {
			w1 = DoubleMatrix.randn(neuronNumbers[0], neuronNumbers[1]);
			w2 = DoubleMatrix.randn(neuronNumbers[1], neuronNumbers[2]);
			w3 = DoubleMatrix.rand(neuronNumbers[2], neuronNumbers[3]);
			b1 = DoubleMatrix.rand(neuronNumbers[1]);
			b2 = DoubleMatrix.rand(neuronNumbers[2]);
			b3 = DoubleMatrix.rand(neuronNumbers[3]);

			weights.add(w1);
			weights.add(w2);
			weights.add(w3);
			biases.add(b1);
			biases.add(b2);
			biases.add(b3);
		}

		else if (noOfLayers == 5) {
			w1 = DoubleMatrix.randn(neuronNumbers[0], neuronNumbers[1]);
			w2 = DoubleMatrix.randn(neuronNumbers[1], neuronNumbers[2]);
			w3 = DoubleMatrix.rand(neuronNumbers[2], neuronNumbers[3]);
			w4 = DoubleMatrix.rand(neuronNumbers[3], neuronNumbers[4]);

			b1 = DoubleMatrix.rand(neuronNumbers[1]);
			b2 = DoubleMatrix.rand(neuronNumbers[2]);
			b3 = DoubleMatrix.rand(neuronNumbers[3]);
			b4 = DoubleMatrix.rand(neuronNumbers[4]);

			weights.add(w1);
			weights.add(w2);
			weights.add(w3);
			weights.add(w4);
			biases.add(b1);
			biases.add(b2);
			biases.add(b3);
			biases.add(b4);
		}

		this.ID = ID;
		this.lamda = lamda;
		this.neuronNumbers = neuronNumbers;
		this.learningRate = learningRate;
		this.n = this;

		init();

		if (neuronNumbers.length == 4) {
			w1 = weights.get(0);
			w2 = weights.get(1);
			w3 = weights.get(2);

			b1 = biases.get(0);
			b2 = biases.get(1);
			b3 = biases.get(2);
		} else if (neuronNumbers.length == 5) {
			w1 = weights.get(0);
			w2 = weights.get(1);
			w3 = weights.get(2);
			w4 = weights.get(3);

			b1 = biases.get(0);
			b2 = biases.get(1);
			b3 = biases.get(2);
			b4 = biases.get(3);
		}

		train(times);
		if (shouldLoop) {
			runLoop();
		}
	}

	public NeuralNetwork(List<DoubleMatrix> weights, List<DoubleMatrix> biases, int[] neuronNumbers,
			double learningRate, double lamda, List<DoubleMatrix> trainingData, List<DoubleMatrix> trainingAns,
			List<DoubleMatrix> validationData, List<DoubleMatrix> validationAns, boolean shouldLoop, String ID) {

		noOfLayers = neuronNumbers.length;

		this.ID = ID;
		this.lamda = lamda;
		this.shouldLoop = shouldLoop;
		this.neuronNumbers = neuronNumbers;
		this.n = this;
		this.weights = weights;
		this.biases = biases;
		this.learningRate = learningRate;

		init();

		if (neuronNumbers.length == 4) {
			w1 = weights.get(0);
			w2 = weights.get(1);
			w3 = weights.get(2);

			b1 = biases.get(0);
			b2 = biases.get(1);
			b3 = biases.get(2);
		}

		else if (neuronNumbers.length == 5) {
			w1 = weights.get(0);
			w2 = weights.get(1);
			w3 = weights.get(2);
			w4 = weights.get(3);

			b1 = biases.get(0);
			b2 = biases.get(1);
			b3 = biases.get(2);
			b4 = biases.get(3);
		}

		train(times);
		if (shouldLoop) {
			runLoop();
		}

	}

	public NeuralNetwork(int[] neuronNumbers, double learningRate, double lamda, List<DoubleMatrix> trainingData,
			List<DoubleMatrix> trainingAns, List<DoubleMatrix> validationData, List<DoubleMatrix> validationAns,
			boolean shouldLoop, String ID) {

		this.ID = ID;
		this.lamda = lamda;
		this.n = this;
		this.shouldLoop = shouldLoop;
		this.neuronNumbers = neuronNumbers;
		this.learningRate = learningRate;

		noOfLayers = neuronNumbers.length;

		if (noOfLayers == 4) {
			w1 = DoubleMatrix.randn(neuronNumbers[0], neuronNumbers[1]);
			w2 = DoubleMatrix.randn(neuronNumbers[1], neuronNumbers[2]);
			w3 = DoubleMatrix.rand(neuronNumbers[2], neuronNumbers[3]);
			b1 = DoubleMatrix.rand(neuronNumbers[1]);
			b2 = DoubleMatrix.rand(neuronNumbers[2]);
			b3 = DoubleMatrix.rand(neuronNumbers[3]);

			weights.add(w1);
			weights.add(w2);
			weights.add(w3);
			biases.add(b1);
			biases.add(b2);
			biases.add(b3);
		}

		if (noOfLayers == 5) {
			w1 = DoubleMatrix.randn(neuronNumbers[0], neuronNumbers[1]);
			w2 = DoubleMatrix.randn(neuronNumbers[1], neuronNumbers[2]);
			w3 = DoubleMatrix.rand(neuronNumbers[2], neuronNumbers[3]);
			w4 = DoubleMatrix.rand(neuronNumbers[3], neuronNumbers[4]);

			b1 = DoubleMatrix.rand(neuronNumbers[1]);
			b2 = DoubleMatrix.rand(neuronNumbers[2]);
			b3 = DoubleMatrix.rand(neuronNumbers[3]);
			b4 = DoubleMatrix.rand(neuronNumbers[4]);

			weights.add(w1);
			weights.add(w2);
			weights.add(w3);
			weights.add(w4);
			biases.add(b1);
			biases.add(b2);
			biases.add(b3);
			biases.add(b4);
		}

		init();

		if (neuronNumbers.length == 4) {
			w1 = weights.get(0);
			w2 = weights.get(1);
			w3 = weights.get(2);

			b1 = biases.get(0);
			b2 = biases.get(1);
			b3 = biases.get(2);
		} else if (neuronNumbers.length == 5) {
			w1 = weights.get(0);
			w2 = weights.get(1);
			w3 = weights.get(2);
			w4 = weights.get(3);

			b1 = biases.get(0);
			b2 = biases.get(1);
			b3 = biases.get(2);
			b4 = biases.get(3);
		}

		train(times);
		if (shouldLoop) {
			runLoop();
		}
	}

	public NeuralNetwork(int times, int[] neuronNumbers, double learningRate, double lamda,
			List<DoubleMatrix> trainingData, List<DoubleMatrix> trainingAns, List<DoubleMatrix> validationData,
			List<DoubleMatrix> validationAns, String ID) {

		weights = new ArrayList<DoubleMatrix>();
		noOfLayers = neuronNumbers.length;
		biases = new ArrayList<DoubleMatrix>();

		this.ID = ID;
		this.lamda = lamda;
		this.times = times;
		this.n = this;
		this.neuronNumbers = neuronNumbers;
		this.learningRate = learningRate;

		if (noOfLayers == 4) {
			w1 = DoubleMatrix.randn(neuronNumbers[0], neuronNumbers[1]);
			w2 = DoubleMatrix.randn(neuronNumbers[1], neuronNumbers[2]);
			w3 = DoubleMatrix.rand(neuronNumbers[2], neuronNumbers[3]);
			b1 = DoubleMatrix.rand(neuronNumbers[1]);
			b2 = DoubleMatrix.rand(neuronNumbers[2]);
			b3 = DoubleMatrix.rand(neuronNumbers[3]);

			weights.add(w1);
			weights.add(w2);
			weights.add(w3);
			biases.add(b1);
			biases.add(b2);
			biases.add(b3);
		}

		if (noOfLayers == 5) {
			w1 = DoubleMatrix.randn(neuronNumbers[0], neuronNumbers[1]);
			w2 = DoubleMatrix.randn(neuronNumbers[1], neuronNumbers[2]);
			w3 = DoubleMatrix.rand(neuronNumbers[2], neuronNumbers[3]);
			w4 = DoubleMatrix.rand(neuronNumbers[3], neuronNumbers[4]);

			b1 = DoubleMatrix.rand(neuronNumbers[1]);
			b2 = DoubleMatrix.rand(neuronNumbers[2]);
			b3 = DoubleMatrix.rand(neuronNumbers[3]);
			b4 = DoubleMatrix.rand(neuronNumbers[4]);

			weights.add(w1);
			weights.add(w2);
			weights.add(w3);
			weights.add(w4);
			biases.add(b1);
			biases.add(b2);
			biases.add(b3);
			biases.add(b4);
		}

		init();

		if (neuronNumbers.length == 4) {
			w1 = weights.get(0);
			w2 = weights.get(1);
			w3 = weights.get(2);

			b1 = biases.get(0);
			b2 = biases.get(1);
			b3 = biases.get(2);
		} else if (neuronNumbers.length == 5) {
			w1 = weights.get(0);
			w2 = weights.get(1);
			w3 = weights.get(2);
			w4 = weights.get(3);

			b1 = biases.get(0);
			b2 = biases.get(1);
			b3 = biases.get(2);
			b4 = biases.get(3);
		}
		train(times);
		if (shouldLoop) {
			runLoop();
		}
	}

	// This is for multithreading
	public NeuralNetwork(int times, List<DoubleMatrix> weights, List<DoubleMatrix> biases, int[] neuronNumbers,
			double learningRate, double lamda, List<DoubleMatrix> trainingData, List<DoubleMatrix> trainingAns,
			List<DoubleMatrix> validationData, List<DoubleMatrix> validationAns, boolean shouldLoop, String ID,
			CountDownLatch latch) {

		this.ID = ID;
		this.lamda = lamda;
		this.times = times;
		this.neuronNumbers = neuronNumbers;
		this.weights = weights;
		this.n = this;
		this.biases = biases;
		this.learningRate = learningRate;
		this.shouldLoop = shouldLoop;
		this.latch = latch;

	}

	public NeuralNetwork(int times, int[] neuronNumbers, double learningRate, double lamda,
			List<DoubleMatrix> trainingData, List<DoubleMatrix> trainingAns, List<DoubleMatrix> validationData,
			List<DoubleMatrix> validationAns, boolean shouldLoop, String ID, CountDownLatch latch) {

		noOfLayers = neuronNumbers.length;

		if (noOfLayers == 4) {
			w1 = DoubleMatrix.randn(neuronNumbers[0], neuronNumbers[1]);
			w2 = DoubleMatrix.randn(neuronNumbers[1], neuronNumbers[2]);
			w3 = DoubleMatrix.rand(neuronNumbers[2], neuronNumbers[3]);
			b1 = DoubleMatrix.rand(neuronNumbers[1]);
			b2 = DoubleMatrix.rand(neuronNumbers[2]);
			b3 = DoubleMatrix.rand(neuronNumbers[3]);

			weights.add(w1);
			weights.add(w2);
			weights.add(w3);
			biases.add(b1);
			biases.add(b2);
			biases.add(b3);
		}

		if (noOfLayers == 5) {
			w1 = DoubleMatrix.randn(neuronNumbers[0], neuronNumbers[1]);
			w2 = DoubleMatrix.randn(neuronNumbers[1], neuronNumbers[2]);
			w3 = DoubleMatrix.rand(neuronNumbers[2], neuronNumbers[3]);
			w4 = DoubleMatrix.rand(neuronNumbers[3], neuronNumbers[4]);

			b1 = DoubleMatrix.rand(neuronNumbers[1]);
			b2 = DoubleMatrix.rand(neuronNumbers[2]);
			b3 = DoubleMatrix.rand(neuronNumbers[3]);
			b4 = DoubleMatrix.rand(neuronNumbers[4]);

			weights.add(w1);
			weights.add(w2);
			weights.add(w3);
			weights.add(w4);
			biases.add(b1);
			biases.add(b2);
			biases.add(b3);
			biases.add(b4);
		}

		this.ID = ID;
		this.lamda = lamda;
		this.times = times;
		this.neuronNumbers = neuronNumbers;
		this.n = this;
		this.learningRate = learningRate;
		this.shouldLoop = shouldLoop;
		this.latch = latch;

	}

	public NeuralNetwork(List<DoubleMatrix> weights, List<DoubleMatrix> biases, int[] neuronNumbers,
			double learningRate, double lamda, List<DoubleMatrix> trainingData, List<DoubleMatrix> trainingAns,
			List<DoubleMatrix> validationData, List<DoubleMatrix> validationAns, boolean shouldLoop, String ID,
			CountDownLatch latch) {

		this.ID = ID;
		this.times = 1;
		this.lamda = lamda;
		this.n = this;
		this.neuronNumbers = neuronNumbers;
		this.weights = weights;
		this.biases = biases;
		this.learningRate = learningRate;
		this.shouldLoop = shouldLoop;
		this.latch = latch;

	}

	public NeuralNetwork(int[] neuronNumbers, double learningRate, double lamda, List<DoubleMatrix> trainingData,
			List<DoubleMatrix> trainingAns, List<DoubleMatrix> validationData, List<DoubleMatrix> validationAns,
			boolean shouldLoop, String ID, CountDownLatch latch) {

		noOfLayers = neuronNumbers.length;

		if (noOfLayers == 4) {
			w1 = DoubleMatrix.randn(neuronNumbers[0], neuronNumbers[1]);
			w2 = DoubleMatrix.randn(neuronNumbers[1], neuronNumbers[2]);
			w3 = DoubleMatrix.rand(neuronNumbers[2], neuronNumbers[3]);
			b1 = DoubleMatrix.rand(neuronNumbers[1]);
			b2 = DoubleMatrix.rand(neuronNumbers[2]);
			b3 = DoubleMatrix.rand(neuronNumbers[3]);

			weights.add(w1);
			weights.add(w2);
			weights.add(w3);
			biases.add(b1);
			biases.add(b2);
			biases.add(b3);
		}

		if (noOfLayers == 5) {
			w1 = DoubleMatrix.randn(neuronNumbers[0], neuronNumbers[1]);
			w2 = DoubleMatrix.randn(neuronNumbers[1], neuronNumbers[2]);
			w3 = DoubleMatrix.rand(neuronNumbers[2], neuronNumbers[3]);
			w4 = DoubleMatrix.rand(neuronNumbers[3], neuronNumbers[4]);

			b1 = DoubleMatrix.rand(neuronNumbers[1]);
			b2 = DoubleMatrix.rand(neuronNumbers[2]);
			b3 = DoubleMatrix.rand(neuronNumbers[3]);
			b4 = DoubleMatrix.rand(neuronNumbers[4]);

			weights.add(w1);
			weights.add(w2);
			weights.add(w3);
			weights.add(w4);
			biases.add(b1);
			biases.add(b2);
			biases.add(b3);
			biases.add(b4);
		}

		this.ID = ID;
		this.times = 1;
		this.lamda = lamda;
		this.neuronNumbers = neuronNumbers;
		this.learningRate = learningRate;
		this.shouldLoop = shouldLoop;
		this.latch = latch;
		this.n = this;

		init();

	}

	public void run() {
		init();
		noOfLayers = neuronNumbers.length;

		if (neuronNumbers.length == 4) {
			w1 = weights.get(0);
			w2 = weights.get(1);
			w3 = weights.get(2);

			b1 = biases.get(0);
			b2 = biases.get(1);
			b3 = biases.get(2);
		} else if (neuronNumbers.length == 5) {
			w1 = weights.get(0);
			w2 = weights.get(1);
			w3 = weights.get(2);
			w4 = weights.get(3);

			b1 = biases.get(0);
			b2 = biases.get(1);
			b3 = biases.get(2);
			b4 = biases.get(3);
		}

		if (shouldLoop) {
			runLoop();
		} else {
			train(times);
			System.out.println(ID + " finished....");
			latch.countDown();
		}
	}

	public void init() {
		time = System.currentTimeMillis();
		bw = IOUtils.openBw(IOUtils.getFileFromResourcesFolder("Comments").getPath(), true);

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				initFrame();
			}
		});
	}

	public List<DoubleMatrix> getWeights() {
		return weights;
	}

	public List<DoubleMatrix> getBiases() {
		return biases;
	}

	public void save() {

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd--HH-mm-ss");
		String date = dtf.format(LocalDateTime.now());

		if (noOfLayers == 4) {
			double[][] weight1 = w1.toArray2();
			double[][] weight2 = w2.toArray2();
			double[][] weight3 = w3.toArray2();
			double[][] bias1 = b1.toArray2();
			double[][] bias2 = b2.toArray2();
			double[][] bias3 = b3.toArray2();

//			URL path = ClassLoader.getSystemClassLoader().getResource("Data/DataAns0.0");
//			File file = new File(path.toString() + "WCW1" + date);
//			System.out.println(path.toString());

			IOUtils.write2dArrayToFile(ResourcePool.outputMode + "WordCategoryWeights/wordCategoriser" + "W1" + ID + "_" + date,
					weight1);
			IOUtils.write2dArrayToFile(ResourcePool.outputMode + "WordCategoryWeights/wordCategoriser" + "W2" + ID + "_" + date,
					weight2);
			IOUtils.write2dArrayToFile(ResourcePool.outputMode + "WordCategoryWeights/wordCategoriser" + "W3" + ID + "_" + date,
					weight3);
			IOUtils.write2dArrayToFile(ResourcePool.outputMode + "WordCategoryWeights/wordCategoriser" + "B1" + ID + "_" + date, bias1);
			IOUtils.write2dArrayToFile(ResourcePool.outputMode + "WordCategoryWeights/wordCategoriser" + "B2" + ID + "_" + date, bias2);
			IOUtils.write2dArrayToFile(ResourcePool.outputMode + "WordCategoryWeights/wordCategoriser" + "B3" + ID + "_" + date, bias3);

			BufferedWriter bw = IOUtils
					.openBw(ResourcePool.outputMode + "WordCategoryWeights/wordCategoriser" + "SA" + ID + "_" + date);
			try {
				bw.write("scaleX1;" + graph.getScaleX());
				bw.newLine();
				bw.write("scaleX2;" + graph.getScaleX2());
				bw.newLine();
				bw.write("scaleY1;" + graph.getScaleY());
				bw.newLine();
				bw.write("scaleY2;" + graph.getScaleY2());
				bw.newLine();
				bw.write("learningRate;" + learningRate);
				bw.newLine();
				bw.write("lamda;" + lamda);
				bw.flush();
				bw.close();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				output.append("error while writing SA file \n");
				e.printStackTrace();
			}
		}
		output.append("successfully saved \n");
	}

	public void initFrame() {

		int height = 700;
		int width = 1400;

		img = null;
		ImageIcon icon = null;
		Dimension d;

		try {
			img = ImageIO.read(IOUtils.getFileFromResourcesFolder("cool-dark-background-wallpapersafari-19.jpeg"));
			icon = new ImageIcon(
					IOUtils.getFileFromResourcesFolder("cool-dark-background-wallpapersafari-19.jpeg").getPath());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		MainFrame frame = new MainFrame("Word Categoriser ver-0.1", img);

		frame.setCursor(new Cursor(2));
		frame.setSize(width, height);

		LeftPane lPanel = new LeftPane((int) (width * 0.25), height);
		RightPane rPanel = new RightPane((int) (width * 0.25), height);
		f = new Draw((int) (width * 0.5), height);
		f.init(neuronNumbers);

//		String[] id = { "inputNeuron", "h1Neuron", "h2Neuron", "outputNeuron" };
//		draw = new DrawNetwork(neuronNumbers, id);
//
//		draw.setPreferredSize(new Dimension((int) (width * 0.5), height));
//		draw.setMaximumSize(new Dimension((int) (width * 0.5), height));
//		draw.setMinimumSize(new Dimension((int) (width * 0.5), height));

		Container c = frame.getContentPane();
		frame.setLayout(new BorderLayout());

		c.add(lPanel, BorderLayout.WEST);
		c.add(rPanel, BorderLayout.EAST);
		c.add(f, BorderLayout.CENTER);

		// GridBagConstraints g = new GridBagConstraints();
		//
		// MainPanel panel = new MainPanel(img);
		// JButton train = new JButton("TRAIN");
		// JButton validate = new JButton("VALIDATE");
		// JButton exit = new JButton("STOP");
		// JTextArea lamdaText = new JTextArea();
		// JButton subLamda = new JButton("LAMDA");
		// JTextArea learningRateText = new JTextArea();
		// JButton subLearningRate = new JButton("LEARNING RATE");
		// JButton save = new JButton("SAVE");
		// JButton load = new JButton("LOAD");
		// JButton subScaleY = new JButton("SCALE Y");
		// scaleY = new JTextArea();
		// JTextArea trainingRate = new JTextArea();
		// JButton subTrainingRate = new JButton("TICK RATE");
		// scaleX = new JTextArea();
		// JButton subScaleX = new JButton("SCALE X");
		// JTextArea input = new JTextArea();
		// output = new TextArea(img);
		// JButton subInput = new JButton("INPUT");
		// JScrollPane inputScroll = new JScrollPane(input);
		// JScrollPane outputScroll = new JScrollPane(output);
		// JButton openNew = new JButton("OPEN NEW");
		// JButton toggle = new JButton("TOGGLE GRPAHS");
		//
		//// graph = new Graph((int) (width * 0.25), (int) (height *
		// 0.5),correctTickRate);
		//
		// panel.setLayout(new GridBagLayout());
		//
		//// panel.setPreferredSize(new Dimension(width, height));
		// // panel.setMaximumSize(new Dimension(width,height));
		// // panel.setMinimumSize(new Dimension(width,height));
		//
		// lamdaText.setPreferredSize(new Dimension((int) (width * 0.25 * 0.5),
		// (int) (height * 0.1)));
		// lamdaText.setMaximumSize(new Dimension((int) (width * 0.25 * 0.5),
		// (int) (height * 0.1)));
		// lamdaText.setMinimumSize(new Dimension((int) (width * 0.25 * 0.5),
		// (int) (height * 0.1)));
		//
		// save.setPreferredSize(new Dimension((int) (width * 0.25), (int)
		// (height * 0.1)));
		// save.setMaximumSize(new Dimension((int) (width * 0.25), (int) (height
		// * 0.1)));
		// save.setMinimumSize(new Dimension((int) (width * 0.25), (int) (height
		// * 0.1)));
		//
		// toggle.setPreferredSize(new Dimension((int) (width * 0.25), (int)
		// (height * 0.1)));
		// toggle.setMaximumSize(new Dimension((int) (width * 0.25), (int)
		// (height * 0.1)));
		// toggle.setMinimumSize(new Dimension((int) (width * 0.25), (int)
		// (height * 0.1)));
		//
		// openNew.setPreferredSize(new Dimension((int) (width * 0.25), (int)
		// (height * 0.1)));
		// openNew.setMaximumSize(new Dimension((int) (width * 0.25), (int)
		// (height * 0.1)));
		// openNew.setMinimumSize(new Dimension((int) (width * 0.25), (int)
		// (height * 0.1)));
		//
		// scaleY.setPreferredSize(new Dimension((int) (width * 0.25 * 0.5),
		// (int) (height * 0.1)));
		// scaleY.setMaximumSize(new Dimension((int) (width * 0.25 * 0.5), (int)
		// (height * 0.1)));
		// scaleY.setMinimumSize(new Dimension((int) (width * 0.25 * 0.5), (int)
		// (height * 0.1)));
		//
		// scaleX.setPreferredSize(new Dimension((int) (width * 0.25 * 0.5),
		// (int) (height * 0.1)));
		// scaleX.setMaximumSize(new Dimension((int) (width * 0.25 * 0.5), (int)
		// (height * 0.1)));
		// scaleX.setMinimumSize(new Dimension((int) (width * 0.25 * 0.5), (int)
		// (height * 0.1)));
		//
		// subScaleY.setPreferredSize(new Dimension((int) (width * 0.25 * 0.5),
		// (int) (height * 0.1)));
		// subScaleY.setMaximumSize(new Dimension((int) (width * 0.25 * 0.5),
		// (int) (height * 0.1)));
		// subScaleY.setMinimumSize(new Dimension((int) (width * 0.25 * 0.5),
		// (int) (height * 0.1)));
		//
		// subScaleX.setPreferredSize(new Dimension((int) (width * 0.25 * 0.5),
		// (int) (height * 0.1)));
		// subScaleX.setMaximumSize(new Dimension((int) (width * 0.25 * 0.5),
		// (int) (height * 0.1)));
		// subScaleX.setMinimumSize(new Dimension((int) (width * 0.25 * 0.5),
		// (int) (height * 0.1)));
		//
		// load.setPreferredSize(new Dimension((int) (width * 0.25), (int)
		// (height * 0.1)));
		// load.setMinimumSize(new Dimension((int) (width * 0.25), (int) (height
		// * 0.1)));
		// load.setMaximumSize(new Dimension((int) (width * 0.25), (int) (height
		// * 0.1)));
		//
		// subTrainingRate.setPreferredSize(new Dimension((int) (width * 0.25 *
		// 0.5), (int) (height * 0.1)));
		// subTrainingRate.setMinimumSize(new Dimension((int) (width * 0.25 *
		// 0.5), (int) (height * 0.1)));
		// subTrainingRate.setMaximumSize(new Dimension((int) (width * 0.25 *
		// 0.5), (int) (height * 0.1)));
		//
		// trainingRate.setPreferredSize(new Dimension((int) (width * 0.25 *
		// 0.5), (int) (height * 0.1)));
		// trainingRate.setMinimumSize(new Dimension((int) (width * 0.25 * 0.5),
		// (int) (height * 0.1)));
		// trainingRate.setMaximumSize(new Dimension((int) (width * 0.25 * 0.5),
		// (int) (height * 0.1)));
		//
		// learningRateText.setPreferredSize(new Dimension((int) (width * 0.25 *
		// 0.5), (int) (height * 0.1)));
		// learningRateText.setMaximumSize(new Dimension((int) (width * 0.25 *
		// 0.5), (int) (height * 0.1)));
		// learningRateText.setMinimumSize(new Dimension((int) (width * 0.25 *
		// 0.5), (int) (height * 0.1)));
		//
		// subLearningRate.setPreferredSize(new Dimension((int) (width * 0.25 *
		// 0.5), (int) (height * 0.1)));
		// subLearningRate.setMaximumSize(new Dimension((int) (width * 0.25 *
		// 0.5), (int) (height * 0.1)));
		// subLearningRate.setMinimumSize(new Dimension((int) (width * 0.25 *
		// 0.5), (int) (height * 0.1)));
		//
		// subLamda.setPreferredSize(new Dimension((int) (width * 0.25 * 0.5),
		// (int) (height * 0.1)));
		// subLamda.setMaximumSize(new Dimension((int) (width * 0.25 * 0.5),
		// (int) (height * 0.1)));
		// subLamda.setMinimumSize(new Dimension((int) (width * 0.25 * 0.5),
		// (int) (height * 0.1)));
		//
		// train.setPreferredSize(new Dimension((int) (width * 0.25), (int)
		// (height * 0.1)));
		// train.setMaximumSize(new Dimension((int) (width * 0.25), (int)
		// (height * 0.1)));
		// train.setMinimumSize(new Dimension((int) (width * 0.25), (int)
		// (height * 0.1)));
		//
		// validate.setPreferredSize(new Dimension((int) (width * 0.25), (int)
		// (height * 0.1)));
		// validate.setMaximumSize(new Dimension((int) (width * 0.25), (int)
		// (height * 0.1)));
		// validate.setMinimumSize(new Dimension((int) (width * 0.25), (int)
		// (height * 0.1)));
		//
		// exit.setPreferredSize(new Dimension((int) (width * 0.25), (int)
		// (height * 0.1)));
		// exit.setMaximumSize(new Dimension((int) (width * 0.25), (int) (height
		// * 0.1)));
		// exit.setMinimumSize(new Dimension((int) (width * 0.25), (int) (height
		// * 0.1)));
		//
		// input.setPreferredSize(new Dimension((int) (width * 0.25 * 0.5),
		// (int) (height * 0.2)));
		//// input.setMaximumSize(new Dimension((int) (width * 0.25 * 0.5),
		// (int) (height * 0.2)));
		// input.setMinimumSize(new Dimension((int) (width * 0.25 * 0.5), (int)
		// (height * 0.2)));
		//
		// subInput.setPreferredSize(new Dimension((int) (width * 0.25 * 0.5),
		// (int) (height * 0.2)));
		// subInput.setMaximumSize(new Dimension((int) (width * 0.25 * 0.5),
		// (int) (height * 0.2)));
		// subInput.setMinimumSize(new Dimension((int) (width * 0.25 * 0.5),
		// (int) (height * 0.2)));
		//
		//// inputScroll.setPreferredSize(new Dimension((int) (width * 0.25 *
		// 0.5), (int) (height * 0.2)));
		////// inputScroll.setMaximumSize(new Dimension((int) (width * 0.25 *
		// 0.5), (int) (height * 0.2)));
		//// inputScroll.setMinimumSize(new Dimension((int) (width * 0.25 *
		// 0.5), (int) (height * 0.2)));
		//
		// panel.setSize(new Dimension(width,height));
		//
		// d = inputScroll.getPreferredSize();
		// d.setSize(d);
		// inputScroll.setPreferredSize(d);
		//
		//
		// output.setPreferredSize(new Dimension((int) (width * 0.25), (int)
		// (height * 0.3)));
		//// output.setMaximumSize(new Dimension((int) (width * 0.25), (int)
		// (height * 0.3)));
		// output.setMinimumSize(new Dimension((int) (width * 0.25), (int)
		// (height * 0.3)));
		//
		//// d = output.getPreferredSize();
		//// d.setSize(d);
		//// output.setPreferredSize(d);
		//
		//
		// d = outputScroll.getPreferredSize();
		// d.setSize(d);
		// outputScroll.setPreferredSize(d);
		//
		// input.setEditable(true);
		// input.setEnabled(true);
		// input.setToolTipText("Type a sentence here to test this system.");
		// input.setLineWrap(true);
		// input.setWrapStyleWord(true);
		// input.setBorder(BorderFactory.createEtchedBorder());
		//
		// output.setEditable(false);
		// output.setLineWrap(true);
		// output.setWrapStyleWord(true);
		// output.setBorder(BorderFactory.createEtchedBorder());
		//
		// outputScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		// outputScroll.validate();
		//
		// inputScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		// inputScroll.validate();
		//
		// lamdaText.setEditable(true);
		// lamdaText.setBorder(BorderFactory.createEtchedBorder());
		// lamdaText.setToolTipText("The larger this value is, the smaller the
		// weights will be");
		// lamdaText.setText("" + lamda);
		//
		// trainingRate.setEditable(true);
		// trainingRate.setToolTipText("The actual number of training data
		// between each being rendered here");
		// trainingRate.setText("" + trainingTickRate);
		// trainingRate.setBorder(BorderFactory.createEtchedBorder());
		//
		// scaleY.setEditable(true);
		// scaleY.setToolTipText("enter value of scaleY");
		// scaleY.setBorder(BorderFactory.createEtchedBorder());
		// scaleY.setText("" + graph.getScaleY());
		//
		// scaleX.setEditable(true);
		// scaleX.setToolTipText("enter value of scaleX");
		// scaleX.setBorder(BorderFactory.createEtchedBorder());
		// scaleX.setText("" + graph.getScaleX());
		//
		// learningRateText.setEditable(true);
		// learningRateText.setBorder(BorderFactory.createEtchedBorder());
		// learningRateText.setToolTipText("The current value of learningRate is
		// : " + Double.toString(this.learningRate));
		// learningRateText.setText("" + learningRate);
		//
		// subInput.addActionListener(new ActionListener() {
		// public void actionPerformed(ActionEvent e) {
		// ResourcePool.setIsTraining(false);
		// ResourcePool.setIsValidating(false);
		//
		// Test t = new Test(input.getText());
		// t.execute();
		// }
		// });
		// // subInput.setIcon(icon);
		// // subScaleX.setIcon(icon);
		// // subScaleY.setIcon(icon);
		// // subLamda.setIcon(icon);
		// // subLearningRate.setIcon(icon);
		// // subTrainingRate.setIcon(icon);
		//
		// openNew.addActionListener(new ActionListener() {
		// public void actionPerformed(ActionEvent e) {
		// ResourcePool.setIsTraining(false);
		// ResourcePool.setIsValidating(false);
		// OpenNew o = new OpenNew();
		// o.execute();
		// }
		// });
		//
		// subScaleY.addActionListener(new ActionListener() {
		// public void actionPerformed(ActionEvent e) {
		// if(isShowCost) {
		// graph.setScaleY(Double.parseDouble(scaleY.getText()));
		// }
		// else {
		// graph.setScaleY2(Double.parseDouble(scaleY.getText()));
		// }
		// }
		// });
		//
		// subScaleX.addActionListener(new ActionListener() {
		// public void actionPerformed(ActionEvent e) {
		// if(isShowCost) {
		// graph.setScaleX(Double.parseDouble(scaleX.getText()));
		// }
		// else {
		// graph.setScaleX2(Double.parseDouble(scaleX.getText()));
		// }
		// }
		// });
		//
		// load.addActionListener(new ActionListener() {
		// public void actionPerformed(ActionEvent e) {
		//
		// System.out.println("load called");
		// File[] files = new File("WordCategoryWeights").listFiles();
		//
		// String[] values = new String[files.length + 1];
		//
		// for (int i = 0; i < files.length; i++) {
		// values[i] = files[i].getPath();
		// }
		// values[files.length] = "cancel";
		//
		// String selectedFile = (String) JOptionPane.showInputDialog(null,
		// "Select the file you want to load",
		// "File selecter", JOptionPane.QUESTION_MESSAGE, null, values,
		// values[values.length - 1]);
		//
		// if (selectedFile.equalsIgnoreCase("cancel")) {
		// return;
		// } else {
		// ResourcePool.setIsTraining(false);
		// ResourcePool.setIsValidating(false);
		//
		// String fin = selectedFile.substring(37, selectedFile.length());
		// output.append(fin + "\n");
		//
		// Load l = new Load(fin);
		// System.out.println("load pressed");
		// l.execute();
		//
		// }
		// }
		// });
		//
		// save.addActionListener(new ActionListener() {
		// public void actionPerformed(ActionEvent e) {
		// Save s = new Save();
		// s.execute();
		// }
		// });
		//
		// subTrainingRate.addActionListener(new ActionListener() {
		// public void actionPerformed(ActionEvent e) {
		// trainingTickRate = Integer.parseInt(trainingRate.getText());
		//
		// }
		// });
		//
		// subLamda.addActionListener(new ActionListener() {
		// public void actionPerformed(ActionEvent e) {
		// System.out.println("subLamda was pressed");
		// lamda = Double.parseDouble(lamdaText.getText());
		// lamdaText.setToolTipText("" + lamda);
		// System.out.println(lamda);
		// }
		// });
		// subLearningRate.addActionListener(new ActionListener() {
		//
		// @Override
		// public void actionPerformed(ActionEvent e) {
		// learningRate = Double.parseDouble(learningRateText.getText());
		// learningRateText.setToolTipText("" + learningRate);
		// System.out.println(learningRate);
		// }
		// });
		//
		// toggle.addActionListener(new ActionListener() {
		// public void actionPerformed(ActionEvent e) {
		// if (isShowCost) {
		// graph.showCorrect();
		// isShowCost = false;
		// scaleX.setText(Double.toString(graph.getScaleX2()));
		// scaleY.setText(Double.toString(graph.getScaleY2()));
		// } else {
		// graph.showCost();
		// isShowCost = true;
		// scaleX.setText(Double.toString(graph.getScaleY()));
		// scaleX.setText(Double.toString(graph.getScaleY()));
		// }
		// }
		// });
		//
		// train.addActionListener(new ActionListener() {
		//
		// @Override
		// public void actionPerformed(ActionEvent e) {
		// ResourcePool.setIsTraining(true);
		// Train t = new Train();
		// t.execute();
		// }
		// });
		//
		// exit.addActionListener(new ActionListener() {
		//
		// @Override
		// public void actionPerformed(ActionEvent e) {
		// ResourcePool.setIsTraining(false);
		// ResourcePool.setIsValidating(false);
		// }
		//
		// });
		//
		// validate.addActionListener(new ActionListener() {
		// public void actionPerformed(ActionEvent e) {
		// ResourcePool.setIsValidating(true);
		// Validate v = new Validate();
		// v.execute();
		// }
		// });
		//
		// g.gridwidth = 1;
		// g.gridx = 0;
		// g.gridy = 0;
		//
		// panel.add(train, g);
		//
		// g.gridy = 1;
		// panel.add(openNew, g);
		//
		// g.gridy = 2;
		// panel.add(exit, g);
		//
		// g.gridy = 3;
		// panel.add(save, g);
		//
		// g.gridy = 4;
		// panel.add(load, g);
		//
		// g.gridy = 5;
		// panel.add(toggle, g);
		//
		// g.gridy = 6;
		// panel.add(graph, g);
		//
		// g.gridx = 1;
		// g.gridy = 0;
		// g.gridheight = 7;
		// g.gridwidth = 1;
		//
		// panel.add(f, g);
		//
		// g.gridheight = 1;
		// g.gridy = 0;
		// g.gridwidth = 1;
		// g.gridx = 2;
		//
		// panel.add(lamdaText, g);
		//
		// g.gridx = 3;
		// g.gridy = 0;
		// panel.add(subLamda, g);
		//
		// g.gridx = 2;
		// g.gridy = 1;
		// panel.add(learningRateText, g);
		//
		// g.gridy = 1;
		// g.gridx = 3;
		// panel.add(subLearningRate, g);
		//
		// g.gridx = 2;
		// g.gridy = 2;
		// panel.add(scaleX, g);
		//
		// g.gridx = 3;
		// g.gridy = 2;
		// panel.add(subScaleX, g);
		//
		// g.gridy = 3;
		// g.gridx = 2;
		// panel.add(scaleY, g);
		//
		// g.gridx = 3;
		// g.gridy = 3;
		// panel.add(subScaleY, g);
		//
		// g.gridx = 2;
		// g.gridy = 4;
		// panel.add(trainingRate, g);
		//
		// g.gridx = 3;
		// g.gridy = 4;
		// panel.add(subTrainingRate, g);
		//
		// g.gridy = 5;
		// g.gridx = 2;
		// panel.add(inputScroll, g);
		//
		// g.gridx = 3;
		// g.gridy = 5;
		// panel.add(subInput, g);
		//
		// g.gridy = 6;
		// g.gridx = 2;
		// g.gridwidth = 2;
		// panel.add(outputScroll, g);

		frame.pack();
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

	}

	public void runLoop() {

		// while (1 < 3) {
		//
		// System.out.println(
		// "Enter save if weights and biases must be saved,end to end loop or
		// train or enter random text to check the system");
		// System.out.println("lamda is : " + lamda);
		// System.out.println("learningRate is : " + learningRate);
		//
		// Scanner s = new Scanner(System.in);
		// String text = s.nextLine();
		//
		// if (text.equals("save")) {
		//
		// save();
		//
		// } else if (text.equals("train") || ResourcePool.getIsTraining()) {
		// ResourcePool.setIsTraining(true);
		// while (1 < 3) {
		// if (!ResourcePool.getIsTraining()) {
		// System.out.println("train terminated");
		// break;
		// }
		// train(1);
		// }
		//
		// } else if (text.equalsIgnoreCase("lamda")) {
		// this.lamda = s.nextDouble();
		// }
		// else if (text.equals("end")) {
		// s.close();
		// return;
		//
		// } else if (text.equalsIgnoreCase("learningRate")) {
		// this.learningRate = s.nextDouble();
		// } else if (text.equalsIgnoreCase("enter testing mode")) {
		//
		// DoubleMatrix input;
		// DoubleMatrix answer;
		// DoubleMatrix yHat;
		// double avg = 0.0;
		// int validNum = 0;
		//
		// for (int j = 0; j < trainingData.size(); j++) {
		//
		// input = validationData.get(j);
		// answer = validationAns.get(j);
		//
		// yHat = run(true, input, answer);
		//
		// if
		// (outputDecoder(yHat.toArray()).equalsIgnoreCase(outputDecoder(answer.toArray())))
		// {
		// validNum++;
		// }
		// avg += costFunction(answer, yHat);
		// }
		//
		// avg /= validationData.size();
		// System.out.println("The average cost is : " + avg);
		// System.out.println(
		// "The total number of right predictions out of " +
		// validationData.size() + " is " + validNum);
		//
		// } else if (text.equalsIgnoreCase("train times")) {
		// train(s.nextInt());
		// } else if (text.equalsIgnoreCase("init random weights and biases")) {
		// int rW1 = neuronNumbers[0];
		// int cW1 = neuronNumbers[1];
		//
		// int rW2 = neuronNumbers[1];
		// int cW2 = neuronNumbers[2];
		//
		// int rW3 = neuronNumbers[2];
		// int cW3 = neuronNumbers[3];
		//
		// w1 = DoubleMatrix.randn(rW1, cW1);
		// w2 = DoubleMatrix.randn(rW2, cW2);
		// w3 = DoubleMatrix.rand(rW3, cW3);
		// b1 = DoubleMatrix.rand(neuronNumbers[1]);
		// b2 = DoubleMatrix.rand(neuronNumbers[2]);
		// b3 = DoubleMatrix.rand(neuronNumbers[3]);
		// } else {
		//
		// List<String> list = new ArrayList<String>();
		// list.add(text);
		// Pair res = Controller.getWordIns(list);
		// List<WordIns> wordIns = res.getWordIns();
		// List<double[]> answers = res.getAnswers();
		// DoubleMatrix yHat;
		//
		// DoubleMatrix input;
		// System.out.println("Word ----- pred ans ------ real ans");
		//
		// System.out.println("size of wordIns is : " + wordIns.size());
		// for (int i = 0; i < wordIns.size(); i++) {
		// input = new
		// DoubleMatrix(wordIns.get(i).getComponentArray().getArray()).transpose();
		// yHat = run(input);
		//
		// updateDrawArea(input, yHat, wordIns.get(i).getText(),
		// NeuralNetwork.outputDecoder(answers.get(i)));
		//
		// System.out.print(wordIns.get(i).getText() + "-----");
		// System.out.print(outputDecoder(run(input).toArray()) + "-----");
		// System.out.println(outputDecoder(answers.get(i)));
		// }
		// }
		// }
	}

	// public void validate() {
	//
	// DoubleMatrix input;
	// DoubleMatrix answer;
	// DoubleMatrix yHat;
	// double avg = 0.0;
	// int validNum = 0;
	//
	// for (int j = 0; j < validationData.size(); j++) {
	//
	// if (!ResourcePool.getIsValidating()) {
	// System.out.println("validation terminated");
	// return;
	// }
	// input = validationData.get(j);
	// answer = validationAns.get(j);
	// yHat = run(input);
	//
	// if
	// (outputDecoder(yHat.toArray()).equalsIgnoreCase(outputDecoder(answer.toArray())))
	// {
	// validNum++;
	// }
	//
	// avg += costFunction(answer, yHat);
	// }
	//
	// avg /= validationData.size();
	// costList.add(avg);
	//
	// validationNum.add(validNum);
	// }

	public void train(int times) {

		DoubleMatrix input;
		DoubleMatrix answer;
		DoubleMatrix yHat;
		int correctCount = 0;
		List<Boolean> correctRate = new ArrayList<Boolean>();

		double count = 0;

		for (int i = 0; i < times; i++) {
			System.out.println("train called");
			for (int j = 0; j < x.size(); j++) {
				if (!ResourcePool.getIsTraining()) {
					// System.out.println("returned becasue resource is false");
					return;
				}

				input = new DoubleMatrix(x.get(j).getComponentArray().getArray()).transpose();
				answer = y.get(j);

				yHat = run(true, input, answer);
				count++;
				correctCount++;

				if ((System.currentTimeMillis() - time) >= 1800000) {
					time = System.currentTimeMillis();
					save();
				}

				if (outputDecoder(answer.toArray()).equals(outputDecoder(yHat.toArray()))) {
					correctRate.add(true);
				} else {
					correctRate.add(false);
				}

				if (correctCount == correctTickRate) {
					int correct = 0;
					for (Boolean b : correctRate) {
						if (b) {
							correct++;
						}
					}
					graph.tickCorrect(correct);
					graph.render();
					correct = 0;
					correctCount = 0;
					correctRate.clear();
				}

				if (count == trainingTickRate) {
					count = 0;
					updateDrawArea(input, yHat, x.get(j).getText(), NeuralNetwork.outputDecoder(answer.toArray()));
					graph.tick(costFunction(answer, yHat));
					graph.render();
				}
			}
		}
	}

	public void updateDrawArea(DoubleMatrix inputNeuron, DoubleMatrix outputNeuron, String text, String ans) {

		// new Thread(new Runnable() {
		// public void run() {
		// DoubleMatrix a2;
		// DoubleMatrix a3;
		// DoubleMatrix z2;
		// DoubleMatrix z3;
		// DoubleMatrix z4;
		// DoubleMatrix yHat;
		//
		// z2 = (inputNeuron.mmul(w1)).add(b1);
		// a2 = Utils.activationFunction(z2);
		// z3 = (a2.mmul(w2)).add(b2);
		// a3 = Utils.activationFunction(z3);
		// z4 = (a3.mmul(w3)).add(b3);
		// yHat = Utils.activationFunction(z4);

		// List<DoubleMatrix> weights = new ArrayList<DoubleMatrix>();
		// weights.add(w1);
		// weights.add(w2);
		// weights.add(w3);
		// List<List<Double>> vals = new ArrayList<List<Double>>();
		// List<Double> temp = new ArrayList<Double>();
		//
		// for(double x : inputNeuron.toArray()) {
		// temp.add(x);
		// }
		// System.out.println("size of input is : " + temp.size());
		// vals.add(temp);
		// temp.clear();
		// for(double x : a2.toArray()) {
		// temp.add(x);
		// }
		// System.out.println("size of a2 is : " + temp.size());
		// vals.add(temp);
		// temp.clear();
		//
		// for(double x : a3.toArray()) {
		// temp.add(x);
		// }
		// System.out.println("size of a3 is : " + temp.size());
		// vals.add(temp);
		// temp.clear();
		//
		// for(double x : yHat.toArray()) {
		// temp.add(x);
		// }
		// System.out.println("size of yHat is : " + temp.size());
		// vals.add(temp);
		// temp.clear();

		// draw.tick(0, 0, vals, Color.red, true);
		// draw.render();
		new Thread(new Runnable() {
			public void run() {
				Utils.print(a.get(0).toArray());
				// f.tick(inputNeuron.toArray(), h1Neurons, h2Neurons,
				// outputNeurons, text, ans, weights);
				f.tick(inputNeuron.toArray(), a.get(0).toArray(), a.get(1).toArray(), outputNeuron.toArray(), text, ans,
						weights);
				f.render();
			}
		}).start();

		// }).start();

		// DoubleMatrix a2;
		// DoubleMatrix a3;
		// DoubleMatrix z2;
		// DoubleMatrix z3;
		// DoubleMatrix z4;
		// DoubleMatrix yHat;
		//
		// z2 = (inputNeuron.mmul(w1)).add(b1);
		// a2 = Utils.activationFunction(z2);
		// z3 = (a2.mmul(w2)).add(b2);
		// a3 = Utils.activationFunction(z3);
		// z4 = (a3.mmul(w3)).add(b3);
		// yHat = Utils.activationFunction(z4);
		//
		// List<DoubleMatrix> weights = new ArrayList<DoubleMatrix>();
		// weights.add(w1);
		// weights.add(w2);
		// weights.add(w3);
		// List<List<Double>> vals = new ArrayList<List<Double>>();
		// List<Double> temp = new ArrayList<Double>();
		//
		// for(double x : inputNeuron.toArray()) {
		// temp.add(x);
		// }
		// vals.add(temp);
		// temp.clear();
		// for(double x : a2.toArray()) {
		// temp.add(x);
		// }
		// vals.add(temp);
		// temp.clear();
		//
		// for(double x : a3.toArray()) {
		// temp.add(x);
		// }
		// vals.add(temp);
		// temp.clear();
		//
		// for(double x : yHat.toArray()) {
		// temp.add(x);
		// }
		// vals.add(temp);
		//
		// draw.tick(0, 0, vals, Color.red, true);
		// draw.render();
		// f.tick(inputNeuron.toArray(), z2.toArray(), z3.toArray(),
		// outputNeuron.toArray(), text,ans, weights);
		// f.render();
	}

	public DoubleMatrix run(boolean isLearn, DoubleMatrix input, DoubleMatrix answer) {

		DoubleMatrix[] a = new DoubleMatrix[noOfLayers - 2];
		DoubleMatrix[] z = new DoubleMatrix[noOfLayers - 1];
		DoubleMatrix[] delta = new DoubleMatrix[noOfLayers - 1];
		DoubleMatrix[] djdw = new DoubleMatrix[noOfLayers - 1];
		DoubleMatrix[] dwdw = new DoubleMatrix[noOfLayers - 1];

		DoubleMatrix yHat = null;

		if (noOfLayers > 2) {
			z[0] = (input.mmul(weights.get(0))).add(biases.get(0));
			a[0] = Utils.activationFunction(z[0]);

			for (int i = 1; i < noOfLayers - 1; i++) {

				z[i] = (a[i - 1].mmul(weights.get(i))).add(biases.get(i));

				if (i == noOfLayers - 2) {
					yHat = Utils.activationFunction(z[z.length - 1]);

				} else {
					a[i] = Utils.activationFunction(z[i]);
				}
			}
			this.a = Utils.arrayToList(a);
		} else if (noOfLayers == 2) {
			z[0] = (input.mmul(weights.get(0))).add(biases.get(0));
			yHat = Utils.activationFunction(z[0]);
		}

		if (isLearn) {
			double[][] temp = null;
			for (int i = 0; i < weights.size(); i++) {
				temp = weights.get(i).toArray2();

				for (int k = 0; k < weights.get(i).rows; k++) {
					for (int j = 0; j < weights.get(i).columns; j++) {
						temp[k][j] = 2 * temp[k][j];
					}
				}
				dwdw[i] = new DoubleMatrix(temp);
			}

			delta[delta.length - 1] = (answer.sub(yHat).mul(-1)).mul(Utils.activationPrime(z[z.length - 1]));

			if (noOfLayers == 1) {
				djdw[0] = (input.transpose().mmul(delta[0])).add(dwdw[0]);
			}

			else {

				djdw[djdw.length - 1] = ((a[a.length - 1].transpose()).mmul(delta[delta.length - 1]))
						.add(dwdw[dwdw.length - 1].mul(lamda));

				for (int i = 1; i < noOfLayers - 1; i++) {

					delta[delta.length - 1 - i] = delta[delta.length - i]
							.mmul((weights.get(noOfLayers - 1 - i).transpose())
									.mmul(Utils.activationPrime(z.length - 1 - i)));

					if (i == noOfLayers - 2) {

						djdw[djdw.length - 1 - i] = (input.transpose().mmul(delta[0])).add(dwdw[0].mul(lamda));

					} else {
						djdw[djdw.length - 1 - i] = (a[a.length - 1 - i].transpose().mmul(delta[delta.length - 1 - i]))
								.add(dwdw[dwdw.length - 1 - i].mul(lamda));
					}
				}
			}

			for (int i = 0; i < weights.size(); i++) {
				weights.set(i, weights.get(i).sub(djdw[i].mul(learningRate)));
				biases.set(i, biases.get(i).sub(delta[i]));
			}

		}

		return yHat;

		// if (noOfLayers == 4) {
		// DoubleMatrix a2;
		// DoubleMatrix a3;
		// DoubleMatrix z2;
		// DoubleMatrix z3;
		// DoubleMatrix z4;
		//
		// z2 = (input.mmul(w1)).add(b1);
		// a2 = Utils.activationFunction(z2);
		// z3 = (a2.mmul(w2)).add(b2);
		// a3 = Utils.activationFunction(z3);
		// z4 = (a3.mmul(w3)).add(b3);
		// yHat = Utils.activationFunction(z4);
		//
		// if (isLearn) {
//		 update(z2, z3, z4, a2, a3, yHat, input, answer);
		// }
		//
		// return yHat;
		// }
		//
		// else if (noOfLayers == 5) {
		// DoubleMatrix a2;
		// DoubleMatrix a3;
		// DoubleMatrix a4;
		// DoubleMatrix z2;
		// DoubleMatrix z3;
		// DoubleMatrix z4;
		// DoubleMatrix z5;
		// }
		// return null;

	}

	public DoubleMatrix run(DoubleMatrix input) {

		DoubleMatrix[] a = new DoubleMatrix[noOfLayers - 2];
		DoubleMatrix[] z = new DoubleMatrix[noOfLayers - 1];
		DoubleMatrix[] delta = new DoubleMatrix[noOfLayers - 1];
		DoubleMatrix[] djdw = new DoubleMatrix[noOfLayers - 1];
		DoubleMatrix[] dwdw = new DoubleMatrix[noOfLayers - 1];

		DoubleMatrix yHat = null;

		z[0] = (input.mmul(weights.get(0))).add(biases.get(0));
		a[0] = Utils.activationFunction(z[0]);

		for (int i = 1; i < noOfLayers - 1; i++) {

			z[i] = (a[i - 1].mmul(weights.get(i))).add(biases.get(i));

			if (i == noOfLayers - 2) {
				yHat = Utils.activationFunction(z[z.length - 1]);

			} else {
				a[i] = Utils.activationFunction(z[i]);
			}
		}

		this.a = Utils.arrayToList(a);

		return yHat;

		// if (noOfLayers == 4) {
		//
		// DoubleMatrix a2;
		// DoubleMatrix a3;
		// DoubleMatrix z2;
		// DoubleMatrix z3;
		// DoubleMatrix z4;
		// DoubleMatrix yHat;
		//
		// z2 = (input.mmul(w1)).add(b1);
		// a2 = Utils.activationFunction(z2);
		// z3 = (a2.mmul(w2)).add(b2);
		// a3 = Utils.activationFunction(z3);
		// z4 = (a3.mmul(w3)).add(b3);
		// yHat = Utils.activationFunction(z4);
		//
		// return yHat;
		// } else if (noOfLayers == 5) {
		// DoubleMatrix a2;
		// DoubleMatrix a3;
		// DoubleMatrix a4;
		// DoubleMatrix z2;
		// DoubleMatrix z3;
		// DoubleMatrix z4;
		// DoubleMatrix z5;
		// DoubleMatrix yHat;
		//
		// z2 = (input.mmul(w1)).add(b1);
		// a2 = Utils.activationFunction(z2);
		// z3 = (a2.mmul(w2)).add(b2);
		// a3 = Utils.activationFunction(z3);
		// z4 = (a3.mmul(w3)).add(b3);
		// a4 = (Utils.activationFunction(z4));
		// z5 = (a4.mmul(w4)).add(b4);
		// yHat = Utils.activationFunction(z5);
		//
		// return yHat;
		//
		// }
		// return null;

	}

	public void update(DoubleMatrix z2, DoubleMatrix z3, DoubleMatrix z4, DoubleMatrix a2, DoubleMatrix a3,
			DoubleMatrix yHat, DoubleMatrix input, DoubleMatrix output) {

		if (noOfLayers == 4) {

			double[][] temp = w3.toArray2();

			for (int i = 0; i < w3.rows; i++) {
				for (int j = 0; j < w3.columns; j++) {
					temp[i][j] = 2 * temp[i][j];
				}
			}

			DoubleMatrix dwdw3 = new DoubleMatrix(temp);
			DoubleMatrix delta4 = (output.sub(yHat).mul(-1)).mul(Utils.activationPrime(z4));
			DoubleMatrix djdw3 = (a3.transpose().mmul(delta4)).add(dwdw3.mul(lamda));

			temp = w2.toArray2();

			for (int i = 0; i < w2.rows; i++) {
				for (int j = 0; j < w2.columns; j++) {
					temp[i][j] = 2 * temp[i][j];
				}
			}
			DoubleMatrix dwdw2 = new DoubleMatrix(temp);
			DoubleMatrix delta3 = (delta4.mmul(w3.transpose())).mul(Utils.activationPrime(z3));
			DoubleMatrix djdw2 = (a2.transpose().mmul(delta3)).add(dwdw2.mul(lamda));

			temp = w1.toArray2();

			for (int i = 0; i < w1.rows; i++) {
				for (int j = 0; j < w1.columns; j++) {
					temp[i][j] = 2 * temp[i][j];
				}
			}
			DoubleMatrix dwdw1 = new DoubleMatrix(temp);
			DoubleMatrix delta2 = (delta3.mmul(w2.transpose()).mul(Utils.activationPrime(z2)));
			DoubleMatrix djdw1 = (input.transpose().mmul(delta2)).add(dwdw1.mul(lamda));

			w1 = w1.sub(djdw1.mul(learningRate));
			w2 = w2.sub(djdw2.mul(learningRate));
			w3 = w3.sub(djdw3.mul(learningRate));

			b1 = b1.sub(delta2.mul(learningRate));
			b2 = b2.sub(delta3.mul(learningRate));
			b3 = b3.sub(delta4.mul(learningRate));
		}

	}

	public static double costFunction(DoubleMatrix answer, DoubleMatrix yHat) {
		double cost = 0.0;

		double[] ans = answer.toArray();
		double[] pred = yHat.toArray();
		double[] temp = new double[ans.length];

		for (int i = 0; i < ans.length; i++) {
			temp[i] = ans[i] - pred[i];
			temp[i] = temp[i] * temp[i];
			temp[i] = temp[i] * 0.5;
		}

		for (double x : temp) {
			cost += x;
		}

		return cost;
	}

	public static double[] outputEncoder(String text) {

		int tempArrSize = 37;
		double[] tempArr = new double[tempArrSize];
		String[] split = text.split("_");

		String s = "";
		int ind = 36;

//		BufferedReader br = IOUtils.openBr(IOUtils.getFileFromResourcesFolder("Legend"));

		for (int i = 0; i < LEGEND.length; i++) {
			if (split[1].equals(LEGEND[i])) {
				ind = i;
				break;
			}
		}

		double[] result = new double[10];
		int doubleInd = 9;

		if (ind == 6 || ind == 7 || ind == 8) {
			doubleInd = 0;
		} else if (ind == 20 || ind == 21 || ind == 19 || ind == 35) {
			doubleInd = 1;
		} else if (ind == 12 || ind == 13 || ind == 14 || ind == 11) {
			doubleInd = 2;
		} else if (ind == 33 || ind == 34 || ind == 17 || ind == 18) {
			doubleInd = 3;
		} else if (ind == 26 || ind == 27 || ind == 28 || ind == 29 || ind == 30 || ind == 31) {
			doubleInd = 4;
		} else if (ind == 32) {
			doubleInd = 5;
		} else if (ind == 5) {
			doubleInd = 6;
		} else if (ind == 0) {
			doubleInd = 7;
		} else if (ind == 2) {
			doubleInd = 8;
		} else {
			doubleInd = 9;
		}

		for (int i = 0; i < 10; i++) {
			if (i != doubleInd) {
				result[i] = 0.0;
			} else {
				result[i] = 1.0;
			}
		}
//		System.out.println(ind);
		return result;

		// if (ind == 12 || ind == 13 || ind == 14 || ind == 11) {
		// double[] result = {1.0,0.0};
		// return result;
		// } else {
		// double[] result = {0.0,1.0};
		// return result;
		// }
	}

	public static String outputDecoder(double[] output) {
		output = Utils.getLargestAsArray(output);

		int ind = 9;

		for (int i = 0; i < output.length; i++) {
			if (output[i] == 1.0) {
				ind = i;
				break;
			}
		}

		if (ind == 0) {
			return "Adjective";
		} else if (ind == 1) {
			return "Adverb";
		} else if (ind == 2) {
			return "Noun";
		} else if (ind == 3) {
			return "Pronoun";
		} else if (ind == 4) {
			return "Verb";
		} else if (ind == 5) {
			return "Determiner";
		} else if (ind == 6) {
			return "Preposisition";
		} else if (ind == 7) {
			return "Conjunction";
		} else if (ind == 8) {
			return "Determiner";
		} else {
			return "Others";
		}

		// if (output[0] == 1) {
		// return "noun";
		// } else {
		// return "other";
		// }

	}

	// class Validate extends SwingWorker {
	//
	// protected Object doInBackground() throws Exception {
	// int times = 1;
	// while (1 < 3) {
	// if (!ResourcePool.getIsTraining()) {
	// System.out.println("validation terminated");
	// break;
	// }
	// System.out.println("validation running for the " + times + "th
	// time.......");
	// train(1);
	//// validate();
	// System.out.println("The total number of right predictions out of " +
	// validationData.size() + " is "
	// + validationNum.get(validationNum.size() - 1));
	// System.out.println("AVG cost is : " + costList.get(costList.size() - 1));
	// times++;
	//
	// if (validationNum.size() >= 2) {
	//
	// if ((validationNum.get(validationNum.size() - 1)) >
	// (validationNum.get(validationNum.size() - 2))) {
	// validationNum.clear();
	// break;
	// }
	// }
	// }
	// return null;
	// }
	//
	// }

	class Train extends SwingWorker {

		@Override
		protected Object doInBackground() throws Exception {
			while (1 < 3) {
				if (!ResourcePool.getIsTraining()) {
					System.out.println("train terminated");
					break;
				}
				train(1);
			}
			return null;
		}

	}

	class Save extends SwingWorker {

		@Override
		protected Object doInBackground() throws Exception {
			save();
			return null;
		}

	}

	class Test extends SwingWorker {

		private String text;

		public Test(String text) {
			this.text = text;
		}

		protected Object doInBackground() throws Exception {

			List<String> list = new ArrayList<String>();
			list.add(text);
			Pair res = new Controller().getWordIns(list);
			List<WordIns> wordIns = res.getWordIns();
			List<double[]> answers = res.getAnswers();

			DoubleMatrix yHat;
			DoubleMatrix input;

			System.out.println(wordIns.size());
			output.append("Input ----Predicted output----real answer" + "\n");
			for (int i = 0; i < wordIns.size(); i++) {
				input = new DoubleMatrix(wordIns.get(i).getComponentArray().getArray()).transpose();
				yHat = n.run(input);

				updateDrawArea(input, yHat, wordIns.get(i).getText(), NeuralNetwork.outputDecoder(answers.get(i)));

				output.append(wordIns.get(i).getText() + ":" + outputDecoder(yHat.toArray()) + ":"
						+ outputDecoder(answers.get(i)) + "\n");

				// System.out.print(wordIns.get(i).getText() + "-----");
				// System.out.print(outputDecoder(yHat.toArray()) + "-----");
				// System.out.println(outputDecoder(answers.get(i)));
			}
			output.append("\n");
			output.append("\n");

			return null;
		}

	}

	class Load extends SwingWorker {

		private String fin;

		public Load(String fin) {
			this.fin = fin;
		}

		@Override
		protected Object doInBackground() throws Exception {
			output.append("Loaded called");
			w1 = IOUtils.getMatrixFromFile(IOUtils.getFileFromResourcesFolder("WordCategoryWeights/wordCategoriser" + "W1" + fin).getPath());
			output.append(""+w1.rows);
			w1 = IOUtils.getMatrixFromFile(ResourcePool.outputMode + "WordCategoryWeights/wordCategoriser" + "W1" + fin);
			w2 = IOUtils.getMatrixFromFile(ResourcePool.outputMode + "WordCategoryWeights/wordCategoriser" + "W2" + fin);
			w3 = IOUtils.getMatrixFromFile(ResourcePool.outputMode + "WordCategoryWeights/wordCategoriser" + "W3" + fin);
			b1 = IOUtils.getMatrixFromFile(ResourcePool.outputMode + "WordCategoryWeights/wordCategoriser" + "B1" + fin);
			b2 = IOUtils.getMatrixFromFile(ResourcePool.outputMode + "WordCategoryWeights/wordCategoriser" + "B2" + fin);
			b3 = IOUtils.getMatrixFromFile(ResourcePool.outputMode + "WordCategoryWeights/wordCategoriser" + "B3" + fin);

			weights.set(0, w1);
			weights.set(1, w2);
			weights.set(2, w3);
			biases.set(0, b1);
			biases.set(1, b2);
			biases.set(2, b3);

			List<Double> values = new ArrayList<Double>();

			if (new File(ResourcePool.outputMode + "WordCategoryWeights/wordCategoriser" + "SA" + fin).exists()) {

				System.out.println("File exists");
				String[] split = null;
				BufferedReader br = IOUtils.openBr(ResourcePool.outputMode + "WordCategoryWeights/wordCategoriser" + "SA" + fin);
				String line = "";

				while ((line = br.readLine()) != null) {

					System.out.println("iside loop");
					split = line.split(";");
					Utils.print(split);
					values.add(Double.parseDouble(split[1]));
					System.out.println("value added");
					System.out.println("value size is : " + values.size());

					if (values.size() == 6) {
						System.out.println("broken");
						break;
					}
				}

				System.out.println("outside loop");
				graph.setScaleX(values.get(0));
				graph.setScaleX2(values.get(1));
				graph.setScaleY(values.get(2));
				graph.setScaleY2(values.get(3));
				learningRate = values.get(4);
				lamda = values.get(5);

				if (isShowCost) {
					scaleX.setText("" + values.get(0));
					scaleY.setText("" + values.get(2));
					System.out.println("setted text");
				} else {
					scaleX.setText("" + values.get(1));
					scaleY.setText("" + values.get(3));
				}
			}

			else {
				System.out.println("countdt find SA file");
				graph.setScaleX(1);
				graph.setScaleX2(1);
				graph.setScaleY(1);
				graph.setScaleY2(1);
				learningRate = 0;
				lamda = 0;

				scaleX.setText("" + 1);
				scaleY.setText("" + 1);

				scaleX.setText("" + 1);
				scaleY.setText("" + 1);
			}

			output.append("successfully loaded files" + "\n");

			ResourcePool.setIsTraining(true);

			return null;
		}

	}

	class OpenNew extends SwingWorker {

		@Override
		protected Object doInBackground() throws Exception {
			w1 = DoubleMatrix.randn(neuronNumbers[0], neuronNumbers[1]);
			w2 = DoubleMatrix.randn(neuronNumbers[1], neuronNumbers[2]);
			w3 = DoubleMatrix.randn(neuronNumbers[2], neuronNumbers[3]);
			b1 = DoubleMatrix.rand(neuronNumbers[1]);
			b2 = DoubleMatrix.rand(neuronNumbers[2]);
			b3 = DoubleMatrix.rand(neuronNumbers[3]);

			weights.set(0, w1);
			weights.set(1, w2);
			weights.set(2, w3);
			biases.set(0, b1);
			biases.set(1, b2);
			biases.set(2, b3);
			return null;
		}

	}

	class LeftPane extends JPanel {

		JButton train;
		JButton exit;
		JButton save;
		JButton load;
		JButton openNew;
		JButton toggle;

		public LeftPane(int w, int h) {

			this.setSize(w, h);

			train = new JButton("TRAIN");
			exit = new JButton("STOP");
			save = new JButton("SAVE");
			load = new JButton("LOAD");
			openNew = new JButton("OPEN NEW");
			toggle = new JButton("TOGGLE GRPAHS");

			graph = new Graph(w, (int) (h * 0.4), correctTickRate, trainingTickRate);

			this.setLayout(new GridBagLayout());

			GridBagConstraints gbc = new GridBagConstraints();

			addComponents(this, train, gbc, 0, 0, 1, 1, w, (int) (h * 0.1));
			addComponents(this, exit, gbc, 0, 1, 1, 1, w, (int) (h * 0.1));
			addComponents(this, save, gbc, 0, 2, 1, 1, w, (int) (h * 0.1));
			addComponents(this, load, gbc, 0, 3, 1, 1, w, (int) (h * 0.1));
			addComponents(this, openNew, gbc, 0, 4, 1, 1, w, (int) (h * 0.1));
			addComponents(this, toggle, gbc, 0, 5, 1, 1, w, (int) (h * 0.1));
			addComponents(this, graph, gbc, 0, 6, 1, 1, w, (int) (h * 0.4));

			addActionListeners();

		}

		public void addComponents(Container c, Component comp, GridBagConstraints g, int x, int y, int w, int h,
				int width, int height) {
			g.gridx = x;
			g.gridy = y;
			g.gridheight = h;
			g.gridwidth = w;
			g.weightx = 1;
			g.weighty = 1;
			g.fill = GridBagConstraints.BOTH;

			comp.setSize(width, height);
			c.add(comp, g);
		}

		public void addActionListeners() {

			openNew.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ResourcePool.setIsTraining(false);
					ResourcePool.setIsValidating(false);
					OpenNew o = new OpenNew();
					o.execute();
				}
			});

			load.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {

					System.out.println("load called");
					File[] files = new File(ResourcePool.outputMode + "WordCategoryWeights").listFiles();

					if (files != null) {
						String[] values = new String[files.length + 1];

						for (int i = 0; i < files.length; i++) {
							values[i] = files[i].getPath();
						}
						values[files.length] = "cancel";

						String selectedFile = (String) JOptionPane.showInputDialog(null,
								"Select the file you want to load", "File selecter", JOptionPane.QUESTION_MESSAGE, null,
								values, values[values.length - 1]);

						if (selectedFile.equalsIgnoreCase("cancel")) {
							return;
						} else {
							ResourcePool.setIsTraining(false);
							ResourcePool.setIsValidating(false);

							String fin = selectedFile.substring(37, selectedFile.length());
							output.append(fin + "\n");

							Load l = new Load(fin);

//							w1 = IOUtils.getMatrixFromFile("./Resources/WordCategoryWeights/wordCategoriser" + "W1" + fin);
//							w2 = IOUtils.getMatrixFromFile("./Resources/WordCategoryWeights/wordCategoriser" + "W2" + fin);
//							w3 = IOUtils.getMatrixFromFile("./Resources/WordCategoryWeights/wordCategoriser" + "W3" + fin);
//							b1 = IOUtils.getMatrixFromFile("./Resources/WordCategoryWeights/wordCategoriser" + "B1" + fin);
//							b2 = IOUtils.getMatrixFromFile("./Resources/WordCategoryWeights/wordCategoriser" + "B2" + fin);
//							b3 = IOUtils.getMatrixFromFile("./Resources/WordCategoryWeights/wordCategoriser" + "B3" + fin);
//
//							weights.set(0, w1);
//							weights.set(1, w2);
//							weights.set(2, w3);
//							biases.set(0, b1);
//							biases.set(1, b2);
//							biases.set(2, b3);
//
//							List<Double> value = new ArrayList<Double>();
//
//							System.out.println("File exists");
//							String[] split = null;
//							BufferedReader br = IOUtils.openBr("./Resources/WordCategoryWeights/wordCategoriser" + "SA" + fin);
//							String line = "";
//
//							try {
//								while ((line = br.readLine()) != null && line != "") {
//
//									split = line.split(";");
//									Utils.print(split);
//									value.add(Double.parseDouble(split[1]));
//								}
//							} catch (NumberFormatException | IOException e1) {
//								// TODO Auto-generated catch block
//								e1.printStackTrace();
//							}
//
//							System.out.println("outside loop");
//							graph.setScaleX(value.get(0));
//							System.out.println("scaleX1 : " + value.get(0));
//							graph.setScaleX2(value.get(1));
//							System.out.println("scalex2 : " + value.get(1));
//							graph.setScaleY(value.get(2));
//							System.out.println("scaley1 : " + value.get(2));
//							graph.setScaleY2(value.get(3));
//							System.out.println("scaley2 : " + value.get(3));
//							learningRate = value.get(4);
//							System.out.println("lesrningRate : " + value.get(4));
//							lamda = value.get(5);
//							System.out.println("lamda : " + value.get(5));
//
//							learningRateText.setText(value.get(4) + "");
//							lamdaText.setText(value.get(5) + "");
//
//							if (isShowCost) {
//								scaleX.setText(Double.toString(graph.getScaleX()));
//								scaleY.setText(Double.toString(graph.getScaleY()));
//								System.out.println("setted text");
//							} else {
//								scaleX.setText(Double.toString(graph.getScaleX2()));
//								scaleY.setText(Double.toString(graph.getScaleY2()));
//							}

							output.append("successfully loaded files" + "\n");

							// ResourcePool.setIsTraining(true);

						}
					} else {
						System.out.println("Folder is empty");
					}
				}
			});

			save.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Save s = new Save();
					s.execute();
				}
			});

			toggle.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (isShowCost) {
						graph.showCorrect();
						isShowCost = false;
						scaleX.setText(Double.toString(graph.getScaleX2()));
						scaleY.setText(Double.toString(graph.getScaleY2()));
					} else {
						graph.showCost();
						isShowCost = true;
						scaleX.setText(Double.toString(graph.getScaleX()));
						scaleY.setText(Double.toString(graph.getScaleY()));
					}
				}
			});

			train.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					ResourcePool.setIsTraining(true);
					Train t = new Train();
					t.execute();
				}
			});

			exit.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					ResourcePool.setIsTraining(false);
					ResourcePool.setIsValidating(false);
				}

			});

		}
	}

	class RightPane extends JPanel {

		JButton subLamda = new JButton("LAMDA");
		JButton subLearningRate = new JButton("LEARNING RATE");
		JButton subScaleY = new JButton("SCALE Y");
		JTextArea trainingRate = new JTextArea();
		JButton subTrainingRate = new JButton("TICK RATE");

		JButton subScaleX = new JButton("SCALE X");
		JTextArea input = new JTextArea();

		JButton subInput = new JButton("INPUT");
		JScrollPane inputScroll = new JScrollPane(input);
		JScrollPane outputScroll = new JScrollPane(output);

		public RightPane(int width, int height) {
			scaleY = new JTextArea();
			scaleX = new JTextArea();
			output = new TextArea(img);
			lamdaText = new JTextArea();
			subLamda = new JButton("LAMDA");
			learningRateText = new JTextArea();
			subLearningRate = new JButton("LEARNING RATE");
			subScaleY = new JButton("SCALE Y");
			trainingRate = new JTextArea();
			subTrainingRate = new JButton("TICK RATE");
			subScaleX = new JButton("SCALE X");
			input = new JTextArea();
			subInput = new JButton("INPUT");
			inputScroll = new JScrollPane(input);
			outputScroll = new JScrollPane(output);

			input.setEditable(true);
			input.setEnabled(true);
			input.setToolTipText("Type a sentence here to test this system.");
			input.setLineWrap(true);
			input.setWrapStyleWord(true);
			input.setBorder(BorderFactory.createEtchedBorder());

			output.setEditable(false);
			output.setLineWrap(true);
			output.setWrapStyleWord(true);
			output.setBorder(BorderFactory.createEtchedBorder());

			this.setSize(width, height);
			this.setLayout(new GridBagLayout());

			GridBagConstraints gbc = new GridBagConstraints();

			addComponents(this, learningRateText, gbc, 0, 0, 1, 1, (int) (width * 0.5), (int) (height * 0.1));
			addComponents(this, lamdaText, gbc, 0, 1, 1, 1, (int) (width * 0.5), (int) (height * 0.1));
			addComponents(this, scaleX, gbc, 0, 2, 1, 1, (int) (width * 0.5), (int) (height * 0.1));
			addComponents(this, scaleY, gbc, 0, 3, 1, 1, (int) (width * 0.5), (int) (height * 0.1));
			addComponents(this, trainingRate, gbc, 0, 4, 1, 1, (int) (width * 0.5), (int) (height * 0.1));

			addComponents(this, subLearningRate, gbc, 1, 0, 1, 1, (int) (width * 0.5), (int) (height * 0.1));
			addComponents(this, subLamda, gbc, 1, 1, 1, 1, (int) (width * 0.5), (int) (height * 0.1));
			addComponents(this, subScaleX, gbc, 1, 2, 1, 1, (int) (width * 0.5), (int) (height * 0.1));
			addComponents(this, subScaleY, gbc, 1, 3, 1, 1, (int) (width * 0.5), (int) (height * 0.1));
			addComponents(this, subTrainingRate, gbc, 1, 4, 1, 1, (int) (width * 0.5), (int) (height * 0.1));

			addComponents(this, inputScroll, gbc, 0, 5, 1, 1, (int) (width * 0.5), (int) (height * 0.2));
			addComponents(this, subInput, gbc, 1, 5, 1, 1, (int) (width * 0.5), (int) (height * 0.2));
			addComponents(this, outputScroll, gbc, 0, 6, 2, 1, (int) (width * 0.5), (int) (height * 0.3));

			lamdaText.setEditable(true);
			lamdaText.setBorder(BorderFactory.createEtchedBorder());
			lamdaText.setToolTipText("The larger this value is, the smaller the weights will be");
			lamdaText.setText("" + lamda);

			trainingRate.setEditable(true);
			trainingRate.setToolTipText("The actual number of training data between each being rendered here");
			trainingRate.setText("" + trainingTickRate);
			trainingRate.setBorder(BorderFactory.createEtchedBorder());

			scaleY.setEditable(true);
			scaleY.setToolTipText("enter value of scaleY");
			scaleY.setBorder(BorderFactory.createEtchedBorder());
			scaleY.setText("" + graph.getScaleY());

			scaleX.setEditable(true);
			scaleX.setToolTipText("enter value of scaleX");
			scaleX.setBorder(BorderFactory.createEtchedBorder());
			scaleX.setText("" + graph.getScaleX());

			learningRateText.setEditable(true);
			learningRateText.setBorder(BorderFactory.createEtchedBorder());
			learningRateText.setToolTipText("The current value of learningRate is : " + Double.toString(learningRate));
			learningRateText.setText("" + learningRate);

			addActionListeners();
		}

		public void addComponents(Container c, Component comp, GridBagConstraints g, int x, int y, int w, int h,
				int width, int height) {
			g.gridx = x;
			g.gridy = y;
			g.gridheight = h;
			g.gridwidth = w;
			g.weightx = 1;
			g.weighty = 1;
			g.fill = GridBagConstraints.BOTH;

			comp.setPreferredSize(new Dimension(width, height));
			comp.setMinimumSize(new Dimension(width, height));
			comp.setMaximumSize(new Dimension(width, height));
			c.add(comp, g);
		}

		public void addActionListeners() {
			subTrainingRate.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					trainingTickRate = Integer.parseInt(trainingRate.getText());
					graph.setTrainingRate(trainingTickRate);
				}
			});

			subLamda.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					lamda = Double.parseDouble(lamdaText.getText());
				}
			});
			subLearningRate.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					learningRate = Double.parseDouble(learningRateText.getText());
				}
			});

			subInput.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					ResourcePool.setIsTraining(false);
					ResourcePool.setIsValidating(false);

					try {
						bw.write(input.getText());
						bw.newLine();
						bw.flush();
						System.out.println("written comment successfully");
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					output.append(input.getText() + "\n");
					Test t = new Test(input.getText());
					try {
						t.execute();
					} catch (Exception c) {
						c.printStackTrace();
					}
				}
			});

			subScaleY.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (isShowCost) {
						graph.setScaleY(Double.parseDouble(scaleY.getText()));
					} else {
						graph.setScaleY2(Double.parseDouble(scaleY.getText()));
					}
				}
			});

			subScaleX.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (isShowCost) {
						graph.setScaleX(Double.parseDouble(scaleX.getText()));
					} else {
						graph.setScaleX2(Double.parseDouble(scaleX.getText()));
					}
				}
			});
		}
	}

	class TickRenderDraw implements Runnable {
		DoubleMatrix yHat;

		public TickRenderDraw(DoubleMatrix yHat) {
			this.yHat = yHat;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub

		}

	}
}
