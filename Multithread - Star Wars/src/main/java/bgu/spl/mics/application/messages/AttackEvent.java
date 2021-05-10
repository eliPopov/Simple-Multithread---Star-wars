package bgu.spl.mics.application.messages;
import bgu.spl.mics.Event;

import java.util.List;

public class AttackEvent implements Event<Boolean> {
	private List<Integer> ewokList;
	private long duration;

	public AttackEvent(List<Integer> serials, long duration){
	    this.ewokList = serials;
	    this.duration = duration;
   	}

	public List<Integer> getEwokList(){
		return ewokList;
	}
	public long getDuration(){
		return duration;
	}
}
