package com.krzem.stickman_game;



import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;



public class GUI extends Constants{
	private static Map<String,BufferedImage> _img_cache=new HashMap<String,BufferedImage>();



	public Main.Main_ cls;
	public Game g;
	public String cursor=DEFAULT_CURSOR_NAME+"";
	public int mx;
	public int my;
	private boolean _en=false;
	private Texture _tx=null;
	private BufferedImage _img=null;
	private Graphics2D _g=null;
	private boolean _ed=false;



	public GUI(Main.Main_ cls,Game g){
		this.cls=cls;
		this.g=g;
	}



	public void update(GL2 gl){
		if (this.cls.KEYBOARD.pressed(27)==true){
			if (this._ed==false){
				this._en=!this._en;
				this.cls.cam.enabled=!this._en;
			}
			this._ed=true;
		}
		else{
			this._ed=false;
		}
		if (this._en==true){
			this.mx=(int)(this.cls.MOUSE_POS.x/WINDOW_SIZE.width*GUI_WINDOW_SIZE.width);
			this.my=(int)(this.cls.MOUSE_POS.y/WINDOW_SIZE.height*GUI_WINDOW_SIZE.height);
		}
	}



	public void draw(GL2 gl){
		if (this._img==null){
			this._img=new BufferedImage(GUI_WINDOW_SIZE.width,GUI_WINDOW_SIZE.height,BufferedImage.TRANSLUCENT);
			this._g=(Graphics2D)this._img.createGraphics();
			this._g.setBackground(new Color(255,255,255,0));
			this._g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
			this._g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
			this._g.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
			this._g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
			this._g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING,RenderingHints.VALUE_COLOR_RENDER_QUALITY);
			this._g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		}
		this._g.clearRect(0,0,GUI_WINDOW_SIZE.width,GUI_WINDOW_SIZE.height);
		if (this._en==true){
			this._g.drawImage(this._get_image(CURSOR_FILE_PATH+this.cursor),this.mx,this.my,null);
		}
		if (this._tx==null){
			this._tx=AWTTextureIO.newTexture(gl.getGLProfile(),this._img,false);
		}
		else{
			this._tx.updateImage(gl,AWTTextureIO.newTextureData(gl.getGLProfile(),this._img,false));
		}
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		this.cls.glu.gluOrtho2D(0,WINDOW_SIZE.width,0,WINDOW_SIZE.height);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glEnable(GL2.GL_BLEND);
		gl.glEnable(GL2.GL_TEXTURE_2D);
		gl.glBlendFunc(GL2.GL_SRC_ALPHA,GL2.GL_ONE_MINUS_SRC_ALPHA);
		this._tx.enable(gl);
		this._tx.bind(gl);
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor4d(1,1,1,1);
		gl.glTexCoord2d(0,1);
		gl.glVertex2d(0,0);
		gl.glTexCoord2d(1,1);
		gl.glVertex2d(WINDOW_SIZE.width,0);
		gl.glTexCoord2d(1,0);
		gl.glVertex2d(WINDOW_SIZE.width,WINDOW_SIZE.height);
		gl.glTexCoord2d(0,0);
		gl.glVertex2d(0,WINDOW_SIZE.height);
		gl.glEnd();
		gl.glDisable(GL2.GL_BLEND);
		gl.glDisable(GL2.GL_TEXTURE_2D);
	}



	private BufferedImage _get_image(String fp){
		try{
			if (this._img_cache.get(fp)==null){
				InputStream is=Skybox.class.getResourceAsStream(IMAGE_FILE_PATH+fp+".png");
				this._img_cache.put(fp,ImageIO.read(is));
				is.close();
			}
			return this._img_cache.get(fp);
		}
		catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}
}
