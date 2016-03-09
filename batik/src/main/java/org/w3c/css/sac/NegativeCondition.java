/*
 * (c) COPYRIGHT 1999 World Wide Web Consortium
 * (Massachusetts Institute of Technology, Institut National de Recherche
 *  en Informatique et en Automatique, Keio University).
 * All Rights Reserved. http://www.w3.org/Consortium/Legal/
 *
 * $Id: NegativeCondition.java,v 1.1 2005/11/21 09:51:23 dev Exp $
 */
package org.w3c.css.sac;

/**
 * @version $Revision: 1.1 $
 * @author  Philippe Le Hegaret
 * @see Condition#SAC_NEGATIVE_CONDITION
 */
public interface NegativeCondition extends Condition {

    /**
     * Returns the condition.
     */    
    public Condition getCondition();
}
