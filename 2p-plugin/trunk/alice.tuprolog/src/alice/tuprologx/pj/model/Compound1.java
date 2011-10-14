/*
 * Compound1.java
 *
 * Created on March 26, 2007, 11:58 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package alice.tuprologx.pj.model;

import java.util.*;

/**
 *
 * @author maurizio
 */
public class Compound1<X1 extends Term<?>> extends Cons<X1,Nil> {    
    public Compound1(String name, X1 x1) {
        super(name,new Vector(Arrays.asList(new Term<?>[]{x1})));
    }
    
    public X1 get0() {return getHead();}
}


