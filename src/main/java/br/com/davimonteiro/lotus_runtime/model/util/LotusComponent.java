/**
 * The MIT License
 * Copyright © 2016 Davi Monteiro Barbosa
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
package br.com.davimonteiro.lotus_runtime.model.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class LotusComponent {

    private final Map<String, Object> mValues = new HashMap<>();
    private boolean mAutoUpdateLabels = true;

    public Object getValue(String key) {
        return mValues.get(key);
    }

    public void setValue(String key, Object value) {
        mValues.put(key, value);
    }

    private void copyState(LotusState from, LotusState to) {
        to.setLabel(from.getLabel());
        to.setLayoutX(from.getLayoutX());
        to.setLayoutY(from.getLayoutY());
    }

    private void copyTransition(LotusTransition from, LotusTransition to) {        
        to.setLabel(from.getLabel());
        to.setProbability(from.getProbability());
        to.setGuard(from.getGuard());
        to.setValue("view.type", from.getValue("view.type"));
    }

    public interface Listener {

        void onChange(LotusComponent component);

        void onStateCreated(LotusComponent component, LotusState state);

        void onStateRemoved(LotusComponent component, LotusState state);

        void onTransitionCreated(LotusComponent component, LotusTransition state);

        void onTransitionRemoved(LotusComponent component, LotusTransition state);
    }

    private String mName;
    private LotusState mInitialState;
    private LotusState mFinalState;
    private LotusState mErrorState;
    private final List<LotusState> mStates = new ArrayList<>();
    private final List<LotusTransition> mTransitions = new ArrayList<>();
    private final List<Listener> mListeners = new ArrayList<>();

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
        for (Listener l : mListeners) {
            l.onChange(this);
        }
    }

    public Iterable<LotusState> getStates() {
        return mStates;
    }

    public int getStatesCount() {
        return mStates.size();
    }

    public int getStateIndex(LotusState s) {
        return mStates.indexOf(s);
    }

    public Iterable<LotusTransition> getTransitions() {
        return mTransitions;
    }

    public int getTransitionsCount() {
        return mTransitions.size();
    }

    public LotusState getStateByID(int id) {
        for (LotusState v : mStates) {
            if (v.getID() == id) {
                return v;
            }
        }
        return null;
    }

    public LotusState newState(int id) {
        LotusState v = new LotusState(this);
        v.setID(id);
        add(v);
        if (mInitialState == null) {
            setInitialState(v);
        }
        return v;
    }

    public void add(LotusState v) {
        mStates.add(v);
        updateStateLabels();
        for (Listener l : mListeners) {
            l.onStateCreated(this, v);
        }
    }

    public void add(LotusTransition t) {
        t.getSource().addOutgoingTransition(t);
        t.getDestiny().addIncomingTransition(t);
        mTransitions.add(t);
        for (Listener l : mListeners) {
            l.onTransitionCreated(this, t);
        }
    }

    public LotusTransition newTransition(LotusState src, LotusState dst) {
        if (src == null) {
            throw new IllegalArgumentException("src state can't be null!");
        }
        if (dst == null) {
            throw new IllegalArgumentException("dst state can't be null!");
        }
        LotusTransition t = new LotusTransition(src, dst);
        add(t);
        return t;
    }

    public LotusTransition newTransition(int idSrc, int idDst) {
        return newTransition(getStateByID(idSrc), getStateByID(idDst));
    }

    public LotusTransition.Builder buildTransition(LotusState src, LotusState dst) {
        if (src == null) {
            throw new IllegalArgumentException("src state can't be null!");
        }
        if (dst == null) {
            throw new IllegalArgumentException("dst state can't be null!");
        }
        LotusTransition t = new LotusTransition(src, dst);
        return new LotusTransition.Builder(this, t);
    }

    public LotusTransition.Builder buildTransition(int idSrc, int idDst) {
        LotusTransition t = new LotusTransition(getStateByID(idSrc), getStateByID(idDst));
        return new LotusTransition.Builder(this, t);
    }

    public void remove(LotusState state) {
    	List<LotusTransition> transitions = new ArrayList<>();
    	transitions.addAll(state.getOutgoingTransitionsList());
    	transitions.addAll(state.getIncomingTransitionsList());
        for (LotusTransition t : transitions) {
            remove(t);
        }
        
        mStates.remove(state);
        for (Listener l : mListeners) {
            l.onStateRemoved(this, state);
        }
        if (state.isInitial()) {
            if (mStates.size() > 0) {
                setInitialState(mStates.get(0));
            }
        }
        updateStateLabels();
    }

    private void updateStateLabels() {
        if (!mAutoUpdateLabels) {
            return;
        }
        int i = 0;
        for (LotusState v : mStates) {
            v.setLabel(String.valueOf(i++));
        }
    }

    public void remove(LotusTransition transition) {
        transition.getSource().removeOutgoingTransition(transition);
        transition.getDestiny().removeIncomingTransition(transition);
        mTransitions.remove(transition);
        for (Listener l : mListeners) {
            l.onTransitionRemoved(this, transition);
        }
    }

    public void addListener(Listener l) {
        mListeners.add(l);
    }

    public void removeListener(Listener l) {
        mListeners.remove(l);
    }

    public LotusState getInitialState() {
        return mInitialState;
    }

    public void setInitialState(LotusState initialState) {
        if (mInitialState != null) {
            mInitialState.markInitial(false);
        }
        mInitialState = initialState;
        if (mInitialState != null) {
            mInitialState.markInitial(true);
        }
    }

    public LotusState getErrorState() {
        return mErrorState;
    }

    public void setErrorState(LotusState errorState) {
        if (mErrorState != null) {
            mErrorState.setError(false);
        }
        mErrorState = errorState;
        if (mErrorState != null) {
            mErrorState.setError(true);
        }
    }

    public LotusState getFinalState() {
        return mFinalState;
    }

    public void setFinalState(LotusState finalState) {
        if (mFinalState != null) {
            mFinalState.setFinal(false);
        }
        mFinalState = finalState;
        if (mFinalState != null) {
            mFinalState.setFinal(true);
        }
    }

    @Override
    public LotusComponent clone() throws CloneNotSupportedException {
        LotusComponent c = new LotusComponent();
        c.mName = mName;
        c.mAutoUpdateLabels = mAutoUpdateLabels;
        for (LotusState oldState : mStates) {
            LotusState newState = c.newState(oldState.getID());
            copyState(oldState, newState);
        }
        for (LotusTransition oldTransition : mTransitions) {
            int src = oldTransition.getSource().getID();
            int dst = oldTransition.getDestiny().getID();
            LotusTransition newTransition = c.newTransition(src, dst);
            copyTransition(oldTransition, newTransition);
        }        

        if (mInitialState != null) {
            c.setInitialState(c.getStateByID(mInitialState.getID()));
        }
        if (mFinalState != null) {
            c.setFinalState(c.getStateByID(mFinalState.getID()));
        }
        if (mErrorState != null) {
            c.setErrorState(c.getStateByID(mErrorState.getID()));
        }
        return c;
    }

    public void setAutoUpdateLabels(boolean value) {
        mAutoUpdateLabels = value;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + Objects.hashCode(this.mName);
        hash = 59 * hash + Objects.hashCode(this.mInitialState);
        hash = 59 * hash + Objects.hashCode(this.mFinalState);
        hash = 59 * hash + Objects.hashCode(this.mErrorState);
        hash = 59 * hash + Objects.hashCode(this.mStates);
        hash = 59 * hash + Objects.hashCode(this.mTransitions);
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
        final LotusComponent other = (LotusComponent) obj;
        if (!Objects.equals(this.mName, other.mName)) {
            return false;
        }
        if (!Objects.equals(this.mInitialState, other.mInitialState)) {
            return false;
        }
        if (!Objects.equals(this.mFinalState, other.mFinalState)) {
            return false;
        }
        if (!Objects.equals(this.mErrorState, other.mErrorState)) {
            return false;
        }
        if (!Objects.equals(this.mStates, other.mStates)) {
            return false;
        }
        if (!Objects.equals(this.mTransitions, other.mTransitions)) {
            return false;
        }
        return true;
    }

}
