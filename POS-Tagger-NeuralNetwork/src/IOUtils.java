
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

//import org.apache.pdfbox.pdmodel.PDDocument;
//import org.apache.pdfbox.text.PDFTextStripper;
import org.jblas.DoubleMatrix;

public class IOUtils {

	public static BufferedReader openBr(File f) {
		try {
			File parent = f.getParentFile();

			if (parent != null) {
				boolean isParentDir = f.getParentFile().exists();
				if (!isParentDir) {
					System.out.println("trying to create new file");
					f.getParentFile().mkdirs();
				}
			}

			f.createNewFile();

			return new BufferedReader(new FileReader(f));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

//	Credits for this method to https://www.baeldung.com/convert-input-stream-to-a-file
	public static File getFileFromResourcesFolder(String path) {

		File file = null;

//		Use Below 2 lines of code when using 
		if (ResourcePool.outputMode == ResourcePool.outputProject) {
			String temp = new String();
			String p = ClassLoader.getSystemResource(""+path).getPath();
			file = new File(p);
		} 
		else {
//		Use Below Code in try block when making jar file
			try {
				InputStream initialStream = ClassLoader.getSystemClassLoader()
						.getResourceAsStream("./Resources/" + path);

				byte[] buffer = new byte[initialStream.available()];
				initialStream.read(buffer);
				file = File.createTempFile("temp", "parseSentence()");
				OutputStream outStream = new FileOutputStream(file);
				outStream.write(buffer);

				outStream.flush();
				outStream.close();
				initialStream.close();

			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return file;
	}

	public static BufferedReader openBr(String s) {
		try {
			File f = new File(s);

			File parent = f.getParentFile();

			if (parent != null) {
				boolean isParentDir = f.getParentFile().exists();
				if (!isParentDir) {
					System.out.println("trying to create new parent directory");
					f.getParentFile().mkdirs();
				}
			}

			f.createNewFile();

			return new BufferedReader(new FileReader(f));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static BufferedWriter openBw(File f) {
		try {
			File parent = f.getParentFile();

			if (parent != null) {
				boolean isParentDir = f.getParentFile().exists();
				if (!isParentDir) {
					System.out.println("trying to create new file");
					f.getParentFile().mkdirs();
				}
			}
			f.createNewFile();

			return new BufferedWriter(new FileWriter(f));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static BufferedWriter openBw(String s) {
		try {
			File f = new File(s);

			File parent = f.getParentFile();

			if (parent != null) {
				boolean isParentDir = f.getParentFile().exists();
				if (!isParentDir) {
					System.out.println("trying to create new file");
					f.getParentFile().mkdirs();
				}
			}

			f.createNewFile();

			return new BufferedWriter(new FileWriter(f));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static BufferedWriter openBw(String s, boolean shouldAppend) {
		try {
			File f = new File(s);

			File parent = f.getParentFile();

			if (parent != null) {
				boolean isParentDir = f.getParentFile().exists();
				if (!isParentDir) {
					System.out.println("trying to create new file");
					f.getParentFile().mkdirs();
				}
			}

			f.createNewFile();

			return new BufferedWriter(new FileWriter(f, shouldAppend));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static List<String> convertFileToStringList(File f) {
		List<String> result = new ArrayList<String>();
		String line;
		BufferedReader br = openBr(f);
		try {
			while ((line = br.readLine()) != null) {
				result.add(line);
			}
			return result;
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("error while making list of String sentences from file");
			return null;
		}
	}

	public static List<String> convertFileToStringList(String s) {
		List<String> result = new ArrayList<String>();
		String line;
		BufferedReader br = openBr(s);
		try {
			while ((line = br.readLine()) != null) {
				result.add(line);
			}
			return result;
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("error while making list of String sentences from file");
			return null;
		}
	}

	public static void write2dArrayToFile(String filename, double[][] array) {

		BufferedWriter bw = IOUtils.openBw(filename);
		DoubleMatrix mat = new DoubleMatrix(array);
		for (int i = 0; i < mat.rows; i++) {
			for (int j = 0; j < mat.columns; j++) {
				String temp = Double.toString(mat.get(i, j));
				try {
					bw.write(temp + " ");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			try {
				bw.newLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			bw.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static DoubleMatrix getMatrixFromFile(String file) {
		BufferedReader br = IOUtils.openBr(file);

		int cols;
		int rows;

		List<String> temp = new ArrayList<String>();
		String text = null;
		try {
			while ((text = br.readLine()) != null) {
				temp.add(text);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cols = temp.get(0).split("\\s+").length;
		rows = temp.size();

		double[][] array = new double[rows][cols];
		String[] temperer;

		for (int i = 0; i < rows; i++) {
			temperer = temp.get(i).split("\\s+");
			for (int j = 0; j < cols; j++) {
				array[i][j] = Double.parseDouble(temperer[j]);
			}
		}
		return new DoubleMatrix(array);

	}
//
//	public static String convertPDFToText(String src) {
//		    PDDocument doc = null;
//			try {
//				doc = PDDocument.load(new File(src));
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		    try {
//				return new PDFTextStripper().getText(doc);
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			return null;
//	}

	public static void writeToFile(String text, String f) {
		BufferedWriter bw = openBw(f, true);
		try {
			bw.write(text);
			bw.flush();
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void formatFile(String file) {
		BufferedReader br = openBr(file);
		boolean format = false;
		List<String> formatted = new ArrayList<String>();
		String text = "";

		try {
			while ((text = br.readLine()) != null) {
				if (text.charAt(0) == ' ') {
					text = text.substring(1, text.length());
					formatted.add(text);
					format = true;
				}
			}
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (format) {
			BufferedWriter bw = openBw(file);
			for (String x : formatted) {
				try {
					bw.write(x);
					bw.newLine();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			try {
				bw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void sortFile(String unsorted, String f) throws IOException {
		BufferedReader br = IOUtils.openBr(unsorted);
		String unsort = "";
		String[] sentences;

		String temp;
		while ((temp = br.readLine()) != null) {
			unsort += temp;
		}
		sentences = unsort.split("(?<=(?<![A-Z])\\.)");
		BufferedWriter bw = IOUtils.openBw(f, true);
		for (String x : sentences) {
			if (!(x.equals(""))) {
				bw.write(x);
				bw.newLine();
			}
			bw.flush();
		}
		bw = openBw(unsorted);
		bw.write("");
		bw.flush();
		bw.close();
	}

}
