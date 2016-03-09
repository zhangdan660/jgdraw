package org.apache.batik.gvt;

import java.awt.geom.AffineTransform;

public class SelfAffineTransform {
	
	 public AffineTransform currentTransform;
	 
	 public AffineTransform preferiosTransform;
	 
	 public AffineTransform useTransform;
	 
	 public SelfAffineTransform(AffineTransform af, AffineTransform at)
	 {
		 this.currentTransform = af;
		 
		 this.preferiosTransform = at;
	 }
}
