/**
 * The MIT License
 * Copyright (c) 2015 Davi Monteiro Barbosa
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package br.com.davimonteiro.lotus_runtime.model;

import java.io.InputStream;
import java.io.OutputStream;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import br.com.davimonteiro.lotus_runtime.model.util.LotusComponent;
import br.com.davimonteiro.lotus_runtime.model.util.LotusModel;
import br.com.davimonteiro.lotus_runtime.model.util.LotusState;
import br.com.davimonteiro.lotus_runtime.model.util.LotusTransition;

public class ProjectXMLSerializer implements ProjectSerializer {

    private static LotusModel mProjeto;
    private static LotusComponent mComponent;
    private static LotusState mState;
    private static LotusTransition mTransition;

    @Override
    public void toStream(LotusModel p, OutputStream stream) {
        XMLWritter xml = new XMLWritter(stream);
        xml.begin("project");
        xml.attr("version", "1.0");
        xml.attr("name", p.getName());
        for (LotusComponent c : p.getComponents()) {
            xml.begin("component");
            xml.attr("name", c.getName());

            xml.begin("states");

            for (LotusState v : c.getStates()) {
                xml.begin("state");
                xml.attr("id", v.getID());
                xml.attr("x", v.getLayoutX());
                xml.attr("y", v.getLayoutY());
                xml.attr("label", v.getLabel());

                if (v.isInitial()) {
                    xml.attr("initial", "true");
                }
                if (v.isError()) {
                    xml.attr("error", "true");
                }
                if (v.isFinal()) {
                    xml.attr("final", "true");
                }

                xml.end();
            }

            xml.end();
            xml.begin("transitions");

            for (LotusTransition t : c.getTransitions()) {
                xml.begin("transition");
                xml.attr("from", t.getSource().getID());
                xml.attr("to", t.getDestiny().getID());
                Double d = t.getProbability();
                if (d != null) {
                    xml.attr("prob", t.getProbability());
                }
                String s = t.getLabel();
                xml.attr("label", s == null ? "" : s);
                s = t.getGuard();
                if (s != null) {
                    xml.attr("guard", s);
                }

                Integer i = (Integer) t.getValue("view.type");
                if (i != null) {
                    xml.attr("view-type", String.valueOf(i));
                }
                xml.end();
            }
            xml.end();

            xml.end();
        }
        xml.end();
    }

    private static final DefaultHandler handler = new DefaultHandler() {
        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            switch (qName) {
                case "project": {
                    parseProjectTag(attributes);
                    break;
                }
                case "component": {
                    parseComponentTag(attributes);
                    break;
                }
                case "state": {
                    parseStateTag(attributes);
                    break;
                }
                case "transition": {
                    parseTransitionTag(attributes);
                    break;
                }
            }
        }
    };

    @Override
    public LotusModel parseStream(InputStream stream) throws Exception {
        XMLReader xr = XMLReaderFactory.createXMLReader();
        xr.setContentHandler(handler);
        xr.parse(new InputSource(stream));
        return mProjeto;
    }

    private static void parseProjectTag(Attributes attributes) {
        mProjeto = new LotusModel();
        mProjeto.setName(attributes.getValue("name"));
    }

    private static void parseComponentTag(Attributes attributes) {
        mComponent = new LotusComponent();
        mComponent.setAutoUpdateLabels(false);
        mComponent.setName(attributes.getValue("name"));
        mProjeto.addComponent(mComponent);
    }

    private static void parseStateTag(Attributes attributes) {
        int id = Integer.parseInt(attributes.getValue("id"));
        mState = mComponent.newState(id);
        mState.setLayoutX(Double.parseDouble(attributes.getValue("x")));
        mState.setLayoutY(Double.parseDouble(attributes.getValue("y")));
        if (Boolean.parseBoolean(attributes.getValue("initial"))) {
            mState.setAsInitial();
        }
        if (Boolean.parseBoolean(attributes.getValue("final"))) {
            mState.setFinal(true);
        }
        if (Boolean.parseBoolean(attributes.getValue("error"))) {
            mState.setError(true);
        }
        mState.setLabel(attributes.getValue("label"));
    }

    private static void parseTransitionTag(Attributes attributes) {
        String s = attributes.getValue("prob");
        String viewType = attributes.getValue("view-type");
        mTransition = mComponent.buildTransition(Integer.parseInt(attributes.getValue("from")), Integer.parseInt(attributes.getValue("to")))
                .setLabel(attributes.getValue("label"))
                .setProbability(s == null ? null : Double.parseDouble(s))
                .setGuard(attributes.getValue("guard"))
                .setValue("view.type", viewType == null ? null : Integer.parseInt(viewType))
                .create();
    }

}
