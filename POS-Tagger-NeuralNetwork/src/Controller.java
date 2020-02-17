

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.jblas.DoubleMatrix;

import com.aliasi.chunk.Chunk;
import com.aliasi.chunk.Chunking;
import com.aliasi.sentences.MedlineSentenceModel;
import com.aliasi.sentences.SentenceChunker;
import com.aliasi.sentences.SentenceModel;
import com.aliasi.tokenizer.IndoEuropeanTokenizerFactory;
import com.aliasi.tokenizer.TokenizerFactory;
import com.aliasi.util.Files;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class Controller extends Thread {

	public static List<String> sentenceListAsStrings;
	public static List<WordIns> wordIns;
	public static List<Integer> indexList;
	
	public static List<List<double[]>> answersList;
	public static List<List<double[]>> questionsList;
	public static List<WordIns> x;
	public static List<DoubleMatrix> y;
//	private ReentrantLock lock;
	
	public static List<double[]> answers;
	public static MaxentTagger tagger;
	public static List<NeuralNetwork> wordCategList;
	
	int count = 0;

	public Controller() {

		// try {
		// IOUtils.sortFile("UnsortedSentences", "Sentences");
		// } catch (IOException e) {
		// e.printStackTrace();
		// }

		// System.out.println("starting to copy senteces to file....");
		// updateSentenceFile();
		// System.out.println("finished copying sentences.....");
		// System.out.println("starting to sort file....");
		// IOUtils.formatFile("Sentences");
		// System.out.println("finished sorting file....");
		// System.out.println("finished updating and making sentence
		// file......");
	}
	
	public void run() {
		Utils.printlnToAI();
		init();
		System.out.println("updating trainingData......");
		updateTrainData();
		System.out.println("fininsed updating trainingData");
		System.out.println("starting to initialise word categoriser neural net......");
		initNeuralNetwork();

	}

	public void initNeuralNetwork() {

		int noOfWordTypes = 10;
		int[] neuronNumbers = { WordIns.numberOfComponents, 50, 50, noOfWordTypes };

		List<DoubleMatrix> weigths = new ArrayList<DoubleMatrix>();
		List<DoubleMatrix> biases = new ArrayList<DoubleMatrix>();
		
		System.out.println("parsing sentences...");
		
//		Pair res = getWordIns(parseSentence(this.getClass().getResource("Sentences").getPath()));
		Pair res = getWordIns(parseSentence("Sentences"));
		wordIns = res.getWordIns();
		answers = res.getAnswers();
		
		System.out.println("finished parsing sentences");
		
		System.out.println("size of wordINs is : " + wordIns.size());
//		for (int i = 0; i < wordIns.size(); i++) {
//			x.add(new DoubleMatrix(wordIns.get(i).getComponentArray().getArray()).transpose());
//		}
		x = wordIns;

		for (int i = 0; i < answers.size(); i++) {
			y.add(new DoubleMatrix(answers.get(i)).transpose());
		}

		List<DoubleMatrix> trainingData = new ArrayList<DoubleMatrix>();		
		List<DoubleMatrix> trainingAns = new ArrayList<DoubleMatrix>();

		int dataSize = x.size();
		System.out.println("The total number of data is : " + x.size());

		int rW1 = neuronNumbers[0];
		int cW1 = neuronNumbers[1];

		int rW2 = neuronNumbers[1];
		int cW2 = neuronNumbers[2];

		int rW3 = neuronNumbers[2];
		int cW3 = neuronNumbers[3];

		DoubleMatrix w1;
		DoubleMatrix w2;
		DoubleMatrix w3;
		DoubleMatrix b1;
		DoubleMatrix b2;
		DoubleMatrix b3;

		// System.out.println("If u want to use existing weight values, enter
		// yes.If entered anything else, random weight values are used.");
		// Scanner s = new Scanner(System.in);
		// String text = s.nextLine();
		//
		// if(text.equals("yes")) {
		//
		// System.out.println("Getting weights and biases from text
		// files........");
		// w1 =
		// IOUtils.getMatrixFromFile("WordCategoryWeights/wordCategoriserW1");
		// w2 =
		// IOUtils.getMatrixFromFile("WordCategoryWeights/wordCategoriserW2");
		// w3 =
		// IOUtils.getMatrixFromFile("WordCategoryWeights/wordCategoriserW3");
		// b1 =
		// IOUtils.getMatrixFromFile("WordCategoryWeights/wordCategoriserB1");
		// b2 =
		// IOUtils.getMatrixFromFile("WordCategoryWeights/wordCategoriserB2");
		// b3 =
		// IOUtils.getMatrixFromFile("WordCategoryWeights/wordCategoriserB3");
		//
		// System.out.println("weights and biases successfully retrieved");
		// }
		// else {
		// System.out.println("random weights being initialised....");
		//
		// w1 = DoubleMatrix.randn(rW1, cW1);
		// w2 = DoubleMatrix.randn(rW2, cW2);
		// w3 = DoubleMatrix.rand(rW3,cW3);
		// b1 = DoubleMatrix.rand(neuronNumbers[1]);
		// b2 = DoubleMatrix.rand(neuronNumbers[2]);
		// b3 = DoubleMatrix.rand(neuronNumbers[3]);
		//
		// System.out.println("random weights and biases initialisation
		// complete");
		// }

		// weigths.add(w1);
		// weigths.add(w2);
		// weigths.add(w3);
		// biases.add(b1);
		// biases.add(b2);
		// biases.add(b3);

		// System.out.println("enter how much neural networks to create: ");
		// Scanner s = new Scanner(System.in);
		// int num = s.nextInt();
		//
		// CountDownLatch l = new CountDownLatch(num);
		// ExecutorService executor = Executors.newCachedThreadPool();
		//
		// System.out.println("started......");
		// for(int i = 0;i < num;i++) {
		// w1 = DoubleMatrix.randn(rW1, cW1);
		// w2 = DoubleMatrix.randn(rW2, cW2);
		// w3 = DoubleMatrix.rand(rW3,cW3);
		// b1 = DoubleMatrix.rand(neuronNumbers[1]);
		// b2 = DoubleMatrix.rand(neuronNumbers[2]);
		// b3 = DoubleMatrix.rand(neuronNumbers[3]);
		//
		// weigths.add(w1);
		// weigths.add(w2);
		// weigths.add(w3);
		// biases.add(b1);
		// biases.add(b2);
		// biases.add(b3);
		//
		// wordCategList.add(new NeuralNetwork(1,weigths,biases,neuronNumbers,
		// 1.0,1.0,trainingData,trainingAns,validationData,validationAns,false,Integer.toString(i),l));
		// executor.submit(wordCategList.get(wordCategList.size() - 1));
		// }
		// executor.shutdown();
		//
		// try {
		// l.await();
		// System.out.println("all threads finished");
		// } catch (InterruptedException e2) {
		// // TODO Auto-generated catch block
		// e2.printStackTrace();
		// }
		//
		// DoubleMatrix avgW1 = DoubleMatrix.zeros(rW1, cW1);
		// DoubleMatrix avgW2 = DoubleMatrix.zeros(rW2, cW2);
		// DoubleMatrix avgW3 = DoubleMatrix.zeros(rW3,cW3);
		// DoubleMatrix avgB1 = DoubleMatrix.zeros(neuronNumbers[1]);
		// DoubleMatrix avgB2 = DoubleMatrix.zeros(neuronNumbers[2]);
		// DoubleMatrix avgB3 = DoubleMatrix.zeros(neuronNumbers[3]);
		//
		// for(int i = 0;i < wordCategList.size();i++) {
		// avgW1 = avgW1.add(wordCategList.get(i).getWeights().get(0));
		// avgW2 = avgW2.add(wordCategList.get(i).getWeights().get(1));
		// avgW3 = avgW3.add(wordCategList.get(i).getWeights().get(2));
		// avgB1 = avgB1.add(wordCategList.get(i).getBiases().get(0));
		// avgB2 = avgB2.add(wordCategList.get(i).getBiases().get(1));
		// avgB3 = avgB3.add(wordCategList.get(i).getBiases().get(2));
		// }
		//
		// double[][] weight1 = avgW1.toArray2();
		// double[][] weight2 = avgW2.toArray2();
		// double[][] weight3 = avgW3.toArray2();
		// double[][] bias1 = avgB1.toArray2();
		// double[][] bias2 = avgB2.toArray2();
		// double[][] bias3 = avgB3.toArray2();
		//
		// IOUtils.write2dArrayToFile("WordCategoryWeights/wordCategoriserW1AVG",
		// weight1);
		// IOUtils.write2dArrayToFile("WordCategoryWeights/wordCategoriserW2AVG",
		// weight2);
		// IOUtils.write2dArrayToFile("WordCategoryWeights/wordCategoriserW3AVG",
		// weight3);
		// IOUtils.write2dArrayToFile("WordCategoryWeights/wordCategoriserB1AVG",bias1);
		// IOUtils.write2dArrayToFile("WordCategoryWeights/wordCategoriserB2AVG",
		// bias2);
		// IOUtils.write2dArrayToFile("WordCategoryWeights/wordCategoriserB3AVG",
		// bias3);
		//
		// weigths.add(avgW1);
		// weigths.add(avgW2);
		// weigths.add(avgW3);
		// biases.add(avgB1);
		// biases.add(avgB2);
		// biases.add(avgB3);
		//
		// System.out.println("AVG net started....");
		//
		// Thread t1 = new Thread(new
		// NeuralNetwork(weigths,biases,neuronNumbers,
		// 1.0,1.0,trainingData,trainingAns,validationData,validationAns,"AVG"));
		// t1.start();
		// try {
		// t1.join();
		// } catch (InterruptedException e1) {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		// }
		//
		// try {
		// executor.awaitTermination(1, TimeUnit.DAYS);
		// System.out.println("All neuralNetworks completed");
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
//		File[] files = new File("/Users/alanjoshua/Documents/TrainingData - wordCateg").listFiles();
//		DataConverter con = new DataConverter(files,"Data",5000);
//		con.saveDataToFile();
//		DataGetter get = new DataGetter();
//		System.out.println("number of words is " + get.getNumOfWords());
//		System.out.println("number of words is " + get.getNumOfAnswers());

//		w1 = IOUtils.getMatrixFromFile("WordCategoryWeights\\wordCategoriserW1TEST_2017-11-12T17:05:58.152");
//		w2 = IOUtils.getMatrixFromFile("WordCategoryWeights\\wordCategoriserW2TEST_2017-11-12T17:05:58.152");
//		w3 = IOUtils.getMatrixFromFile("WordCategoryWeights\\wordCategoriserW3TEST_2017-11-12T17:05:58.152");
//		b1 = IOUtils.getMatrixFromFile("C:\\Users\\alanj\\eclipse-workspace\\Java-Neural-Network\\WordCategoryWeights\\wordCategoriserB1TEST_2017-11-12T17_05_58.152");
//		b2 = IOUtils.getMatrixFromFile("WordCategoryWeights\\wordCategoriserB2TEST_2017-11-12T17:05:58.152");
//		b3 = IOUtils.getMatrixFromFile("WordCategoryWeights\\wordCategoriserB3TEST_2017-11-12T17:05:58.152");
		
		w1 = DoubleMatrix.randn(neuronNumbers[0], neuronNumbers[1]);
		w2 = DoubleMatrix.randn(neuronNumbers[1], neuronNumbers[2]);
		w3 = DoubleMatrix.randn(neuronNumbers[2], neuronNumbers[3]);
		b1 = DoubleMatrix.rand(neuronNumbers[1]);
		b2 = DoubleMatrix.rand(neuronNumbers[2]);
		b3 = DoubleMatrix.rand(neuronNumbers[3]);
		
//		w1 = IOUtils.getMatrixFromFile("WordCategoryWeights\\wordCategoriserW1TEST_2017-11-12T17_05_58.152");
//		w2 = IOUtils.getMatrixFromFile("WordCategoryWeights\\wordCategoriserW2TEST_2017-11-12T17_05_58.152");
//		w3 = IOUtils.getMatrixFromFile("WordCategoryWeights\\wordCategoriserW3TEST_2017-11-12T17_05_58.152");
//		b1 = IOUtils.getMatrixFromFile("WordCategoryWeights\\wordCategoriserB1TEST_2017-11-12T17_05_58.152");
//		b2 = IOUtils.getMatrixFromFile("WordCategoryWeights\\wordCategoriserB2TEST_2017-11-12T17_05_58.152");
//		b3 = IOUtils.getMatrixFromFile("WordCategoryWeights\\wordCategoriserB3TEST_2017-11-12T17_05_58.152");
		
//		for(int i = 0;i < 3;i++) {
//			
//		}
		weigths.add(w1);
		weigths.add(w2);
		weigths.add(w3);
		biases.add(b1);
		biases.add(b2);
		biases.add(b3);

		// NeuralNetwork n1 = new NeuralNetwork(weigths,biases,neuronNumbers,
		// 0.005,0.0000001,trainingData, trainingAns,
		// validationData, validationAns, "TEST");
		
//		WordCategNN nn = new WordCategNN(neuronNumbers,"WOrdCateg",10,20,0.01,5,1.5,Color.YELLOW,Color.GREEN,0.01,0.00001,1000,200,x,y);
//		System.out.println("nn created");
//		
//		NormalFrame frame = new NormalFrame(1400,1000,nn,0.1,0.005,0.25,0.5);
//		System.out.println("frame created");
//		
//		nn.addFrame(frame);
//		System.out.println("frame added");
//		frame.initFrame(null);
//		System.out.println("frame inited controller");
//		NeuralNetwork n1 = new NeuralNetwork(neuronNumbers,0.01,0.0001,x,y,"TEST");
		
		NeuralNetwork N = new NeuralNetwork(neuronNumbers,weigths, biases,0.01,0.0001,x,y,"WordCateg");
		
		questionsList.clear();
		answersList.clear();

		// NeuralNetwork(1,neuronNumbers,0.1,0.0001,trainingData,trainingAns,validationData,validationAns,"AVG");

	}

	public void init() {
		x = new ArrayList<WordIns>();
		y = new ArrayList<DoubleMatrix>();
		
		answers = new ArrayList<double[]>();
		wordCategList = new ArrayList<NeuralNetwork>();
//		lock = new ReentrantLock();
		tagger = null;
		answersList = new ArrayList<List<double[]>>();
		questionsList = new ArrayList<List<double[]>>();
	
		File f = IOUtils.getFileFromResourcesFolder("taggers/wsj-0-18-bidirectional-distsim.tagger");
		tagger = new MaxentTagger(f.getPath());
//		tagger = new MaxentTagger("taggers\\wsj-0-18-bidirectional-distsim.tagger");
		

	}
	
	public List<String> parseSentence(String path) {
		
		List<String> ans = new ArrayList<String>();
		
		TokenizerFactory TOKENIZER_FACTORY = IndoEuropeanTokenizerFactory.INSTANCE;
		SentenceModel SENTENCE_MODEL = new MedlineSentenceModel();
		SentenceChunker SENTENCE_CHUNKER = new SentenceChunker(TOKENIZER_FACTORY, SENTENCE_MODEL);
	    
	    File file = IOUtils.getFileFromResourcesFolder(path);
//		try {
//			
//			InputStream initialStream = this.getClass().getResourceAsStream("/"+path);
//			byte[] buffer = new byte[initialStream.available()];
//		    initialStream.read(buffer);
//			file = File.createTempFile("temp", "parseSentence()");
//			OutputStream outStream = new FileOutputStream(file);
//		    outStream.write(buffer);
//		    
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		
//		File file = new File(path);
		
		String text = null;
		String line = "";
		try {
			text = Files.readFromFile(file, "UTF-8");
			System.out.println("Loaded file");
//			BufferedReader br = IOUtils.openBr(file);
//			while((line = br.readLine()) != null) {
//				text += line;
//			}
		} catch (IOException e) {
			System.err.println("Error in try parseSentence()");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		System.out.println("INPUT TEXT: ");
//		System.out.println(text);

		Chunking chunking = SENTENCE_CHUNKER.chunk(text.toCharArray(), 0, text.length());
		Set<Chunk> sentences = chunking.chunkSet();
		if (sentences.size() < 1) {
			System.out.println("No sentence chunks found.");
			return null;
		}
		String slice = chunking.charSequence().toString();
		int i = 1;
		for (Iterator<Chunk> it = sentences.iterator(); it.hasNext();) {
			Chunk sentence = it.next();
			int start = sentence.start();
			int end = sentence.end();
//			System.out.println("SENTENCE " + (i++) + ":");
//			System.out.println(slice.substring(start, end));
			String temp = slice.substring(start, end);
			if(temp.length() < 500) {
				ans.add(slice.substring(start, end));
			}
//			System.out.println(slice.substring(start, end));
		}
		System.out.println("Returning out of parse sentence");
		return ans;
	
	}

	public static void updateSentenceFile() {
		try {
			String temp = new String();
			IOUtils.sortFile(temp.getClass().getResource("UnsortedSentences").getPath(), temp.getClass().getResource("Sentences").getPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void updateTrainData() {

//		sentenceListAsStrings = IOUtils.convertFileToStringList("Sentences");
//		IOUtils.writeToFile(IOUtils.convertPDFToText("/Users/alanjoshua/Documents/Catching Fire.pdf"),"UnsortedSentences");
//		IOUtils.writeToFile(IOUtils.convertPDFToText("/Users/alanjoshua/Documents/book/Eoin Colfer - [Artemis Fowl 04] - The Opal Deception (v1.5).pdf"),"UnsortedSentences");
//		IOUtils.writeToFile(IOUtils.convertPDFToText("/Users/alanjoshua/Documents/book/Richard_Dawkins_The_Selfish_Gene.pdf"),"UnsortedSentences");
//		IOUtils.writeToFile(IOUtils.convertPDFToText("/Users/alanjoshua/Documents/Mocking Jay.pdf"),"UnsortedSentences");
//		IOUtils.writeToFile(IOUtils.convertPDFToText("/Users/alanjoshua/Documents/artemis_fowl_the_time_paradox.pdf"),"UnsortedSentences");
//		
		//		sentenceListAsStrings = parseSentence("UnsortedSentences");
//		System.out.println("finished loading files");
//		
//		ExecutorService exec = Executors.newFixedThreadPool((int)threads);
//		
//		System.out.println("number of sentences is :" + sentenceListAsStrings.size());
//		
//		int size = (int) (sentenceListAsStrings.size() * (1.0 / threads));
//		System.out.println("size is : " + size);
//		int diff = (int)(sentenceListAsStrings.size() - (size * threads));
//		System.out.println("diff is : " + diff);
//		
//		long start = System.currentTimeMillis();
//		
//		for (threadCounter = 0; threadCounter < threads; threadCounter++) {
//			exec.submit(new Runnable() {
//				public void run() {
//					long start = System.currentTimeMillis();
//					Pair res = getWordIns(sentenceListAsStrings.subList(threadCounter * size, (threadCounter * size) + size));
//					add(res.getAnswers(), res.getWordIns());
//					System.out.println((System.currentTimeMillis() - start) / 1000.0);
//				}
//			});
//		}
//		
//		exec.submit(new Runnable() {
//			public void run() {
//				long start = System.currentTimeMillis();
//				Pair res = getWordIns(sentenceListAsStrings.subList((int)(size * threads), (int)((size * threads) + diff)));
//				add(res.getAnswers(),res.getWordIns());
//				System.out.println((System.currentTimeMillis() - start) / 1000.0);
//			}
//		});
//		
//		exec.shutdown();
//		try {
//			exec.awaitTermination(1,TimeUnit.HOURS);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		long end  = System.currentTimeMillis();
//		System.out.println("Finished getting wordIns");
//		System.out.println("time taken is : " + (end - start) / 1000.0);
	}

//	public void add(List<double[]> ans, List<WordIns> words) {
//		
//		System.out.println("add called   " + count);
//		incrementCount();
//		List<DoubleMatrix> list = new ArrayList<DoubleMatrix>();
//		List<DoubleMatrix> list2 = new ArrayList<DoubleMatrix>();
//		
//		for(double[] x : ans) {
//			list.add(new DoubleMatrix(x).transpose());
//		}
//		
//		for(WordIns x : words) {
//			list2.add(new DoubleMatrix(x.getComponentArray().getArray()).transpose());
//		}
//
////			for (double[] x : ans) {
////				answers.add(x);
////			}
////
////			for (WordIns ins : words) {
////				wordIns.add(ins);
////			}
//			lock.lock();
//			try {
//			answersList.add(list);
//			questionsList.add(list2);
//			System.out.println("finished adding files   ");
//			}finally {
//				lock.unlock();
//			}
//	}
//	
//	public synchronized void incrementCount() {
//		count++;
//	}
//
	public Pair getWordIns(List<String> list) {

		List<String[]> POSedWords;
		List<String> POSedSentenceList;
		List<String[]> POSwordList;
		List<double[]> answers = new ArrayList<double[]>();

		POSedWords = new ArrayList<String[]>();
		POSedSentenceList = new ArrayList<String>();
		POSwordList = new ArrayList<String[]>();

		Pair result;
		
		System.out.println("inside getWordIns");
		
		System.out.println("Started tagging");
		for (String x : list) {
			POSedSentenceList.add(tagger.tagString(x));
		}
		System.out.println("Finished tagging");
		
		System.out.println("Stating splitting of words");
		// create POsedwords
		String[] split = null;
		for (String x : POSedSentenceList) {
			split = x.split("\\s+");
			POSedWords.add(split);
		}
		
		System.out.println("Finished creating POSed Words");
		
		System.out.println("outputEncoding..");
		
		
		for (int i = 0; i < POSedWords.size(); i++) {
			String[] templar = new String[POSedWords.get(i).length];
			for (int j = 0; j < POSedWords.get(i).length; j++) {
				templar[j] = ((POSedWords.get(i)[j]).split("_")[0]);
				answers.add(NeuralNetwork.outputEncoder(POSedWords.get(i)[j]));
				
			}
			
			POSwordList.add(templar);
		}
		
		System.out.println("Finished encoding");
		List<WordIns> wordIns = Utils.makeWordInstances(POSwordList);
//		Utils.addInstanceProperties(wordIns);

		result = new Pair(answers, wordIns);
		return result;
	}
}
