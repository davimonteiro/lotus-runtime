/**
 * The MIT License
 * Copyright © 2017 Davi Monteiro Barbosa
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

public class LotusModel {
    
    private final Map<String, Object> mValues = new HashMap<String, Object>();

    public interface Listener {

        void onChange(LotusModel project);

        void onComponentCreated(LotusModel project, LotusComponent component);

        void onComponentRemoved(LotusModel project, LotusComponent component);
    }

    private String mName;
    private final List<LotusComponent> mComponents = new ArrayList<>();
    private final List<Listener> mListeners = new ArrayList<>();

    public void addComponent(LotusComponent c) {
        mComponents.add(c);
        //System.out.println(mListeners);
        for (Listener l : mListeners) {
            l.onComponentCreated(this, c);
        }
    }

    public void removeComponent(LotusComponent c) {
        mComponents.remove(c);
        for (Listener l : mListeners) {
            l.onComponentRemoved(this, c);
        }
    }

    public void removeComponentByIndex(int index) {
        LotusComponent c = mComponents.remove(index);
        for (Listener l : mListeners) {
            l.onComponentRemoved(this, c);
        }
    }

    public LotusComponent getComponent(int index) {
        return mComponents.get(index);
    }

    public Iterable<LotusComponent> getComponents() {
        return mComponents;
    }

    public int getComponentsCount() {
        return mComponents.size();
    }

    public int indexOfComponent(LotusComponent component) {
        int i = 0;
        for (LotusComponent c : mComponents) {
            if (c == component) {
                return i;
            }
            i++;
        }
        return -1;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
        for (Listener l : mListeners) {
            l.onChange(this);
        }
    }

    public void addListener(Listener l) {
        mListeners.add(l);
    }

    public void removeListener(Listener l) {
        mListeners.remove(l);
    }
    
    public Object getValue(String key) {
        return mValues.get(key);
    }

    public void setValue(String key, Object value) {
        mValues.put(key, value);
    }

}
