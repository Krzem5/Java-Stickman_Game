package com.krzem.stickman_game;



import com.jogamp.opengl.GL2;
import java.util.ArrayList;
import java.util.List;



public class Game extends Constants{
	public Main.Main_ cls;
	public Skybox sb;
	public Terrain t;
	public GUI gui;



	public Game(Main.Main_ cls){
		this.cls=cls;
		this.sb=new Skybox(this.cls,this);
		this.t=new Terrain(this.cls,this);
		this.gui=new GUI(this.cls,this);
	}



	public void update(GL2 gl){
		this.t.update(gl);
		this.gui.update(gl);
	}



	public void draw(GL2 gl){
		// this.sb.draw(gl);
		this.t.draw(gl);
		this.gui.draw(gl);
	}
}