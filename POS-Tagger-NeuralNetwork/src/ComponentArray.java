

import java.util.ArrayList;
import java.util.List;

public class ComponentArray {
	
	private List<Double> componentArray;
//	private double[] componentArray;
	
	public ComponentArray() {
//		componentArray = new double[WordIns.numberOfComponents];
		componentArray = new ArrayList<Double>();
		resetAll();
	}
	
	public double[] getArray() {
		return Utils.doubleListToArray(componentArray);
	}
	
	public ComponentArray getComponentArray() {
		return this;
	}
	
	public void addComponent(int index,double value) {
//		componentArray[index] = Utils.activationPrime(value);
		componentArray.add(index, Utils.activationFunction(value));
//		componentArray[index] = value;
	}
	
	public void addComponent(double value) {
		componentArray.add(Utils.activationFunction(value));
//		componentArray[compInd] = value; 
	}
	
	public void addComponent(double value,boolean x) {
		if(x) {
			componentArray.add(Utils.activationFunction(value));
		}
		else {
			componentArray.add(value);
		}
//		componentArray[compInd] = value;
	}
	
	public Double getComponent(int index) {
		return componentArray.get(index);
	}
	
	public void resetComponent(int index) {
		componentArray.set(index, (double) 0);
	}
	
	public void resetAll() {
		componentArray.clear();
	}
	
}
