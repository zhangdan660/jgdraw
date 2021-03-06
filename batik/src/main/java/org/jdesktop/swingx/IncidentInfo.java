/*
 * $Id: IncidentInfo.java,v 1.7 2006/07/21 17:48:59 rbair Exp $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

package org.jdesktop.swingx;

import java.util.logging.Level;

/**
 * This is a simple class that incapsulates all the information needed
 * to report a problem to the automated report/processing system.
 *
 * @author Alexander Zuev
 * @version 1.4
 */
public class IncidentInfo {
    /**
     * Short string that will be used as a error header
     */
    private String header;
    /**
     * Basic message that describes incident
     */
    private String basicErrorMessage;
    /**
     * Message that will fully describe the incident with all the
     * available details
     */
    private String detailedErrorMessage;
    /**
     * Optional Throwable that will be used as a possible source for 
     * additional information
     */
    private Throwable errorException;
    /**
     * Used to specify how bad this error was.
     */
    private Level errorLevel;

    /**
     * Main constructor that adds all the information to <code>IncidentInfo</code>
     * @param header Header that will be used as the quick reference for the 
     *        incident (e.g. title for a dialog or subject for the incident message)
     * @param basicErrorMessage Short description of the given problem
     * @param detailedErrorMessage Full description of the problem
     * @param errorException <code>Throwable</code> that can be used as a source for 
     *        additional information such as call stack, thread name, etc.
     */
    public IncidentInfo(String header, String basicErrorMessage,
                        String detailedErrorMessage, Throwable errorException) {
        this.header = header;
        if(basicErrorMessage != null) {
            this.basicErrorMessage = basicErrorMessage;
        } else {
            if(errorException != null) {
                this.basicErrorMessage = errorException.getLocalizedMessage();
                if (this.basicErrorMessage == null) {
                    this.basicErrorMessage = errorException.toString();
                }
            } else {
                this.basicErrorMessage = "";
            }
        }
        this.detailedErrorMessage = detailedErrorMessage;
        this.errorException = errorException;
        this.errorLevel = Level.SEVERE;
    }

    /**
     * Constructor that creates <code>IncidentInfo</code> with all the provided descriptions
     * @param header Header that will be used as the quick reference for the 
     *        incident (e.g. title for a dialog or subject for the incident message)
     * @param basicErrorMessage Short description of the given problem
     * @param detailedErrorMessage Full description og the problem
     */
    public IncidentInfo(String header, String basicErrorMessage, String detailedErrorMessage) {
        this(header, basicErrorMessage, detailedErrorMessage, null);
    }

    /**
     * Constructor that creates <code>IncidentInfo</code> retreiving all the 
     * information from the provided <code>Throwable</code>
     * @param header Header that will be used as the quick reference for the 
     *        incident (e.g. title for a dialog or subject for the incident message)
     * @param errorException <code>Throwable</code> that can be used as a main source of
     *        information about the incident
     */
    public IncidentInfo(String header, Throwable errorException) {
        this(header, null, null, errorException);
    }

    /**
     * Get the current header string
     *
     * @return header string
     */
    public String getHeader() {
        return header;
    }

    /**
     * Set the current header string
     *
     * @param header
     */
    public void setHeader(String header) {
        this.header = header;
    }

    /**
     * Get the basic error description
     *
     * @return basic error description
     */
    public String getBasicErrorMessage() {
        return basicErrorMessage;
    }

    /**
     * Set the current basic error description
     *
     * @param basicErrorMessage new basic error description
     */
    public void setBasicErrorMessage(String basicErrorMessage) {
        this.basicErrorMessage = basicErrorMessage;
    }

    /**
     * Get the detailed error description
     *
     * @return detailed description
     */
    public String getDetailedErrorMessage() {
        return detailedErrorMessage;
    }

    /**
     * Set the detailed description for this error
     *
     * @param detailedErrorMessage new detailed description
     */
    public void setDetailedErrorMessage(String detailedErrorMessage) {
        this.detailedErrorMessage = detailedErrorMessage;
    }

    /**
     * Get an exception that contains some additional information about the
     * error if provided.
     *
     * @return exception or null if no exception provided
     */
    public Throwable getErrorException() {
        return errorException;
    }

    /**
     * Set the exception that may contain additional information about the
     * error.
     *
     * @param errorException new <code>Throwable</code> or <code>null</code>
     *        if there is no <code>Throwable</code> related to this error
     */
    public void setErrorException(Throwable errorException) {
        this.errorException = errorException;
    }
    
    /**
     * Returns the severity of the Error. This defaults to Level.SEVERE, but
     * may be specified via setErrorLevel
     *
     * @return the Level. This will never be null
     */
    public Level getErrorLevel() {
        return errorLevel;
    }
    
    /**
     * Sets the error level for this IncidentInfo
     *
     * @param errorLevel any Level (Level.SEVERE, Level.WARNING, etc). If null,
     *        then the level will be set to SEVERE.
     */
    public void setErrorLevel(Level errorLevel) {
        this.errorLevel = errorLevel == null ? Level.SEVERE : errorLevel;
    }
}