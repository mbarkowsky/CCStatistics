package threshold;

public class AvgDevThreshold extends Threshold{

	private String name;
	private double average;
	private double deviation;
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String getName(){
		return name;
	}

	public double getAverage() {
		return average;
	}
	
	public void setAverage(double average) {
		this.average = average;
	}
	
	public double getDeviation() {
		return deviation;
	}
	
	public void setDeviation(double deviation) {
		this.deviation = deviation;
	}

	@Override
	public ThresholdType getType() {
		return ThresholdType.AVG_DEV;
	}

	@Override
	public String getStringForSave() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(getType());
		buffer.append("#");
		buffer.append(getName());
		buffer.append(";");
		buffer.append(getAverage());
		buffer.append(";");
		buffer.append(getDeviation());
		return buffer.toString();
	}
	
}
