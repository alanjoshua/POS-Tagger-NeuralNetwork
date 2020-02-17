

import java.util.ArrayList;
import java.util.List;

import org.jblas.DoubleMatrix;

public class Utils {
	
	public static List<String[]> getWordListAsStrings(List<String> sentences) {
		List<String[]> ans = new ArrayList<String[]>();
 		String[] word; 
		
		for(int x = 0;x < sentences.size();x++) {
			
			word  = sentences.get(x).split(" ");
			ans.add(word);
		}
		return ans;
	}
	
//	public static List<String> getIndWordListAsString(List<String[]> list) {
//		List<String> present = new ArrayList<String>();
//		List<String> result = new ArrayList<String>();
//		
//		for(int x = 0;x < list.size();x++) {
//			if(!isPresent(present,list.get(x))) {
//			result.add(list.get(x));
//			present.add(list.get(x));
//			}
//		}
//		return result;
//	}
	
	public static List arrayToList(Object[] array) {
		List result = new ArrayList();
		for (Object x : array) {
			result.add(x);
		}
		return result;
	}
	
	public static List<String> concatList(List<String> parent,List<String> subList) {
		for(String x : subList) {
			parent.add(x);
		}
		return parent;
	}
	
	public static List<Word> makeWordObjList(List<String> list) {
		List<Word> result = new ArrayList<Word>();
		
		for(String x:list) {
			result.add(new Word(x.toLowerCase()));
		}
		
		return result;
	}
	
	public static void print(String[] array) {
		for(String x : array) {
			System.out.println(x);
		}
	}
	
	public static void print(double[][] array) {
		DoubleMatrix matrix = new DoubleMatrix(array);
		for(int i = 0;i < matrix.rows;i++) {
			for(int j = 0;j < matrix.columns;j++) {
				System.out.print(matrix.get(i, j) + "  ");
			}
			System.out.println();
		}
	}
	
	public static List<WordIns> makeWordInstances(List<String[]> list) {
		List<WordIns> ans = new ArrayList<WordIns>();
		for(String[] x : list) {
			for(int y = 0;y < x.length;y++) {
				ans.add(new WordIns(x[y],y + 1,x));
			}
		}
		return ans;
	}
	
//	public static void addInstanceProperties(List<WordIns> wordIns) {
//		for (WordIns w : wordIns) {
//			w.addComponents();
//		}
//	}
	
	public static boolean isPresent(List<String> check,String s) {
		for(int x = 0;x < check.size();x++) {
			if(s.equalsIgnoreCase(check.get(x))) return true; 
		}
		return false;
	}
	
	public static Word getWord(String text,List<Word> list) {
		for(Word w: list) {
			if(w.getText().equalsIgnoreCase(text)) {
				return w;
			}
		}
		return null;
	}
	
	public static void print(List list) {
		for(Object o: list) {
			if(o instanceof String) {
				System.out.println(((String)o));
			}
			if(o instanceof Integer) {
				System.out.print((int)o);
			}
		}
	}
	
	public static void print(double[] array) {
		for(double x: array) {
			System.out.print(x + "  ");
		}
		System.out.println();
	}
	
	public static void printToAI(String s) {
		ResourcePool.getAITextField().append(s);
	}
	
	public static void printToAI() {
		ResourcePool.getAITextField().append("");
	}
	
	public static void printlnToAI() {
		ResourcePool.getAITextField().append("\n");
	}
	
	public static void printlnToAI(String s) {
		ResourcePool.getAITextField().append(s + "\n");
	}
	
	public static double activationFunction(double x) {
		return 1 / (1 + Math.exp(-x));
//		return Math.max(0, x);
	}
	
	public static double activationPrime(double x) {
		return Math.exp(x) / Math.pow((1 + Math.exp(x)), 2);
//		return 1 / (1 + Math.exp(-x));
//		if(x > 0) {
//			return x;
//		}else {
//			return 0;
//		}
	}
	
	public static double compress(double x) {
		double ans = 0.0;
		if(x >= 0) {
			ans = 1 - (1 / (x +1));
		}
		if(x < 0) {
			ans = 0.0;
		}
		return ans;
	}
	
	public static DoubleMatrix activationFunction(DoubleMatrix z) {
		double[][] temp = z.toArray2();
		double[][] tempRes = new double[z.rows][z.columns];

		for (int i = 0; i < z.rows; i++) {
			for (int j = 0; j < z.columns; j++) {
				tempRes[i][j] = Utils.activationFunction(temp[i][j]);
			}
		}
		return new DoubleMatrix(tempRes);
	}
	
	public static List<Integer> convertStringToBit(String x) {
		
		byte[] arr = x.getBytes();
		
		String bit = "";
		
		for(byte b : arr) {
			bit += Integer.toBinaryString(b);
		}
		
		List<Integer> result = new ArrayList<Integer>();
		
		for(int i = 0;i < bit.length();i++) {
			x = bit.substring(i, i + 1);
			result.add(Integer.parseInt(x));
		}
		return result;
	}

	public static double[] ListToArray(List<Integer> list) {
		
		double[] result = new double[list.size()];
		
		for(int i = 0;i < list.size();i++) {
			result[i] = list.get(i).doubleValue();
		}
		
		return result;
	}
	
	public static double[] doubleListToArray(List<Double> list) {
		
		double[] result = new double[list.size()];
		
		for(int i = 0;i < list.size();i++) {
			result[i] = list.get(i);
		}
		
		return result;
	}

	public static DoubleMatrix activationPrime(DoubleMatrix z) {
		double[][] temp = z.toArray2();

		for (int i = 0; i < z.rows; i++) {
			for (int j = 0; j < z.columns; j++) {
				temp[i][j] = Utils.activationPrime(temp[i][j]);
			}
		}

		return new DoubleMatrix(temp);
	}


	
	public static double[] getLargestAsArray(double[] array) {
		double max = array[0];
		double[] result = new double[array.length];
		int index = 0;
		
		if(array.length == 1) {
			if(array[0] < 0.5) {
				result[0] = 0.0;
				return result;
			}
			else {
				result[0] = 1.0;
				return result;
			}
		}
		
		for(int i = 0;i < array.length;i++) {
			if(max < array[i]) {
				max = array[i];
				index = i;
			}
		}
		for(int i = 0;i < array.length;i++) {
			if(i != index) {
				result[i] = 0;
			}else {
				result[i] = 1;
			}
		}
		return result;
	}
	
	public static int[] getLargestAsArray(int[] array) {
		int max = array[0];
		int[] result = new int[array.length];
		int index = 0;
		for(int i = 0;i < array.length;i++) {
			if(max < array[i]) {
				max = array[i];
				index = i;
			}
		}
		for(int i = 0;i < array.length;i++) {
			if(i != index) {
				result[i] = 0;
			}else {
				result[i] = 1;
			}
		}
		return result;
	}
	
	public static float[] getLargestAsArray(float[] array) {
		float max = array[0];
		float[] result = new float[array.length];
		int index = 0;
		for(int i = 0;i < array.length;i++) {
			if(max < array[i]) {
				max = array[i];
				index = i;
			}
		}
		for(int i = 0;i < array.length;i++) {
			if(i != index) {
				result[i] = 0;
			}else {
				result[i] = 1;
			}
		}
		return result;
	}
	
}
