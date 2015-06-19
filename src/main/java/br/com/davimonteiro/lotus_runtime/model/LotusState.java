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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LotusState {

    private final LotusComponent mComponent;

    public void setAsInitial() {
        mComponent.setInitialState(this);
    }

    void markInitial(boolean b) {
        mInitial = b;
        for (Listener l : mListeners) {
            l.onChange(this);
        }
    }

    public interface Listener {

        void onChange(LotusState state);

    }
    public static final String TEXTSTYLE_NORMAL = "normal";
    public static final String TEXTSTYLE_ITALIC = "italic";
    public static final String TEXTSTYLE_BOLD = "bold";

    //Graph properties
    private int mID;
    private final List<LotusTransition> mTransicoesSaida = new ArrayList<>();
    private final List<LotusTransition> mTransicoesEntrada = new ArrayList<>();
    private final Map<String, Object> mValues = new HashMap<>();
    private final List<Listener> mListeners = new ArrayList<>();
    //View properties    
    private String mLabel;
    private double mLayoutX;
    private double mLayoutY;
    private String mColor;
    private String mBorderColor;
    private Integer mBorderWidth;
    private String mTextColor;
    private String mTextStyle = TEXTSTYLE_NORMAL;
    private Integer mTextSize;
    //lts properties
    private boolean mInitial;
    private boolean mError;
    private boolean mFinal;

    LotusState(LotusComponent c) {
        mComponent = c;
    }

    public void setID(int id) {
        mID = id;
    }

    public int getID() {
        return mID;
    }

    public double getLayoutX() {
        return mLayoutX;
    }

    public void setLayoutX(double layoutX) {
        mLayoutX = layoutX;
        for (Listener l : mListeners) {
            l.onChange(this);
        }
    }

    public double getLayoutY() {
        return mLayoutY;
    }

    public void setLayoutY(double layoutY) {
        mLayoutY = layoutY;
        for (Listener l : mListeners) {
            l.onChange(this);
        }
    }

    public String getColor() {
        return mColor;
    }

    public void setColor(String color) {
        mColor = color;
        for (Listener l : mListeners) {
            l.onChange(this);
        }
    }

    public String getBorderColor() {
        return mBorderColor;
    }

    public void setBorderColor(String color) {
        mBorderColor = color;
        for (Listener l : mListeners) {
            l.onChange(this);
        }
    }

    public Integer getBorderWidth() {
        return mBorderWidth;
    }

    public void setBorderWidth(Integer width) {
        mBorderWidth = width;
        for (Listener l : mListeners) {
            l.onChange(this);
        }
    }

    public String getTextColor() {
        return mTextColor;
    }

    public void setTextColor(String color) {
        mTextColor = color;
        for (Listener l : mListeners) {
            l.onChange(this);
        }
    }

    public Integer getTextSize() {
        return mTextSize;
    }

    public void setTextSize(Integer size) {
        mTextSize = size;
        for (Listener l : mListeners) {
            l.onChange(this);
        }
    }

    public String getTextStyle() {
        return mTextStyle;
    }

    public void setTextSyle(String color) {
        mTextStyle = color;
        for (Listener l : mListeners) {
            l.onChange(this);
        }
    }

    public String getLabel() {
        return mLabel;
    }

    public void setLabel(String label) {
        mLabel = label;
        for (Listener l : mListeners) {
            l.onChange(this);
        }
    }

    public boolean isInitial() {
        return mInitial;
    }

    public boolean isError() {
        return mError;
    }

    public void setError(boolean value) {
        mError = value;
        for (Listener l : mListeners) {
            l.onChange(this);
        }
    }

    public boolean isFinal() {
        return mFinal;
    }

    public void setFinal(boolean value) {
        mFinal = value;
        for (Listener l : mListeners) {
            l.onChange(this);
        }
    }

    public Iterable<LotusTransition> getIncomingTransitions() {
        return mTransicoesEntrada;
    }

    public Iterable<LotusTransition> getOutgoingTransitions() {    	
        return mTransicoesSaida;
    }
    
    public List<LotusTransition> getIncomingTransitionsList() {
        return Collections.unmodifiableList(mTransicoesEntrada);
    }

    public List<LotusTransition> getOutgoingTransitionsList() {    	
        return Collections.unmodifiableList(mTransicoesSaida);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + this.mID;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final LotusState other = (LotusState) obj;
        if (this.mID != other.mID) {
            return false;
        }
        return true;
    }

    public void addListener(Listener l) {
        mListeners.add(l);
    }

    public void removeListener(Listener l) {
        mListeners.remove(l);
    }

    void addIncomingTransition(LotusTransition t) {
        mTransicoesEntrada.add(t);
    }

    void addOutgoingTransition(LotusTransition t) {
        mTransicoesSaida.add(t);
    }

    void removeIncomingTransition(LotusTransition transition) {
        mTransicoesEntrada.remove(transition);
    }

    void removeOutgoingTransition(LotusTransition transition) {
        mTransicoesSaida.remove(transition);
    }

    public int getOutgoingTransitionsCount() {
        return mTransicoesSaida.size();
    }
    
    public int getIncomingTransitionsCount() {
        return mTransicoesEntrada.size();
    }

    public Object getValue(String key) {
        return mValues.get(key);
    }

    public void setValue(String key, Object value) {
        mValues.put(key, value);
    }

    void copy(LotusState s) {
        mID = s.mID;
        mInitial = s.mInitial;
        mError = s.mError;
        mFinal = s.mFinal;
        mBorderColor = s.mBorderColor;
        mBorderWidth = s.mBorderWidth;
        mColor = s.mColor;
        mLabel = s.mLabel;
        mLayoutX = s.mLayoutX;
        mLayoutY = s.mLayoutY;
        mTextColor = s.mTextColor;
        mTextSize = s.mTextSize;
    }

    public LotusTransition getTransitionTo(LotusState s) {
        for (LotusTransition t : mTransicoesSaida) {
            if (t.getDestiny().equals(s)) {
                return t;
            }
        }
        return null;
    }

    public List<LotusTransition> getTransitionsTo(LotusState s) {
        List<LotusTransition> r = new ArrayList<>();
        for (LotusTransition t : mTransicoesSaida) {
            if (t.getDestiny().equals(s)) {
                r.add(t);
            }
        }
        return r;
    }

    public LotusTransition getTransitionByLabel(String label) {
        for (LotusTransition t : mTransicoesSaida) {
            if (t.getLabel().equals(label)) {
                return t;
            }
        }
        return null;
    }

}
