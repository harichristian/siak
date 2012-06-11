package id.ac.idu.webui.irs.jadwalkuliah;

import id.ac.idu.backend.model.Tjadkuldetil;
import org.apache.log4j.Logger;
import org.zkoss.zk.ui.sys.ComponentsCtrl;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Hari Christian
 * Date: 11/03/12
 * Time: 22:42
 * To change this template use File | Settings | File Templates.
 */
public class JadkuldetilListItemRenderer implements ListitemRenderer, Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(JadkuldetilListItemRenderer.class);

    @Override
    public void render(Listitem item, Object data) throws Exception {
        final Tjadkuldetil tjadkuldetil = (Tjadkuldetil) data;
        Listcell lc = new Listcell(tjadkuldetil.getMhari().getCnmhari());
        lc.setParent(item);
        lc = new Listcell(tjadkuldetil.getMsesikuliah().getCkdsesi());
        lc.setParent(item);
        lc = new Listcell(tjadkuldetil.getMsesikuliah().getCjamawal());
        lc.setParent(item);
        lc = new Listcell(tjadkuldetil.getMsesikuliah().getCjamakhir());
        lc.setParent(item);
        lc = new Listcell(tjadkuldetil.getMruang().getCnmRuang());
        lc.setParent(item);
        lc = new Listcell(String.valueOf(tjadkuldetil.getNjmlsesi()));
        lc.setStyle("text-align: right");
        lc.setParent(item);
        lc = new Listcell(String.valueOf(tjadkuldetil.getNmaks()));
        lc.setStyle("text-align: right");
        lc.setParent(item);
        lc = new Listcell(String.valueOf(tjadkuldetil.getNisi()));
        lc.setStyle("text-align: right");
        lc.setParent(item);
        item.setValue(data);
        ComponentsCtrl.applyForward(item, "onDoubleClick=onDoubleClickedTjadkuldetilItem");

    }
}
