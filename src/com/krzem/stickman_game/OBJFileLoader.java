package com.krzem.stickman_game;



import com.jogamp.opengl.GL2;
import java.lang.Exception;
import java.lang.Math;
import java.io.InputStream;
import java.io.Reader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class OBJFileLoader extends Constants{
	private static Map<String,Model> _cache=new HashMap<String,Model>();
	private static Map<String,Map<String,double[]>> _mat_cache=new HashMap<String,Map<String,double[]>>();



	public static Model load(Main.Main_ cls,GL2 gl,String fp){
		if (OBJFileLoader._cache.get(fp)!=null){
			return OBJFileLoader._cache.get(fp).clone();
		}
		try{
			InputStream is=OBJFileLoader.class.getResourceAsStream(MODEL_FILE_PATH+fp);
			String dt=OBJFileLoader._read_data(is).replace("\r","");
			is.close();
			List<Double> vl=new ArrayList<Double>();
			List<double[]> vnl=new ArrayList<double[]>();
			List<Short> il=new ArrayList<Short>();
			List<String> cl=new ArrayList<String>();
			Map<String,double[]> cm=new HashMap<String,double[]>();
			String c_mtl=null;
			for (String l:dt.split("\n")){
				switch (l.split(" ")[0]){
					case "mtllib":
						if (OBJFileLoader._mat_cache.get(l.substring(7))!=null){
							cm=OBJFileLoader._mat_cache.get(l.substring(7));
							break;
						}
						is=OBJFileLoader.class.getResourceAsStream(MODEL_FILE_PATH+l.substring(7));
						String mdt=OBJFileLoader._read_data(is).replace("\r","");
						is.close();
						String c_nm=null;
						for (String ml:mdt.split("\n")){
							switch (ml.split(" ")[0]){
								case "newmtl":
									c_nm=ml.split(" ")[1];
									break;
								case "Kd":
									cm.put(c_nm,new double[]{Double.parseDouble(ml.split(" ")[1]),Double.parseDouble(ml.split(" ")[2]),Double.parseDouble(ml.split(" ")[3])});
									break;
							}
						}
						OBJFileLoader._mat_cache.put(l.substring(7),cm);
						break;
					case "usemtl":
						c_mtl=l.split(" ")[1];
						break;
					case "v":
						vl.add(Double.parseDouble(l.split(" ")[1]));
						vl.add(Double.parseDouble(l.split(" ")[2]));
						vl.add(Double.parseDouble(l.split(" ")[3]));
						cl.add(null);
						break;
					case "f":
						il.add((short)(Short.parseShort(l.split(" ")[1].split("//")[0])-1));
						il.add((short)(Short.parseShort(l.split(" ")[2].split("//")[0])-1));
						il.add((short)(Short.parseShort(l.split(" ")[3].split("//")[0])-1));
						double[] fvn=OBJFileLoader._normal(vl,il.get(il.size()-3)*3,il.get(il.size()-2)*3,il.get(il.size()-1)*3);
						vnl.add(new double[]{Math.acos(fvn[1]/Math.sqrt(fvn[0]*fvn[0]+fvn[1]*fvn[1]+fvn[2]*fvn[2])),Math.atan2(fvn[2],fvn[0])});
						if (cl.get(il.get(il.size()-3))!=null){
							vl.add(vl.get(il.get(il.size()-3)*3));
							vl.add(vl.get(il.get(il.size()-3)*3+1));
							vl.add(vl.get(il.get(il.size()-3)*3+2));
							il.set(il.size()-3,(short)(vl.size()/3-1));
							cl.add(c_mtl);
						}
						else{
							cl.set(il.get(il.size()-3),c_mtl);
						}
						break;
				}
			}
			double[] mn=null;
			double[] mx=null;
			double[] va=new double[vl.size()];
			for (int i=0;i<va.length;i+=3){
				va[i]=vl.get(i);
				va[i+1]=vl.get(i+1);
				va[i+2]=vl.get(i+2);
				if (mn==null){
					mn=new double[]{va[i]+0,va[i+1]+0,va[i+2]+0};
					mx=new double[]{va[i]+0,va[i+1]+0,va[i+2]+0};
				}
				else{
					mn=new double[]{Math.min(mn[0],va[i]),Math.min(mn[1],va[i+1]),Math.min(mn[2],va[i+2])};
					mx=new double[]{Math.max(mx[0],va[i]),Math.max(mx[1],va[i+1]),Math.max(mx[2],va[i+2])};
				}
			}
			short[] ia=new short[il.size()];
			for (int i=0;i<ia.length;i++){
				ia[i]=il.get(i);
			}
			double[] ca=new double[cl.size()*3];
			for (int i=0;i<ca.length;i+=3){
				if (cl.get(i/3)==null){
					continue;
				}
				ca[i]=cm.get(cl.get(i/3))[0];
				ca[i+1]=cm.get(cl.get(i/3))[1];
				ca[i+2]=cm.get(cl.get(i/3))[2];
			}
			double[][] vna=new double[vnl.size()][2];
			for (int i=0;i<vna.length;i++){
				vna[i]=vnl.get(i);
			}
			if (mn==null||mx==null){
				throw new NullPointerException();
			}
			OBJFileLoader._cache.put(fp,new Model(cls,gl,fp,va,ia,ca,vna,mx[1]-mn[1],new double[]{mx[0]/2-mn[0]/2,mx[1]/2-mn[1]/2,mx[2]/2-mn[2]/2},Math.sqrt((mx[0]/2-mn[0]/2)*(mx[0]/2-mn[0]/2)+(mx[1]/2-mn[1]/2)*(mx[1]/2-mn[1]/2)+(mx[2]/2-mn[2]/2)*(mx[2]/2-mn[2]/2))));
			return OBJFileLoader._cache.get(fp).clone();
		}
		catch (Exception e){
			e.printStackTrace();
			System.exit(1);
		}
		return null;
	}



	private static double[] _normal(List<Double> vl,int ai,int bi,int ci){
		double[] u=new double[]{vl.get(bi)-vl.get(ai),vl.get(bi+1)-vl.get(ai+1),vl.get(bi+2)-vl.get(ai+2)};
		double[] v=new double[]{vl.get(ci)-vl.get(ai),vl.get(ci+1)-vl.get(ai+1),vl.get(ci+2)-vl.get(ai+2)};
		return new double[]{u[1]*v[2]-u[2]*v[1],u[2]*v[0]-u[0]*v[2],u[0]*v[1]-u[1]*v[0]};
	}



	private static String _read_data(InputStream is){
		final char[] bf=new char[4096];
		final StringBuilder o=new StringBuilder();
		try (Reader r=new InputStreamReader(is,"UTF-8")){
			while (true){
				int sz=r.read(bf,0,bf.length);
				if (sz<0)
					break;
				o.append(bf,0,sz);
			}
		}
		catch (UnsupportedEncodingException ex){
			ex.printStackTrace();
		}
		catch (IOException ex){
			ex.printStackTrace();
		}
		return o.toString();
	}
}
