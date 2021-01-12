package com.krzem.stickman_game;



import com.jogamp.opengl.GL2;
import java.awt.Cursor;
import java.util.HashMap;
import java.util.Map;



public class Terrain extends Constants{
	public Main.Main_ cls;
	public Game g;
	public Map<String,Chunk> cl;
	public int _gsm_o;



	public Terrain(Main.Main_ cls,Game g){
		this.cls=cls;
		this.g=g;
	}



	public void update(GL2 gl){
		if (this.cl==null){
			this._generate(gl);
		}
		for (Map.Entry<String,Chunk> e:this.cl.entrySet()){
			e.getValue().update(gl);
		}
	}



	public void draw(GL2 gl){
		for (Map.Entry<String,Chunk> e:this.cl.entrySet()){
			e.getValue().draw(gl);
		}
	}



	public double height_at(double x,double z){
		int cx=(int)x/(CHUNK_SIZE*CHUNK_BLOCK_SIZE)-(x<0?1:0);
		int cz=(int)z/(CHUNK_SIZE*CHUNK_BLOCK_SIZE)-(z<0?1:0);
		if ((int)x-cx*(CHUNK_SIZE*CHUNK_BLOCK_SIZE)==(CHUNK_SIZE*CHUNK_BLOCK_SIZE)){
			cx++;
		}
		if ((int)z-cz*(CHUNK_SIZE*CHUNK_BLOCK_SIZE)==(CHUNK_SIZE*CHUNK_BLOCK_SIZE)){
			cz++;
		}
		if (this.cl.get(String.format("%d,%d",cx,cz))==null){
			return 0;
		}
		return this.cl.get(String.format("%d,%d",cx,cz)).dt[(int)(x-cx*(CHUNK_SIZE*CHUNK_BLOCK_SIZE))/CHUNK_BLOCK_SIZE][(int)(z-cz*(CHUNK_SIZE*CHUNK_BLOCK_SIZE))/CHUNK_BLOCK_SIZE].height(((x-cx*(CHUNK_SIZE*CHUNK_BLOCK_SIZE))%CHUNK_BLOCK_SIZE)/CHUNK_BLOCK_SIZE,((z-cz*(CHUNK_SIZE*CHUNK_BLOCK_SIZE))%CHUNK_BLOCK_SIZE)/CHUNK_BLOCK_SIZE);
	}



	public Block get(int x,int z){
		int cx=x/CHUNK_SIZE-(x<0?1:0);
		int cz=z/CHUNK_SIZE-(z<0?1:0);
		if (x-cx*CHUNK_SIZE==CHUNK_SIZE){
			cx++;
		}
		if (z-cz*CHUNK_SIZE==CHUNK_SIZE){
			cz++;
		}
		if (this.cl.get(String.format("%d,%d",cx,cz))==null){
			return null;
		}
		return this.cl.get(String.format("%d,%d",cx,cz)).dt[x-cx*CHUNK_SIZE][z-cz*CHUNK_SIZE];
	}



	private void _generate(GL2 gl){
		this.cl=new HashMap<String,Chunk>();
		this.cl.put("0,0",new Chunk(this.cls,this.g,this,0,0));
		this.cl.put("0,1",new Chunk(this.cls,this.g,this,0,1));
		this.cl.put("1,0",new Chunk(this.cls,this.g,this,1,0));
		this.cl.put("1,1",new Chunk(this.cls,this.g,this,1,1));
		for (Map.Entry<String,Chunk> e:this.cl.entrySet()){
			e.getValue().update(gl);
		}
		this._gsm_o=this.cl.get("0,0").dt[0][0].th+0;
		for (Map.Entry<String,Chunk> e:this.cl.entrySet()){
			e.getValue().smooth_all();
		}
	}
}