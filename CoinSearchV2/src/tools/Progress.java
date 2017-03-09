package tools;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class Progress {
	private DoubleProperty progress;	

	public double getProgress() {
		if(progress != null){
			return progress.get();
		}
		return 0;
	}
	
	public void setProgress(double progress){
		this.progressProperty().set(progress);
	}
	
	public DoubleProperty progressProperty(){
		if(progress == null){
			progress = new SimpleDoubleProperty(0);
		}
		return progress;
	}
}