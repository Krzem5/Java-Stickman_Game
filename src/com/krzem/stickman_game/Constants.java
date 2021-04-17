package com.krzem.stickman_game;



import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.lang.Math;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;



public class Constants{
	public static final int DISPLAY_ID=0;
	public static final GraphicsDevice SCREEN=GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[DISPLAY_ID];
	public static final Rectangle WINDOW_SIZE=SCREEN.getDefaultConfiguration().getBounds();
	public static final Rectangle GUI_WINDOW_SIZE=new Rectangle(0,0,1920,1080);

	public static final double EPSILON=Math.ulp(1d);
	public static final Random RANDOM_GEN=new Random(123456);

	public static final double PERLIN_NOISE_OFFSET_SCALE=40;

	public static final String SKYBOX_IMAGE_FILE_PATH="/rsrc/skybox/";
	public static final String MODEL_FILE_PATH="/rsrc/models/";
	public static final String IMAGE_FILE_PATH="/rsrc/";
	public static final String CURSOR_FILE_PATH="misc/";

	public static final boolean MODEL_DEBUG=false;
	public static final int MODEL_GROUP_NAME_LENGTH=2;
	public static final Map<Integer,Integer> MODEL_NAME_LENGTH=new HashMap<Integer,Integer>(){{
		this.put(0,2);
		this.put(1,2);
		this.put(2,3);
		this.put(3,3);
		this.put(4,3);
		this.put(5,1);
		this.put(6,2);
		this.put(7,2);
		this.put(8,3);
		this.put(9,2);
		this.put(10,2);
		this.put(11,2);
		this.put(12,3);
	}};

	public static final double CAMERA_MOVE_SPEED=0.5d;
	public static final double CAMERA_ROT_SPEED=0.075d;
	public static final double CAMERA_MIN_EASE_DIFF=0.05d;
	public static final double CAMERA_EASE_PROC=0.85d;
	public static final double CAMERA_MIN_ANGLE=75d;
	public static final double CAMERA_MAX_ANGLE=135d;
	public static final double CAMERA_INIT_DIST=400d;// 10d;
	public static final double CAMERA_MIN_DIST=1d;
	public static final double CAMERA_MAX_DIST=400d;
	public static final double CAMERA_NEAR=0.1d;
	public static final double CAMERA_FAR=1000d;
	public static final double CAMERA_VISIBILITY_BUFFOR=100d;

	public static final int CHUNK_SIZE=32;
	public static final double CHUNK_GENERATION_NOISE_SCALE=2;
	public static final double CHUNK_GENERATION_MAX_HEIGHT=20;
	public static final int CHUNK_BLOCK_SIZE=8;
	public static final double CHUNK_BLOCK_HEIGHT=0.55;
	public static final double CHUNK_BLOCK_TERRIAIN_TREE_PROC=0.03;
	public static final double CHUNK_BLOCK_TERRIAIN_TREE_THIN_PROC=0.65;
	public static final double CHUNK_BLOCK_TERRIAIN_TREE_SIZE=10;
	public static final double CHUNK_BLOCK_TERRIAIN_TREE_OFFSET_MARGIN=0.2;
	public static final Map<String,String> CHUNK_BLOCK_NAME_LOOKUP_TABLE=new HashMap<String,String>(){{
		this.put("grass_low",Constants._model(4,163));
		this.put("grass_high",Constants._model(4,36));
		this.put("grass_slope",Constants._model(4,152));
		this.put("grass_corner_slope",Constants._model(4,140));
		this.put("grass_corner_slope_high",Constants._model(4,37));
		this.put("tree_thin",Constants._model(4,19));
		this.put("tree_thick",Constants._model(4,20));
		this.put("path_end",Constants._model(4,17));
		this.put("path_straight",Constants._model(4,23));
		this.put("path_straight_up",Constants._model(4,24));
		this.put("path_straight_down",Constants._model(4,15));
		this.put("path_straight_slope",Constants._model(4,18));
		this.put("path_corner_small",Constants._model(4,22));
		this.put("path_corner_big",Constants._model(4,16));
		this.put("path_junction_3",Constants._model(4,13));
		this.put("path_junction_4",Constants._model(4,14));
	}};

	public static final String DEFAULT_CURSOR_NAME="default";



	private static String _model(int gi,int mi){
		return String.format(String.format("%%%ds-%%%ds.obj",MODEL_GROUP_NAME_LENGTH,MODEL_NAME_LENGTH.get(gi)),gi,mi).replace(" ","0");
	}

	public static final double[] STICKMAN_POS_OFFSET=new double[]{0,5.65,0};
	public static final Map<String,Double> STICKMAN_JOIN_LENGTHS=new HashMap<String,Double>(){{
		this.put("Lleg",6d);
		this.put("Rleg",6.d);
		this.put("torso",2.8d);
		this.put("Larm",3d);
		this.put("Rarm",3d);
		this.put("neck",0.4d);
		this.put("head",2d);
		this.put("Lhead",1.4d);
		this.put("Rhead",1.4d);
	}};
	public static final Map<String,double[]> STICKMAN_DEFAULT_JOIN_ROTATIONS=new HashMap<String,double[]>(){{
		this.put("body",new double[]{0,0,0});
		this.put("Lleg",new double[]{Math.PI*0.9,0,0});
		this.put("Rleg",new double[]{-Math.PI*0.9,0,0});
		this.put("torso",new double[]{0,0,0});
		this.put("Larm",new double[]{Math.PI*0.8,0,0});
		this.put("Rarm",new double[]{-Math.PI*0.8,0,0});
		this.put("neck",new double[]{0,0,0});
		this.put("head",new double[]{0,0,0});
		this.put("Lhead",new double[]{Math.PI/2,0,0});
		this.put("Rhead",new double[]{-Math.PI/2,0,0});
	}};
}
