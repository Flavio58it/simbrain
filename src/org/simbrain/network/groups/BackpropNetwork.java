/*
 * Part of Simbrain--a java-based neural network kit Copyright (C) 2005,2007 The
 * Authors. See http://www.simbrain.net/credits This program is free software;
 * you can redistribute it and/or modify it under the terms of the GNU General
 * Public License as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. This program is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You
 * should have received a copy of the GNU General Public License along with this
 * program; if not, write to the Free Software Foundation, Inc., 59 Temple Place
 * - Suite 330, Boston, MA 02111-1307, USA.
 */
package org.simbrain.network.groups;

import java.awt.geom.Point2D.Double;

import org.simbrain.network.interfaces.RootNetwork;
import org.simbrain.network.trainers.*;

/**
 * @author Jeff Yoshimi
 */
public class BackpropNetwork extends LayeredNetwork {

    private final Trainer trainer;

    public BackpropNetwork(RootNetwork network, int[] nodesPerLayer,
            Double initialPosition) {
        super(network, nodesPerLayer, initialPosition);
        int numLayers = getLayers().size();
        trainer = new Trainer(network, this.getLayers().get(0).getNeuronList(),
                this.getLayers().get(numLayers - 1).getNeuronList(),
                new Backprop());
        setLabel("Backprop");

    }

    /**
     * @return the trainer
     */
    public Trainer getTrainer() {
        return trainer;
    }

}
