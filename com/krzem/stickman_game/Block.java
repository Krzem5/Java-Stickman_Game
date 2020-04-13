package com.krzem.stickman_game;



import com.jogamp.opengl.GL2;
import java.lang.Math;
import java.util.List;
import java.util.ArrayList;



public class Block extends Constants{
	public Main.Main_ cls;
	public Game g;
	public Terrain t;
	public Chunk c;
	public int x;
	public int z;
	public List<String> tl;
	public int ti;
	public int th;
	public double th_px;
	public List<Model> _m;
	public boolean _sm=false;
	public int _sm_o=0;



	public Block(Main.Main_ cls,Game g,Terrain t,Chunk c,int x,int z,List<String> tl){
		this.cls=cls;
		this.g=g;
		this.t=t;
		this.c=c;
		this.x=x;
		this.z=z;
		this.tl=tl;
		this._m=new ArrayList<Model>();
		for (int i=0;i<this.tl.size();i++){
			if (this.tl.get(i)==null){
				this.ti=i-1;
				return;
			}
		}
		this.ti=this.tl.size()-1;
	}



	public void update(GL2 gl){
		this.th=0;
		this.th_px=0;
		boolean ch=false;
		for (int i=0;i<this.tl.size();i++){
			if (this.tl.get(i)==null){
				continue;
			}
			while (i>=this._m.size()){
				this._m.add(null);
			}
			if (this._m.get(i)==null){
				this._m.set(i,OBJFileLoader.load(this.cls,gl,CHUNK_BLOCK_NAME_LOOKUP_TABLE.get(this.tl.get(i).split("@")[0])));
				this._m.get(i).scale(CHUNK_BLOCK_SIZE);
				ch=true;
			}
			if (ch==true){
				this._m.get(i).offset(this.c.ox*CHUNK_SIZE*CHUNK_BLOCK_SIZE+this.x*CHUNK_BLOCK_SIZE,this.th_px,this.c.oz*CHUNK_SIZE*CHUNK_BLOCK_SIZE+this.z*CHUNK_BLOCK_SIZE);
				if (this.tl.get(i).split("@").length==2){
					String[] sp=this.tl.get(i).split("@")[1].split(",");
					this._m.get(i).rotate(Integer.parseInt(sp[0])/180d*Math.PI,Integer.parseInt(sp[1])/180d*Math.PI,Integer.parseInt(sp[2])/180d*Math.PI);
				}
			}
			this.th_px+=this._m.get(i).height();
			this.th+=(this._m.get(i).height()<0.5?1:3);
		}
	}



	public void draw(GL2 gl){
		for (int i=0;i<this._m.size();i++){
			if (this._m.get(i)==null){
				continue;
			}
			this._m.get(i).draw(gl);
		}
	}



	public double height(double x,double z){
		if (this.ti>=this._m.size()||this._m.get(this.ti)==null){
			return 0;
		}
		return this._m.get(this.ti).max(x,z)+this.th_px;
	}



	@Override
	public String toString(){
		return String.format("Block(x=%d, z=%d, chunk_x=%d, chunk_z=%d, height=%d (%.3f px), top=%s)",this.x,this.z,this.c.ox,this.c.oz,this.th,this.th_px,this.tl.get(this.tl.size()-1));
	}



	public static Block from_height(Main.Main_ cls,Game g,Terrain t,Chunk c,int x,int z,int y){
		List<String> tl=new ArrayList<String>();
		for (int i=0;i<=y;i++){
			tl.add("grass_high");
		}
		return new Block(cls,g,t,c,x,z,tl);
	}
}