package com.krzem.stickman_game;



import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.Exception;
import java.lang.Math;
import javax.imageio.ImageIO;



public class Skybox extends Constants{
	public Main.Main_ cls;
	public Game g;
	private Texture[] _imgl=null;
	private int _sz;



	public Skybox(Main.Main_ cls,Game g){
		this.cls=cls;
		this.g=g;
	}



	public void draw(GL2 gl){
		if (this._imgl==null){
			this._load(gl,SKYBOX_IMAGE_FILE_PATH);
		}
		gl.glEnable(GL2.GL_TEXTURE_2D);
		gl.glColor3d(1,1,1);
		this._imgl[0].enable(gl);
		this._imgl[0].bind(gl);
		gl.glBegin(GL2.GL_QUADS);
		gl.glTexCoord2d(0,1);
		gl.glVertex3d(this.cls.cam.x+this._sz,this.cls.cam.y-this._sz,this.cls.cam.z-this._sz);
		gl.glTexCoord2d(1,1);
		gl.glVertex3d(this.cls.cam.x+this._sz,this.cls.cam.y-this._sz,this.cls.cam.z+this._sz);
		gl.glTexCoord2d(1,0);
		gl.glVertex3d(this.cls.cam.x+this._sz,this.cls.cam.y+this._sz,this.cls.cam.z+this._sz);
		gl.glTexCoord2d(0,0);
		gl.glVertex3d(this.cls.cam.x+this._sz,this.cls.cam.y+this._sz,this.cls.cam.z-this._sz);
		gl.glEnd();
		this._imgl[0].disable(gl);
		this._imgl[1].enable(gl);
		this._imgl[1].bind(gl);
		gl.glBegin(GL2.GL_QUADS);
		gl.glTexCoord2d(0,1);
		gl.glVertex3d(this.cls.cam.x-this._sz,this.cls.cam.y+this._sz,this.cls.cam.z-this._sz);
		gl.glTexCoord2d(1,1);
		gl.glVertex3d(this.cls.cam.x-this._sz,this.cls.cam.y+this._sz,this.cls.cam.z+this._sz);
		gl.glTexCoord2d(1,0);
		gl.glVertex3d(this.cls.cam.x+this._sz,this.cls.cam.y+this._sz,this.cls.cam.z+this._sz);
		gl.glTexCoord2d(0,0);
		gl.glVertex3d(this.cls.cam.x+this._sz,this.cls.cam.y+this._sz,this.cls.cam.z-this._sz);
		gl.glEnd();
		this._imgl[1].disable(gl);
		this._imgl[2].enable(gl);
		this._imgl[2].bind(gl);
		gl.glBegin(GL2.GL_QUADS);
		gl.glTexCoord2d(1,1);
		gl.glVertex3d(this.cls.cam.x-this._sz,this.cls.cam.y-this._sz,this.cls.cam.z+this._sz);
		gl.glTexCoord2d(0,1);
		gl.glVertex3d(this.cls.cam.x+this._sz,this.cls.cam.y-this._sz,this.cls.cam.z+this._sz);
		gl.glTexCoord2d(0,0);
		gl.glVertex3d(this.cls.cam.x+this._sz,this.cls.cam.y+this._sz,this.cls.cam.z+this._sz);
		gl.glTexCoord2d(1,0);
		gl.glVertex3d(this.cls.cam.x-this._sz,this.cls.cam.y+this._sz,this.cls.cam.z+this._sz);
		gl.glEnd();
		this._imgl[2].disable(gl);
		this._imgl[3].enable(gl);
		this._imgl[3].bind(gl);
		gl.glBegin(GL2.GL_QUADS);
		gl.glTexCoord2d(0,1);
		gl.glVertex3d(this.cls.cam.x-this._sz,this.cls.cam.y-this._sz,this.cls.cam.z-this._sz);
		gl.glTexCoord2d(1,1);
		gl.glVertex3d(this.cls.cam.x-this._sz,this.cls.cam.y-this._sz,this.cls.cam.z+this._sz);
		gl.glTexCoord2d(1,0);
		gl.glVertex3d(this.cls.cam.x-this._sz,this.cls.cam.y+this._sz,this.cls.cam.z+this._sz);
		gl.glTexCoord2d(0,0);
		gl.glVertex3d(this.cls.cam.x-this._sz,this.cls.cam.y+this._sz,this.cls.cam.z-this._sz);
		gl.glEnd();
		this._imgl[3].disable(gl);
		this._imgl[4].enable(gl);
		this._imgl[4].bind(gl);
		gl.glBegin(GL2.GL_QUADS);
		gl.glTexCoord2d(0,1);
		gl.glVertex3d(this.cls.cam.x-this._sz,this.cls.cam.y-this._sz,this.cls.cam.z-this._sz);
		gl.glTexCoord2d(1,1);
		gl.glVertex3d(this.cls.cam.x-this._sz,this.cls.cam.y-this._sz,this.cls.cam.z+this._sz);
		gl.glTexCoord2d(1,0);
		gl.glVertex3d(this.cls.cam.x+this._sz,this.cls.cam.y-this._sz,this.cls.cam.z+this._sz);
		gl.glTexCoord2d(0,0);
		gl.glVertex3d(this.cls.cam.x+this._sz,this.cls.cam.y-this._sz,this.cls.cam.z-this._sz);
		gl.glEnd();
		this._imgl[4].disable(gl);
		this._imgl[5].enable(gl);
		this._imgl[5].bind(gl);
		gl.glBegin(GL2.GL_QUADS);
		gl.glTexCoord2d(1,1);
		gl.glVertex3d(this.cls.cam.x-this._sz,this.cls.cam.y-this._sz,this.cls.cam.z-this._sz);
		gl.glTexCoord2d(0,1);
		gl.glVertex3d(this.cls.cam.x+this._sz,this.cls.cam.y-this._sz,this.cls.cam.z-this._sz);
		gl.glTexCoord2d(0,0);
		gl.glVertex3d(this.cls.cam.x+this._sz,this.cls.cam.y+this._sz,this.cls.cam.z-this._sz);
		gl.glTexCoord2d(1,0);
		gl.glVertex3d(this.cls.cam.x-this._sz,this.cls.cam.y+this._sz,this.cls.cam.z-this._sz);
		gl.glEnd();
		this._imgl[5].disable(gl);
		gl.glDisable(GL2.GL_TEXTURE_2D);
	}



	private void _load(GL2 gl,String fp){
		this._imgl=new Texture[6];
		this._sz=(int)(CAMERA_FAR/Math.sqrt(3));
		this._imgl[0]=this._read(gl,fp+"front.png",this._sz);
		this._imgl[1]=this._read(gl,fp+"top.png",this._sz);
		this._imgl[2]=this._read(gl,fp+"left.png",this._sz);
		this._imgl[3]=this._read(gl,fp+"back.png",this._sz);
		this._imgl[4]=this._read(gl,fp+"bottom.png",this._sz);
		this._imgl[5]=this._read(gl,fp+"right.png",this._sz);
	}



	private Texture _read(GL2 gl,String fp,int sz){
		try{
			BufferedImage img=ImageIO.read(new File(fp));
			BufferedImage o=new BufferedImage(sz,sz,BufferedImage.TYPE_INT_ARGB);
			o=new AffineTransformOp(AffineTransform.getScaleInstance((double)sz/img.getWidth(),(double)sz/img.getHeight()),AffineTransformOp.TYPE_NEAREST_NEIGHBOR).filter(img,o);
			return AWTTextureIO.newTexture(gl.getGLProfile(),o,true);
		}
		catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}
}