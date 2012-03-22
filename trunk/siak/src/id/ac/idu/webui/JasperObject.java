package id.ac.idu.webui;

/**
 * Created by IntelliJ IDEA.
 * User: valeo
 * Date: 3/7/12
 * Time: 2:23 AM
 * To change this template use File | Settings | File Templates.
 */
public class JasperObject {
    private Object id;
    private Object src;
    private Object parameters;
    private Object datasource;
    private Object type;
    private Object parent;

    public Object getSrc() {
        return src;
    }

    public void setSrc(Object src) {
        this.src = src;
    }

    public Object getParameters() {
        return parameters;
    }

    public void setParameters(Object parameters) {
        this.parameters = parameters;
    }

    public Object getDatasource() {
        return datasource;
    }

    public void setDatasource(Object datasource) {
        this.datasource = datasource;
    }

    public Object getType() {
        return type;
    }

    public void setType(Object type) {
        this.type = type;
    }

    public Object getParent() {
        return parent;
    }

    public void setParent(Object parent) {
        this.parent = parent;
    }

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }
}
