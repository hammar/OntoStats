package org.ontologyengineering.ontometrics.plugins;

/**
 * @author Aidan Delaney <aidan@phoric.eu>
 */
class TestStruct {
    public DiagramMetric dm;
    public Filter.FilterType lhs, rhs;
    public TestStruct(DiagramMetric dm, Filter.FilterType lhs, Filter.FilterType rhs) {
        this.dm  = dm;
        this.lhs = lhs;
        this.rhs = rhs;
    }
}
