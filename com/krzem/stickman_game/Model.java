package com.krzem.stickman_game;



import com.jogamp.opengl.GL2;
import java.nio.DoubleBuffer;
import java.nio.ShortBuffer;



public class Model extends Constants{
	public String nm;
	private Camera _c;
	private GL2 _gl;
	private DoubleBuffer _vb=null;
	private ShortBuffer _ib=null;
	private DoubleBuffer _cb=null;
	private double[] _cl=null;
	private double[][] _vnl=null;
	private int _v_id=-1;
	private int _i_id=-1;
	private int _c_id=-1;
	private double _scl=1;
	private double[] _off=new double[3];
	private double[] _rot=new double[3];



	public Model(Camera c,GL2 gl,String nm){
		this._c=c;
		this._gl=gl;
		this.nm=nm;
	}



	public Model clone(){
		Model o=new Model(this._c,this._gl,this.nm);
		o._scl=this._scl+0;
		o._off=new double[]{this._off[0]+0,this._off[1]+0,this._off[2]+0};
		o._rot=new double[]{this._rot[0]+0,this._rot[1]+0,this._rot[2]+0};
		double[] nvl=new double[this._vb.capacity()];
		for (int i=0;i<nvl.length;i++){
			nvl[i]=this._vb.get(i)+0;
		}
		short[] nil=new short[this._ib.capacity()];
		for (int i=0;i<nil.length;i++){
			nil[i]=(short)(this._ib.get(i)+0);
		}
		double[] ncl=new double[this._cb.capacity()];
		for (int i=0;i<ncl.length;i++){
			ncl[i]=this._cb.get(i)+0;
		}
		o.update(nvl,nil,ncl,this._vnl);
		return o;
	}



	public void scale(double v){
		double nv=v/this._scl;
		this._scl=v;
		for (int i=0;i<this._vb.capacity();i+=3){
			this._vb.put(i,(this._vb.get(i)-this._off[0])*nv+this._off[0]);
			this._vb.put(i+1,(this._vb.get(i+1)-this._off[1])*nv+this._off[1]);
			this._vb.put(i+2,(this._vb.get(i+2)-this._off[2])*nv+this._off[2]);
		}
		this._gl.glBindBuffer(GL2.GL_ARRAY_BUFFER,this._v_id);
		this._gl.glBufferData(GL2.GL_ARRAY_BUFFER,this._vb.capacity()*8,this._vb,GL2.GL_STATIC_DRAW);
		this._gl.glBindBuffer(GL2.GL_ARRAY_BUFFER,0);
		this._recalc_light();
	}



	public void offset(double x,double y,double z){
		double nx=x-this._off[0];
		double ny=y-this._off[1];
		double nz=z-this._off[2];
		this._off=new double[]{x,y,z};
		for (int i=0;i<this._vb.capacity();i+=3){
			this._vb.put(i,this._vb.get(i)+nx);
			this._vb.put(i+1,this._vb.get(i+1)+ny);
			this._vb.put(i+2,this._vb.get(i+2)+nz);
		}
		this._gl.glBindBuffer(GL2.GL_ARRAY_BUFFER,this._v_id);
		this._gl.glBufferData(GL2.GL_ARRAY_BUFFER,this._vb.capacity()*8,this._vb,GL2.GL_STATIC_DRAW);
		this._gl.glBindBuffer(GL2.GL_ARRAY_BUFFER,0);
		this._recalc_light();
	}



	public void rotate(double a,double b,double c){
		double na=this._rot[0]-a;
		double nb=this._rot[1]-b;
		double nc=this._rot[2]-c;
		this._rot=new double[]{a,b,c};
		for (int i=0;i<this._vb.capacity();i+=3){
			double x=this._vb.get(i)-this._off[0]-this._scl/2;
			double y=this._vb.get(i+1)-this._off[1]-this._scl;
			double z=this._vb.get(i+2)-this._off[2]-this._scl/2;
			this._vb.put(i,x*Math.cos(na)*Math.cos(nb)+y*(Math.cos(na)*Math.sin(nb)*Math.sin(nc)-Math.sin(na)*Math.cos(nc))+z*(Math.cos(na)*Math.sin(nb)*Math.cos(nc)+Math.sin(na)*Math.sin(nc))+this._off[0]+this._scl/2);
			this._vb.put(i+1,x*Math.sin(na)*Math.cos(nb)+y*(Math.sin(na)*Math.sin(nb)*Math.sin(nc)+Math.cos(na)*Math.cos(nc))+z*(Math.sin(na)*Math.sin(nb)*Math.cos(nc)-Math.cos(na)*Math.sin(nc))+this._off[1]+this._scl);
			this._vb.put(i+2,-x*Math.sin(nb)+y*Math.cos(nb)*Math.sin(nc)+z*Math.cos(nb)*Math.cos(nc)+this._off[2]+this._scl/2);
		}
		for (int i=0;i<this._vnl.length;i++){
			double[] fvn=this._normal(this._vb.array(),this._ib.get(i*3)*3,this._ib.get(i*3+1)*3,this._ib.get(i*3+2)*3);
			this._vnl[i]=new double[]{Math.acos(fvn[1]/Math.sqrt(fvn[0]*fvn[0]+fvn[1]*fvn[1]+fvn[2]*fvn[2])),Math.atan2(fvn[2],fvn[0])};
		}
		this._gl.glBindBuffer(GL2.GL_ARRAY_BUFFER,this._v_id);
		this._gl.glBufferData(GL2.GL_ARRAY_BUFFER,this._vb.capacity()*8,this._vb,GL2.GL_STATIC_DRAW);
		this._gl.glBindBuffer(GL2.GL_ARRAY_BUFFER,0);
		this._recalc_light();
	}



	public double max(double x,double z){
		double[] ro=new double[]{x*this._scl+this._off[0],this._off[1],z*this._scl+this._off[2]};
		double[] rd=new double[]{0,1,0};
		double rl=0;
		double[] e1;
		double[] e2;
		double[] h;
		double[] s;
		double[] q;
		for (int i=0;i<this._ib.capacity();i+=3){
			double[][] t=new double[][]{{this._vb.get(this._ib.get(i)*3),this._vb.get(this._ib.get(i)*3+1),this._vb.get(this._ib.get(i)*3+2)},{this._vb.get(this._ib.get(i+1)*3),this._vb.get(this._ib.get(i+1)*3+1),this._vb.get(this._ib.get(i+1)*3+2)},{this._vb.get(this._ib.get(i+2)*3),this._vb.get(this._ib.get(i+2)*3+1),this._vb.get(this._ib.get(i+2)*3+2)}};
			e1=new double[]{t[1][0]-t[0][0],t[1][1]-t[0][1],t[1][2]-t[0][2]};
			e2=new double[]{t[2][0]-t[0][0],t[2][1]-t[0][1],t[2][2]-t[0][2]};
			h=new double[]{rd[1]*e2[2]-rd[2]*e2[1],rd[2]*e2[0]-rd[0]*e2[2],rd[0]*e2[1]-rd[1]*e2[0]};
			double a=e1[0]*h[0]+e1[1]*h[1]+e1[2]*h[2];
			if (a>-EPSILON&&a<EPSILON){
				continue;
			}
			double f=1/a;
			s=new double[]{ro[0]-t[0][0],ro[1]-t[0][1],ro[2]-t[0][2]};
			double u=f*(s[0]*h[0]+s[1]*h[1]+s[2]*h[2]);
			if (u<0||u>1){
				continue;
			}
			q=new double[]{s[1]*e1[2]-s[2]*e1[1],s[2]*e1[0]-s[0]*e1[2],s[0]*e1[1]-s[1]*e1[0]};
			double v=f*(rd[0]*q[0]+rd[1]*q[1]+rd[2]*q[2]);
			if (v<0||u+v>1){
				continue;
			}
			double ln=f*(e2[0]*q[0]+e2[1]*q[1]+e2[2]*q[2])-CAMERA_CAM_NEAR*2;
			if (ln>EPSILON){
				rl=Math.max(rl,ln);
			}
		}
		return rl;
	}



	public double height(){
		double mx=0;
		for (int i=0;i<this._vb.capacity();i+=3){
			mx=Math.max(mx,this._vb.get(i+1)-this._off[1]);
		}
		return mx;
	}



	public void update(double[] vl,short[] il,double[] cl,double[][] vnl){
		if (this._vb==null){
			this._vb=DoubleBuffer.wrap(vl);
		}
		else if (vl!=null){
			this._vb.put(vl);
			this._vb.flip();
		}
		if (this._ib==null){
			this._ib=ShortBuffer.wrap(il);
		}
		else if (il!=null){
			this._ib.put(il);
			this._ib.flip();
		}
		if (this._cb==null){
			this._cl=new double[cl.length];
			for (int i=0;i<cl.length;i++){
				this._cl[i]=cl[i]+0;
			}
			this._cb=DoubleBuffer.wrap(cl);
		}
		else if (cl!=null){
			this._cl=new double[cl.length];
			for (int i=0;i<cl.length;i++){
				this._cl[i]=cl[i]+0;
			}
			this._cb.put(cl);
			this._cb.flip();
		}
		this._vnl=vnl;
		if (this._v_id==-1||this._i_id==-1||this._c_id==-1){
			int[] tmp=new int[3];
			this._gl.glGenBuffers(3,tmp,0);
			this._v_id=tmp[0];
			this._gl.glBindBuffer(GL2.GL_ARRAY_BUFFER,this._v_id);
			this._gl.glBufferData(GL2.GL_ARRAY_BUFFER,this._vb.capacity()*8,this._vb,GL2.GL_STATIC_DRAW);
			this._gl.glBindBuffer(GL2.GL_ARRAY_BUFFER,0);
			this._i_id=tmp[1];
			this._gl.glBindBuffer(GL2.GL_ARRAY_BUFFER,this._i_id);
			this._gl.glBufferData(GL2.GL_ARRAY_BUFFER,this._ib.capacity()*2,this._ib,GL2.GL_STATIC_DRAW);
			this._gl.glBindBuffer(GL2.GL_ARRAY_BUFFER,0);
			this._c_id=tmp[2];
			this._gl.glBindBuffer(GL2.GL_ARRAY_BUFFER,this._c_id);
			this._gl.glBufferData(GL2.GL_ARRAY_BUFFER,this._cb.capacity()*8,this._cb,GL2.GL_STATIC_DRAW);
			this._gl.glBindBuffer(GL2.GL_ARRAY_BUFFER,0);
		}
	}



	public void draw(GL2 gl){
		gl.glProvokingVertex(GL2.GL_FIRST_VERTEX_CONVENTION);
		gl.glShadeModel(GL2.GL_FLAT);
		gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL2.GL_COLOR_ARRAY);
		gl.glBindBuffer(GL2.GL_ARRAY_BUFFER,this._v_id);
		gl.glVertexPointer(3,GL2.GL_DOUBLE,0,0);
		gl.glBindBuffer(GL2.GL_ARRAY_BUFFER,this._c_id);
		gl.glColorPointer(3,GL2.GL_DOUBLE,0,0);
		gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER,this._i_id);
		gl.glDrawElements(GL2.GL_TRIANGLES,this._ib.capacity(),GL2.GL_UNSIGNED_SHORT,0);
		gl.glDisableClientState(GL2.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL2.GL_COLOR_ARRAY);
		gl.glShadeModel(GL2.GL_SMOOTH);
		if (MODEL_NORMALS==true){
			gl.glBegin(GL2.GL_LINES);
			gl.glColor3d(1,0,0);
			for (int i=0;i<this._vnl.length;i++){
				double[] p=new double[]{this._vb.get(this._ib.get(i*3)*3)/3+this._vb.get(this._ib.get(i*3+1)*3)/3+this._vb.get(this._ib.get(i*3+2)*3)/3,this._vb.get(this._ib.get(i*3)*3+1)/3+this._vb.get(this._ib.get(i*3+1)*3+1)/3+this._vb.get(this._ib.get(i*3+2)*3+1)/3,this._vb.get(this._ib.get(i*3)*3+2)/3+this._vb.get(this._ib.get(i*3+1)*3+2)/3+this._vb.get(this._ib.get(i*3+2)*3+2)/3};
				gl.glVertex3d(p[0],p[1],p[2]);
				gl.glVertex3d(p[0]+Math.sin(this._vnl[i][0])*Math.cos(this._vnl[i][1]),p[1]+Math.cos(this._vnl[i][0]),p[2]+Math.sin(this._vnl[i][0])*Math.sin(this._vnl[i][1]));
			}
			gl.glEnd();
		}
	}



	private void _recalc_light(){
		for (int i=0;i<this._vnl.length;i++){
			double v=this._map(this._map(this._vnl[i][0],0,Math.PI,0,1)+this._map(Math.abs(this._vnl[i][1]),0,Math.PI,0,0.5),0,1.5,0,1);
			if (this._vnl[i][0]==Math.PI){
				v=1;
			}
			if (this._vnl[i][0]==0){
				v=0;
			}
			this._cb.put(this._ib.get(i*3)*3,this._map(v,0,1,this._cl[this._ib.get(i*3)*3],this._cl[this._ib.get(i*3)*3]/3));
			this._cb.put(this._ib.get(i*3)*3+1,this._map(v,0,1,this._cl[this._ib.get(i*3)*3+1],this._cl[this._ib.get(i*3)*3+1]/3));
			this._cb.put(this._ib.get(i*3)*3+2,this._map(v,0,1,this._cl[this._ib.get(i*3)*3+2],this._cl[this._ib.get(i*3)*3+2]/3));
		}
		this._gl.glBindBuffer(GL2.GL_ARRAY_BUFFER,this._c_id);
		this._gl.glBufferData(GL2.GL_ARRAY_BUFFER,this._cb.capacity()*8,this._cb,GL2.GL_STATIC_DRAW);
		this._gl.glBindBuffer(GL2.GL_ARRAY_BUFFER,0);
	}



	private double _diff(double a,double b,double mx){
		return Math.min(Math.abs(a-b),Math.abs(((a+mx/2)%mx)-((b+mx/2)%mx)));
	}



	private double _map(double v,double aa,double ab,double ba,double bb){
		return Math.min(Math.max((v-aa)/(ab-aa)*(bb-ba)+ba,Math.min(ba,bb)),Math.max(ba,bb));
	}



	private double[] _normal(double[] vl,int ai,int bi,int ci){
		double[] u=new double[]{vl[bi]-vl[ai],vl[bi+1]-vl[ai+1],vl[bi+2]-vl[ai+2]};
		double[] v=new double[]{vl[ci]-vl[ai],vl[ci+1]-vl[ai+1],vl[ci+2]-vl[ai+2]};
		return new double[]{u[1]*v[2]-u[2]*v[1],u[2]*v[0]-u[0]*v[2],u[0]*v[1]-u[1]*v[0]};
	}
}