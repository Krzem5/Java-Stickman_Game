package com.krzem.stickman_game;



import com.jogamp.opengl.GL2;
import java.util.List;
import java.util.ArrayList;



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
		for (int i=0;i<CHUNK_SIZE;i++){
			for (int j=0;j<CHUNK_SIZE;j++){
				this.dt[i][j]._sm=false;
				this.dt[i][j]._sm_o=0;
			}
		}
	}



	private void _smooth(int x,int z,boolean f){
		List<Block> nbl=new ArrayList<Block>();
		List<Integer> nol=new ArrayList<Integer>();
		List<Boolean> nccl=new ArrayList<Boolean>();
		nbl.add(this.dt[x][z]);
		nol.add(0);
		nccl.add(false);
		while (nbl.size()>0){
			Block b_cls=nbl.remove(0);
			int o=nol.remove(0);
			boolean cc_=nccl.remove(0);
			if (o>b_cls._sm_o){
				for (int i=0;i<o-b_cls._sm_o;i++){
					b_cls.tl.add(Math.max(b_cls.tl.size()-2,0),"grass_low");
				}
			}
			else if (o<0&&b_cls._sm==false){
				o=Math.abs(o);
				// System.out.println(o);
				// System.out.println("C => "+Integer.toString(b_cls.tl.size()));
				// System.out.println(o);
				while (this._count(b_cls.tl,"grass_low")<o){
					b_cls.tl.remove(b_cls.tl.lastIndexOf("grass_high"));
					for (int i=0;i<3;i++){
						b_cls.tl.add(Math.max(b_cls.tl.size()-2,0),"grass_low");
					}
					// System.out.println(this._count(b_cls.tl,"grass_low"));
				}
				// System.out.println("C2 => "+Integer.toString(b_cls.tl.size()));
				for (int i=0;i<o-b_cls._sm_o;i++){
					b_cls.tl.remove(b_cls.tl.lastIndexOf("grass_low"));
				}
				o=-o;
				// System.out.println("D => "+Integer.toString(b_cls.tl.size()));
				// System.out.printf("%s, %d, %d\n",b_cls.tl.toString(),b_cls.ti,o);
			}
			if (cc_==true){
				Block a=b_cls.t.get(b_cls.c.ox*CHUNK_SIZE+b_cls.x-1,b_cls.c.oz*CHUNK_SIZE+b_cls.z);
				Block b=b_cls.t.get(b_cls.c.ox*CHUNK_SIZE+b_cls.x,b_cls.c.oz*CHUNK_SIZE+b_cls.z-1);
				Block c=b_cls.t.get(b_cls.c.ox*CHUNK_SIZE+b_cls.x+1,b_cls.c.oz*CHUNK_SIZE+b_cls.z);
				Block d=b_cls.t.get(b_cls.c.ox*CHUNK_SIZE+b_cls.x,b_cls.c.oz*CHUNK_SIZE+b_cls.z+1);
				if (a!=null&&b!=null&&(a.tl.get(a.tl.size()-1).equals("grass_slope@0,270,0")||a.tl.get(a.tl.size()-1).equals("grass_corner_slope@0,270,0"))&&(b.tl.get(b.tl.size()-1).equals("grass_slope@0,180,0")||b.tl.get(b.tl.size()-1).equals("grass_corner_slope@0,270,0"))){
					b_cls.tl.set(b_cls.tl.size()-1,"grass_corner_slope_high@0,270,0");
				}
				else if (b!=null&&c!=null&&(b.tl.get(b.tl.size()-1).equals("grass_slope@0,0,0")||b.tl.get(b.tl.size()-1).equals("grass_corner_slope@0,0,0"))&&(c.tl.get(c.tl.size()-1).equals("grass_slope@0,270,0")||c.tl.get(c.tl.size()-1).equals("grass_corner_slope@0,0,0"))){
					b_cls.tl.set(b_cls.tl.size()-1,"grass_corner_slope_high@0,0,0");
				}
				else if (c!=null&&d!=null&&(c.tl.get(c.tl.size()-1).equals("grass_slope@0,90,0")||c.tl.get(c.tl.size()-1).equals("grass_corner_slope@0,90,0"))&&(d.tl.get(d.tl.size()-1).equals("grass_slope@0,0,0")||d.tl.get(d.tl.size()-1).equals("grass_corner_slope@0,90,0"))){
					b_cls.tl.set(b_cls.tl.size()-1,"grass_corner_slope_high@0,90,0");
				}
				else if (a!=null&&d!=null&&(a.tl.get(a.tl.size()-1).equals("grass_slope@0,90,0")||a.tl.get(a.tl.size()-1).equals("grass_corner_slope@0,180,0"))&&(d.tl.get(d.tl.size()-1).equals("grass_slope@0,180,0")||d.tl.get(d.tl.size()-1).equals("grass_corner_slope@0,180,0"))){
					b_cls.tl.set(b_cls.tl.size()-1,"grass_corner_slope_high@0,180,0");
				}
				if (b_cls._sm==true){
					continue;
				}
			}
			if (b_cls._sm==true/*&&Math.abs(o)<=Math.abs(b_cls._sm_o)*/){
				continue;
			}
			System.out.printf("%s => %b, %d, %d\n",b_cls.toString(),b_cls._sm,o,b_cls._sm_o);
			b_cls._sm_o=(Math.abs(o)>=Math.abs(b_cls._sm_o)?o:b_cls._sm_o);
			b_cls._sm=true;
			o=Math.abs(o);
			int[] d=new int[]{2,2,2,2};
			int[] sc=new int[]{-1,-1,-1,-1};
			boolean[] cc=new boolean[4];
			if (b_cls.t.get(b_cls.c.ox*CHUNK_SIZE+b_cls.x-1,b_cls.c.oz*CHUNK_SIZE+b_cls.z)!=null){
				d[0]=this._sign(b_cls.t.get(b_cls.c.ox*CHUNK_SIZE+b_cls.x-1,b_cls.c.oz*CHUNK_SIZE+b_cls.z).th-b_cls.th);
				sc[0]=o+0;
			}
			if (b_cls.t.get(b_cls.c.ox*CHUNK_SIZE+b_cls.x,b_cls.c.oz*CHUNK_SIZE+b_cls.z-1)!=null){
				d[1]=this._sign(b_cls.t.get(b_cls.c.ox*CHUNK_SIZE+b_cls.x,b_cls.c.oz*CHUNK_SIZE+b_cls.z-1).th-b_cls.th);
				sc[1]=o+0;
			}
			if (b_cls.t.get(b_cls.c.ox*CHUNK_SIZE+b_cls.x+1,b_cls.c.oz*CHUNK_SIZE+b_cls.z)!=null){
				d[2]=this._sign(b_cls.t.get(b_cls.c.ox*CHUNK_SIZE+b_cls.x+1,b_cls.c.oz*CHUNK_SIZE+b_cls.z).th-b_cls.th);
				sc[2]=o+0;
			}
			if (b_cls.t.get(b_cls.c.ox*CHUNK_SIZE+b_cls.x,b_cls.c.oz*CHUNK_SIZE+b_cls.z+1)!=null){
				d[3]=this._sign(b_cls.t.get(b_cls.c.ox*CHUNK_SIZE+b_cls.x,b_cls.c.oz*CHUNK_SIZE+b_cls.z+1).th-b_cls.th);
				sc[3]=o+0;
			}
			if (d[0]==-1&&Math.abs(d[1])!=1&&Math.abs(d[2])!=1&&Math.abs(d[3])!=1){
				b_cls.tl.set(b_cls.tl.size()-1,"grass_slope@0,180,0");
				sc[0]=o+1;
				cc[1]=true;
				cc[3]=true;
			}
			else if (Math.abs(d[0])!=1&&d[1]==-1&&Math.abs(d[2])!=1&&Math.abs(d[3])!=1){
				b_cls.tl.set(b_cls.tl.size()-1,"grass_slope@0,270,0");
				sc[1]=o+1;
				cc[0]=true;
				cc[2]=true;
			}
			else if (Math.abs(d[0])!=1&&Math.abs(d[1])!=1&&d[2]==-1&&Math.abs(d[3])!=1){
				b_cls.tl.set(b_cls.tl.size()-1,"grass_slope@0,0,0");
				sc[2]=o+1;
				cc[1]=true;
				cc[3]=true;
			}
			else if (Math.abs(d[0])!=1&&Math.abs(d[1])!=1&&Math.abs(d[2])!=1&&d[3]==-1){
				b_cls.tl.set(b_cls.tl.size()-1,"grass_slope@0,90,0");
				sc[3]=o+1;
				cc[0]=true;
				cc[2]=true;
			}
			else if (d[0]==-1&&d[1]==-1&&Math.abs(d[2])!=1&&Math.abs(d[3])!=1){
				b_cls.tl.set(b_cls.tl.size()-1,"grass_corner_slope@0,270,0");
				sc[0]=o+1;
				sc[1]=o+1;
				cc[2]=true;
				cc[3]=true;
			}
			else if (Math.abs(d[0])!=1&&d[1]==-1&&d[2]==-1&&Math.abs(d[3])!=1){
				b_cls.tl.set(b_cls.tl.size()-1,"grass_corner_slope@0,0,0");
				sc[1]=o+1;
				sc[2]=o+1;
				cc[0]=true;
				cc[3]=true;
			}
			else if (Math.abs(d[0])!=1&&Math.abs(d[1])!=1&&d[2]==-1&&d[3]==-1){
				b_cls.tl.set(b_cls.tl.size()-1,"grass_corner_slope@0,90,0");
				sc[2]=o+1;
				sc[3]=o+1;
				cc[0]=true;
				cc[1]=true;
			}
			else if (d[0]==-1&&Math.abs(d[1])!=1&&Math.abs(d[2])!=1&&d[3]==-1){
				b_cls.tl.set(b_cls.tl.size()-1,"grass_corner_slope@0,180,0");
				sc[0]=o+1;
				sc[3]=o+1;
				cc[1]=true;
				cc[2]=true;
			}
			System.out.println(java.util.Arrays.toString(d));
			if (sc[0]!=-1&&d[0]==0){
				nbl.add(b_cls.t.get(b_cls.c.ox*CHUNK_SIZE+b_cls.x-1,b_cls.c.oz*CHUNK_SIZE+b_cls.z));
				nol.add(o+0);
				nccl.add(false);
			}
			if (sc[1]!=-1&&Math.abs(d[1])!=1){
				nbl.add(b_cls.t.get(b_cls.c.ox*CHUNK_SIZE+b_cls.x,b_cls.c.oz*CHUNK_SIZE+b_cls.z-1));
				nol.add(o+0);
				nccl.add(false);
			}
			if (sc[2]!=-1&&Math.abs(d[2])!=1){
				nbl.add(b_cls.t.get(b_cls.c.ox*CHUNK_SIZE+b_cls.x+1,b_cls.c.oz*CHUNK_SIZE+b_cls.z));
				nol.add(o+0);
				nccl.add(false);
			}
			if (sc[3]!=-1&&Math.abs(d[3])!=1){
				nbl.add(b_cls.t.get(b_cls.c.ox*CHUNK_SIZE+b_cls.x,b_cls.c.oz*CHUNK_SIZE+b_cls.z+1));
				nol.add(o+0);
				nccl.add(false);
			}
			// if (sc[0]!=-1&&(Math.abs(d[0])==1||cc[0]==true||f==true)){
			// 	nbl.add(b_cls.t.get(b_cls.c.ox*CHUNK_SIZE+b_cls.x-1,b_cls.c.oz*CHUNK_SIZE+b_cls.z));
			// 	nol.add((d[0]==-1?sc[0]:-o-1));
			// 	nccl.add(cc[0]);
			// }
			// if (sc[1]!=-1&&(Math.abs(d[1])==1||cc[1]==true||f==true)){
			// 	nbl.add(b_cls.t.get(b_cls.c.ox*CHUNK_SIZE+b_cls.x,b_cls.c.oz*CHUNK_SIZE+b_cls.z-1));
			// 	nol.add((d[1]==-1?sc[1]:-o-1));
			// 	nccl.add(cc[1]);
			// }
			// if (sc[2]!=-1&&(Math.abs(d[2])==1||cc[2]==true||f==true)){
			// 	nbl.add(b_cls.t.get(b_cls.c.ox*CHUNK_SIZE+b_cls.x+1,b_cls.c.oz*CHUNK_SIZE+b_cls.z));
			// 	nol.add((d[2]==-1?sc[2]:-o-1));
			// 	nccl.add(cc[2]);
			// }
			// if (sc[3]!=-1&&(Math.abs(d[3])==1||cc[3]==true||f==true)){
			// 	nbl.add(b_cls.t.get(b_cls.c.ox*CHUNK_SIZE+b_cls.x,b_cls.c.oz*CHUNK_SIZE+b_cls.z+1));
			// 	nol.add((d[3]==-1?sc[3]:-o-1));
			// 	nccl.add(cc[3]);
			// }
		}
	}



	private int _sign(double v){
		return (v==0?v<0?0:-1:1);
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