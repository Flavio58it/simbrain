package org.simbrain.plot;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.simbrain.util.Utils;
import org.simbrain.workspace.Consumer;
import org.simbrain.workspace.Coupling;
import org.simbrain.workspace.CouplingContainer;
import org.simbrain.workspace.Producer;
import org.simbrain.workspace.Workspace;
import org.simbrain.workspace.WorkspaceComponent;
import org.simbrain.workspace.gui.CouplingMenuItem;
import org.simbrain.workspace.gui.SimbrainDesktop;

public class PlotComponent extends WorkspaceComponent implements CouplingContainer {

    /** Time series. */
    XYSeries series = new XYSeries("Time series");

    /** Consumer list. */
    private ArrayList<Consumer> consumers= new ArrayList<Consumer>();

    /** Coupling list. */
    private ArrayList<Coupling> couplings = new ArrayList<Coupling>();

    /** Coupling menu item. Must be reset every time.  */
    JMenuItem couplingMenuItem;

    /**
     * Construct a new world panel.  Set up the toolbars.  Create an  instance of a world object.
     * @param ws the workspace associated with this frame
     */
    public PlotComponent(String name) {
        super(name);
    }

    public CouplingContainer getCouplingContainer() {
        return this;
    }
    
    
    /**
     * Responds to actions performed.
     * @param e Action event
     */
    public void actionPerformed(final ActionEvent e) {

        // Handle Coupling wireup
        if (e.getSource() instanceof CouplingMenuItem) {
            CouplingMenuItem m = (CouplingMenuItem) e.getSource();
            Coupling coupling = new Coupling(m.getProducingAttribute(), this.getConsumers().get(0).getDefaultConsumingAttribute());
            getCouplings().clear();
            getCouplings().add(coupling);
        }
    }

    int time = 0;
    
    public void setValue(double value) {
        series.add(time++, value);
    }

    @Override
    public String getFileExtension() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isChangedSinceLastSave() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void save(File saveFile) {
        // TODO Auto-generated method stub
    }

    @Override
    public void close() {
        // TODO Auto-generated method stub
    }

    
    /**
     * {@inheritDoc}
     */
    public List<Consumer> getConsumers() {
        return consumers;
    }

    /**
     * {@inheritDoc}
     */
    public List<Coupling> getCouplings() {
        return couplings;
    }

    /**
     * No producers.
     */
    public List<Producer> getProducers() {
        return null;
    }

    @Override
    public void open(File openFile) {
        // TODO Auto-generated method stub
    }

    @Override
    public void update() {
        // TODO Auto-generated method stub
        
    }
}
