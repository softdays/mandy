/*
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

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.softdays.mandy.core.exception.UnrecoverableException;
import org.softdays.mandy.service.SchoolHolidayService;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

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

    private Collection<LocalDate> schoolHolidays;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    /**
     * Instantiates a new school holiday service impl.
     */
    public SchoolHolidayServiceImpl() {
        super();
        try {
            this.init();
        } catch (FactoryConfigurationError | ParserConfigurationException | SAXException
                | IOException e) {
            throw new UnrecoverableException(e);
        }
    }

    /**
     * Init method.
     * 
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     * @throws FactoryConfigurationError
     * 
     * @throws Exception
     *             the exception
     */
    private void init() throws FactoryConfigurationError, ParserConfigurationException,
            SAXException, IOException {
        this.schoolHolidays = new HashSet<>();
        final Document doc = this.createDocument();

        // optional, but recommended
        // read this:
        // http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
        doc.getDocumentElement().normalize();

        final NodeList zones = doc.getElementsByTagName(TAG_ZONE);
        for (int i = 0; i < zones.getLength(); ++i) {
            final Element zone = (Element) zones.item(i);
            if ("A".equals(zone.getAttribute(ATTR_LIBELLE))) {
                final NodeList vacances = zone.getElementsByTagName(TAG_VACANCES);
                for (int j = 0; j < vacances.getLength(); ++j) {
                    final Element vacance = (Element) vacances.item(j);
                    final String debut = vacance.getAttribute(ATTR_DEBUT);
                    final String fin = vacance.getAttribute(ATTR_FIN);
                    final LocalDate start = LocalDate.parse(debut, FORMATTER);
                    final LocalDate end = LocalDate.parse(fin, FORMATTER);
                    this.addRange(start, end);
                }
            }
        }

    }

    private Document createDocument() throws FactoryConfigurationError,
            ParserConfigurationException, SAXException, IOException {
        final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        final InputStream input = classLoader.getResourceAsStream(XML_DATASOURCE);
        final DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        final DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

        return dBuilder.parse(input);
    }

    /**
     * Il ne faut pas inclure les bornes.
     */
    private void addRange(final LocalDate start, final LocalDate end) {
        LocalDate date = start.plusDays(1); // J+1
        while (!date.equals(end)) {
            this.schoolHolidays.add(date);
            date = date.plusDays(1);
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
    public boolean isSchoolHoliday(final LocalDate givenDate) {

        return this.schoolHolidays.contains(givenDate);
    }

}
