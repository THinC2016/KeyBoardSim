package com.example.jacobdurrah.keyboardsim;

import com.example.jacobdurrah.keyboardsim.Vibration;
import com.example.jacobdurrah.keyboardsim.Task;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Queue;

import javax.xml.parsers.*;
/**
 * Created by John on 2/12/2016.
 *
 * This class is meant to parse XML files containing information on vibration events
 * and task events for the THinC FAA study. This class will populate two queues that
 * are owned by the main activity
 */
public class FlightScenarioReader {
    String mScenarioFile;

    public  FlightScenarioReader(String scenarioFile){
        mScenarioFile = scenarioFile;
    }

    //Reads the entirety of the XML scenario into vibrationEvents and taskEvents
    public void updateQueues(Queue<Vibration> vibrationEvents, Queue<Task> taskEvents)
            throws IOException, ParserConfigurationException, FileNotFoundException
    {
        File xmlFile = new File(mScenarioFile);
        InputStream is = new FileInputStream(xmlFile);

        DocumentBuilderFactory builderFactory =
                DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = builderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        try {
            Document document = builder.parse(new FileInputStream(mScenarioFile));
            NodeList vibNodes = document.getElementsByTagName("vibration");
            NodeList taskNodes = document.getElementsByTagName("task");

            for(int vibnode = 0; vibnode < vibNodes.getLength(); vibnode++){
                Node vnode = vibNodes.item(vibnode);
                Element e = (Element) vnode;


                int frequency = Integer.parseInt(e.getElementsByTagName("freq").item(0).getTextContent());
                int amplitude = Integer.parseInt(e.getElementsByTagName("ampl").item(0).getTextContent());
                String startTime = e.getAttribute("startTime");

                vibrationEvents.add(new Vibration(frequency, amplitude, startTime));
            }

            for(int tasknode = 0; tasknode < taskNodes.getLength(); tasknode++){
                Node tnode = vibNodes.item(tasknode);

                Element e = (Element) tnode;

                int scenario = Integer.parseInt(e.getElementsByTagName("scen").item(0).getTextContent());
                String type = e.getElementsByTagName("type").item(0).getTextContent();
                String startTime = e.getAttribute("startTime");

                taskEvents.add(new Task(scenario, type, startTime));
            }

        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
