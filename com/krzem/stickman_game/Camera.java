package com.krzem.stickman_game;



import com.jogamp.opengl.GL2;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.lang.Exception;
import java.lang.Math;



public class Camera extends Constants{
	public Main.Main_ cls;
	public double fov;
	public double x;
	public double y;
	public double z;
	public double _x;
	public double _y;
	public double _z;
	public double rx;
	public double ry;
	public double d;
	public double dx;
	public double dz;
	public double drx;
	public double dry;
	public double dd;
	public boolean enabled=true;
	private Robot rb;
	public boolean _center=true;
	private boolean _init=false;
	private int _s_c=0;



	public Camera(Main.Main_ cls){
		this.cls=cls;
		try{
			this.rb=new Robot(SCREEN);
		}
		catch (Exception e){
			e.printStackTrace();
		}
		this.fov=70d;
		this.x=0d;
		this.y=0d;
		this.z=0d;
		this._x=0d;
		this._y=0d;
		this._z=0d;
		this.rx=0d;
		this.ry=0d;
		this.d=CAMERA_INIT_DIST+0;
		this.dx=0d;
		this.dz=0d;
		this.drx=0d;
		this.dry=0d;
		this.dd=CAMERA_INIT_DIST+0;
	}



	public void setup(GL2 gl){
		gl.glViewport(0,0,WINDOW_SIZE.width,WINDOW_SIZE.height);
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		this.cls.glu.gluPerspective((float)this.fov,(float)WINDOW_SIZE.width/(float)WINDOW_SIZE.height,CAMERA_CAM_NEAR,CAMERA_CAM_FAR);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
	}



	public void update(){
		if (this._center==true&&this.enabled==true){
			if (this._init==false){
				this.rb.mouseMove(WINDOW_SIZE.width/2,WINDOW_SIZE.height/2);
				this.cls.canvas.setCursor(this.cls.canvas.getToolkit().createCustomCursor(new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB),new Point(),null));
				this._init=true;
			}
			if (this._s_c<4){
				this._s_c++;
				this.rb.mouseMove(WINDOW_SIZE.width/2,WINDOW_SIZE.height/2);
			}
			else{
				this.drx+=(WINDOW_SIZE.height/2-this.cls.MOUSE_POS.y)*CAMERA_ROT_SPEED;
				this.dry+=(WINDOW_SIZE.width/2-this.cls.MOUSE_POS.x)*CAMERA_ROT_SPEED;
				this.drx=Math.max(Math.min(this.drx,180-75),180-135);
				double dx=Math.sin(-(this.dry+90)/180d*Math.PI)*CAMERA_MOVE_SPEED;
				double dz=Math.cos(-(this.dry+90)/180d*Math.PI)*CAMERA_MOVE_SPEED;
				if (this.cls.KEYBOARD.pressed(87)){
					this.dx+=dx;
					this.dz-=dz;
				}
				if (this.cls.KEYBOARD.pressed(83)){
					this.dx-=dx;
					this.dz+=dz;
				}
				if (this.cls.KEYBOARD.pressed(65)){
					this.dx-=dz;
					this.dz-=dx;
				}
				if (this.cls.KEYBOARD.pressed(68)){
					this.dx+=dz;
					this.dz+=dx;
				}
				if (this.cls.KEYBOARD.pressed(16)){
					this.dd--;
				}
				if (this.cls.KEYBOARD.pressed(32)){
					this.dd++;
				}
				this.dd=Math.min(Math.max(this.dd,CAMERA_MIN_DIST),CAMERA_MAX_DIST);
				this._x=this._ease(this._x,this.dx);
				this._z=this._ease(this._z,this.dz);
				this._y=this.cls.g.t.height_at(this._x,this._z);
				this.rx=this._ease(this.rx,this.drx+270);
				this.ry=this._ease(this.ry,this.dry+90);
				this.d=this._ease(this.d,this.dd);
				this.x=this._x+this.d*Math.sin((this.rx-270)/180*Math.PI)*Math.cos(-(this.ry-90)/180*Math.PI);
				this.y=this._y+this.d*Math.cos((this.rx-270)/180*Math.PI);
				this.z=this._z+this.d*Math.sin((this.rx-270)/180*Math.PI)*Math.sin(-(this.ry-90)/180*Math.PI);
				this.rb.mouseMove(WINDOW_SIZE.width/2,WINDOW_SIZE.height/2);
			}
		}
		else{
			this._init=false;
			this._s_c=0;
		}
	}


	public void draw(GL2 gl){
		gl.glViewport(0,0,WINDOW_SIZE.width,WINDOW_SIZE.height);
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		this.cls.glu.gluPerspective((float)this.fov,(float)WINDOW_SIZE.width/(float)WINDOW_SIZE.height,0.05,1000);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glRotatef((float)-this.rx,1.0f,0.0f,0.0f);
		gl.glRotatef((float)-this.ry,0.0f,1.0f,0.0f);
		gl.glTranslatef((float)-this.x,(float)-this.y,(float)-this.z);
	}



	private double _ease(double c,double t){
		if (Math.abs(t-c)<CAMERA_MIN_EASE_DIFF){
			return t+0;
		}
		else{
			return c*CAMERA_EASE_PROC+(1-CAMERA_EASE_PROC)*t;
		}
	}



	private double _map(double v,double aa,double ab,double ba,double bb){
		return (v-aa)/(ab-aa)*(bb-ba)+ba;
	}
}