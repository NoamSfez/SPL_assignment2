package bgu.spl.mics.application;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;

import com.google.gson.Gson;

import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.services.*;

/** This is the Main class of the application. You should parse the input file, 
 * create the different components of the application, and run the system.
 * In the end, you should output a JSON.
 */
public class Main {
	
	public static void main(String[] args) throws IOException {
		Input in;
		Gson gson = new Gson();
		try(Reader reader = new FileReader(args[0])){
			in = gson.fromJson(reader, Input.class);
			
			MicroService Leia = new LeiaMicroservice(in.getAttacks());
			MicroService HanSolo = new HanSoloMicroservice();
			MicroService C3PO = new C3POMicroservice();
			MicroService R2D2 = new R2D2Microservice(in.getR2D2());
			MicroService Lando = new LandoMicroservice(in.getLando());
			
			Thread t1 = new Thread(Leia);
			Thread t2 = new Thread(HanSolo);
			Thread t3 = new Thread(C3PO);
			Thread t4 = new Thread(R2D2);
			Thread t5 = new Thread(Lando);	
			
			t2.start();
			t3.start();
			t4.start();
			t5.start();
			t1.start();

			t1.join();		
			t2.join();
			t3.join();
			t4.join();
			t5.join();
			
			FileWriter fw = new FileWriter("Output.json");
			gson.toJson(Diary.getInstance(), fw);
			fw.close();
		}
		catch (Exception e) {
			System.out.println("run Main");
		}
	}
}