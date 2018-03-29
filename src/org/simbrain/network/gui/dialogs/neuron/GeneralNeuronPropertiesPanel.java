/*
 * Part of Simbrain--a java-based neural network kit
 * Copyright (C) 2005,2007 The Authors.  See http://www.simbrain.net/credits
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.simbrain.network.gui.dialogs.neuron;

import org.simbrain.network.core.Neuron;
import org.simbrain.network.core.NeuronUpdateRule;
import org.simbrain.util.propertyeditor2.AnnotatedPropertyEditor;
import org.simbrain.util.propertyeditor2.EditableObject;
import org.simbrain.util.widgets.DropDownTriangle;
import org.simbrain.util.widgets.DropDownTriangle.UpDirection;
import org.simbrain.util.widgets.EditablePanel;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A panel containing more detailed generic information about neurons. Generally
 * speaking, this panel is not meant to exist in a dialog by itself, it is a set
 * of commonly used (hence generic) neuron value fields which is shared by
 * multiple complete dialogs.
 * <p>
 * Values included are: Activation, upper / lower bounds, label, priority and
 * increment.
 *
 * @author ztosi
 * @author jyoshimi
 */
@SuppressWarnings("serial")
public class GeneralNeuronPropertiesPanel extends JPanel implements EditablePanel {

    /**
     * The neurons being modified.
     */
    private List<Neuron> neuronList;

    /**
     * Holder for neuron property widgets generated by {@link org.simbrain.util.UserParameter} annotations.
     */
    private AnnotatedPropertyEditor neuronPropertyWidgets;

    /*
     * Panel for basic neuron properties.
     */
    private JPanel basicStatsPanel;

    /**
     * Panel containing fields for upper bound, lower bound, and clipping.
     */
    private BoundsClippingPanel boundsClippingPanel;

    /**
     * A triangle that switches between an up (left) and a down state Used for
     * showing/hiding extra neuron data.
     */
    private final DropDownTriangle detailTriangle;

    /**
     * The extra data panel. Includes: increment, upper bound, lower bound, and
     * priority.
     */
    private final JPanel detailPanel = new JPanel();

    /**
     * Parent reference so pack can be called.
     */
    private final Window parent;

    /**
     * Creates a basic neuron info panel. Here the whether or not ID info is
     * displayed is manually set. This is the case when the number of neurons
     * (such as when adding multiple neurons) is unknown at the time of display.
     * In fact this is probably the only reason to use this factory method over
     *
     * @param neuronList    the neurons whose information is being displayed/made
     *                      available to edit on this panel
     * @param parent        the parent window for dynamic resizing
     * @return A basic neuron info panel with the specified parameters
     */
    public static GeneralNeuronPropertiesPanel createPanel(final List<Neuron> neuronList, final Window parent) {
        GeneralNeuronPropertiesPanel bnip = new GeneralNeuronPropertiesPanel(neuronList, parent);
        bnip.addListeners();
        return bnip;
    }

    /**
     * Construct the panel.
     *
     * @param neuronList    list of neurons
     * @param parent        parent window
     */
    private GeneralNeuronPropertiesPanel(final List<Neuron> neuronList, final Window parent) {
        this.neuronList = neuronList;
        this.parent = parent;
        detailTriangle = new DropDownTriangle(UpDirection.LEFT, false, "More", "Less", parent);
        boundsClippingPanel = new BoundsClippingPanel(neuronList, parent);

//        DefaultFormatter format = new DefaultFormatter();
//        format.setOverwriteMode(false);
//        tfNeuronLabel = new JFormattedTextField(format);

        basicStatsPanel = new JPanel();

        neuronPropertyWidgets = new AnnotatedPropertyEditor(neuronList);
        List<EditableObject> ruleList = neuronList.stream().map(Neuron::getUpdateRule).collect(Collectors.toList());
        initializeLayout();
        fillFieldValues();
    }

    /**
     * Lays out the panel.
     */
    private void initializeLayout() {

        setLayout(new BorderLayout());

        basicStatsPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        GridLayout gL = new GridLayout(0, 2);
        gL.setVgap(2);
        basicStatsPanel.setLayout(gL);
        // Show id if editing one neuron
        if (neuronList.size() == 1) {
            basicStatsPanel.add(new JLabel("Id:"));
            basicStatsPanel.add(new JLabel(""+ neuronList.get(0).getId()));
        }
        basicStatsPanel.add(new JLabel("Activation:"));
        basicStatsPanel.add(neuronPropertyWidgets.getWidget("Activation").component);
        basicStatsPanel.add(new JLabel("Label:"));
        basicStatsPanel.add(neuronPropertyWidgets.getWidget("Label").component);

        this.add(basicStatsPanel, BorderLayout.NORTH);

        this.add(neuronPropertyWidgets.getWidget("Update Rule").component,
            BorderLayout.CENTER);

        TitledBorder tb = BorderFactory.createTitledBorder("Neuron Properties");
        this.setBorder(tb);

    }

    /**
     * Add listeners.
     */
    private void addListeners() {

        // Add a listener to display/hide extra editable neuron data
        detailTriangle.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Repaint to show/hide extra data
                detailPanel.setVisible(detailTriangle.isDown());
                detailPanel.repaint();
                parent.pack();
                // parent.setLocationRelativeTo(null);
            }
        });
    }

    /**
     * Update field visibility based on whether rule is bounded and/or clipped.
     *
     * @param rule the current rule
     */
    public void updateFieldVisibility(NeuronUpdateRule rule) {
        boundsClippingPanel.updateFieldVisibility(rule);
//        if (rule != null) {
//            inputType.setSelectedItem(rule.getInputType().toString());
//        }
    }

    @Override
    public void fillFieldValues() {
        if (neuronList == null || neuronList.isEmpty()) {
            return;
        }
        Neuron neuronRef = neuronList.get(0);
        if (neuronRef == null) {
            return;
        }

    }

    /**
     * Uses the values from text fields to alter corresponding values in the
     * neuron(s) being edited. Called externally to apply changes. Returns a
     * success value that is false if, for example, text was placed in a numeric
     * field.
     */
    @Override
    public boolean commitChanges() {
        boolean success = true;

        neuronPropertyWidgets.commitChanges(neuronList);

        // TODO: Below a hack. Why only needed for labels and not activation? And what if no change to labels?
        // Update neuron labels only
        if (!neuronList.isEmpty()) {
            neuronList.forEach(neuronList.get(0).getNetwork()::fireNeuronLabelChanged);
        }

        return success;
    }

    @Override
    public JPanel getPanel() {
        return this;
    }

}
