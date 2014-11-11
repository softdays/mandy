/**
 * MANDY is a simple webapp to track man-day consumption on activities.
 * 
 * Copyright 2014, rpatriarche
 *
 * This file is part of MANDY software.
 *
 * MANDY is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * MANDY is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.softdays.mandy.service.support;

import java.io.InputStream;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.softdays.mandy.service.SchoolHolidayService;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * The Class SchoolHolidayServiceImpl.
 * 
 * @author rpatriarche
 * @since 1.0.0
 */
@Service
public class SchoolHolidayServiceImpl implements SchoolHolidayService {

    private static final String XML_DATASOURCE = "holidays/vacances.xml";

    private static final String TAG_ZONE = "zone";

    private static final String ATTR_FIN = "fin";

    private static final String ATTR_DEBUT = "debut";

    private static final String TAG_VACANCES = "vacances";

    private static final String ATTR_LIBELLE = "libelle";

    private Collection<Date> schoolHolidays;

    private static final DateTimeFormatter FORMATTER = DateTimeFormat
	    .forPattern("yyyy/MM/dd");

    /**
     * Instantiates a new school holiday service impl.
     */
    public SchoolHolidayServiceImpl() {
	super();
	try {
	    this.init();
	} catch (Exception e) {
	    throw new RuntimeException(e);
	}
    }

    /**
     * Inits the.
     * 
     * @throws Exception
     *             the exception
     */
    public void init() throws Exception {
	schoolHolidays = new HashSet<Date>();
	ClassLoader classLoader = Thread.currentThread()
		.getContextClassLoader();
	InputStream input = classLoader.getResourceAsStream(XML_DATASOURCE);
	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	Document doc = dBuilder.parse(input);

	// optional, but recommended
	// read this -
	// http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
	doc.getDocumentElement().normalize();

	NodeList zones = doc.getElementsByTagName(TAG_ZONE);
	for (int i = 0; i < zones.getLength(); i++) {
	    Element zone = (Element) zones.item(i);
	    if ("A".equals(zone.getAttribute(ATTR_LIBELLE))) {
		NodeList vacances = zone.getElementsByTagName(TAG_VACANCES);
		for (int j = 0; j < vacances.getLength(); j++) {
		    Element vacance = (Element) vacances.item(j);
		    String debut = vacance.getAttribute(ATTR_DEBUT);
		    String fin = vacance.getAttribute(ATTR_FIN);
		    DateTime start = FORMATTER.parseDateTime(debut);
		    DateTime end = FORMATTER.parseDateTime(fin);
		    this.addRange(start, end);
		}
	    }
	}

    }

    /**
     * Il ne faut pas inclure les bornes.
     */
    private void addRange(DateTime start, DateTime end) {
	DateTime d = start.plusDays(1); // J+1
	while (!d.equals(end)) {
	    this.schoolHolidays.add(d.toDate());
	    // on avance, on avance [...] Faut pas qu'on réfléchisse ni qu'on
	    // pense. Il faut qu'on avance, tagagatsointsoin
	    d = d.plusDays(1);
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.softdays.mandy.service.SchoolHolidayService#isSchoolHoliday(java.
     * util.Date)
     */
    @Override
    public boolean isSchoolHoliday(Date givenDate) {

	return this.schoolHolidays.contains(givenDate);
    }

}
