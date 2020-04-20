package com.krzem.stickman_game;



import com.jogamp.opengl.GL2;
import java.util.ArrayList;
import java.util.List;



public class Chunk extends Constants{
	public Main.Main_ cls;
	public Game g;
	public Terrain t;
	public int ox;
	public int oz;
	public Block[][] dt;




	public Chunk(Main.Main_ cls,Game g,Terrain t,int ox,int oz){
		this.cls=cls;
		this.g=g;
		this.t=t;
		this.ox=ox;
		this.oz=oz;
		this.dt=new Block[CHUNK_SIZE][CHUNK_SIZE];
		for (int i=0;i<CHUNK_SIZE;i++){
			for (int j=0;j<CHUNK_SIZE;j++){
				this.dt[i][j]=Block.from_height(this.cls,this.g,this.t,this,i,j,(int)(PerlinNoise.get((double)(i+this.ox*CHUNK_SIZE)/CHUNK_SIZE/CHUNK_GENERATION_NOISE_SCALE,(double)(j+this.oz*CHUNK_SIZE)/CHUNK_SIZE/CHUNK_GENERATION_NOISE_SCALE,0)*CHUNK_GENERATION_MAX_HEIGHT));
			}
		}
	}



	public void update(GL2 gl){
		for (int i=0;i<CHUNK_SIZE;i++){
			for (int j=0;j<CHUNK_SIZE;j++){
				this.dt[i][j].update(gl);
			}
		}
	}



	public void draw(GL2 gl){
		for (int i=0;i<CHUNK_SIZE;i++){
			for (int j=0;j<CHUNK_SIZE;j++){
				this.dt[i][j].draw(gl);
			}
		}
	}



	public void smooth(int x,int z){
		this._smooth(x,z,false);
	}



	public void smooth_all(){
		this._smooth(0,0,true);
	}



	private void _smooth(int x,int z,boolean f){
		List<Block> sbl=new ArrayList<Block>();
		List<Block> nbl=new ArrayList<Block>();
		List<Integer> nol=new ArrayList<Integer>();
		List<Boolean> nccl=new ArrayList<Boolean>();
		nbl.add(this.dt[x][z]);
		nol.add((this.t._gsm_o-this.dt[x][z].th)/3);
		nccl.add(false);
		while (nbl.size()>0){
			Block b_cls=nbl.remove(nbl.size()-1);
			int o=nol.remove(nol.size()-1);
			boolean cc_=nccl.remove(nccl.size()-1);
			sbl.add(b_cls);
			if (cc_==true){
				Block a=this.t.get(this.ox*CHUNK_SIZE+b_cls.x-1,this.oz*CHUNK_SIZE+b_cls.z);
				Block b=this.t.get(this.ox*CHUNK_SIZE+b_cls.x,this.oz*CHUNK_SIZE+b_cls.z-1);
				Block c=this.t.get(this.ox*CHUNK_SIZE+b_cls.x+1,this.oz*CHUNK_SIZE+b_cls.z);
				Block d=this.t.get(this.ox*CHUNK_SIZE+b_cls.x,this.oz*CHUNK_SIZE+b_cls.z+1);
				if (a!=null&&a.c!=this){
					a.c._smooth(a.x,a.z,f);
				}
				if (b!=null&&b.c!=this){
					b.c._smooth(b.x,b.z,f);
				}
				if (c!=null&&c.c!=this){
					c.c._smooth(c.x,c.z,f);
				}
				if (d!=null&&d.c!=this){
					d.c._smooth(d.x,d.z,f);
				}
				if (a!=null&&b!=null&&(a.tl.get(a.tl.size()-1).equals("grass_slope@0,270,0")||a.tl.get(a.tl.size()-1).equals("grass_corner_slope@0,270,0"))&&(b.tl.get(b.tl.size()-1).equals("grass_slope@0,180,0")||b.tl.get(b.tl.size()-1).equals("grass_corner_slope@0,270,0"))){
					b_cls.tl.set(b_cls.tl.size()-1,"grass_corner_slope_high@0,270,0");
					b_cls._m.clear();
				}
				else if (b!=null&&c!=null&&(b.tl.get(b.tl.size()-1).equals("grass_slope@0,0,0")||b.tl.get(b.tl.size()-1).equals("grass_corner_slope@0,0,0"))&&(c.tl.get(c.tl.size()-1).equals("grass_slope@0,270,0")||c.tl.get(c.tl.size()-1).equals("grass_corner_slope@0,0,0"))){
					b_cls.tl.set(b_cls.tl.size()-1,"grass_corner_slope_high@0,0,0");
					b_cls._m.clear();
				}
				else if (c!=null&&d!=null&&(c.tl.get(c.tl.size()-1).equals("grass_slope@0,90,0")||c.tl.get(c.tl.size()-1).equals("grass_corner_slope@0,90,0"))&&(d.tl.get(d.tl.size()-1).equals("grass_slope@0,0,0")||d.tl.get(d.tl.size()-1).equals("grass_corner_slope@0,90,0"))){
					b_cls.tl.set(b_cls.tl.size()-1,"grass_corner_slope_high@0,90,0");
					b_cls._m.clear();
				}
				else if (a!=null&&d!=null&&(a.tl.get(a.tl.size()-1).equals("grass_slope@0,90,0")||a.tl.get(a.tl.size()-1).equals("grass_corner_slope@0,180,0"))&&(d.tl.get(d.tl.size()-1).equals("grass_slope@0,180,0")||d.tl.get(d.tl.size()-1).equals("grass_corner_slope@0,180,0"))){
					b_cls.tl.set(b_cls.tl.size()-1,"grass_corner_slope_high@0,180,0");
					b_cls._m.clear();
				}
				continue;
			}
			if (o>0&&o>b_cls._sm_o){
				for (int i=0;i<o-b_cls._sm_o;i++){
					b_cls.tl.add(Math.max(b_cls.tl.size()-2,0),"grass_low");
				}
				b_cls._m.clear();
			}
			else if (o<0&&b_cls._sm_o==0){
				o=Math.abs(o);
				// System.out.printf("%d => %d ===> ",this._count(b_cls.tl,"grass_high"),this._count(b_cls.tl,"grass_low"));
				while (this._count(b_cls.tl,"grass_low")<o&&b_cls.tl.lastIndexOf("grass_high")!=-1){
					b_cls.tl.remove(b_cls.tl.lastIndexOf("grass_high"));
					for (int i=0;i<3;i++){
						b_cls.tl.add(Math.max(b_cls.tl.size()-1,0),"grass_low");
					}
				}
				System.out.println(this._count(b_cls.tl,"grass_low"));
				for (int i=0;i<o-b_cls._sm_o;i++){
					if (b_cls.tl.lastIndexOf("grass_low")==-1){
						break;
					}
					b_cls.tl.remove(b_cls.tl.lastIndexOf("grass_low"));
				}
				System.out.println(this._count(b_cls.tl,"grass_low"));
				o=-o;
				if (b_cls.tl.size()==0){
					b_cls.tl.add("grass_low");
				}
				b_cls._m.clear();
				// System.out.printf("%d => %d\n",this._count(b_cls.tl,"grass_high"),this._count(b_cls.tl,"grass_low"));
			}
			if (b_cls._sm==true){
				continue;
			}
			b_cls._sm_o=(Math.abs(o)>=Math.abs(b_cls._sm_o)?o:b_cls._sm_o);
			b_cls._sm=true;
			int[] d=new int[]{2,2,2,2};
			boolean[] cc=new boolean[4];
			boolean ch=false;
			if (this.t.get(this.ox*CHUNK_SIZE+b_cls.x-1,this.oz*CHUNK_SIZE+b_cls.z)!=null){
				d[0]=this._sign(this.t.get(this.ox*CHUNK_SIZE+b_cls.x-1,this.oz*CHUNK_SIZE+b_cls.z).th-b_cls.th);
			}
			if (this.t.get(this.ox*CHUNK_SIZE+b_cls.x,this.oz*CHUNK_SIZE+b_cls.z-1)!=null){
				d[1]=this._sign(this.t.get(this.ox*CHUNK_SIZE+b_cls.x,this.oz*CHUNK_SIZE+b_cls.z-1).th-b_cls.th);
			}
			if (this.t.get(this.ox*CHUNK_SIZE+b_cls.x+1,this.oz*CHUNK_SIZE+b_cls.z)!=null){
				d[2]=this._sign(this.t.get(this.ox*CHUNK_SIZE+b_cls.x+1,this.oz*CHUNK_SIZE+b_cls.z).th-b_cls.th);
			}
			if (this.t.get(this.ox*CHUNK_SIZE+b_cls.x,this.oz*CHUNK_SIZE+b_cls.z+1)!=null){
				d[3]=this._sign(this.t.get(this.ox*CHUNK_SIZE+b_cls.x,this.oz*CHUNK_SIZE+b_cls.z+1).th-b_cls.th);
			}
			if (d[0]==-1&&Math.abs(d[1])!=1&&Math.abs(d[2])!=1&&Math.abs(d[3])!=1){
				b_cls.tl.set(b_cls.tl.size()-1,"grass_slope@0,180,0");
				cc[1]=true;
				cc[3]=true;
				b_cls._m.clear();
				ch=true;
			}
			else if (Math.abs(d[0])!=1&&d[1]==-1&&Math.abs(d[2])!=1&&Math.abs(d[3])!=1){
				b_cls.tl.set(b_cls.tl.size()-1,"grass_slope@0,270,0");
				cc[0]=true;
				cc[2]=true;
				b_cls._m.clear();
				ch=true;
			}
			else if (Math.abs(d[0])!=1&&Math.abs(d[1])!=1&&d[2]==-1&&Math.abs(d[3])!=1){
				b_cls.tl.set(b_cls.tl.size()-1,"grass_slope@0,0,0");
				cc[1]=true;
				cc[3]=true;
				b_cls._m.clear();
				ch=true;
			}
			else if (Math.abs(d[0])!=1&&Math.abs(d[1])!=1&&Math.abs(d[2])!=1&&d[3]==-1){
				b_cls.tl.set(b_cls.tl.size()-1,"grass_slope@0,90,0");
				cc[0]=true;
				cc[2]=true;
				b_cls._m.clear();
				ch=true;
			}
			else if (d[0]==-1&&d[1]==-1&&Math.abs(d[2])!=1&&Math.abs(d[3])!=1){
				b_cls.tl.set(b_cls.tl.size()-1,"grass_corner_slope@0,270,0");
				cc[2]=true;
				cc[3]=true;
				b_cls._m.clear();
				ch=true;
			}
			else if (Math.abs(d[0])!=1&&d[1]==-1&&d[2]==-1&&Math.abs(d[3])!=1){
				b_cls.tl.set(b_cls.tl.size()-1,"grass_corner_slope@0,0,0");
				cc[0]=true;
				cc[3]=true;
				b_cls._m.clear();
				ch=true;
			}
			else if (Math.abs(d[0])!=1&&Math.abs(d[1])!=1&&d[2]==-1&&d[3]==-1){
				b_cls.tl.set(b_cls.tl.size()-1,"grass_corner_slope@0,90,0");
				cc[0]=true;
				cc[1]=true;
				b_cls._m.clear();
				ch=true;
			}
			else if (d[0]==-1&&Math.abs(d[1])!=1&&Math.abs(d[2])!=1&&d[3]==-1){
				b_cls.tl.set(b_cls.tl.size()-1,"grass_corner_slope@0,180,0");
				cc[1]=true;
				cc[2]=true;
				b_cls._m.clear();
				ch=true;
			}
			if ((ch==true&&f==false)||f==true){
				if (b_cls.x-1>=0&&Math.abs(d[0])==1){
					nbl.add(this.t.get(this.ox*CHUNK_SIZE+b_cls.x-1,this.oz*CHUNK_SIZE+b_cls.z));
					nol.add((d[0]==-1?o+1:o-1));
					nccl.add(false);
				}
				if (b_cls.z-1>=0&&Math.abs(d[1])==1){
					nbl.add(this.t.get(this.ox*CHUNK_SIZE+b_cls.x,this.oz*CHUNK_SIZE+b_cls.z-1));
					nol.add((d[1]==-1?o+1:o-1));
					nccl.add(false);
				}
				if (b_cls.x+1<CHUNK_SIZE&&Math.abs(d[2])==1){
					nbl.add(this.t.get(this.ox*CHUNK_SIZE+b_cls.x+1,this.oz*CHUNK_SIZE+b_cls.z));
					nol.add((d[2]==-1?o+1:o-1));
					nccl.add(false);
				}
				if (b_cls.z+1<CHUNK_SIZE&&Math.abs(d[3])==1){
					nbl.add(this.t.get(this.ox*CHUNK_SIZE+b_cls.x,this.oz*CHUNK_SIZE+b_cls.z+1));
					nol.add((d[3]==-1?o+1:o-1));
					nccl.add(false);
				}
				if (b_cls.x-1>=0&&d[0]==0){
					nbl.add(this.t.get(this.ox*CHUNK_SIZE+b_cls.x-1,this.oz*CHUNK_SIZE+b_cls.z));
					nol.add(o+0);
					nccl.add(false);
				}
				if (b_cls.z-1>=0&&d[1]==0){
					nbl.add(this.t.get(this.ox*CHUNK_SIZE+b_cls.x,this.oz*CHUNK_SIZE+b_cls.z-1));
					nol.add(o+0);
					nccl.add(false);
				}
				if (b_cls.x+1<CHUNK_SIZE&&d[2]==0){
					nbl.add(this.t.get(this.ox*CHUNK_SIZE+b_cls.x+1,this.oz*CHUNK_SIZE+b_cls.z));
					nol.add(o+0);
					nccl.add(false);
				}
				if (b_cls.z+1<CHUNK_SIZE&&d[3]==0){
					nbl.add(this.t.get(this.ox*CHUNK_SIZE+b_cls.x,this.oz*CHUNK_SIZE+b_cls.z+1));
					nol.add(o+0);
					nccl.add(false);
				}
			}
			if (cc[0]==true&&b_cls.x-1>=0){
				nbl.add(this.t.get(this.ox*CHUNK_SIZE+b_cls.x-1,this.oz*CHUNK_SIZE+b_cls.z));
				nol.add(0);
				nccl.add(true);
			}
			if (cc[1]==true&&b_cls.z-1>=0){
				nbl.add(this.t.get(this.ox*CHUNK_SIZE+b_cls.x,this.oz*CHUNK_SIZE+b_cls.z-1));
				nol.add(0);
				nccl.add(true);
			}
			if (cc[2]==true&&b_cls.x+1<CHUNK_SIZE){
				nbl.add(this.t.get(this.ox*CHUNK_SIZE+b_cls.x+1,this.oz*CHUNK_SIZE+b_cls.z));
				nol.add(0);
				nccl.add(true);
			}
			if (cc[3]==true&&b_cls.z+1<CHUNK_SIZE){
				nbl.add(this.t.get(this.ox*CHUNK_SIZE+b_cls.x,this.oz*CHUNK_SIZE+b_cls.z+1));
				nol.add(0);
				nccl.add(true);
			}
		}
		for (Block b:sbl){
			if (b.z==0&&this.oz==0){
				System.out.println(b.tl);
			}
		}
	}



	private int _sign(int v){
		return (v==0?0:(v<0?-1:1));
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
}