package bgu.spl.mics.application;
import java.io.*;

import com.google.gson.Gson;

import bgu.spl.mics.application.passiveObjects.Attack;

public class Input {
	
	private Attack[] attacks;
	int R2D2;
	int Lando;
	int Ewoks;

	public Attack[] getAttacks() {
		return attacks;
	}

	public void setAttacks(Attack[] attacks) {
		this.attacks = attacks;
	}

	public int getR2D2() {
		return R2D2;
	}

	public void setR2D2(int r2d2) {
		R2D2 = r2d2;
	}

	public int getLando() {
		return Lando;
	}

	public void setLando(int lando) {
		Lando = lando;
	}

	public int getEwoks() {
		return Ewoks;
	}

	public void setEwoks(int ewoks) {
		Ewoks = ewoks;
	}

	
}
