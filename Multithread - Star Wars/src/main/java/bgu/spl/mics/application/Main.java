package bgu.spl.mics.application;

import bgu.spl.mics.application.passiveObjects.*;
import bgu.spl.mics.application.services.*;
import com.google.gson.Gson;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/** This is the Main class of the application. You should parse the input file,
 * create the different components of the application, and run the system.
 * In the end, you should output a JSON.
 */
public class Main {
	public static void main(String[] args) {
		Gson gson = new Gson();
		Attack[] attacks = new Attack[0];
		long r2d2 = 0;
		long lando = 0;
		int ewokNum = 0;
		try (Reader reader = new FileReader(args[0])) {
			Input input = gson.fromJson(reader, Input.class);
			attacks = input.getAttacks();
			r2d2 = input.getR2D2();
			lando = input.getLando();
			ewokNum = input.getEwoks();
		}catch(IOException ignored){}
		Ewoks.setSize(ewokNum);
		Diary diary = Diary.getInstance();
		Thread Leia = new Thread(new LeiaMicroservice(attacks));
		Thread HanSolo = new Thread(new HanSoloMicroservice());
		Thread C3PO = new Thread(new C3POMicroservice());
		R2D2Microservice R2D2 = new R2D2Microservice(r2d2);
		LandoMicroservice Lando = new LandoMicroservice(lando);
		ExecutorService executor = Executors.newFixedThreadPool(5);
		executor.submit(Leia);
		executor.submit(HanSolo);
		executor.submit(C3PO);
		executor.submit(R2D2);
		executor.submit(Lando);
		executor.shutdown();
		while(!executor.isTerminated()) {
		}
		gson.toJson(diary.toString());
		System.out.println(diary.toString());
	}
}
