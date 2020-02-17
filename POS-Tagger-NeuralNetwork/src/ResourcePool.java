

import javax.swing.JTextArea;

public class ResourcePool {

	private static Object lock1 = new Object();
	private static Object lock2 = new Object();
	private static Object lock3 = new Object();
	private static Object lock4 = new Object();

	private static JTextArea AITextField = new JTextArea();
	private static boolean start = false;
	private static JTextArea inputTextArea = new JTextArea();
	private static boolean isValidating = false;
	private static boolean isTraining = false;
	
	public final static String outputJar = "./Resources/";
	public final static String outputProject = "";
	public static String outputMode = outputProject;

	public static void setAITextField(JTextArea AITextField) {
		synchronized (lock1) {
			ResourcePool.AITextField = AITextField;
		}
	}

	public static void setIsTraining(boolean x) {
		synchronized (lock4) {
			isTraining = x;
		}
	}

	public static boolean getIsTraining() {
		synchronized (lock4) {
			return isTraining;
		}
	}

	public static void setIsValidating(boolean x) {
		synchronized (lock3) {
			isValidating = x;
		}
	}

	public static boolean getIsValidating() {
		synchronized (lock3) {
			return isValidating;
		}
	}

	public static String getIinput() {
		String input;
		while (1 < 3) {
			input = inputTextArea.getText();
			if (input != null) {
				return input;
			}
		}
	}

	public static void setInputTextArea(JTextArea inputTextArea) {
		synchronized (lock2) {
			ResourcePool.inputTextArea = inputTextArea;
		}
	}

	public static void setStartTrue() {
		ResourcePool.start = true;
	}

	public static boolean getStart() {
		return start;
	}

	public static JTextArea getAITextField() {
		return AITextField;
	}

	public static JTextArea geInputTextField() {
		return inputTextArea;
	}
}
