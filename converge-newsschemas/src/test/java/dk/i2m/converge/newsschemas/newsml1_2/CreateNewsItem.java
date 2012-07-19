/*
 * Copyright (C) 2012 Interactive Media Management
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package dk.i2m.converge.newsschemas.newsml1_2;

import dk.i2m.converge.newsml.v1_2.DateAndTimeType;
import dk.i2m.converge.newsml.v1_2.NIType;
import dk.i2m.converge.newsml.v1_2.NIType.Identification;
import dk.i2m.converge.newsml.v1_2.NIType.Identification.NewsIdentifier;
import dk.i2m.converge.newsml.v1_2.NIType.NewsManagement;
import dk.i2m.converge.newsml.v1_2.NewsComponentType;
import dk.i2m.converge.newsml.v1_2.NewsComponentType.ContentItem;
import dk.i2m.converge.newsml.v1_2.NewsComponentType.DescriptiveMetadata.Genre;
import dk.i2m.converge.newsml.v1_2.NewsComponentType.DescriptiveMetadata.Language;
import dk.i2m.converge.newsml.v1_2.NewsComponentType.DescriptiveMetadata.Location;
import dk.i2m.converge.newsml.v1_2.NewsComponentType.DescriptiveMetadata.SubjectCode;
import dk.i2m.converge.newsml.v1_2.NewsComponentType.NewsLines.HeadLine;
import dk.i2m.converge.newsml.v1_2.NewsML;
import dk.i2m.converge.newsml.v1_2.NewsML.NewsEnvelope;
import dk.i2m.converge.newsml.v1_2.NewsML.NewsEnvelope.NewsProduct;
import dk.i2m.converge.newsml.v1_2.NewsML.NewsEnvelope.NewsService;
import dk.i2m.converge.newsml.v1_2.NewsML.NewsEnvelope.Priority;
import dk.i2m.converge.newsml.v1_2.NewsML.NewsEnvelope.TransmissionId;
import dk.i2m.converge.newsml.v1_2.ObjectFactory;
import dk.i2m.converge.newsml.v1_2.PropertyType;
import java.io.File;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import junit.framework.TestCase;
import org.junit.Test;

/**
 * Example of how to create a NewsML 1.2 News Item.
 *
 * @author Allan Lykke Christensen
 */
public class CreateNewsItem extends TestCase {

    public CreateNewsItem(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void testCreate() {
        // Format used in NewsML to express dates
        final String DATE_PATTERN = "yyyyMMdd'T'HHmmss";
        DateFormat dateFormatter = new SimpleDateFormat(DATE_PATTERN);
        Date now = Calendar.getInstance().getTime();
        
        
        // Factory used for creating the necessary objects
        ObjectFactory objFactory = new ObjectFactory();
        
        // Overall package
        NewsML newsml = objFactory.createNewsML();
        newsml.setVersion("1.2"); // NewsML version
        
        // The envelop contains information about the delivery
        NewsEnvelope envelop = objFactory.createNewsMLNewsEnvelope();
        
        // Transmission ID is a unique identifier we use to identify the 
        // transmission of the package from the origin
        TransmissionId tid = objFactory.createNewsMLNewsEnvelopeTransmissionId();
        tid.setValue(UUID.randomUUID().toString());
        envelop.setTransmissionId(tid);
        
        // Set the date and time of when the package was created and sent
        DateAndTimeType sentTime = objFactory.createDateAndTimeType();
        sentTime.setValue(dateFormatter.format(now));
        envelop.setDateAndTime(sentTime);
        
        // Specify the news service from where the package came
        NewsService newsService = objFactory.createNewsMLNewsEnvelopeNewsService();
        newsService.setFormalName("Interactive Media Management");
        envelop.getNewsService().add(newsService);
        
        // Specify the news product (of the news service) from where the 
        // package is being dissemminated.
        NewsProduct newsProduct = objFactory.createNewsMLNewsEnvelopeNewsProduct();
        newsProduct.setFormalName("Citizen Mobile");
        envelop.getNewsProduct().add(newsProduct);
        
        // Specify the priority of the package
        Priority priority = objFactory.createNewsMLNewsEnvelopePriority();
        priority.setFormalName("5");
        envelop.setPriority(priority);
        
        // Attach envelop to package
        newsml.setNewsEnvelope(envelop);
        
        // Create a news item (the body of the package)
        NIType newsItem = objFactory.createNIType();
        
        // Identification details of the news item
        Identification id = objFactory.createNITypeIdentification();
        
        String providerId = "the-star.co.ke";
        String newsItemId = "KE-CMOBILE-WESTLANDS-ACCIDENT-HALT-TRAFFIC";
        String nameLabel = "Westlands-accident-halt-traffic";
        BigInteger revisionId = BigInteger.ZERO; // Used for multiple revisions of the same story
        
        
        NewsIdentifier ni = objFactory.createNITypeIdentificationNewsIdentifier();
        ni.setProviderId(objFactory.createNITypeIdentificationNewsIdentifierProviderId());
        ni.getProviderId().setValue(providerId);
        ni.setDateId(dateFormatter.format(now));
        ni.setNewsItemId(objFactory.createNITypeIdentificationNewsIdentifierNewsItemId());
        ni.getNewsItemId().setValue(newsItemId);
        ni.setRevisionId(objFactory.createNITypeIdentificationNewsIdentifierRevisionId());
        ni.getRevisionId().setValue(revisionId);
        ni.setPublicIdentifier("urn:newsml:" + providerId + ":" + dateFormatter.format(now) + ":" +  newsItemId + ":" + revisionId);
        id.setNewsIdentifier(ni);
        id.setNameLabel(objFactory.createNITypeIdentificationNameLabel());
        id.getNameLabel().setValue(nameLabel);
        newsItem.setIdentification(id);
        
        // News management - type of news, status and urgency
        NewsManagement nm = objFactory.createNITypeNewsManagement();
        nm.setNewsItemType(objFactory.createNITypeNewsManagementNewsItemType());
        nm.getNewsItemType().setFormalName("Citizen Report");
        nm.setStatus(objFactory.createStatusType());
        nm.getStatus().setFormalName("Usable");
        nm.setUrgency(objFactory.createNITypeNewsManagementUrgency());
        nm.getUrgency().setFormalName("5");
        newsItem.setNewsManagement(nm);
        
        // News component - Story and meta data
        NewsComponentType nc = objFactory.createNewsComponentType();
        
        nc.setNewsLines(objFactory.createNewsComponentTypeNewsLines());
        HeadLine headline = objFactory.createNewsComponentTypeNewsLinesHeadLine();
        headline.setLang("en");
        headline.getContent().add("Traffic accident on Waiyaki Way closes all traffic beyond the Westlands roundabout");
        newsItem.setNewsComponent(nc);
        nc.getNewsLines().getHeadLineAndSubHeadLineOrByLine().add(headline);

        NewsComponentType.DescriptiveMetadata metadata = objFactory.createNewsComponentTypeDescriptiveMetadata();
        Language english = objFactory.createNewsComponentTypeDescriptiveMetadataLanguage();
        english.setFormalName("en");
        metadata.getLanguage().add(english);
        
        Genre genre = objFactory.createNewsComponentTypeDescriptiveMetadataGenre();
        genre.setFormalName("Citizen Report");
        metadata.getGenre().add(genre);
        
        SubjectCode trafficCode = objFactory.createNewsComponentTypeDescriptiveMetadataSubjectCode();
        SubjectCode.SubjectDetail trafficDetail = objFactory.createNewsComponentTypeDescriptiveMetadataSubjectCodeSubjectDetail();
        trafficDetail.setFormalName("Traffic");
        trafficCode.getSubjectOrSubjectMatterOrSubjectDetail().add(trafficDetail);
        metadata.getSubjectCode().add(trafficCode);
        
        Location westlands = objFactory.createNewsComponentTypeDescriptiveMetadataLocation();
        PropertyType suburb = objFactory.createPropertyType();
        suburb.setFormalName("Suburb");
        suburb.setValue("WESTLANDS");
        
        PropertyType city = objFactory.createPropertyType();
        city.setFormalName("City");
        city.setValue("NAIROBI");
        
        PropertyType country = objFactory.createPropertyType();
        country.setFormalName("Country");
        country.setValue("KE");
        
        westlands.getProperty().add(suburb);
        westlands.getProperty().add(city);
        westlands.getProperty().add(country);
        
        metadata.getLocation().add(westlands);
        
        PropertyType generator = objFactory.createPropertyType();
        generator.setFormalName("GeneratorSoftware");
        generator.setValue("Citizen Mobile Server 1.0");
        
        metadata.getProperty().add(generator);
        
        nc.setDescriptiveMetadata(metadata);
        
        
        StringBuilder story = new StringBuilder();
        story.append("<p>This is the body of the story</p>");
        story.append("<p>It could have many paragraphs</p>");
        story.append("<p>and lots of other stuff");
        
        
        ContentItem ci = objFactory.createNewsComponentTypeContentItem();
        ci.setMediaType(objFactory.createNewsComponentTypeContentItemMediaType());;
        ci.getMediaType().setFormalName("text");
        ci.setFormat(objFactory.createNewsComponentTypeContentItemFormat());
        ci.getFormat().setFormalName("XHTML");
        ci.setDataContent(objFactory.createNewsComponentTypeContentItemDataContent());
        ci.getDataContent().getContent().add(story.toString());
        
        nc.getContentItem().add(ci);
        
        
        newsml.getNewsItem().add(newsItem);
        
        File out = new File("target/newsml1_2_sample.xml");
        JAXBContext ctx;
        try {
            ctx = JAXBContext.newInstance(NewsML.class);
            Marshaller marshaller = ctx.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(newsml, out);
        } catch (JAXBException ex) {
            Logger.getLogger(CreateNewsItem.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
