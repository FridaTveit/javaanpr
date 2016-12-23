/*
 * Copyright 2013 JavaANPR contributors
 * Copyright 2006 Ondrej Martinsky
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.osedu.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package net.sf.javaanpr.imageanalysis;

import net.sf.javaanpr.configurator.Configurator;

import java.util.Vector;

public class PlateHorizontalGraph extends Graph {

    private static int horizontalDetectionType =
            Configurator.getConfigurator().getIntProperty("platehorizontalgraph_detectionType");

    public float derivation(int index1, int index2) {
        return yValues.elementAt(index1) - yValues.elementAt(index2);
    }

    public Vector<Peak> findPeak() {
        if (PlateHorizontalGraph.horizontalDetectionType == 1) {
            return findPeakEdgedetection();
        }
        return findPeakDerivative();
    }

    public Vector<Peak> findPeakDerivative() {
        int a = 2;
        int b = yValues.size() - 1 - 2;
        float maxVal = getMaxValue();
        while ((-derivation(a, a + 4) < (maxVal * 0.2)) && (a < (yValues.size() - 2 - 2 - 4))) {
            a++;
        }
        while ((derivation(b - 4, b) < (maxVal * 0.2)) && (b > (a + 2))) {
            b--;
        }
        Vector<Peak> outPeaks = new Vector<Peak>();
        outPeaks.add(new Peak(a, b));
        super.peaks = outPeaks;
        return outPeaks;
    }

    public Vector<Peak> findPeakEdgedetection() {
        float average = getAverageValue();
        int a = 0;
        int b = yValues.size() - 1;
        while (yValues.elementAt(a) < average) {
            a++;
        }
        while (yValues.elementAt(b) < average) {
            b--;
        }
        Vector<Peak> outPeaks = new Vector<Peak>();
        a = Math.max(a - 5, 0);
        b = Math.min(b + 5, yValues.size());
        outPeaks.add(new Peak(a, b));
        super.peaks = outPeaks;
        return outPeaks;
    }
}
