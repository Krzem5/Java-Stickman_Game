package com.krzem.stickman_game;



import com.jogamp.opengl.GL2;
import java.lang.Math;
import java.util.ArrayList;
import java.util.List;



public class Block extends Constants{
	public Main.Main_ cls;
	public Game g;
	public Terrain t;
	public Chunk c;
	public int x;
	public int z;
	public List<String> tl;
	public String tr;
	public int ti;
	public int th;
	public double th_px;
	public List<Model> _m;
	public List<Boolean> _mr;
	public Model _tr_m;
	public double[] _tr_off;
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
		this._mr=new ArrayList<Boolean>();
		this.tr=((this.tl.get(this.tl.size()-1).equals("grass_low")||this.tl.get(this.tl.size()-1).equals("grass_high"))&&RANDOM_GEN.nextDouble()<CHUNK_BLOCK_TERRIAIN_TREE_PROC?RANDOM_GEN.nextDouble()<=CHUNK_BLOCK_TERRIAIN_TREE_THIN_PROC?"tree_thin":"tree_thick":null);
		this._tr_off=new double[]{this._map(RANDOM_GEN.nextDouble(),0,1,CHUNK_BLOCK_TERRIAIN_TREE_OFFSET_MARGIN*CHUNK_BLOCK_SIZE,(1-CHUNK_BLOCK_TERRIAIN_TREE_OFFSET_MARGIN)*CHUNK_BLOCK_SIZE)-CHUNK_BLOCK_SIZE/2d,this._map(RANDOM_GEN.nextDouble(),0,1,CHUNK_BLOCK_TERRIAIN_TREE_OFFSET_MARGIN*CHUNK_BLOCK_SIZE,(1-CHUNK_BLOCK_TERRIAIN_TREE_OFFSET_MARGIN)*CHUNK_BLOCK_SIZE)-CHUNK_BLOCK_SIZE/2d};
	}



	public void update(GL2 gl){
		// while (this._count(this.tl,"grass_low")/3>=1){
		// 	for (int i=0;i<3;i++){
		// 		this.tl.remove(this.tl.lastIndexOf("grass_low"));
		// 	}
		// 	this.tl.add(this.tl.size()-1,"grass_high");
		// }
		this.th_px=0;
		boolean ch=false;
		for (int i=0;i<this.tl.size();i++){
			if (this.tl.get(i)==null){
				continue;
			}
			while (i>=this._m.size()){
				this._m.add(null);
				this._mr.add(true);
			}
			if (this._m.get(i)==null){
				this._m.set(i,OBJFileLoader.load(this.cls,gl,CHUNK_BLOCK_NAME_LOOKUP_TABLE.get(this.tl.get(i).split("@")[0])));
				;
				this._mr.set(i,(i<this.tl.size()-1?(this._check_visible(this.t.get(this.c.ox*CHUNK_SIZE+this.x-1,this.c.oz*CHUNK_SIZE+this.z),this.th_px,this._m.get(i).height())==true||this._check_visible(this.t.get(this.c.ox*CHUNK_SIZE+this.x,this.c.oz*CHUNK_SIZE+this.z-1),this.th_px,this._m.get(i).height())==true||this._check_visible(this.t.get(this.c.ox*CHUNK_SIZE+this.x+1,this.c.oz*CHUNK_SIZE+this.z),this.th_px,this._m.get(i).height())==true||this._check_visible(this.t.get(this.c.ox*CHUNK_SIZE+this.x,this.c.oz*CHUNK_SIZE+this.z+1),this.th_px,this._m.get(i).height())==true?true:false):true));
				this._m.get(i).scale(CHUNK_BLOCK_SIZE);
				this._tr_m=null;
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
		}
		if (this.tl.get(this.tl.size()-1).equals("grass_low")==false&&this.tl.get(this.tl.size()-1).equals("grass_high")==false){
			this.tr=null;
		}
		if (this._tr_m==null&&this.tr!=null){
			this._tr_m=OBJFileLoader.load(this.cls,gl,CHUNK_BLOCK_NAME_LOOKUP_TABLE.get(this.tr));
			this._tr_m.scale(CHUNK_BLOCK_TERRIAIN_TREE_SIZE);
			this._tr_m.offset(this.c.ox*CHUNK_SIZE*CHUNK_BLOCK_SIZE+this.x*CHUNK_BLOCK_SIZE+this._tr_off[0],this.th_px,this.c.oz*CHUNK_SIZE*CHUNK_BLOCK_SIZE+this.z*CHUNK_BLOCK_SIZE+this._tr_off[1]);
		}
		this.th=this.tl.size()*3;
	}



	public void draw(GL2 gl){
		for (int i=0;i<this._m.size();i++){
			if (this._m.get(i)==null||this._mr.get(i)==false){
				continue;
			}
			this._m.get(i).draw(gl);
		}
		if (this._tr_m!=null){
			this._tr_m.draw(gl);
		}
	}



	public double height(double x,double z){
		if (this.tl.size()-1>=this._m.size()||this._m.get(this.tl.size()-1)==null){
			return 0;
		}
		return this._m.get(this.tl.size()-1).max(x,z)+this.th_px;
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



	private int _count(List<String> a,String s){
		int o=0;
		for (int i=0;i<a.size();i++){
			if (a.get(i).equals(s)==true){
				o++;
			}
		}
		return o;
	}



	private double _map(double v,double aa,double ab,double ba,double bb){
		return (v-aa)/(ab-aa)*(bb-ba)+ba;
	}



	private boolean _check_visible(Block b,double s,double h){
		if (b==null){
			return true;
		}
		///////////////////////////////
		return false;
	}
}