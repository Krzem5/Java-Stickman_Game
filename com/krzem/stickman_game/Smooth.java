package com.krzem.stickman_game;



import java.lang.Math;



public class Smooth{
	public double v;
	public double a;
	public double rr;
	private boolean _d;
	private boolean _off;
	private double _l;



	public Smooth(double v,double a,double rr,boolean d,boolean off){
		this.v=v;
		this.a=a;
		this.rr=rr;
		this._d=d;
		this._off=off;
		this._l=0;
	}



	public double next(){
		double o=this._map(this._smooth(this.v),0,1,-this.rr,this.rr)*(this._off==true?-1:1)+(this._off==true?this._l:0);
		this._l=this._map(this._smooth(this.v),0,1,-this.rr,this.rr);
		this.v+=this.a*(this._d==true?1:-1);
		if (this.v<=0||this.v>=1){
			this.v=(this._d==true?1:0);
			this._d=!this._d;
		}
		return o;
	}



	private double _map(double v,double aa,double ab,double ba,double bb){
		return (v-aa)/(ab-aa)*(bb-ba)+ba;
	}



	private double _smooth(double t){
		return (t<0.5?2*t*t:-1+(4-2*t)*t);
		// return (t<0.5?8*t*t*t*t:1-8*(--t)*t*t*t);
	}
}