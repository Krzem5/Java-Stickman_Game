package com.krzem.stickman_game;



import com.jogamp.opengl.GL2;
import java.lang.Math;
import java.util.HashMap;
import java.util.Map;



public class Stickman extends Constants{
	public Main.Main_ cls;
	public Game g;
	public double[] pos;
	public Map<String,double[]> jl;
	private Smooth _tzs;
	private Smooth _txs;
	private Smooth _lazs;
	private Smooth _laxs;
	private Smooth _razs;
	private Smooth _raxs;



	public Stickman(Main.Main_ cls,Game g,double[] pos){
		this.cls=cls;
		this.g=g;
		this.pos=pos;
		this.jl=new HashMap<String,double[]>(){{
			for (Map.Entry<String,double[]> e:STICKMAN_DEFAULT_JOIN_ROTATIONS.entrySet()){
				this.put(e.getKey(),new double[]{e.getValue()[0],e.getValue()[1],e.getValue()[2]});
			}
		}};
		this._tzs=new Smooth(1,0.001,Math.PI/50,false,true);
		this._txs=new Smooth(0.5,0.0025,Math.PI/80,true,true);
		this._lazs=new Smooth(0.5,0.003,Math.PI/45,true,true);
		this._laxs=new Smooth(0.5,0.002,Math.PI/75,true,true);
		this._razs=new Smooth(0.5,0.003,Math.PI/45,false,true);
		this._raxs=new Smooth(0.5,0.002,Math.PI/75,false,true);
	}



	public void update(GL2 gl){
		this.jl.get("torso")[0]+=this._tzs.next();
		this.jl.get("torso")[2]+=this._txs.next();
		this.jl.get("Larm")[0]+=this._lazs.next();
		this.jl.get("Larm")[2]+=this._laxs.next();
		this.jl.get("Rarm")[0]+=this._razs.next();
		this.jl.get("Rarm")[2]+=this._raxs.next();
	}



	public void draw(GL2 gl){
		gl.glBegin(GL2.GL_LINES);
		gl.glColor3d(1,0,0);
		this._vertex(gl,this.pos);
		this._vertex(gl,new double[]{this.pos[0]+5,this.pos[1],this.pos[2]});
		gl.glColor3d(0,1,0);
		this._vertex(gl,this.pos);
		this._vertex(gl,new double[]{this.pos[0],this.pos[1]+5,this.pos[2]});
		gl.glColor3d(0,0,1);
		this._vertex(gl,this.pos);
		this._vertex(gl,new double[]{this.pos[0],this.pos[1],this.pos[2]+5});
		gl.glColor3d(0.5,0,0.7);
		double[] b=this._add(this._rot(STICKMAN_POS_OFFSET,this.jl.get("body")),this.pos);
		double[] ll=this._add(b,this._rot(new double[]{0,STICKMAN_JOIN_LENGTHS.get("Lleg"),0},this.jl.get("body"),this.jl.get("Lleg")));
		double[] rl=this._add(b,this._rot(new double[]{0,STICKMAN_JOIN_LENGTHS.get("Rleg"),0},this.jl.get("body"),this.jl.get("Rleg")));
		double[] t=this._add(b,this._rot(new double[]{0,STICKMAN_JOIN_LENGTHS.get("torso"),0},this.jl.get("body"),this.jl.get("torso")));
		double[] la=this._add(t,this._rot(new double[]{0,STICKMAN_JOIN_LENGTHS.get("Larm"),0},this.jl.get("body"),this.jl.get("torso"),this.jl.get("Larm")));
		double[] ra=this._add(t,this._rot(new double[]{0,STICKMAN_JOIN_LENGTHS.get("Rarm"),0},this.jl.get("body"),this.jl.get("torso"),this.jl.get("Rarm")));
		double[] n=this._add(t,this._rot(new double[]{0,STICKMAN_JOIN_LENGTHS.get("neck"),0},this.jl.get("body"),this.jl.get("torso"),this.jl.get("neck")));
		double[] h=this._add(n,this._rot(new double[]{0,STICKMAN_JOIN_LENGTHS.get("head"),0},this.jl.get("body"),this.jl.get("torso"),this.jl.get("neck"),this.jl.get("head")));
		double[] lh=this._add(h,this._rot(new double[]{0,STICKMAN_JOIN_LENGTHS.get("Lhead"),0},this.jl.get("body"),this.jl.get("torso"),this.jl.get("neck"),this.jl.get("head"),this.jl.get("Lhead")));
		double[] rh=this._add(h,this._rot(new double[]{0,STICKMAN_JOIN_LENGTHS.get("Rhead"),0},this.jl.get("body"),this.jl.get("torso"),this.jl.get("neck"),this.jl.get("head"),this.jl.get("Rhead")));
		this._line(gl,b,ll);
		this._line(gl,b,rl);
		this._line(gl,b,t);
		this._line(gl,t,la);
		this._line(gl,t,ra);
		this._line(gl,t,n);
		this._line(gl,n,lh);
		this._line(gl,n,rh);
		this._line(gl,lh,rh);
		gl.glEnd();
	}



	private void _line(GL2 gl,double[] a,double[] b){
		gl.glVertex3d(a[0],a[1],a[2]);
		gl.glVertex3d(b[0],b[1],b[2]);
	}



	private void _vertex(GL2 gl,double[] p){
		gl.glVertex3d(p[0],p[1],p[2]);
	}



	private double[] _rot(double[] o,double[] ...rl){
		for (int i=rl.length-1;i>=0;i--){
			o=new double[]{o[0]*Math.cos(rl[i][0])*Math.cos(rl[i][1])+o[1]*(Math.cos(rl[i][0])*Math.sin(rl[i][1])*Math.sin(rl[i][2])-Math.sin(rl[i][0])*Math.cos(rl[i][2]))+o[2]*(Math.cos(rl[i][0])*Math.sin(rl[i][1])*Math.cos(rl[i][2])+Math.sin(rl[i][0])*Math.sin(rl[i][2])),o[0]*Math.sin(rl[i][0])*Math.cos(rl[i][1])+o[1]*(Math.sin(rl[i][0])*Math.sin(rl[i][1])*Math.sin(rl[i][2])+Math.cos(rl[i][0])*Math.cos(rl[i][2]))+o[2]*(Math.sin(rl[i][0])*Math.sin(rl[i][1])*Math.cos(rl[i][2])-Math.cos(rl[i][0])*Math.sin(rl[i][2])),-o[0]*Math.sin(rl[i][1])+o[1]*Math.cos(rl[i][1])*Math.sin(rl[i][2])+o[2]*Math.cos(rl[i][1])*Math.cos(rl[i][2])};
		}
		return o;
	}



	private double[] _axis(double[] a,double[] b){
		double[] o=new double[]{a[0]-b[0],a[1]-b[1],a[2]-b[2]};
		double m=Math.sqrt(o[0]*o[0]+o[1]*o[1]+o[2]*o[2]);
		return new double[]{o[0]/m,o[1]/m,o[2]/m};
	}



	private double[] _add(double[] ...l){
		double[] o=new double[l[0].length];
		for (int i=0;i<l.length;i++){
			for (int j=0;j<l[0].length;j++){
				o[j]+=l[i][j];
			}
		}
		return o;
	}
}
